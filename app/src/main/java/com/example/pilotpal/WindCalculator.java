package com.example.pilotpal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import com.example.pilotpal.databinding.ActivityWindCalculatorBinding;
import java.util.Objects;

public class WindCalculator extends AppCompatActivity {
    ActivityWindCalculatorBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityWindCalculatorBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.windDirectionEditText.addTextChangedListener(textWatcher);
        view.windSpeedEditText.addTextChangedListener(textWatcher);
        view.runwayEditText.addTextChangedListener(textWatcher);
        view.gustSpeedEditText.addTextChangedListener(textWatcher);

        view.goback.setOnClickListener(v -> {
            Intent intent = new Intent(WindCalculator.this, MainMenu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            view.headwindEditText.setText("");
            view.crosswindEditText.setText("");

            String windDirection = Objects.requireNonNull(view.windDirectionEditText.getText()).toString();
            String windSpeed = Objects.requireNonNull(view.windSpeedEditText.getText()).toString();
            String runway = Objects.requireNonNull(view.runwayEditText.getText()).toString();
            String gustSpeed = Objects.requireNonNull(view.gustSpeedEditText.getText()).toString();
            boolean shouldCalculate = true;

            if (!windDirection.isEmpty() && Integer.parseInt(windDirection) > 360) {
                view.windDirectionInputLayout.setError("Between 0 and 360");
                shouldCalculate = false;
            } else {
                view.windDirectionInputLayout.setError(null);
            }

            if (!runway.isEmpty() && (Integer.parseInt(runway) < 1 || Integer.parseInt(runway) > 36)) {
                view.runwayInputLayout.setError("Between 1 and 36");
                shouldCalculate = false;
            } else {
                view.runwayInputLayout.setError(null);
            }

            if (!windDirection.isEmpty() && !windSpeed.isEmpty() && !runway.isEmpty() && shouldCalculate) {
                if (gustSpeed.isEmpty()) {
                    calculate(Integer.parseInt(windDirection), Integer.parseInt(windSpeed), Integer.parseInt(runway), 0);
                } else {
                    calculate(Integer.parseInt(windDirection), Integer.parseInt(windSpeed), Integer.parseInt(runway), Integer.parseInt(gustSpeed));
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void handleDisplay(double calculatedHeadwind, double calculatedCrosswind) {
        view.windDirectionInputLayout.setError(null);
        view.runwayInputLayout.setError(null);
        if (calculatedHeadwind >= 0 && calculatedCrosswind >= 0) {
            view.headwindTextView.setText(getString(R.string.headwind_label));
        }
        if ((calculatedHeadwind < 0 && calculatedCrosswind >= 0)) {
            calculatedHeadwind = Math.abs(calculatedHeadwind);
            view.headwindTextView.setText(getString(R.string.tailwind_label));
        }
        if (calculatedHeadwind >= 0 && calculatedCrosswind < 0) {
            calculatedCrosswind = Math.abs(calculatedCrosswind);
            view.headwindTextView.setText(getString(R.string.headwind_label));
        }
        if (calculatedHeadwind < 0 && calculatedCrosswind < 0) {
            calculatedCrosswind = Math.abs(calculatedCrosswind);
            calculatedHeadwind = Math.abs(calculatedHeadwind);
            view.headwindTextView.setText(getString(R.string.tailwind_label));
        }
        view.headwindEditText.setText(getString(R.string.wind_speed_display, calculatedHeadwind));
        view.crosswindEditText.setText(getString(R.string.wind_speed_display, calculatedCrosswind));
    }
    private void calculate(int windDirection, int windSpeed, int runway, int gustSpeed) {
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
        handleDisplay(headwind, crosswind);
    }
}
