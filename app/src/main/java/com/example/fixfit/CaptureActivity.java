package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView capture_img;
    Button capture_btn, classifier_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        capture_img = (ImageView) findViewById(R.id.capture_img);
        capture_btn = (Button) findViewById(R.id.capture_btn);
        classifier_btn = (Button) findViewById(R.id.classifier_btn);

        capture_btn.setOnClickListener(this);
        classifier_btn.setOnClickListener(this);
        // 카메라 권한
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.capture_btn:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                break;
            case R.id.classifier_btn:
                intent = new Intent(this, ClassifierActivity.class);
                BitmapDrawable drawable = (BitmapDrawable) capture_img.getDrawable();
                Bitmap sendBitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);
                startActivity(intent);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 카메라 권한 확인
        if (ActivityCompat.checkSelfPermission(CaptureActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 카메라 촬영을 하면 이미지뷰에 사진 삽입
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                // Bundle로 데이터를 입력
                Bundle extras = data.getExtras();
                // Bitmap으로 컨버전
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                // 이미지뷰에 Bitmap으로 이미지를 입력
                capture_img.setImageBitmap(imageBitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 촬영 취소", Toast.LENGTH_LONG).show();
            }
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }

    }
}