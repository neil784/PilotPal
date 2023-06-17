package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Metar extends AppCompatActivity {
    TextInputLayout icaoInputLayout;
    EditText icaoEditText;
    Button metarBTN;
    Button tafBTN;
    Button airportBTN;
    TextView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_metar);
        icaoInputLayout = findViewById(R.id.icaoInputLayout);
        icaoEditText = findViewById(R.id.icaoEditText);
        metarBTN = findViewById(R.id.metarBTN);
        tafBTN = findViewById(R.id.tafBTN);
        airportBTN = findViewById(R.id.airportBTN);
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Metar.this, MainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        metarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String icao = icaoEditText.getText().toString().toLowerCase().trim();
                if (icao.isEmpty()) {
                    icaoInputLayout.setError("Please enter an airport ICAO code");
                } else if (icao.length() != 4) {
                    icaoInputLayout.setError("Please enter a valid 4 character ICAO code");
                } else {
                    icaoInputLayout.setError(null);
                    Intent intent = new Intent(Metar.this, MetarWebview.class);
                    intent.putExtra("ICAO", icao);
                    startActivity(intent);
                }
            }
        });
        tafBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String icao = icaoEditText.getText().toString().toLowerCase().trim();
                if (icao.isEmpty()) {
                    icaoInputLayout.setError("Please enter an airport ICAO code");
                } else if (icao.length() != 4) {
                    icaoInputLayout.setError("Please enter a valid 4 character ICAO code");
                } else {
                    icaoInputLayout.setError(null);
                    Intent intent = new Intent(Metar.this, TafWebview.class);
                    intent.putExtra("ICAO", icao);
                    startActivity(intent);
                }
            }
        });
        airportBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String icao = icaoEditText.getText().toString().toLowerCase().trim();
                if (icao.isEmpty()) {
                    icaoInputLayout.setError("Please enter an airport ICAO code");
                } else if (icao.length() != 4) {
                    icaoInputLayout.setError("Please enter a valid 4 character ICAO code");
                } else {
                    icaoInputLayout.setError(null);
                    Intent intent = new Intent(Metar.this, AirportWebview.class);
                    intent.putExtra("ICAO", icao);
                    startActivity(intent);
                }
            }
        });
    }
}