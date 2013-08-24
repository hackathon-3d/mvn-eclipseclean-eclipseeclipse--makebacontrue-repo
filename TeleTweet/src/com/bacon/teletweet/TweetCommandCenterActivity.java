package com.bacon.teletweet;

import java.util.*;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bacon.teletweet.Utility.TwitterUtil;
import twitter4j.QueryResult;
import twitter4j.Status;

public class TweetCommandCenterActivity extends Activity {

	private Map<String, QueryResult> tweetLists;
	private List<String> hashtags;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_command_center);
		
		tweetLists = new HashMap<String, QueryResult>(5);
		
		hashtags = new ArrayList<String>(5);
		hashtags.add("#BreakingBad");
		hashtags.add("#WalterWhite");
		hashtags.add("#bryancranston");
		hashtags.add("#BBWalterWhite");
		
		TwitterUtil.search(hashtags, new TwitterUtil.SearchCallback(){
			@Override
			public void searched(Map<String, QueryResult> results)
			{
				Log.i("TeleTweet","THAT'S how you do Async, bitches.");
				tweetLists = results;
				adapter.notifyDataSetChanged();
			}
		});
		
		//setup page adapter
		ViewPager v = (ViewPager)findViewById(R.id.tweetdecks);
		v.setAdapter(adapter);
	}
	
	private PagerAdapter adapter = new PagerAdapter(){
		@Override public Object instantiateItem(ViewGroup vg, int pos)
		{
			LinearLayout ll = new LinearLayout(TweetCommandCenterActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			
			TextView test = new TextView(TweetCommandCenterActivity.this);
			test.setText(hashtags.get(pos));
			test.setTextSize(20);
			ll.addView(test);
			
			TextView firstTweet = new TextView(TweetCommandCenterActivity.this);
			List<Status> tweetList = tweetLists.get(hashtags.get(pos)).getTweets();
			if(tweetList.size() > 0)
			{
				Status s = tweetList.get(0);
				firstTweet.setText(s.getText());
			}
			else
			{
				Log.i("TeleTweet","Query "+hashtags.get(pos)+" has no tweets!");
			}
			ll.addView(firstTweet);
			
			vg.addView(ll);
			
			return ll;
		}
		
		@Override public void destroyItem(ViewGroup vg, int pos, Object view)
		{
			vg.removeView((LinearLayout)view);
		}
		
		@Override public int getCount(){
			return tweetLists.size();
		}
		
		@Override public float getPageWidth(int pos)
		{
			return 0.37f;
		}
		
		@Override public boolean isViewFromObject(View view, Object object)
		{
			return view==object;
		}
		
		@Override public void finishUpdate(ViewGroup vg){}
		@Override public void startupdate(ViewGroup vg){}
		@Override public void restoreState(Parcelable p, ClassLoader cl){}
		@Override public Parcelable saveState(){return null;}
	};
}
