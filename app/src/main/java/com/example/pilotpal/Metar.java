package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.example.pilotpal.databinding.ActivityMetarBinding;
import java.util.Objects;

public class Metar extends AppCompatActivity {
    ActivityMetarBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityMetarBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.goback.setOnClickListener(v -> {
            Intent intent = new Intent(Metar.this, MainMenu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        view.searchMetarButton.setOnClickListener(v -> {
            String icao = Objects.requireNonNull(view.icaoEditText.getText()).toString().toLowerCase().trim();
            if (icao.isEmpty()) {
                view.icaoInputLayout.setError("Please enter an airport ICAO code");
            } else if (icao.length() != 4) {
                view.icaoInputLayout.setError("Please enter a valid 4 character ICAO code");
            } else {
                view.icaoInputLayout.setError(null);
                Intent intent = new Intent(Metar.this, MetarWebview.class);
                intent.putExtra("ICAO", icao);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        view.searchTAFButton.setOnClickListener(v -> {
            String icao = Objects.requireNonNull(view.icaoEditText.getText()).toString().toLowerCase().trim();
            if (icao.isEmpty()) {
                view.icaoInputLayout.setError("Please enter an airport ICAO code");
            } else if (icao.length() != 4) {
                view.icaoInputLayout.setError("Please enter a valid 4 character ICAO code");
            } else {
                view.icaoInputLayout.setError(null);
                Intent intent = new Intent(Metar.this, TafWebview.class);
                intent.putExtra("ICAO", icao);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        view.searchAirportInfoButton.setOnClickListener(v -> {
            String icao = Objects.requireNonNull(view.icaoEditText.getText()).toString().toLowerCase().trim();
            if (icao.isEmpty()) {
                view.icaoInputLayout.setError("Please enter an airport ICAO code");
            } else if (icao.length() != 4) {
                view.icaoInputLayout.setError("Please enter a valid 4 character ICAO code");
            } else {
                view.icaoInputLayout.setError(null);
                Intent intent = new Intent(Metar.this, AirportWebview.class);
                intent.putExtra("ICAO", icao);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
