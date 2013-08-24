package com.bacon.teletweet.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class HashtagKeeper
{
	private static String SP_KEY="YouWillNeverGussThisKeyInABajillionYearsOhWaitYouJustReadIt.";
	
	public static void storeHashtagsForShow(Context c, String showName, List<String> hashtags)
	{
		Log.i("TeleTweet","Saving hashtags for "+showName);
		Gson gson = new Gson();
		String json = gson.toJson(hashtags);
		SharedPreferences sp = c.getSharedPreferences(SP_KEY, 0);
		SharedPreferences.Editor e = sp.edit();
		Log.d("TeleTweet",json);
		e.putString(showName, json);
		e.commit();
	}
	
	public static List<String> getHashtagsForShow(Context c, String showName)
	{
		Log.i("TeleTweet","Getting hashtags for "+showName);
		SharedPreferences sp = c.getSharedPreferences(SP_KEY, 0);
		String json = sp.getString(showName, "{}");
		Gson gson = new Gson();
		if(json.equalsIgnoreCase("{}")){Log.i("TeleTweet","Fail");return new ArrayList<String>();}
		else{return gson.fromJson(json, new TypeToken<List<String>>(){}.getType());}
	}
}
