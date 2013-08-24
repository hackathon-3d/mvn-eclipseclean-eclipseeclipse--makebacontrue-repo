package com.bacon.teletweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import com.bacon.teletweet.Utility.TwitterUtil;

public class HomeActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_splash);
		
		TwitterUtil.initAndAwaitAuthenticationResult(this);
	}
	
	@Override public void onActivityResult(int request, int result, Intent i)
	{
		TwitterUtil.OauthComplete(i);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
