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
		
		Status tweet = getItem(position);
		
		ViewHolder vh = (ViewHolder)convertView.getTag();
		TextView username = vh.getUserName();
		TextView userhandle = vh.getUserHandle();
		TextView tweetTime = vh.getTweetTime();
		TextView tweetBody = vh.getTweetBody();
		
		username.setText(tweet.getUser().getName());
		userhandle.setText("@"+tweet.getUser().getScreenName());
		tweetTime.setText(tweet.getCreatedAt().toLocaleString());
		tweetBody.setText(tweet.getText());
		return convertView;
	}
	
	public class ViewHolder
	{
		private TextView username;
		private TextView userhandle;
		private TextView tweetTime;
		private TextView tweetBody;
		
		public ViewHolder(View v){
			username = (TextView)v.findViewById(R.id.username);
			userhandle = (TextView)v.findViewById(R.id.userhandle);
			tweetTime = (TextView)v.findViewById(R.id.tweetTime);
			tweetBody = (TextView)v.findViewById(R.id.tweetBody);
		}
		
		public TextView getUserName(){return username;}
		public TextView getUserHandle(){return userhandle;}
		public TextView getTweetTime(){return tweetTime;}
		public TextView getTweetBody(){return tweetBody;}
	}
}
