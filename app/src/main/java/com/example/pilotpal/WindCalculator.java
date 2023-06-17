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

import com.google.android.material.textfield.TextInputLayout;
public class WindCalculator extends AppCompatActivity {
    private TextInputLayout windDirectionInputLayout;
    private EditText windDirectionEditText;
    private TextInputLayout windSpeedInputLayout;
    private EditText windSpeedEditText;
    private TextInputLayout runwayInputLayout;
    private EditText runwayEditText;
    private TextInputLayout gustSpeedInputLayout;
    private EditText gustSpeedEditText;
    private TextView firstDisplay;
    private TextView secondDisplay;
    private TextView goback;
    private double calculatedHeadwind;
    private double calculatedCrosswind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wind_calculator);

        windDirectionInputLayout = findViewById(R.id.windDirectionInputLayout);
        windSpeedInputLayout = findViewById(R.id.windSpeedInputLayout);
        runwayInputLayout = findViewById(R.id.runwayInputLayout);
        gustSpeedInputLayout = findViewById(R.id.gustSpeedInputLayout);

        windDirectionEditText = findViewById(R.id.windDirectionEditText);
        windSpeedEditText = findViewById(R.id.windSpeedEditText);
        runwayEditText = findViewById(R.id.runwayEditText);
        gustSpeedEditText = findViewById(R.id.gustSpeedEditText);

        firstDisplay = findViewById(R.id.firstDisplay);
        secondDisplay = findViewById(R.id.secondDisplay);

        goback = findViewById(R.id.goback);

        windDirectionEditText.addTextChangedListener(textWatcher);
        windSpeedEditText.addTextChangedListener(textWatcher);
        runwayEditText.addTextChangedListener(textWatcher);
        gustSpeedEditText.addTextChangedListener(textWatcher);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WindCalculator.this, MainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String windDirection = windDirectionEditText.getText().toString();
            String windSpeed = windSpeedEditText.getText().toString();
            String runway = runwayEditText.getText().toString();
            String gustSpeed = gustSpeedEditText.getText().toString();
            boolean shouldCalculate = true;

            if (!windDirection.isEmpty() && Integer.parseInt(windDirection) > 360) {
                windDirectionInputLayout.setError("Between 0 and 360");
                firstDisplay.setText("");
                secondDisplay.setText("");
                shouldCalculate = false;
            } else {
                windDirectionInputLayout.setError(null);
            }

            if (!runway.isEmpty() && (Integer.parseInt(runway) < 1 || Integer.parseInt(runway) > 36)) {
                runwayInputLayout.setError("Between 1 and 36");
                firstDisplay.setText("");
                secondDisplay.setText("");
                shouldCalculate = false;
            } else {
                runwayInputLayout.setError(null);
            }

            if (!windDirection.isEmpty() && !windSpeed.isEmpty() && !runway.isEmpty() && shouldCalculate) {
                if (gustSpeed.isEmpty()) {
                    calculate(Integer.parseInt(windDirection), Integer.parseInt(windSpeed), Integer.parseInt(runway), 0);
                } else {
                    calculate(Integer.parseInt(windDirection), Integer.parseInt(windSpeed), Integer.parseInt(runway), Integer.parseInt(gustSpeed));
                }
                handleDisplay();
            } else {
                firstDisplay.setText("");
                secondDisplay.setText("");
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void handleDisplay() {
        windDirectionInputLayout.setError(null);
        runwayInputLayout.setError(null);
        if (calculatedHeadwind >= 0 && calculatedCrosswind >= 0) {
            firstDisplay.setText("Headwind: " + truncateTo(calculatedHeadwind, 2) + " kts");
            secondDisplay.setText("Crosswind: " + truncateTo(calculatedCrosswind, 2) + " kts");
        }
        if ((calculatedHeadwind < 0 && calculatedCrosswind >= 0)) {
            calculatedHeadwind = Math.abs(calculatedHeadwind);
            firstDisplay.setText("Tailwind: " + truncateTo(calculatedHeadwind, 2) + " kts");
            secondDisplay.setText("Crosswind: " + truncateTo(calculatedCrosswind, 2) + " kts");
        }
        if (calculatedHeadwind >= 0 && calculatedCrosswind < 0) {
            calculatedCrosswind = Math.abs(calculatedCrosswind);
            firstDisplay.setText("Headwind: " + truncateTo(calculatedHeadwind, 2) + " kts");
            secondDisplay.setText("Crosswind: " + truncateTo(calculatedCrosswind, 2) + " kts");
        }
        if (calculatedHeadwind < 0 && calculatedCrosswind < 0) {
            calculatedCrosswind = Math.abs(calculatedCrosswind);
            calculatedHeadwind = Math.abs(calculatedHeadwind);
            firstDisplay.setText("Tailwind: " + truncateTo(calculatedHeadwind, 2) + " kts");
            secondDisplay.setText("Crosswind: " + truncateTo(calculatedCrosswind, 2) + " kts");
        }
    }

    static double truncateTo(double unroundedNumber, int decimalPlaces) {
        int truncatedNumberInt = (int)( unroundedNumber * Math.pow(10, decimalPlaces));
        double truncatedNumber = (double)( truncatedNumberInt / Math.pow(10, decimalPlaces));
        return truncatedNumber;
    }
    public void calculate(int windDirection, int windSpeed, int runway, int gustSpeed) {
        if (gustSpeed > windSpeed) {
            windSpeed = gustSpeed;
        }
        runway *= 10;
        int angle;
        if (windDirection > runway)
            angle = 360 - (windDirection - runway);
        else
            angle = 360 - (runway - windDirection);
        double headwind = (Math.cos(Math.toRadians(angle))) * windSpeed;
        double crosswind = (Math.sin(Math.toRadians(angle))) * windSpeed;
        calculatedHeadwind = truncateTo(headwind, 2);
        calculatedCrosswind = truncateTo(crosswind, 2);
    }
}
