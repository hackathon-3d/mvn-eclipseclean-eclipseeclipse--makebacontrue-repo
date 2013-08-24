package com.bacon.teletweet.Utility;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.bacon.teletweet.TwitterAuthenticationActivity;
import com.google.gag.annotation.remark.Facepalm;

public class TwitterUtil {
	private static AsyncTwitter twit;
	private static RequestToken rqTok;
	private static boolean authenticated = false;
	private static Context context;
	private static TwitterListener tl;

	// So... whichever activity calls this needs to be able to handle
	// an onActivityResult that calls OauthComplete
	@Facepalm
	public static void initAndAwaitAuthenticationResult(Activity c) {
		if (authenticated) {
			return;
		}

		context = c;

		// setup config
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(authVars.CONSUMER_KEY).setOAuthConsumerSecret(
				authVars.CONSUMER_SECRET);

		twit = new AsyncTwitterFactory(cb.build()).getInstance();

		new AsyncTask<Void, Void, RequestToken>() {
			@Override
			public RequestToken doInBackground(Void... blah) {
				try {
					return twit.getOAuthRequestToken();
				} catch (TwitterException e) {
					Log.i("TeleTweet",
							"Man, I couldn't even launch the activity. "
									+ e.getMessage());
					return null;
				}
			}

			@Override
			public void onPostExecute(RequestToken tok) {
				Log.i("TeleTweet", "Wow, that works?");
				rqTok = tok;
				Intent i = new Intent(context,
						TwitterAuthenticationActivity.class);
				String authToken = Uri.parse(rqTok.getAuthorizationURL())
						.getQueryParameter("oauth_token");
				Log.i("TeleTweet", "Token is " + authToken);
				i.putExtra(TwitterAuthenticationActivity.URL,
						rqTok.getAuthorizationURL()
								+ "&oauth_callback=http://www.oob.com");
				((Activity) context).startActivityForResult(i, 5);
			}
		}.execute();
	}

	public static void OauthComplete(final Intent data) {
		Log.i("TeleTweet", "Hey, I got here!");

		new AsyncTask<Void, Void, AccessToken>() {
			@Override
			public AccessToken doInBackground(Void... blah) {
				try {
					return twit.getOAuthAccessToken(rqTok,
							data.getStringExtra("oauth_verifier"));
				} catch (TwitterException e) {
					Log.i("TeleTweet",
							"Man, I couldn't even get the auth token. "
									+ e.getMessage());
					return null;
				}
			}

			@Override
			public void onPostExecute(AccessToken tok) {
				Log.i("TeleTweet", "C'mon c'mon c'mon");
				twit.setOAuthAccessToken(tok);
				authenticated = true;
				twit.search(new Query("foo"));
			}
		}.execute();
	}

	public static void search(Query query, final SearchCallback scb) {
		TwitterListener li = new TwitterAdapter() {
			// Um.
			// So I'm a fan of anonymous stuff, and I'd like if you could pass a
			// listener in
			// with each search call.
			// But even as-is, I'm not sure how twitter4j would handle multiple
			// listeners.
			// Hence this hack.
			private boolean run = false;

			@Override
			public void searched(QueryResult result) {
				if (run) {
					return;
				}
				scb.searched(result);
				run = true;
				// List<Status> twits = result.getTweets();
				// Log.i("TeleTweet","Got "+twits.size()+" tweets");
				// Log.i("TeleTweet","Tweet 1 is "+twits.get(0).getText());
			}

			public void onException(TwitterException e, int method) {
				if (run) {
					return;
				}
				run = true;
				Log.e("TeleTweet", "Crap, the " + method + " method broke. "
						+ e.getErrorMessage());
			}
		};

		// make tweets!

		twit.addListener(li);
		twit.search(query);
	}

	public interface SearchCallback {
		public void searched(QueryResult result);
	}

	// setup listeners
	/*
	 * TwitterListener li = new TwitterAdapter() {
	 * 
	 * @Override public void searched(QueryResult result) { List<Status> twits =
	 * result.getTweets(); Log.i("TeleTweet","Got "+twits.size()+" tweets");
	 * Log.i("TeleTweet","Tweet 1 is "+twits.get(0).getText()); }
	 * 
	 * @Override public void onException(TwitterException e, int method) {
	 * Log.e("TeleTweet",
	 * "Crap, the "+method+" method broke. "+e.getErrorMessage()); } };
	 */

	// make tweets!

	// twit.addListener(li);

	// open authentication view

	/* twit.search(new Query("foo")); */
}
