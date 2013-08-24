package com.bacon.teletweet.Utility;

import twitter4j.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.bacon.teletweet.TwitterAuthenticationActivity;
import com.google.gag.annotation.remark.Facepalm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {
	private static Twitter twit;
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

		twit = new TwitterFactory(cb.build()).getInstance();

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
				String authToken = Uri.parse(rqTok.getAuthorizationURL()).getQueryParameter("oauth_token");
				Log.i("TeleTweet", "Token is " + authToken);
				i.putExtra(TwitterAuthenticationActivity.URL,
						rqTok.getAuthorizationURL()
								+ "&oauth_callback=http://www.oob.com");
				((Activity) context).startActivityForResult(i, 5);
			}
		}.execute();
	}

	public static void OauthComplete(final Callback cb, final Intent data) {
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
				cb.callBack();
			}
		}.execute();
	}

	public static void search(List<String> queries, final SearchCallback scb) {
		
		new AsyncTask<String, Void, Map<String, QueryResult>>(){
			@Override
			public Map<String, QueryResult> doInBackground(String... queries)
			{
				Map<String, QueryResult> results = new HashMap<String, QueryResult>(2);
				try
				{
					for(String s : queries)
					{
						results.put(s, twit.search(new Query(s)));
					}
				}
				catch(Exception e)
				{
					Log.e("TeleTweet","Searching failed in TwitterUtil! "+e.getMessage());
				}
				return results;
			}
			
			@Override
			public void onPostExecute(Map<String, QueryResult> results)
			{
				scb.searched(results);
			}
			
		}.execute(queries.toArray(new String[0]));
	}

	public interface SearchCallback {
		public void searched(Map<String, QueryResult> results);
	}
}
