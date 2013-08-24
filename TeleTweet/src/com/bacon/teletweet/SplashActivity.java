package com.bacon.teletweet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.bacon.teletweet.Pojos.Show;

public class SplashActivity extends Activity {
	private static final String SHOW_LISTING_STRING = "showlisting";
	ArrayList<Show> showsList = new ArrayList<Show>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.home_splash);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
		try {

			// Parse that JSON!
			JSONObject jObject = new JSONObject(
					byteArrayOutputStream.toString());
			JSONArray jArray = jObject.getJSONArray(SHOW_LISTING_STRING);
			Log.d("Size", Integer.toString(jArray.length()));
			for (int i = 0; i < jArray.length(); i++) {
				final int _i = i;
				String showName = jArray.getString(i);
				// make your show object
				Show tmpShow = new Show(showName, this);
				tmpShow.loadShowInfo(new Show.CallbackInterface() {

					@Override
					public void showLoaded(Show show) {
						// TODO Auto-generated method stub
						updateHollywoodSquare(_i, show);
					}
				});
				showsList.add(tmpShow);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gotoCommandCenter(Show s) {
		if (s == null) {
			Log.i("TeleTweet", "Show was null!");
			return;
		}

		Intent i = new Intent(this, TweetCommandCenterActivity.class);
		i.putExtra("Show", s);
		startActivity(i, null);
		Log.i("TeleTweet", "Starting yaay");
	}

	public void updateHollywoodSquare(Integer position, Show loadedShow) {
		ImageButton button = new ImageButton(this);
		switch (position) {
		case 0:
			button = (ImageButton) findViewById(R.id.slot1);
			break;
		case 1:
			button = (ImageButton) findViewById(R.id.slot2);
			break;
		case 2:
			button = (ImageButton) findViewById(R.id.slot3);
			break;
		case 3:
			button = (ImageButton) findViewById(R.id.slot4);
			break;
		case 4:
			button = (ImageButton) findViewById(R.id.slot5);
			break;
		case 5:
			button = (ImageButton) findViewById(R.id.slot6);
			break;
		case 6:
			button = (ImageButton) findViewById(R.id.slot7);
			break;
		case 7:
			button = (ImageButton) findViewById(R.id.slot8);
			break;
		case 8:
			button = (ImageButton) findViewById(R.id.slot9);
			break;
		case 9:
			button = (ImageButton) findViewById(R.id.slot10);
			break;
		default:
			break;
		}
		Log.d("Showing", "image");
		button.setImageBitmap(loadedShow.getBitmap());
		button.setTag(showsList.get(position));
		final ImageButton buttonRef = button;
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoCommandCenter((Show) buttonRef.getTag());
			}
		});
	}
}
