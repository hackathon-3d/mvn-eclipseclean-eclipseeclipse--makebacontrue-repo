package com.bacon.teletweet.Pojos;

/**
 * Created by Droidweb on 8/23/13.
 */
public class Show {
	private final String name;
	private String description;
	private String imageURL;
	private final boolean gotInfo;

	public Show(String showName) {
		gotInfo = false;
		this.name = showName;
	}

	public void loadShowInfo() {

	}
	// will add more as needed
}
