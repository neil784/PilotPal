package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TafWebview extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_taf_webview);
        webView = findViewById(R.id.webview);
        Bundle extras = getIntent().getExtras();
        String icao = extras.getString("ICAO");
        String url = "https://www.checkwx.com/weather/" + icao + "/taf";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVisibility(View.INVISIBLE); // Hide the WebView initially

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Called when the WebView starts loading a new page
                webView.setVisibility(View.INVISIBLE); // Hide the WebView
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Called when the WebView finishes loading a page
                view.loadUrl("javascript:(function() { " +
                        "var element = document.getElementsByClassName('max-w-8xl mx-auto p-4 md:px-6 flex flex-col')[0]; " +
                        "document.body.innerHTML = element.outerHTML; })();");
                webView.setVisibility(View.VISIBLE); // Show the WebView after the page has finished loading
            }
        });
        webView.loadUrl(url);
    }
}
