package com.shrimpcolo.johnnytam.idouban.moviewebsite;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.base.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.utils.AppConstants;

public class WebViewActivity extends BaseActivity {

    private String mUrl;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {
        mUrl = getIntent().getStringExtra(AppConstants.INTENT_EXTRA_WEBSITE_URL);
        Log.e(HomeActivity.TAG, "===> website = " + mUrl);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movie_web_view);

        WebView webView = (WebView) findViewById(R.id.douban_webview);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(mUrl);
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
