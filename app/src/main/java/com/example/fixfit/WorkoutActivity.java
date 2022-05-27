package com.example.fixfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fixfit.Camera.CameraSource;
import com.example.fixfit.Camera.CameraSourcePreview;
import com.example.fixfit.Camera.GraphicOverlay;
import com.example.fixfit.Camera.PreferenceUtils;
import com.example.fixfit.Model.WorkoutModel;
import com.example.fixfit.posedetector.PoseDetectorProcessor;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.io.IOException;

public class WorkoutActivity extends AppCompatActivity {

    private TextView workout_txt, workout_time_txt;
    private ImageView workout_img;
    private LinearLayout workout_timer;
    private LinearLayout dialog_timer, dialog_finish;
    private WorkoutModel item;
    private int poseCode;
    private static final String TAG = "RealTimeActivity";
    private CameraSourcePreview preview;
    private CameraSource cameraSource = null;
    private GraphicOverlay graphicOverlay;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;



    private final String[] seconds = {"-초-", "10", "20", "30", "40", "50"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workout_txt = (TextView) findViewById(R.id.workout_txt);
        workout_img = (ImageView) findViewById(R.id.workout_img);
        workout_timer = (LinearLayout) findViewById(R.id.workout_timer);
        workout_time_txt = (TextView) findViewById(R.id.workout_time_txt);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bld = intent.getExtras();

            Object obj = bld.get("item");
            if (obj != null && obj instanceof WorkoutModel) {
                this.item = (WorkoutModel) obj;
            }
            this.poseCode = (int) bld.get("code");
        }

        workout_txt.setText(item.getWorkoutName());
        workout_img.setImageResource(item.getImgResId());

        preview = findViewById(R.id.workout_view);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.workout_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        createCameraSource();
        startCameraSource();


        workout_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }
        try {
            PoseDetectorOptionsBase poseDetectorOptions =
                    PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
            Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
            boolean shouldShowInFrameLikelihood =
                    PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
            boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
            boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
            boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
            cameraSource.setMachineLearningFrameProcessor(
                    new PoseDetectorProcessor(
                            this,
                            poseDetectorOptions,
                            poseCode,
                            shouldShowInFrameLikelihood,
                            visualizeZ,
                            rescaleZ,
                            runClassification,
                            /* isStreamMode = */ true));
        } catch (RuntimeException e) {
            Log.e(TAG, "Can not create image processor: " + "Pose Detector", e);
            Toast.makeText(
                    getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        createCameraSource();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    // 타이머
    private void startTimer(long time) {


        mCountDownTimer = new CountDownTimer(time + 1000, 1000) {
            @Override
            public void onTick(long l) {
                String format;
                if(l < 10000){
                    format = "00 : 0" + l / 1000;
                }
                else {
                    format = "00 : " + l / 1000;
                }
                workout_time_txt.setText(format);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                dialog_finish = (LinearLayout) View.inflate(WorkoutActivity.this, R.layout.dialog_finish, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(WorkoutActivity.this);
                dlg.setView(dialog_finish);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        }.start();
        mTimerRunning = true;

    }

    // 타이머 다이얼로그
    private void showDialog() {
        dialog_timer = (LinearLayout) View.inflate(WorkoutActivity.this, R.layout.dialog_timer, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(WorkoutActivity.this);
        Spinner spinner = (Spinner) dialog_timer.findViewById(R.id.timer_second);
        ArrayAdapter adapter = new ArrayAdapter(WorkoutActivity.this, android.R.layout.simple_spinner_item, seconds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dlg.setView(dialog_timer);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                long time = Long.parseLong(spinner.getSelectedItem().toString()) * 1000;
                Log.v("timer", spinner.getSelectedItem().toString());
                timerStartStop(time);
            }
        });
        dlg.show();
    }

    private void timerStartStop(long time){
        if(mTimerRunning) mCountDownTimer.cancel();
        startTimer(time);
    }
}

