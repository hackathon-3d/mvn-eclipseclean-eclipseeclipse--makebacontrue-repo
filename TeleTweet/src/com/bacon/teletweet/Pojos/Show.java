package com.bacon.teletweet.Pojos;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bacon.teletweet.Utility.authVars;

/**
 * Created by Droidweb on 8/23/13.
 */
public class Show {
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

	public void loadShowInfo(CallbackInterface hollabackgirl) {
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
										+ "/" + id + "/json/clearlogo/1/1",
								new Response.Listener<String>() {
									@Override
									public void onResponse(String response) {
										Log.d("IMAGE RESPONSE", response);
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
}
