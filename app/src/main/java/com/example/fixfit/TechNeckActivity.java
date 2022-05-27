package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

public class TechNeckActivity extends AppCompatActivity implements View.OnClickListener {

//    LinearLayout tech_neck_realtime;
    LinearLayout camera_btn, gallery_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_neck);

//        tech_neck_realtime = (LinearLayout) findViewById(R.id.tech_neck_realtime);
//        tech_neck_realtime.setOnClickListener(this);

        camera_btn = (LinearLayout) findViewById(R.id.camera_btn);
        gallery_btn = (LinearLayout) findViewById(R.id.gallery_btn);
        camera_btn.setOnClickListener(this);
        gallery_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.tech_neck_realtime:
//                intent = new Intent(this, ClassifierActivity.class);
//                startActivity(intent);
//                break;
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