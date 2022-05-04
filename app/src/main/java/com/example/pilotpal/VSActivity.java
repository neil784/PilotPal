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

public class VSActivity extends AppCompatActivity {
    EditText startingAltitude;
    EditText endingAltitude;
    EditText descentRate;
    EditText speedBelow;
    EditText speedAbove;
    TextView speedBelowText;
    TextView speedAboveText;
    Button calculate;
    TextView display;
    TextView goback;

    static double time;
    static double angle;
    static double vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_v_s);
        DecimalFormat numberFormat=new DecimalFormat("#.0");

        goback = findViewById(R.id.goback);

        startingAltitude = findViewById(R.id.startingAltitude);
        endingAltitude = findViewById(R.id.endingAltitude);
        descentRate = findViewById(R.id.descentRate);
        speedBelow = findViewById(R.id.speedBelow);
        speedAbove = findViewById(R.id.speedAbove);
        speedBelowText = findViewById(R.id.speedBelowText);
        speedAboveText = findViewById(R.id.speedAboveText);
        calculate = findViewById(R.id.calculate);
        display = findViewById(R.id.display);

        startingAltitude.addTextChangedListener(textWatcher);
        endingAltitude.addTextChangedListener(textWatcher);
        descentRate.addTextChangedListener(textWatcher);
        speedBelow.addTextChangedListener(textWatcher);
        speedAbove.addTextChangedListener(textWatcher);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VSActivity.this, DescentCalculator.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startingAlt = startingAltitude.getText().toString();
                String endingAlt = endingAltitude.getText().toString();
                String descentR = descentRate.getText().toString();
                String speedB = speedBelow.getText().toString();
                String speedA = speedAbove.getText().toString();

                if(Double.parseDouble(endingAlt)>Double.parseDouble(startingAlt)) {
                    display.setText("Error: Target Altitude is above Cruise Altitude");
                }
                else {
                    if (Double.parseDouble(startingAlt) >= 10000) {
                        vsDouble(Double.parseDouble(startingAlt), Double.parseDouble(endingAlt), Double.parseDouble(speedA), Double.parseDouble(speedB), Double.parseDouble(descentR));
                        display.setText("Vertical Speed: " + numberFormat.format(vs) + " ft/min \n Glideslope: " + numberFormat.format(angle) + "° \n Time to Destination: " + Math.round(time) + " min");
                    } else if (Double.parseDouble(startingAlt) < 10000) {
                        vsSingle(Double.parseDouble(startingAlt), Double.parseDouble(endingAlt), Double.parseDouble(speedB), Double.parseDouble(descentR));
                        display.setText("Vertical Speed: " + numberFormat.format(vs) + " ft/min \n Glideslope: " + numberFormat.format(angle) + "° \n Time to Destination: " + Math.round(time) + " min");
                    } else {
                        display.setText("NaNN");
                    }
                }
            }
        });
    }
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            display.setText("");
            calculate.setEnabled(false);

            String startingAlt = startingAltitude.getText().toString();
            String endingAlt = endingAltitude.getText().toString();
            String descentR = descentRate.getText().toString();
            String speedB = speedBelow.getText().toString();
            String speedA = speedAbove.getText().toString();

            if(startingAlt.isEmpty()) {
                speedAboveText.setText("");
                speedAbove.setVisibility(View.INVISIBLE);
                calculate.setEnabled(false);
            }
            else if(Double.parseDouble(startingAlt)>=10000) {
                speedAboveText.setText("GS above 10,000ft:");
                speedAbove.setVisibility(View.VISIBLE);
                speedBelowText.setText("GS below 10,000ft:");
                if(!startingAlt.isEmpty() && !endingAlt.isEmpty() && !descentR.isEmpty() && !speedB.isEmpty() && !speedA.isEmpty())
                    calculate.setEnabled(true);
            }
            else if(Double.parseDouble(startingAlt)<10000) {
                speedAboveText.setText("");
                speedBelowText.setText("Ground Speed:");
                speedAbove.setVisibility(View.INVISIBLE);
                calculate.setEnabled(!startingAlt.isEmpty() && !endingAlt.isEmpty() && !descentR.isEmpty() && !speedB.isEmpty());
            }
            else {
                //speedAboveText.setText("");
                //speedAbove.setVisibility(View.INVISIBLE);
                //calculate.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    public static void vsDouble(double startingAltitude, double endingAltitude, double speedAbove, double speedBelow, double distance) {
        double vs1;
        double altitude1 = ((startingAltitude-10000)/(-6076.12));
        double vsConverted1 = (speedAbove*altitude1)/distance;
        vs1 = (vsConverted1*6076.12)/60;

        double vs2;
        double altitude2 = ((10000-endingAltitude)/(-6076.12));
        double vsConverted2 = (speedBelow*altitude2)/distance;
        vs2 = (vsConverted2*6076.12)/60;

        double angle1 = Math.toDegrees(Math.atan(vsConverted1/speedAbove));
        double angle2 = Math.toDegrees(Math.atan(vsConverted2/speedBelow));
        angle = (angle1+angle2)/2;

        time = 60*((distance/speedAbove)+(distance/speedBelow));
        vs = vs1+vs2;
    }
    public static void vsSingle(double startingAltitude, double endingAltitude, double speed, double distance) {
        double calculatedVs;
        double altitude = ((startingAltitude-endingAltitude)/(-6076.12));
        double vsConverted =  (speed*altitude)/distance;
        calculatedVs = (vsConverted*6076.12)/-60;

        angle = Math.toDegrees(Math.atan(vsConverted/speed));
        time = 60*(distance/speed);
        vs =  calculatedVs;
    }
}