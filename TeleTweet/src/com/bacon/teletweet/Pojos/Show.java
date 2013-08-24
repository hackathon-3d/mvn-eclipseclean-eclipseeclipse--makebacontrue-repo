package com.bacon.teletweet.Pojos;

import android.graphics.Bitmap;

/**
 * Created by Droidweb on 8/23/13.
 */
public class Show {
	private final String name;
	private String description;
	private String imageURL;
	private Bitmap bitmap;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Show(String showName) {
		this.name = showName;
	}

	public void loadShowInfo(CallbackInterface hollabackgirl) {

		// call this at the end
		hollabackgirl.showLoaded(this);
	}

	// will add more as needed

	public interface CallbackInterface {
		public void showLoaded(Show show);
	}
}
