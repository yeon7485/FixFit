package com.example.fixfit;

import static java.lang.Math.max;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fixfit.Camera.GraphicOverlay;
import com.example.fixfit.Camera.PreferenceUtils;
import com.example.fixfit.Camera.VisionImageProcessor;
import com.example.fixfit.posedetector.PoseDetectorProcessor;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;


public class ClassifierActivity2 extends AppCompatActivity {

    private static final String TAG = "ClassifierActivity2";

    private static final int CLASSIFIER = 101;

    private View rootView;
    private ImageView preview;
    private TextView neck_angle_tv;
    private GraphicOverlay graphicOverlay;
    private VisionImageProcessor imageProcessor;
    private PoseDetectorProcessor poseImageProcessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier2);

        preview = (ImageView) findViewById(R.id.preview);
        rootView = findViewById(R.id.rootView);
        neck_angle_tv = (TextView) findViewById(R.id.neck_angle_tv);
        graphicOverlay = findViewById(R.id.neck_overlay);
        getImage();

        //String angle = String.valueOf(Math.round(poseImageProcessor.getAngle()*10)/10.0);
        //neck_angle_tv.setText(angle);

    }

    private void getImage(){
        Bundle extras = getIntent().getExtras();
        byte[] arr = extras.getByteArray("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        graphicOverlay.clear();
//        Log.v("rootWidth", String.valueOf(rootView.getWidth()));
//        Log.v("rootHeight", String.valueOf(rootView.getHeight()));
//
//        float scaleFactor =
//                max(
//                        (float) image.getWidth() / (float) 1440,
//                        (float) image.getHeight() / (float) 2576);
//        Bitmap resizedBitmap =
//                Bitmap.createScaledBitmap(
//                        image,
//                        (int) (image.getWidth() / scaleFactor),
//                        (int) (image.getHeight() / scaleFactor),
//                        true);
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
}