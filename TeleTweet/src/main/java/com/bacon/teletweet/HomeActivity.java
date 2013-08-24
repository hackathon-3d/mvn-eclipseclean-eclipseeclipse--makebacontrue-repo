package com.bacon.teletweet;

import twitter4j.*;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import com.bacon.teletweet.Utility.authVars;
import java.util.List;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class HomeActivity extends Activity {

	private AsyncTwitter twit;
	private RequestToken rqTok;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		//setup config
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(authVars.CONSUMER_KEY).setOAuthConsumerSecret(authVars.CONSUMER_SECRET);
		
		//setup listeners
		TwitterListener li = new TwitterAdapter()
		{
			@Override public void searched(QueryResult result)
			{
				List<Status> twits = result.getTweets();
				Log.i("TeleTweet","Got "+twits.size()+" tweets");
				Log.i("TeleTweet","Tweet 1 is "+twits.get(0).getText());
			}
			
			@Override public void onException(TwitterException e, int method)
			{
				Log.e("TeleTweet", "Crap, the "+method+" method broke. "+e.getErrorMessage());
			}
		};
		
		//make tweets!
		twit = new AsyncTwitterFactory(cb.build()).getInstance();
		twit.addListener(li);
		
		//open authentication view
		
		new AsyncTask<Void, Void, RequestToken>(){
			@Override public RequestToken doInBackground(Void... blah)
			{
				try
				{
					return twit.getOAuthRequestToken();
				}
				catch (TwitterException e)
				{
					Log.i("TeleTweet","Man, I couldn't even launch the activity. "+e.getMessage());
					return null;
				}
			}
			
			@Override public void onPostExecute(RequestToken tok)
			{
				Log.i("TeleTweet","Wow, that works?");
				AsyncDone(tok);
			}
		}.execute();
		
		/*twit.search(new Query("foo"));*/
		Log.i("TeleTweet","Welp, here goes nothing!");
	}
	
	public void AsyncDone(RequestToken t)
	{
		rqTok = t;
		Intent i = new Intent(this, TwitterAuthenticationActivity.class);
		String authToken = Uri.parse(rqTok.getAuthorizationURL()).getQueryParameter("oauth_token");
		Log.i("TeleTweet","Token is "+authToken);
		i.putExtra(TwitterAuthenticationActivity.URL, rqTok.getAuthorizationURL()+"&oauth_callback=http://www.oob.com");
		startActivityForResult(i, 5);
	}
	
	@Override
	public void onActivityResult(int request, int result, final Intent data)
	{
		super.onActivityResult(request, result, data);
		Log.i("TeleTweet","Hey, I got here!");
		
			new AsyncTask<Void, Void, AccessToken>(){
				@Override public AccessToken doInBackground(Void... blah)
				{
					try
					{
						return twit.getOAuthAccessToken(rqTok, data.getStringExtra("oauth_verifier"));
					}
					catch (TwitterException e)
					{
						Log.i("TeleTweet","Man, I couldn't even get the auth token. "+e.getMessage());
						return null;
					}
				}

				@Override public void onPostExecute(AccessToken tok)
				{
					Log.i("TeleTweet","C'mon c'mon c'mon");
					OauthAccessDone(tok);
				}
			}.execute();
	}
	
	public void OauthAccessDone(AccessToken t)
	{
		twit.setOAuthAccessToken(t);
		twit.search(new Query("foo"));
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
