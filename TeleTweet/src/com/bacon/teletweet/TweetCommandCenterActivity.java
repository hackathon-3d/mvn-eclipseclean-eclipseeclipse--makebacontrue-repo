package com.bacon.teletweet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TweetCommandCenterActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_command_center);
		
		//setup page adapter
		ViewPager v = (ViewPager)findViewById(R.id.tweetdecks);
		v.setAdapter(adapter);
	}
	
	private PagerAdapter adapter = new PagerAdapter(){
		@Override public Object instantiateItem(ViewGroup vg, int pos)
		{
			TextView test = new TextView(TweetCommandCenterActivity.this);
			test.setText("Yo tweets");
			test.setTextSize(20);
			vg.addView(test);
			return test;
		}
		
		@Override public void destroyItem(ViewGroup vg, int pos, Object view)
		{
			vg.removeView((TextView)view);
		}
		
		@Override public int getCount(){
			return 5;
		}
		
		@Override public float getPageWidth(int pos)
		{
			return 0.4f;
		}
		
		@Override public boolean isViewFromObject(View view, Object object)
		{
			return view==object;
		}
		
		@Override public void finishUpdate(ViewGroup vg){}
		//@Override public void startupdate(ViewGroup vg){}
		@Override public void restoreState(Parcelable p, ClassLoader cl){}
		@Override public Parcelable saveState(){return null;}
	};
}
