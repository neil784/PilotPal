package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.example.pilotpal.databinding.ActivitySpecialOpsBinding;
import java.util.Objects;

public class SpecialOps extends AppCompatActivity {
    ActivitySpecialOpsBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivitySpecialOpsBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.takeoffButton.setOnClickListener(v -> {
            Intent intent = new Intent(SpecialOps.this, Takeoff.class);
            startActivity(intent);
        });
        view.landingButton.setOnClickListener(v -> {
            Intent intent = new Intent(SpecialOps.this, Landing.class);
            startActivity(intent);
        });
        view.goback.setOnClickListener(v -> {
            Intent intent = new Intent(SpecialOps.this, MainMenu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
