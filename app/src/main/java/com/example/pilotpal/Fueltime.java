package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Fueltime extends AppCompatActivity {
    EditText distance;
    EditText groundSpeed;
    EditText flightTime;
    EditText fuelConsumption;
    EditText fuelRequired;
    EditText fuelRequired2;
    EditText fuelRequired3;
    TextView goback;

    public static double fthours = 0;
    public static String hours = "";
    public static String minutes = "";
    public static String seconds = "";
    public static double calculatedFuelRequired = 0;
    public static double calculatedFuelRequired30 = 0;
    public static double calculatedFuelRequired45 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fueltime);
        distance = findViewById(R.id.distance);
        groundSpeed = findViewById(R.id.groundSpeed);
        flightTime = findViewById(R.id.flightTime1);
        fuelConsumption = findViewById(R.id.fuelConsumption);
        fuelRequired = findViewById(R.id.fuelRequired);
        fuelRequired2 = findViewById(R.id.fuelRequired2);
        fuelRequired3 = findViewById(R.id.fuelRequired3);
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fueltime.this, MainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        distance.addTextChangedListener(textWatcher);
        groundSpeed.addTextChangedListener(textWatcher);
        fuelConsumption.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String distanceInput = distance.getText().toString();
            String groundSpeedInput = groundSpeed.getText().toString();
            String fuelConsumptionInput = fuelConsumption.getText().toString();
            if (!distanceInput.isEmpty() && !groundSpeedInput.isEmpty() && ! fuelConsumptionInput.isEmpty()) {
                calculateFlightTime(Double.parseDouble(distance.getText().toString()), Double.parseDouble(groundSpeed.getText().toString()));
                flightTime.setText(hours + ":" + minutes + ":" + seconds);
                calculateFuelRequired(hoursToMinutes(fthours), Double.parseDouble(fuelConsumption.getText().toString()));
                fuelRequired.setText(new DecimalFormat("0.00").format(calculatedFuelRequired) + " gallons");
                fuelRequired2.setText(new DecimalFormat("0.00").format(calculatedFuelRequired30) + " gallons");
                fuelRequired3.setText(new DecimalFormat("0.00").format(calculatedFuelRequired45) + " gallons");
            } else if (!distanceInput.isEmpty() && !groundSpeedInput.isEmpty()) {
                calculateFlightTime(Double.parseDouble(distance.getText().toString()), Double.parseDouble(groundSpeed.getText().toString()));
                flightTime.setText(hours + ":" + minutes + ":" + seconds);
                fuelRequired.setText("");
                fuelRequired2.setText("");
                fuelRequired3.setText("");
            }  else {
                flightTime.setText("00:00:00");
                fuelRequired.setText("");
                fuelRequired2.setText("");
                fuelRequired3.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    public static void calculateFlightTime(double distance, double groundSpeed) {
        double groundSpeedRec = 1 / groundSpeed;
        fthours = groundSpeedRec * distance;
        hours = new DecimalFormat("00").format((int) fthours);
        double ftminutes = hoursToMinutes(fthours - Double.parseDouble(hours));
        minutes = new DecimalFormat("00").format((int) ftminutes);
        double ftseconds = minutesToSeconds(ftminutes - Double.parseDouble(minutes));
        seconds = new DecimalFormat("00").format((int) ftseconds);

    }
    public static void calculateFuelRequired(double flightTime, double fuelConsumption) {
        double hourConverted = minutesToHours(flightTime);
        calculatedFuelRequired = fuelConsumption * hourConverted;

        flightTime += 30;
        double hourConverted30 = minutesToHours(flightTime);
        calculatedFuelRequired30 = fuelConsumption * hourConverted30;

        flightTime += 15;
        double hourConverted45 = minutesToHours(flightTime);
        calculatedFuelRequired45 = fuelConsumption * hourConverted45;
    }
    public static double hoursToMinutes(double hours) {
        return hours * 60;
    }
    public static double minutesToSeconds(double minutes) {
        return minutes * 60;
    }
    public static double minutesToHours(double minutes) {
        return minutes / 60;
    }
}