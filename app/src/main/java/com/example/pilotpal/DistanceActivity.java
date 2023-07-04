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
import java.text.DecimalFormat;

public class DistanceActivity extends AppCompatActivity {
    TextInputLayout cruiseAltitudeInputLayout;
    TextInputLayout targetAltitudeInputLayout;
    TextInputLayout verticalSpeedInputLayout;
    TextInputLayout groundSpeed1InputLayout;
    TextInputLayout groundSpeed2InputLayout;
    EditText cruiseAltitudeEditText;
    EditText targetAltitudeEditText;
    EditText verticalSpeedEditText;
    EditText groundSpeed1EditText;
    EditText groundSpeed2EditText;
    EditText distanceEditText;
    EditText glideslopeEditText;
    EditText timeToTargetEditText;
    TextView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_distance);

        cruiseAltitudeInputLayout = findViewById(R.id.cruiseAltitudeInputLayout);
        targetAltitudeInputLayout = findViewById(R.id.targetAltitudeInputLayout);
        verticalSpeedInputLayout = findViewById(R.id.verticalSpeedInputLayout);
        groundSpeed1InputLayout = findViewById(R.id.groundSpeed1InputLayout);
        groundSpeed2InputLayout = findViewById(R.id.groundSpeed2InputLayout);

        cruiseAltitudeEditText = findViewById(R.id.cruiseAltitudeEditText);
        targetAltitudeEditText = findViewById(R.id.targetAltitudeEditText);
        verticalSpeedEditText = findViewById(R.id.verticalSpeedEditText);
        groundSpeed1EditText = findViewById(R.id.groundSpeed1EditText);
        groundSpeed2EditText = findViewById(R.id.groundSpeed2EditText);

        distanceEditText = findViewById(R.id.distanceEditText);
        glideslopeEditText = findViewById(R.id.glideslopeEditText);
        timeToTargetEditText = findViewById(R.id.timeToTargetEditText);

        cruiseAltitudeEditText.addTextChangedListener(textWatcher);
        targetAltitudeEditText.addTextChangedListener(textWatcher);
        verticalSpeedEditText.addTextChangedListener(textWatcher);
        groundSpeed1EditText.addTextChangedListener(textWatcher);
        groundSpeed2EditText.addTextChangedListener(textWatcher);

        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DistanceActivity.this, DescentCalculator.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //targetAltitudeInputLayout.setError(null);
            distanceEditText.setText("");
            glideslopeEditText.setText("");
            timeToTargetEditText.setText("");

            String cruiseAltitude = cruiseAltitudeEditText.getText().toString();
            String targetAltitude = targetAltitudeEditText.getText().toString();
            String verticalSpeed = verticalSpeedEditText.getText().toString();
            String groundSpeed1 = groundSpeed1EditText.getText().toString();
            String groundSpeed2 = groundSpeed2EditText.getText().toString();

            if (!cruiseAltitude.isEmpty() && Double.parseDouble(cruiseAltitude) >= 10000 &&
                !targetAltitude.isEmpty() && Double.parseDouble(targetAltitude) < 10000) {
                groundSpeed1InputLayout.setHint("Ground Speed above 10,000ft");
                groundSpeed2InputLayout.setVisibility(View.VISIBLE);
            } else {
                groundSpeed1InputLayout.setHint("Ground Speed");
                groundSpeed2InputLayout.setVisibility(View.GONE);
            }

            if (!cruiseAltitude.isEmpty() && !targetAltitude.isEmpty() &&
                    Double.parseDouble(targetAltitude) > Double.parseDouble(cruiseAltitude)) {
                targetAltitudeInputLayout.setError("Target Altitude cannot be above Cruise Altitude");
            } else {
                targetAltitudeInputLayout.setError(null);
            }

            if (isValidForComplexCalculation()) {
                calculateComplexDescentAndDisplay(Double.parseDouble(cruiseAltitude),
                        Double.parseDouble(targetAltitude), Double.parseDouble(verticalSpeed),
                        Double.parseDouble(groundSpeed1), Double.parseDouble(groundSpeed2));
            } else if (isValidForSimpleCalculation()) {
                calculateSimpleDescentAndDisplay(Double.parseDouble(cruiseAltitude),
                        Double.parseDouble(targetAltitude), Double.parseDouble(verticalSpeed),
                        Double.parseDouble(groundSpeed1));
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };
    private boolean isValidForComplexCalculation() {
        String cruiseAltitude = cruiseAltitudeEditText.getText().toString();
        String targetAltitude = targetAltitudeEditText.getText().toString();
        String verticalSpeed = verticalSpeedEditText.getText().toString();
        String groundSpeed1 = groundSpeed1EditText.getText().toString();
        String groundSpeed2 = groundSpeed2EditText.getText().toString();

        return !cruiseAltitude.isEmpty() && Double.parseDouble(cruiseAltitude) >= 10000 &&
                !targetAltitude.isEmpty() && Double.parseDouble(targetAltitude) < 10000 &&
                Double.parseDouble(cruiseAltitude) >= Double.parseDouble(targetAltitude) &&
                !verticalSpeed.isEmpty() && !groundSpeed1.isEmpty() && !groundSpeed2.isEmpty();

    }
    private boolean isValidForSimpleCalculation() {
        String cruiseAltitude = cruiseAltitudeEditText.getText().toString();
        String targetAltitude = targetAltitudeEditText.getText().toString();
        String verticalSpeed = verticalSpeedEditText.getText().toString();
        String groundSpeed1 = groundSpeed1EditText.getText().toString();
        String groundSpeed2 = groundSpeed2EditText.getText().toString();

        return !cruiseAltitude.isEmpty() && !targetAltitude.isEmpty() &&
                Double.parseDouble(cruiseAltitude) >= Double.parseDouble(targetAltitude) &&
                !verticalSpeed.isEmpty() && !groundSpeed1.isEmpty() && !groundSpeed2.isEmpty();
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

        DecimalFormat numberFormat = new DecimalFormat("#.0");
        distanceEditText.setText(numberFormat.format(distance) + " nm");
        glideslopeEditText.setText(numberFormat.format(angle) + "°");
        timeToTargetEditText.setText(Math.round(time) + " min");
    }
    private void calculateSimpleDescentAndDisplay(double cruiseAltitude, double targetAltitude, double verticalSpeed, double speed) {
        double altitude = (cruiseAltitude - targetAltitude) / -6076.12;
        double descent =  (verticalSpeed * -60) / 6076.12;

        double distance = (speed * altitude) / descent;
        double angle = Math.toDegrees(Math.atan(descent / speed));
        double time = 60 * (distance / speed);

        DecimalFormat numberFormat = new DecimalFormat("#.0");
        distanceEditText.setText(numberFormat.format(distance) + " nm");
        glideslopeEditText.setText(numberFormat.format(angle) + "°");
        timeToTargetEditText.setText(Math.round(time) + " min");
    }
}