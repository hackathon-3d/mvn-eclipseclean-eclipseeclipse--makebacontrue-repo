package com.bacon.teletweet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import twitter4j.Status;

public class TweetDeckListAdapter extends BaseAdapter
{
	private List<Status> tweets;
	private Context c;
	
	public TweetDeckListAdapter(Context pContext, List<Status> pTweets)
	{
		tweets = pTweets;
		c = pContext;
	}
	
	@Override 
	public int getCount()
	{
		return tweets.size();
	}
	
	@Override
	public Status getItem(int position)
	{
		return tweets.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
		{
			convertView = LayoutInflater.from(c).inflate(R.layout.tweet, parent, false);
			convertView.setTag(new ViewHolder(convertView));
		}
		
		ViewHolder vh = (ViewHolder)convertView.getTag();
		TextView tweetBody = vh.getTweetBody();
		Status tweet = getItem(position);
		tweetBody.setText(tweet.getText());
		return convertView;
	}
	
	public class ViewHolder
	{
		private TextView tv;
		public ViewHolder(View v){tv = (TextView)v.findViewById(R.id.tweetBody);}
		public TextView getTweetBody(){return tv;}
	}
}
