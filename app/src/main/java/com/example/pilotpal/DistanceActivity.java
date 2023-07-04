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
import com.example.pilotpal.databinding.ActivityDistanceBinding;
import java.util.Objects;

public class DistanceActivity extends AppCompatActivity {
    ActivityDistanceBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityDistanceBinding.inflate(getLayoutInflater());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(view.getRoot());

        view.cruiseAltitudeEditText.addTextChangedListener(textWatcher);
        view.targetAltitudeEditText.addTextChangedListener(textWatcher);
        view.verticalSpeedEditText.addTextChangedListener(textWatcher);
        view.groundSpeed1EditText.addTextChangedListener(textWatcher);
        view.groundSpeed2EditText.addTextChangedListener(textWatcher);
        view.goback.setOnClickListener(v -> {
            Intent intent = new Intent(DistanceActivity.this, DescentCalculator.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            view.distanceEditText.setText("");
            view.glideslopeEditText.setText("");
            view.timeToTargetEditText.setText("");

            String cruiseAltitude = Objects.requireNonNull(view.cruiseAltitudeEditText.getText()).toString();
            String targetAltitude = Objects.requireNonNull(view.targetAltitudeEditText.getText()).toString();
            String verticalSpeed = Objects.requireNonNull(view.verticalSpeedEditText.getText()).toString();
            String groundSpeed1 = Objects.requireNonNull(view.groundSpeed1EditText.getText()).toString();
            String groundSpeed2 = Objects.requireNonNull(view.groundSpeed2EditText.getText()).toString();

            if (!cruiseAltitude.isEmpty() && Double.parseDouble(cruiseAltitude) >= 10000 &&
                !targetAltitude.isEmpty() && Double.parseDouble(targetAltitude) < 10000) {
                view.groundSpeed1InputLayout.setHint("Ground Speed above 10,000ft");
                view.groundSpeed2InputLayout.setVisibility(View.VISIBLE);
            } else {
                view.groundSpeed1InputLayout.setHint("Ground Speed");
                view.groundSpeed2InputLayout.setVisibility(View.GONE);
            }

            if (!cruiseAltitude.isEmpty() && !targetAltitude.isEmpty() &&
                    Double.parseDouble(targetAltitude) > Double.parseDouble(cruiseAltitude)) {
                view.targetAltitudeInputLayout.setError("Target Altitude cannot be above Cruise Altitude");
            } else {
                view.targetAltitudeInputLayout.setError(null);
            }

            if (isValidForComplexCalculation(cruiseAltitude, targetAltitude, verticalSpeed, groundSpeed1, groundSpeed2)) {
                calculateComplexDescentAndDisplay(Double.parseDouble(cruiseAltitude),
                        Double.parseDouble(targetAltitude), Double.parseDouble(verticalSpeed),
                        Double.parseDouble(groundSpeed1), Double.parseDouble(groundSpeed2));
            } else if (isValidForSimpleCalculation(cruiseAltitude, targetAltitude, verticalSpeed, groundSpeed1)) {
                calculateSimpleDescentAndDisplay(Double.parseDouble(cruiseAltitude),
                        Double.parseDouble(targetAltitude), Double.parseDouble(verticalSpeed),
                        Double.parseDouble(groundSpeed1));
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };
    private boolean isValidForComplexCalculation(String cruiseAltitude, String targetAltitude, String verticalSpeed, String groundSpeed1, String groundSpeed2) {
        return !cruiseAltitude.isEmpty() && Double.parseDouble(cruiseAltitude) >= 10000 &&
                !targetAltitude.isEmpty() && Double.parseDouble(targetAltitude) < 10000 &&
                Double.parseDouble(cruiseAltitude) >= Double.parseDouble(targetAltitude) &&
                !verticalSpeed.isEmpty() && !groundSpeed1.isEmpty() && !groundSpeed2.isEmpty();
    }
    private boolean isValidForSimpleCalculation(String cruiseAltitude, String targetAltitude, String verticalSpeed, String groundSpeed1) {
        return !cruiseAltitude.isEmpty() && !targetAltitude.isEmpty() &&
                (Double.parseDouble(cruiseAltitude) < 10000 || Double.parseDouble(cruiseAltitude) >= 10000 && Double.parseDouble(targetAltitude) >= 10000) &&
                Double.parseDouble(cruiseAltitude) >= Double.parseDouble(targetAltitude) &&
                !verticalSpeed.isEmpty() && !groundSpeed1.isEmpty();
    }
    private void calculateComplexDescentAndDisplay(double cruiseAltitude, double targetAltitude, double verticalSpeed, double speedAbove, double speedBelow) {
        double altitude1 = (cruiseAltitude - 10000) / (-6076.12);
        double descent1 =  (verticalSpeed * -60) / (6076.12);
        double distance1 = (speedAbove * altitude1) / descent1;

        double altitude2 = ((10000 - targetAltitude) / (-6076.12));
        double descent2 =  (verticalSpeed * -60) / (6076.12);
        double distance2 = (speedBelow * altitude2) / descent2;

        double angle1 = Math.toDegrees(Math.atan(descent1 / speedAbove));
        double angle2 = Math.toDegrees(Math.atan(descent2 / speedBelow));

        double angle = (angle1 + angle2) / 2;
        double time = (60 * (distance1 / speedAbove)) + (60 * (distance2 / speedBelow));
        double distance = distance1 + distance2;

        view.distanceEditText.setText(getString(R.string.distance_display, distance));
        view.glideslopeEditText.setText(getString(R.string.glideslope_display, angle));
        view.timeToTargetEditText.setText(getString(R.string.time_to_target_display, time));
    }
    private void calculateSimpleDescentAndDisplay(double cruiseAltitude, double targetAltitude, double verticalSpeed, double speed) {
        double altitude = (cruiseAltitude - targetAltitude) / -6076.12;
        double descent =  (verticalSpeed * -60) / 6076.12;

        double distance = (speed * altitude) / descent;
        double angle = Math.toDegrees(Math.atan(descent / speed));
        double time = 60 * (distance / speed);

        view.distanceEditText.setText(getString(R.string.distance_display, distance));
        view.glideslopeEditText.setText(getString(R.string.glideslope_display, angle));
        view.timeToTargetEditText.setText(getString(R.string.time_to_target_display, time));
    }
}