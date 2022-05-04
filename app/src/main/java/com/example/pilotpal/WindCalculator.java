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

public class WindCalculator extends AppCompatActivity {
    private EditText runwayNumber;
    private EditText windDirection;
    private EditText windSpeed;
    private TextView firstDisplay;
    private TextView secondDisplay;
    private TextView gustSpeed;
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

        goback = findViewById(R.id.goback);

        runwayNumber = findViewById(R.id.runwayNumber);
        windDirection = findViewById(R.id.windDirection);
        windSpeed = findViewById(R.id.windSpeed);
        gustSpeed = findViewById(R.id.gustSpeed);
        firstDisplay = findViewById(R.id.firstDisplay);
        secondDisplay = findViewById(R.id.secondDisplay);

        runwayNumber.addTextChangedListener(textWatcher);
        windDirection.addTextChangedListener(textWatcher);
        windSpeed.addTextChangedListener(textWatcher);
        gustSpeed.addTextChangedListener(textWatcher);

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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String runway = runwayNumber.getText().toString();
            String windD = windDirection.getText().toString();
            String windS = windSpeed.getText().toString();
            String gustS = gustSpeed.getText().toString();
            if(!runway.isEmpty() && !windD.isEmpty() && !windS.isEmpty() && !gustS.isEmpty()) {
                if (Integer.parseInt(runwayNumber.getText().toString()) > 36 || Integer.parseInt(runwayNumber.getText().toString()) < 1) {
                    firstDisplay.setText("Please enter a valid runway number");
                    secondDisplay.setText("");
                }
                else if (Integer.parseInt(windD) > 360 || Integer.parseInt(windD) < 0) {
                    firstDisplay.setText("Please enter a valid wind direction");
                    secondDisplay.setText("");
                }
                else if(Integer.parseInt(windS) == 0) {
                    firstDisplay.setText("Headwind: 0 kts");
                    secondDisplay.setText("Crosswind: 0 kts");
                }

                else {
                    calculate(Integer.parseInt(runway), Integer.parseInt(windS), Integer.parseInt(windD), Integer.parseInt(gustS));
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
            }
            else if(!runway.isEmpty() && !windD.isEmpty() && !windS.isEmpty()) {
                if (Integer.parseInt(runwayNumber.getText().toString()) > 36 || Integer.parseInt(runwayNumber.getText().toString()) < 1) {
                    firstDisplay.setText("Please enter a valid runway number");
                    secondDisplay.setText("");
                }
                else if (Integer.parseInt(windD) > 360 || Integer.parseInt(windD) < 0) {
                    firstDisplay.setText("Please enter a valid wind direction");
                    secondDisplay.setText("");
                }
                else if(Integer.parseInt(windS) == 0) {
                    firstDisplay.setText("Headwind: 0 kts");
                    secondDisplay.setText("Crosswind: 0 kts");
                }
                else if(10*(Integer.parseInt(runway))==Integer.parseInt(windD)) {
                    firstDisplay.setText("Headwind: " + truncateTo(Integer.parseInt(windSpeed.getText().toString()), 2) + " kts");
                    secondDisplay.setText("Crosswind: 0 kts");
                }
                else {
                    calculate(Integer.parseInt(runway), Integer.parseInt(windS), Integer.parseInt(windD), 0);
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
            }
            else {
                firstDisplay.setText("");
                secondDisplay.setText("");
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    static double truncateTo( double unroundedNumber, int decimalPlaces ) {
        int truncatedNumberInt = (int)( unroundedNumber * Math.pow( 10, decimalPlaces ) );
        double truncatedNumber = (double)( truncatedNumberInt / Math.pow( 10, decimalPlaces ) );
        return truncatedNumber;
    }
    public  void calculate(int runway, int windSpeed, int windDirection, int gustSpeed) {
        if(gustSpeed>windSpeed) {
            windSpeed = gustSpeed;
        }
        runway*=10;
        int angle;
        if(windDirection>runway)
            angle=360-(windDirection-runway);
        else
            angle=360-(runway-windDirection);
        double headwind = (Math.cos(Math.toRadians(angle)))*windSpeed;
        double crosswind = (Math.sin(Math.toRadians(angle)))*windSpeed;
        calculatedHeadwind = truncateTo(headwind, 2);
        calculatedCrosswind = truncateTo(crosswind, 2);
    }
}
