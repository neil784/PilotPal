package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.pilotpal.databinding.ActivityMainMenuBinding;
import java.util.Objects;

public class MainMenu extends AppCompatActivity {
    ActivityMainMenuBinding view;
    boolean backPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityMainMenuBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.weatherAndAirportButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, Metar.class);
            startActivity(intent);
        });
        view.windCalculatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, WindCalculator.class);
            startActivity(intent);
        });
        view.flightTimeAndFuelButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, Fueltime.class);
            startActivity(intent);
        });
        view.descentCalculatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, DescentCalculator.class);
            startActivity(intent);
        });
        view.specialOpsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, SpecialOps.class);
            startActivity(intent);
        });
        view.phoneticAlphabetButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, PhoneticAlphabet.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            finishAffinity();
            System.exit(0);
        }
        Toast.makeText(this, "Back one more time to exit", Toast.LENGTH_SHORT).show();
        backPressed = true;
    }
}
