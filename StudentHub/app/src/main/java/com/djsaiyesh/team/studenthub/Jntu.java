package com.djsaiyesh.team.studenthub;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class Jntu extends Fragment {


    public Jntu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View x = inflater.inflate(R.layout.fragment_jntu, container, false);
        WebView mWebView = (WebView) x.findViewById(R.id.webview1);

        mWebView.getSettings().setBuiltInZoomControls(true);
        String url = "http://202.63.105.185/RESULT/front.jsp";
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

        return x;
    }

    

}




