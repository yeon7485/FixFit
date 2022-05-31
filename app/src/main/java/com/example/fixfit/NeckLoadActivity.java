package com.example.fixfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NeckLoadActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView neck_load_tv;
    private ImageView neck_0, neck_15, neck_30, neck_45, neck_60;
    private LinearLayout home_btn, stretch_btn;
    private double angle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neck_load);

        neck_load_tv = (TextView) findViewById(R.id.neck_load_tv);
        neck_0 = (ImageView) findViewById(R.id.neck_0);
        neck_15 = (ImageView) findViewById(R.id.neck_15);
        neck_30 = (ImageView) findViewById(R.id.neck_30);
        neck_45 = (ImageView) findViewById(R.id.neck_45);
        neck_60 = (ImageView) findViewById(R.id.neck_60);
        home_btn = (LinearLayout) findViewById(R.id.home_btn);
        stretch_btn = (LinearLayout) findViewById(R.id.stretch_btn);

        home_btn.setOnClickListener(this);
        stretch_btn.setOnClickListener(this);

        Intent intent = getIntent();
        angle = Double.parseDouble(intent.getStringExtra("angle"));

        setVisibleImg(angle);

    }

    private void setVisibleImg(double angle) {
        if (angle < 15) {
            neck_0.setVisibility(View.VISIBLE);
            neck_load_tv.setText("약 4.5~5.4kg!");
        } else if (angle < 30) {
            neck_15.setVisibility(View.VISIBLE);
            neck_load_tv.setText("약 12.2kg!");
        } else if (angle < 45) {
            neck_30.setVisibility(View.VISIBLE);
            neck_load_tv.setText("약 18.1kg!");
        } else if (angle < 60) {
            neck_45.setVisibility(View.VISIBLE);
            neck_load_tv.setText("약 22.2kg!");
        } else {
            neck_60.setVisibility(View.VISIBLE);
            neck_load_tv.setText("약 27.2kg!");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.home_btn:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("noFirst", "noFirst");
                startActivity(intent);
                finish();
                break;
            case R.id.stretch_btn:
                intent = new Intent(this, RehabilitationActivity.class);
                startActivity(intent);
                break;
        }
    }

}