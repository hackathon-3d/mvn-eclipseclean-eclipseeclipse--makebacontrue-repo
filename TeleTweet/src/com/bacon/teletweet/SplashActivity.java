package com.bacon.teletweet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.bacon.teletweet.Pojos.Show;

public class SplashActivity extends Activity {
	private static final String SHOW_LISTING_STRING = "showlisting";
	ArrayList<Show> showsList = new ArrayList<Show>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_splash);

		// lets start grabbing our movie images
		InputStream inputStream = getResources().openRawResource(
				R.raw.movieseed);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int ctr;
		try {
			ctr = inputStream.read();
			while (ctr != -1) {
				byteArrayOutputStream.write(ctr);
				ctr = inputStream.read();
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.v("Text Data", byteArrayOutputStream.toString());
		try {

			// Parse that JSON!
			JSONObject jObject = new JSONObject(
					byteArrayOutputStream.toString());
			JSONArray jArray = jObject.getJSONArray(SHOW_LISTING_STRING);
			Log.d("Size", Integer.toString(jArray.length()));
			for (int i = 0; i < jArray.length(); i++) {
				String showName = jArray.getString(i);
				showsList.add(new Show(showName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
