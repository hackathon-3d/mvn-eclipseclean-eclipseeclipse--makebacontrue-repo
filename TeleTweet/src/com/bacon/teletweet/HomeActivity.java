package com.bacon.teletweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.bacon.teletweet.Utility.Callback;
import com.bacon.teletweet.Utility.TwitterUtil;

public class HomeActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_login);
		
		Button b = (Button)findViewById(R.id.loginButton);
		b.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				login();
			}
		});
	}
	
	@Override public void onActivityResult(int request, int result, Intent i)
	{
		TwitterUtil.OauthComplete(new Callback(){
			@Override
			public void callBack()
			{
				//go to next screen
				startActivity(new Intent(HomeActivity.this, SplashActivity.class), null);
			}
		}, i);
	}
	
	private void login()
	{
		TwitterUtil.initAndAwaitAuthenticationResult(this);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
