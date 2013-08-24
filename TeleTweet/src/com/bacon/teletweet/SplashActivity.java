package com.bacon.teletweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SplashActivity extends Activity
{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_splash);
		
		//setup button listeners
		TextView t = (TextView)findViewById(R.id.show1);
		t.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v)
			{
				gotoCommandCenter();
			}
		});
	}
	
	public void gotoCommandCenter()
	{
		startActivity(new Intent(this, TweetCommandCenterActivity.class),null);
	}
}
