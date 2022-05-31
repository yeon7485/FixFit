package com.example.fixfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class TechNeckActivity extends AppCompatActivity implements View.OnClickListener {

    //    LinearLayout tech_neck_realtime;
    LinearLayout camera_btn, gallery_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_neck);

        camera_btn = (LinearLayout) findViewById(R.id.camera_btn);
        gallery_btn = (LinearLayout) findViewById(R.id.gallery_btn);
        camera_btn.setOnClickListener(this);
        gallery_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.camera_btn:
                intent = new Intent(this, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.gallery_btn:
                intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
                break;
        }
    }
}