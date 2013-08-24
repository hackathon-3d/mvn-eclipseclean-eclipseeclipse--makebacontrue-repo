package com.bacon.teletweet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.bacon.teletweet.R;
 
public class TwitterAuthenticationActivity extends Activity {
  
 final static String URL = "twitter_authorization_url";
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  WebView webView = new WebView(this);
   
  webView.getSettings().setJavaScriptEnabled(true);
  webView.getSettings().setAppCacheEnabled(false);
  webView.getSettings().setSaveFormData(false);
  webView.getSettings().setSavePassword(false);
  webView.setWebViewClient(new WebViewClient() {
   @Override
   public void onLoadResource(WebView view, String url) {
    checkURL(url);
   }
    
  });
  webView.loadUrl(getIntent().getStringExtra(URL));
  setContentView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
 }
  
 private void checkURL(String url) {
  if(url.contains("oauth_verifier=")) {
   Uri uri = Uri.parse(url);
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            Intent i = new Intent();
            i.putExtra("oauth_verifier", oauthVerifier);
            setResult(RESULT_OK, i);
            finish();
        }
  else if(url.contains("callback?denied")) {
            setResult(RESULT_FIRST_USER);
            finish();
  }
  //Banned URLs
  else if(url.contains("twitter.com/home")){
            setResult(RESULT_FIRST_USER);
            finish();
  }
  else {
  }
   
 }
  
 @Override
 public void onBackPressed() {
  super.onBackPressed();
  setResult(RESULT_FIRST_USER);
        finish();
 }
 
}
