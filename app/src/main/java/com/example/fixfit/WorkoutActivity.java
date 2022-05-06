package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fixfit.item.Workout;

public class WorkoutActivity extends AppCompatActivity {

    private TextView workout_txt;
    private ImageView workout_img;
    private Workout item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workout_txt = (TextView) findViewById(R.id.workout_txt);
        workout_img = (ImageView) findViewById(R.id.workout_img);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bld = intent.getExtras();

            Object obj = bld.get("item");
            if (obj != null && obj instanceof Workout) {
                this.item = (Workout) obj;
            }
        }

        workout_txt.setText(item.getWorkoutName());
        workout_img.setImageResource(item.getImgResId());

    }
}