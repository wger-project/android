package de.wger;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//private static final String TAG = "MyActivity";

class WgerWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	/*
    	 * Load all external links with the system's browser
    	 */
    	//Log.v("test", Uri.parse(url).getHost());
        if (Uri.parse(url).getHost().equals("trurl.lem")) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
