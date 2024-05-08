package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.example.pilotpal.databinding.ActivityDescentCalculatorBinding;
import java.util.Objects;

public class DescentCalculator extends AppCompatActivity {
    ActivityDescentCalculatorBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityDescentCalculatorBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.goback.setOnClickListener(v -> {
            Intent intent = new Intent(DescentCalculator.this, MainMenu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        view.calculateDescentDistanceButton.setOnClickListener(v -> {
            Intent intent = new Intent(DescentCalculator.this, DistanceActivity.class);
            startActivity(intent);
        });
        view.calculateVerticalSpeedButton.setOnClickListener(v -> {
            Intent intent = new Intent(DescentCalculator.this, VSActivity.class);
            startActivity(intent);
        });
    }
}