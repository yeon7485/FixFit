package com.example.fixfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CHOOSE_IMAGE = 102;
    private static final int GALLERY_ACT = 2;
    private ImageView gallery_img;
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
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            getImage();
        }
        // 판정 화면에서 뒤로가기로 넘어왔을 때
        else{
            byte[] arr = extras.getByteArray("image");
            Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            gallery_img.setImageBitmap(image);
        }


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.gallery_btn:
                getImage();
                break;
            case R.id.classifier_btn:
                Drawable d = this.getResources().getDrawable(R.drawable.no_photo);
                BitmapDrawable bd = (BitmapDrawable) d;
                Bitmap bit = bd.getBitmap();

                intent = new Intent(this, ClassifierActivity.class);
                BitmapDrawable drawable = (BitmapDrawable) gallery_img.getDrawable();
                Bitmap sendBitmap = drawable.getBitmap();
                // 이미지가 없으면
                if(sendBitmap == bit){
                    Toast.makeText(this, "이미지가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image", byteArray);
                    intent.putExtra("tag", GALLERY_ACT);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 이미지를 고르게 되면 이미지뷰에 사진 삽입
        if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK) {
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