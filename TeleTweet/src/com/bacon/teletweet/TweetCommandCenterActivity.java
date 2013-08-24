package com.bacon.teletweet;

import android.widget.*;
import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.bacon.teletweet.Pojos.Show;
import com.bacon.teletweet.Utility.HashtagKeeper;
import com.bacon.teletweet.Utility.StartingTags;
import com.bacon.teletweet.Utility.TwitterUtil;
import twitter4j.QueryResult;

public class TweetCommandCenterActivity extends Activity {

	private Map<String, QueryResult> tweetLists;
	private List<String> hashtags;
	private Show show;
	private Handler refresher = new Handler();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_command_center);
		
		tweetLists = new HashMap<String, QueryResult>(5);
		
		Bundle b = getIntent().getExtras();
		show = b.getParcelable("Show");
		
		hashtags = new ArrayList<String>(5);
		buildHashTags();
		
		search();
		
		TextView showDesc = (TextView)findViewById(R.id.showDescription);
		showDesc.setText(show.getDescription());
		
		//setup page adapter
		ViewPager v = (ViewPager)findViewById(R.id.tweetdecks);
		v.setPageMargin(15);
		v.setAdapter(adapter);
	}
	
	private void search()
	{
		TwitterUtil.search(hashtags, new TwitterUtil.SearchCallback(){
				@Override
				public void searched(Map<String, QueryResult> results)
				{
					Log.i("TeleTweet","THAT'S how you do Async, bitches.");
					tweetLists.clear();
					tweetLists.putAll(results);
					adapter.notifyDataSetChanged();
				}
			});
			
		//start again soon;
		refresher.postDelayed(refresh, 10000);
	}
	
	private Runnable refresh = new Runnable(){
			@Override
			public void run(){search();}
		};
		
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		refresher.removeCallbacks(refresh);
	}
	
	private void buildHashTags()
	{
		hashtags.clear();
		
		if(show == null)
		{Log.i("TeleTweet","Ooops!");return;}
		
		List<String> tags = HashtagKeeper.getHashtagsForShow(TweetCommandCenterActivity.this, show.getName());
		if(tags != null && tags.size() > 0)
		{hashtags = tags; return;}
		else
		{
			hashtags = StartingTags.getStartingTags(show.getName());
		}
	}
	
	public void startTweet(String tag)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter tweet");
		final EditText input = new EditText(this);
		input.setText(tag);
		input.setSelection(0);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		builder.setPositiveButton("Send", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					String tweet = input.getText().toString();
					if(tweet.length() > 140){tweet = tweet.substring(0,140);}
					TwitterUtil.sendTweet(tweet);
				}
			});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
				}
			});

		builder.create().show();
	}
	
	public void addNewHashtag()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter hashtag");
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String tag = input.getText().toString();
				if(!tag.contains("#")){tag = "#"+tag;}
				hashtags.add(tag);
				HashtagKeeper.storeHashtagsForShow(TweetCommandCenterActivity.this, show.getName(), hashtags);
				adapter.notifyDataSetChanged();
				search();
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		
		builder.create().show();
	}
	
	private PagerAdapter adapter = new PagerAdapter(){
		@Override public Object instantiateItem(ViewGroup vg, final int pos)
		{
			int grayColor = 0xFF222222;
			int lightColor = 0xFFEEEEEE;
			int miniGray = 0xFF555555;
			int miniLight = 0xFFBBBBBB;
			
			RelativeLayout rl = new RelativeLayout(TweetCommandCenterActivity.this);
			RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
			//rlParams.setMargins(10,10,10,10);
			rl.setBackgroundColor(pos%2==0?grayColor:lightColor);
			rl.setLayoutParams(rlParams);
			
			if(pos < hashtags.size())
			{
				LinearLayout hashTagRow = new LinearLayout(TweetCommandCenterActivity.this);
				hashTagRow.setId(555);
				hashTagRow.setOrientation(LinearLayout.HORIZONTAL);
				hashTagRow.setBackgroundColor(pos%2==0?miniGray:miniLight);
				rl.addView(hashTagRow);
				
				TextView queryName = new TextView(TweetCommandCenterActivity.this);
				queryName.setText(hashtags.get(pos));
				queryName.setTextSize(28);
				queryName.setTextColor(pos%2==0?lightColor:grayColor);
				hashTagRow.addView(queryName);
				
				ImageView shareView = new ImageView(TweetCommandCenterActivity.this);
				shareView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_share));
				
				View.OnClickListener listener = new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						startTweet(hashtags.get(pos));
					}
				};
				
				shareView.setOnClickListener(listener);
				queryName.setOnClickListener(listener);
				
				hashTagRow.addView(shareView);
				
				View v = new View(TweetCommandCenterActivity.this);
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,0,1);
				v.setLayoutParams(p);
				hashTagRow.addView(v);
				
				ImageView iv = new ImageView(TweetCommandCenterActivity.this);
				iv.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
				iv.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						hashtags.remove(hashtags.get(pos));
						HashtagKeeper.storeHashtagsForShow(TweetCommandCenterActivity.this, show.getName(), hashtags);
						adapter.notifyDataSetChanged();
					}
				});
				hashTagRow.addView(iv);
				
				if(tweetLists != null && tweetLists.get(hashtags.get(pos)) != null)
				{
					QueryResult tweets = tweetLists.get(hashtags.get(pos));
				
					ListView lv = new ListView(TweetCommandCenterActivity.this);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
					params.addRule(RelativeLayout.BELOW, hashTagRow.getId());
					lv.setLayoutParams(params);
				
					lv.setAdapter(new TweetDeckListAdapter(TweetCommandCenterActivity.this, tweets.getTweets(), pos%2==0));
				
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
			}
			else
			{
				rl.setClickable(true);
				rl.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						addNewHashtag();
					}
				});
				
				TextView tv = new TextView(TweetCommandCenterActivity.this);
				tv.setText("Tap to add hashtag!");
				tv.setTextSize(20);
				tv.setTextColor(pos%2==0?lightColor:grayColor);
				rl.addView(tv);
			}
			
			vg.addView(rl);

			return rl;
		}
		
		@Override public void destroyItem(ViewGroup vg, int pos, Object view)
		{
			vg.removeView((RelativeLayout)view);
		}
		
		@Override public int getCount(){
			return hashtags.size()+1;
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
