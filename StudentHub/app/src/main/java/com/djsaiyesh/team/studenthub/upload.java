package com.djsaiyesh.team.studenthub;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class upload extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        WebView mWebView = (WebView)findViewById(R.id.webview1);
        mWebView.getSettings().setBuiltInZoomControls(true);
        String url = "https://studenthubdaljit.000webhostapp.com/uploadattenmarks.php";
        // probably a good idea to check it's not null, to avoid these situations:
        if(mWebView != null){
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }
}
