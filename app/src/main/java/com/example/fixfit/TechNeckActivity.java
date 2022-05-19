package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class TechNeckActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout tech_neck_realtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_neck);

        tech_neck_realtime = (LinearLayout) findViewById(R.id.tech_neck_realtime);

        tech_neck_realtime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tech_neck_realtime:
                intent = new Intent(this, ClassifierActivity.class);
                startActivity(intent);
                break;
        }
    }
}