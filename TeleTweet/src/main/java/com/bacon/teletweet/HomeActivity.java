package com.bacon.teletweet;

import twitter4j.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import java.util.List;
import twitter4j.conf.ConfigurationBuilder;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	@Override
	public void onResume()
	{
		super.onResume();
		
		//setup config
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("").setOAuthConsumerSecret("")
		.setOAuthAccessToken("").setOAuthAccessTokenSecret("");
		
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
		AsyncTwitter twit = new AsyncTwitterFactory(cb.build()).getInstance();
		twit.addListener(li);
		twit.search(new Query("foo"));
		Log.i("TeleTweet","Welp, here goes nothing!");
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
