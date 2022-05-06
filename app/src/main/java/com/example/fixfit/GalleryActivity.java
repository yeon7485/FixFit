package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0;
    ImageView gallery_img;
    Button gallery_btn, classifier_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gallery_img = (ImageView) findViewById(R.id.gallery_img);
        gallery_btn = (Button) findViewById(R.id.gallery_btn);
        classifier_btn = (Button) findViewById(R.id.classifier_btn);

        gallery_btn.setOnClickListener(this);
        classifier_btn.setOnClickListener(this);

        getImage();

    }

    public void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    gallery_img.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.gallery_btn:
                getImage();
                break;
            case R.id.classifier_btn:
                break;
        }
    }
}