/*
 * This file is part of wger Android.
 *
 * wger Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * wger Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with wger Android. If not, see <http://www.gnu.org/licenses/>.
 */

package de.wger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainActivity extends Activity {	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WgerWebViewClient());
        myWebView.setDownloadListener(new DownloadListener() {
        	
           /*
            * Special care for the application downloads
            * 
            * This is needed because they are only available to logged in users
            * and we have to send the authentication cookie and handle the rest
            */
            public void onDownloadStart(String url,
                                        String userAgent,
                                        String contentDisposition,
                                        String mimetype,
                                        long contentLength) {
                                            String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
                                            String cookie = CookieManager.getInstance().getCookie(url);
                                            
                                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                            Request request = new Request(Uri.parse(url));
                                            request.setTitle(fileName);
                                            request.addRequestHeader("Cookie", cookie);
                                            dm.enqueue(request);

                                            // Open the download manager
                                            Intent downloadsIntent = new Intent();
                                            downloadsIntent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                                            startActivity(downloadsIntent);
                                        }
            });
        WebSettings webSettings = myWebView.getSettings();
        
        /*
         *  Change WebView UserAgent
         *  
         *  This is needed for example to hide the persona login button, which does
         *  not work when called from an app.
         */
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " WgerAndroidWebApp");
        

        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://wger.de/dashboard");
    }
}