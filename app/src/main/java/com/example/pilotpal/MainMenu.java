package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    Button checklist;
    Button windcalculator;
    Button descentcalculator;
    Button phoneticalphabet;
    Button metar;
    Button fueltime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        checklist = findViewById(R.id.checklist);
        windcalculator = findViewById(R.id.windcalculator);
        descentcalculator = findViewById(R.id.descentcalculator);
        phoneticalphabet = findViewById(R.id.phoneticalphabet);
        metar = findViewById(R.id.metar);
        fueltime = findViewById(R.id.fueltime);
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, SpecialOps.class);
                startActivity(intent);
            }
        });
        windcalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainMenu.this, WindCalculator.class);
               startActivity(intent);
            }
        });
        descentcalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, DescentCalculator.class);
                startActivity(intent);
            }
        });
        phoneticalphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, PhoneticAlphabet.class);
                startActivity(intent);
            }
        });
        metar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Metar.class);
                startActivity(intent);
            }
        });
        fueltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Fueltime.class);
                startActivity(intent);
            }
        });
    }
}