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
import com.example.pilotpal.databinding.ActivityAirportWebviewBinding;
import java.util.Objects;

public class AirportWebview extends AppCompatActivity {
    ActivityAirportWebviewBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityAirportWebviewBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        Bundle extras = getIntent().getExtras();
        String icao = extras.getString("ICAO");
        String url = "https://www.checkwx.com/weather/" + icao;
        view.webView.getSettings().setJavaScriptEnabled(true);
        view.webView.setVisibility(View.INVISIBLE); // Hide the WebView initially

        view.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webview, String url, Bitmap favicon) {
                // Called when the WebView starts loading a new page
                view.webView.setVisibility(View.INVISIBLE); // Hide the WebView
            }
            @Override
            public void onPageFinished(WebView webview, String url) {
                // Called when the WebView finishes loading a page
                webview.loadUrl("javascript:(function() { " +
                        "var element = document.getElementsByClassName('max-w-8xl mx-auto p-4 md:px-6 flex flex-col')[0]; " +
                        "document.body.innerHTML = element.outerHTML; })();");
                view.webView.setVisibility(View.VISIBLE); // Show the WebView after the page has finished loading
            }
        });
        view.webView.loadUrl(url);
    }
}
