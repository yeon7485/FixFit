package com.example.fixfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixfit.Camera.GraphicOverlay;
import com.example.fixfit.Camera.PreferenceUtils;
import com.example.fixfit.Camera.VisionImageProcessor;
import com.example.fixfit.posedetector.PoseDetectorProcessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ClassifierActivity extends AppCompatActivity {

    private static final String TAG = "ClassifierActivity";

    private static final int CLASSIFIER = 101;

    private View rootView;
    private ImageView preview;
    private TextView neck_angle_tv, neck_load_tv;
    private GraphicOverlay graphicOverlay;
    private VisionImageProcessor imageProcessor;
    private PoseDetectorProcessor poseImageProcessor;
    private String angle = "";

    private DatabaseReference dbAngle = FirebaseDatabase.getInstance().getReference("Angle");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);

        preview = (ImageView) findViewById(R.id.preview);
        rootView = findViewById(R.id.rootView);
        neck_angle_tv = (TextView) findViewById(R.id.neck_angle_tv);
        neck_load_tv = (TextView) findViewById(R.id.neck_load_tv);
        graphicOverlay = findViewById(R.id.neck_overlay);

        getImage();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAngle();
            }
        }, 100); //딜레이 타임 조절

        neck_load_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NeckLoadActivity.class);
                intent.putExtra("angle", angle);
                startActivity(intent);
            }
        });

    }

    private void getImage() {
        Bundle extras = getIntent().getExtras();
        byte[] arr = extras.getByteArray("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        graphicOverlay.clear();

        preview.setImageBitmap(image);
        createImageProcessor();

        if (imageProcessor != null) {
            graphicOverlay.setImageSourceInfo(
                    image.getWidth(), image.getHeight(), /* isFlipped= */ false);
            imageProcessor.processBitmap(image, graphicOverlay);
        } else {
            Log.e(TAG, "Null imageProcessor, please check adb logs for imageProcessor creation error");
        }
    }

    private void createImageProcessor() {
        if (imageProcessor != null) {
            imageProcessor.stop();
        }
        PoseDetectorOptionsBase poseDetectorOptions =
                PreferenceUtils.getPoseDetectorOptionsForStillImage(this);
        Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
        boolean shouldShowInFrameLikelihood =
                PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodStillImage(this);
        boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
        boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
        boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
        poseImageProcessor = new PoseDetectorProcessor(
                this,
                poseDetectorOptions,
                CLASSIFIER,
                shouldShowInFrameLikelihood,
                visualizeZ,
                rescaleZ,
                runClassification,
                /* isStreamMode = */ false);
        imageProcessor = poseImageProcessor;
    }

    private void getAngle() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("M", Locale.KOREA);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        String date = yearFormat.format(currentTime) + "-" + monthFormat.format(currentTime) + "-" + dayFormat.format(currentTime);

        dbAngle.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting nickname", task.getException());
                } else {
                    angle = String.valueOf(task.getResult().getValue());
                    neck_angle_tv.setText("나의 목 각도 : " + angle + "°");
                }
            }
        });

    }

}
