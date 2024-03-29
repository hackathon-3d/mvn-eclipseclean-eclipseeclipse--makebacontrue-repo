package com.bacon.teletweet.Pojos;

import com.android.volley.*;
import javax.xml.xpath.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bacon.teletweet.Utility.authVars;
import java.io.StringReader;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

/**
 * Created by Droidweb on 8/23/13.
 */
public class Show implements Parcelable{
	private final String name;
	private String description;
	private String imageURL;
	private Bitmap bitmap;
	private String id;
	private final RequestQueue mRequestQueue;
	private static final String TVDBUrl = "http://thetvdb.com/api/GetSeries.php";
	// http://api.fanart.tv/webservice/series/apikey/thetvdb_id/format/type/sort/limit/
	private static final String FANARTUrlStub = "http://api.fanart.tv/webservice/series/";
	private static final String DESCRIPTION_XPATH = "//Overview";
	private static final String ID_XPATH = "//id";

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Show(String showName, Context context) {
		this.name = showName;
		mRequestQueue = Volley.newRequestQueue(context);

	}

	public void loadShowInfo(final CallbackInterface hollabackgirl) {
		final ErrorListener EL = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// handle error response
				// TODO: HANDLE THAT ERROR
			}
		};
		StringRequest request = new StringRequest(Request.Method.GET, TVDBUrl
				+ "?seriesname=" + Uri.encode(this.name),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						InputSource source = new InputSource(new StringReader(
								response));
						XPath xPath = XPathFactory.newInstance().newXPath();
						try {
							description = (String) xPath.evaluate(
									DESCRIPTION_XPATH, source,
									XPathConstants.STRING);
							source = new InputSource(new StringReader(response));
							id = (String) xPath.evaluate(ID_XPATH, source,
									XPathConstants.STRING);
						} catch (XPathExpressionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getImage();
					}

					private void getImage() {
						// get the fanart url
						StringRequest imgRequest = new StringRequest(
								FANARTUrlStub + authVars.FAN_ART_TV_API_KEY
										+ "/" + id + "/json/tvthumb/1/1",
								new Response.Listener<String>() {
									@Override
									public void onResponse(String response) {
										try {
											JSONObject jObject = new JSONObject(
													response);
											Iterator<String> keys = jObject
													.keys();
											jObject = jObject
													.getJSONObject(keys.next());
											JSONArray jArray = jObject
													.getJSONArray("tvthumb");
											jObject = jArray.getJSONObject(0);
											imageURL = jObject.getString("url");
											Log.d("IMAGE", imageURL);
											getImageFromURL();
										} catch (JSONException e) {
											// TODO Auto-generated catch
											// block
											e.printStackTrace();
										}

									}

									private void getImageFromURL() {
										// TODO Auto-generated method stub
										ImageRequest iRequest = new ImageRequest(
												imageURL,
												new Listener<Bitmap>() {
													@Override
													public void onResponse(
															Bitmap response) {
														Log.d("REPSONSE",
																"Got hte response");
														bitmap = response;
														// make callback
														hollabackgirl
																.showLoaded(Show.this);
													}
												}, 0, 0,
												Bitmap.Config.ARGB_8888, EL);
										mRequestQueue.add(iRequest);
									}
								}, EL);
						mRequestQueue.add(imgRequest);
						// call this at the end
					}
				}, EL);
		mRequestQueue.add(request);

	}

	// will add more as needed

	public interface CallbackInterface {
		public void showLoaded(Show show);
	}
	
		/* Parcelling */
	public void writeToParcel(Parcel toParcel, int flags)
	{
	/*private final String name;
	private String description;
	private String imageURL;
	private Bitmap bitmap;
	private String id;*/
		toParcel.writeString(name);
		toParcel.writeString(description);
		toParcel.writeString(imageURL);
		toParcel.writeString(id);
		//bitmap.writeToParcel(toParcel, 0);
	}

	public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>()
	{
		@Override
		public Show createFromParcel(Parcel in)
		{
			try
			{
				Show s = new Show(in.readString());
				s.setDescription(in.readString());
				s.setImageURL(in.readString());
				s.setId(in.readString());
				//in.setDataPosition(0);
				//s.setBitmap(Bitmap.CREATOR.createFromParcel(in));
				return s;
			}
			catch(Exception e)
			{
				Log.e("TeleTweet","Bad news, dude, Show didn't Parcel correctly!");
				return null;
			}
		}

		@Override public Show[] newArray(int size){return new Show[size];}
	};

	private Show(String pName){name=pName;mRequestQueue=null;}
	private void setDescription(String pDesc){description=pDesc;}
	private void setImageURL(String pURL){imageURL=pURL;}
	private void setId(String pId){id=pId;}
	
	public String getName(){return name;}
	public String getDescription(){return description;}
	public String getImageURL(){return imageURL;}
	@Override public int describeContents(){return 0;}
}
