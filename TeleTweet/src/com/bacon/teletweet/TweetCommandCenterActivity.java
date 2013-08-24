package com.bacon.teletweet;

import android.widget.*;
import java.util.*;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.bacon.teletweet.Utility.TwitterUtil;
import twitter4j.QueryResult;

public class TweetCommandCenterActivity extends Activity {

	private Map<String, QueryResult> tweetLists;
	private List<String> hashtags;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_command_center);
		
		tweetLists = new HashMap<String, QueryResult>(5);
		
		hashtags = new ArrayList<String>(5);
		buildHashTags();
		
		TwitterUtil.search(hashtags, new TwitterUtil.SearchCallback(){
			@Override
			public void searched(Map<String, QueryResult> results)
			{
				Log.i("TeleTweet","THAT'S how you do Async, bitches.");
				tweetLists.clear();
				tweetLists.putAll(results);
				buildHashTags();
				adapter.notifyDataSetChanged();
			}
		});
		
		//setup page adapter
		ViewPager v = (ViewPager)findViewById(R.id.tweetdecks);
		v.setAdapter(adapter);
	}
	
	private void buildHashTags()
	{
		hashtags.clear();
		hashtags.add("#BreakingBad");
		hashtags.add("#WalterWhite");
		hashtags.add("#bryancranston");
		hashtags.add("#BBWalterWhite");
	}
	
	private PagerAdapter adapter = new PagerAdapter(){
		@Override public Object instantiateItem(ViewGroup vg, int pos)
		{
			RelativeLayout rl = new RelativeLayout(TweetCommandCenterActivity.this);
			//rl.setOrientation(LinearLayout.VERTICAL);

			TextView queryName = new TextView(TweetCommandCenterActivity.this);
			queryName.setText(hashtags.get(pos));
			queryName.setTextSize(20);
			rl.addView(queryName);
			
			if(tweetLists != null && tweetLists.get(hashtags.get(pos)) != null)
			{
				QueryResult tweets = tweetLists.get(hashtags.get(pos));
			
				ListView lv = new ListView(TweetCommandCenterActivity.this);
				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
				lv.setLayoutParams(params);
			
				lv.setAdapter(new TweetDeckListAdapter(TweetCommandCenterActivity.this, tweets.getTweets()));
			
				rl.addView(lv);
			}
			else
			{
				Log.i("TeleTweet","Skipping "+hashtags.get(pos)+" for some reason");
				ProgressBar pb = new ProgressBar(TweetCommandCenterActivity.this);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				lp.addRule(RelativeLayout.CENTER_VERTICAL);
				pb.setLayoutParams(lp);
				rl.addView(pb);
			}
			
			vg.addView(rl);
			
			return rl;
		}
		
		@Override public void destroyItem(ViewGroup vg, int pos, Object view)
		{
			vg.removeView((RelativeLayout)view);
		}
		
		@Override public int getCount(){
			return hashtags.size();
		}
		
		@Override public int getItemPosition(Object obj){return POSITION_NONE;}
		
		@Override public float getPageWidth(int pos)
		{
			return 0.37f;
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
