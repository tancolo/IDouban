package com.shrimpcolo.johnnytam.idouban.activity.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void initVariables() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        final String url = intent.getStringExtra("website");
        Log.e(HomeActivity.TAG, "===> website = " + url);

        WebView webView = (WebView) findViewById(R.id.douban_webview);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }
}
