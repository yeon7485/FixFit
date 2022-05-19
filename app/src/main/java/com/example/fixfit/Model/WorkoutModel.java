package com.example.fixfit.Model;

import java.io.Serializable;

public class WorkoutModel implements Serializable {
    private int imgResId;
    private String workoutName;

    public WorkoutModel(int imgResId, String workoutName) {
        this.imgResId = imgResId;
        this.workoutName = workoutName;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }
}
