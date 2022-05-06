package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MetarWebview extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_metar_webview);
        webView = findViewById(R.id.webview);
        Bundle extras = getIntent().getExtras();
        String icao = extras.getString("ICAO");
        String url = "https://www.checkwx.com/weather/"
                + icao
                + "/metar";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('navbar navbar-expand-lg navbar-dark bg-dark')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('mt-5 pt-5 footer')[0].style.display='none'; " +
                        "})()");
            }
        });
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });*/
        webView.loadUrl(url);
    }
}