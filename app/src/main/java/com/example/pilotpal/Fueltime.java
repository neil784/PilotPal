package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import com.example.pilotpal.databinding.ActivityFueltimeBinding;
import java.text.DecimalFormat;
import java.util.Objects;

public class Fueltime extends AppCompatActivity {
    ActivityFueltimeBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityFueltimeBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.goback.setOnClickListener(v -> {
            Intent intent = new Intent(Fueltime.this, MainMenu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        view.distanceEditText.addTextChangedListener(textWatcher);
        view.groundSpeedEditText.addTextChangedListener(textWatcher);
        view.fuelConsumptionEditText.addTextChangedListener(textWatcher);
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            view.flightTime.setText(getString(R.string.flight_time_default));
            view.fuelRequired.setText("");
            view.fuelRequired30.setText("");
            view.fuelRequired45.setText("");

            String distanceInput = Objects.requireNonNull(view.distanceEditText.getText()).toString();
            String groundSpeedInput = Objects.requireNonNull(view.groundSpeedEditText.getText()).toString();
            String fuelConsumptionInput = Objects.requireNonNull(view.fuelConsumptionEditText.getText()).toString();

            if (!distanceInput.isEmpty() && !groundSpeedInput.isEmpty() && ! fuelConsumptionInput.isEmpty() && Integer.parseInt(groundSpeedInput) != 0) {
                calculateAndDisplayFlightTime(Double.parseDouble(view.distanceEditText.getText().toString()), Double.parseDouble(view.groundSpeedEditText.getText().toString()));
                calculateAndDisplayFuelRequired(Double.parseDouble(view.distanceEditText.getText().toString()), Double.parseDouble(view.groundSpeedEditText.getText().toString()), Double.parseDouble(view.fuelConsumptionEditText.getText().toString()));
            } else if (!distanceInput.isEmpty() && !groundSpeedInput.isEmpty() && Integer.parseInt(groundSpeedInput) != 0) {
                calculateAndDisplayFlightTime(Double.parseDouble(view.distanceEditText.getText().toString()), Double.parseDouble(view.groundSpeedEditText.getText().toString()));
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };
    private void calculateAndDisplayFlightTime(double distance, double groundSpeed) {
        double fthours = distance / groundSpeed;
        String hours = new DecimalFormat("00").format((int) fthours);
        double ftminutes = 60 * (fthours - Double.parseDouble(hours));
        String minutes = new DecimalFormat("00").format((int) ftminutes);
        double ftseconds = 60 * (ftminutes - Double.parseDouble(minutes));
        String seconds = new DecimalFormat("00").format((int) ftseconds);
        view.flightTime.setText(getString(R.string.flight_time_display, hours, minutes, seconds));
    }
    private void calculateAndDisplayFuelRequired(double distance, double groundSpeed, double fuelConsumption) {
        double flightTime = 60 * (distance / groundSpeed);
        double hourConverted = flightTime / 60;
        double calculatedFuelRequired = fuelConsumption * hourConverted;

        flightTime += 30;
        double hourConverted30 = flightTime / 60;
        double calculatedFuelRequired30 = fuelConsumption * hourConverted30;

        flightTime += 15;
        double hourConverted45 = flightTime / 60;
        double calculatedFuelRequired45 = fuelConsumption * hourConverted45;

        view.fuelRequired.setText(getString(R.string.fuel_display, calculatedFuelRequired));
        view.fuelRequired30.setText(getString(R.string.fuel_display, calculatedFuelRequired30));
        view.fuelRequired45.setText(getString(R.string.fuel_display, calculatedFuelRequired45));
    }
}
