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
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class Fueltime extends AppCompatActivity {
    private EditText distanceEditText;
    private EditText groundSpeedEditText;
    private EditText flightTime;
    private EditText fuelConsumptionEditText;
    private EditText fuelRequired;
    private EditText fuelRequired30;
    private EditText fuelRequired45;
    private TextView goback;

    private static double fthours = 0;
    private static String hours = "";
    private static String minutes = "";
    private static String seconds = "";
    private static double calculatedFuelRequired = 0;
    private static double calculatedFuelRequired30 = 0;
    private static double calculatedFuelRequired45 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fueltime);

        distanceEditText = findViewById(R.id.distanceEditText);
        groundSpeedEditText = findViewById(R.id.groundSpeedEditText);
        flightTime = findViewById(R.id.flightTime1);
        fuelConsumptionEditText = findViewById(R.id.fuelConsumptionEditText);
        fuelRequired = findViewById(R.id.fuelRequired);
        fuelRequired30 = findViewById(R.id.fuelRequired30);
        fuelRequired45 = findViewById(R.id.fuelRequired45);
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fueltime.this, MainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        distanceEditText.addTextChangedListener(textWatcher);
        groundSpeedEditText.addTextChangedListener(textWatcher);
        fuelConsumptionEditText.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String distanceInput = distanceEditText.getText().toString();
            String groundSpeedInput = groundSpeedEditText.getText().toString();
            String fuelConsumptionInput = fuelConsumptionEditText.getText().toString();
            if (!distanceInput.isEmpty() && !groundSpeedInput.isEmpty() && ! fuelConsumptionInput.isEmpty() && Integer.parseInt(groundSpeedInput) != 0) {
                calculateFlightTime(Double.parseDouble(distanceEditText.getText().toString()), Double.parseDouble(groundSpeedEditText.getText().toString()));
                flightTime.setText(hours + ":" + minutes + ":" + seconds);
                calculateFuelRequired(hoursToMinutes(fthours), Double.parseDouble(fuelConsumptionEditText.getText().toString()));
                fuelRequired.setText(new DecimalFormat("0.00").format(calculatedFuelRequired) + " gallons");
                fuelRequired30.setText(new DecimalFormat("0.00").format(calculatedFuelRequired30) + " gallons");
                fuelRequired45.setText(new DecimalFormat("0.00").format(calculatedFuelRequired45) + " gallons");
            } else if (!distanceInput.isEmpty() && !groundSpeedInput.isEmpty() && Integer.parseInt(groundSpeedInput) != 0) {
                calculateFlightTime(Double.parseDouble(distanceEditText.getText().toString()), Double.parseDouble(groundSpeedEditText.getText().toString()));
                flightTime.setText(hours + ":" + minutes + ":" + seconds);
                fuelRequired.setText("");
                fuelRequired30.setText("");
                fuelRequired45.setText("");
            }  else {
                flightTime.setText("00:00:00");
                fuelRequired.setText("");
                fuelRequired30.setText("");
                fuelRequired45.setText("");
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };
    public static void calculateFlightTime(double distance, double groundSpeed) {
        fthours = distance / groundSpeed;
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