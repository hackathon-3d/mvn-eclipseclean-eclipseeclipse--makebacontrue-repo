import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 
public class TwitterWebView extends Activity {
  
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
  Log.d(TwitterWebView.class.getName(), "Loading Url: " + getIntent().getStringExtra(URL));
  webView.loadUrl(getIntent().getStringExtra(URL));
  setContentView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
 }
  
 private void checkURL(String url) {
  if(url.contains(getString(R.string.twitter_callbackGranted))) {
   Uri uri = Uri.parse(url);
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            Intent i = new Intent();
            i.putExtra("oauth_verifier", oauthVerifier);
            Log.d(TwitterWebView.class.getName(), "OK, Url: " + url);
            setResult(RESULT_OK, i);
            finish();
        }
  else if(url.contains(getString(R.string.twitter_callbackDenied))) {
   Log.d(TwitterWebView.class.getName(), "Unauthorized, Url: " + url);
            setResult(RESULT_FIRST_USER);
            finish();
  }
  //Banned URLs
  else if(url.contains(getString(R.string.twitter_home))    ||
    url.contains(getString(R.string.twitter_appSettings))  ||
    url.contains(getString(R.string.twitter_tos))    ||
    url.contains(getString(R.string.twitter_privacy))   ||
    url.contains(getString(R.string.website)))     {
   Log.d(TwitterWebView.class.getName(), "Unauthorized, Url: " + url);
            setResult(RESULT_FIRST_USER);
            finish();
  }
  else {
   Log.d(TwitterWebView.class.getName(), "Passed Url: " + url);
  }
   
 }
  
 @Override
 public void onBackPressed() {
  super.onBackPressed();
  setResult(RESULT_FIRST_USER);
        finish();
 }
 
}