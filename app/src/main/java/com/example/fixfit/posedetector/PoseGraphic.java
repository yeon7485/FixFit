/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.fixfit.posedetector;

import static java.lang.Math.atan2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.PointF3D;
import com.example.fixfit.Camera.GraphicOverlay;
import com.example.fixfit.Camera.GraphicOverlay.Graphic;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Draw the detected pose in preview.
 */
public class PoseGraphic extends Graphic {

    private static final float DOT_RADIUS = 8.0f;
    private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
    private static final float STROKE_WIDTH = 10.0f;
//  private static final float POSE_CLASSIFICATION_TEXT_SIZE = 60.0f;

    private final Pose pose;
    private final boolean showInFrameLikelihood;
    private final boolean visualizeZ;
    private final boolean rescaleZForVisualization;
    private float zMin = Float.MAX_VALUE;
    private float zMax = Float.MIN_VALUE;
    private int poseCode;
    private double neckAngle;

    private final List<String> poseClassification;
    private final Paint classificationTextPaint;
    private final Paint leftPaint;
    private final Paint rightPaint;
    private final Paint whitePaint;
    private final Paint redPaint;

    PoseGraphic(
            GraphicOverlay overlay,
            Pose pose,
            int poseCode,
            boolean showInFrameLikelihood,
            boolean visualizeZ,
            boolean rescaleZForVisualization,
            List<String> poseClassification) {
        super(overlay);
        this.pose = pose;
        this.poseCode = poseCode;
        this.showInFrameLikelihood = showInFrameLikelihood;
        this.visualizeZ = visualizeZ;
        this.rescaleZForVisualization = rescaleZForVisualization;

        this.poseClassification = poseClassification;
        classificationTextPaint = new Paint();
        classificationTextPaint.setColor(Color.WHITE);
//    classificationTextPaint.setTextSize(POSE_CLASSIFICATION_TEXT_SIZE);
        classificationTextPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setStrokeWidth(STROKE_WIDTH);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
        leftPaint = new Paint();
        leftPaint.setStrokeWidth(STROKE_WIDTH);
        leftPaint.setColor(Color.RED);
        rightPaint = new Paint();
        rightPaint.setStrokeWidth(STROKE_WIDTH);
        rightPaint.setColor(Color.GREEN);
        redPaint = new Paint();
        redPaint.setStrokeWidth(STROKE_WIDTH);
        redPaint.setColor(Color.RED);
        redPaint.setTextSize(50);
    }

    @Override
    public void draw(Canvas canvas) {
        List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();

        if (landmarks.isEmpty()) {
            return;
        }

        // Draw all the points
        for (PoseLandmark landmark : landmarks) {
            drawPoint(canvas, landmark, whitePaint);
            if (visualizeZ && rescaleZForVisualization) {
                zMin = min(zMin, landmark.getPosition3D().getZ());
                zMax = max(zMax, landmark.getPosition3D().getZ());
            }
        }

        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
        PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);

        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

        PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
        PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
        PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
        PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);


        //각도 계산
        //waist 1
        double leftArmAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
                pose.getPoseLandmark(PoseLandmark.LEFT_WRIST));

        double rightArmAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
                pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST));

        double leftShoulderAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
                pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.LEFT_HIP));

        double rightShoulderAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
                pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.RIGHT_HIP));

        double leftHipAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
                pose.getPoseLandmark(PoseLandmark.LEFT_KNEE));

        double rightHipAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE));

        double leftKneeAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
                pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
                pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE));

        double rightKneeAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
                pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE));

        double leftFootAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
                pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE),
                pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX));

        double rightFootAngle = getAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
                pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE),
                pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX));

        neckAngle = getNeckAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_EAR),
                pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER));

        if(poseCode == 101){
            drawLine(canvas, rightEar, rightShoulder, redPaint);
        }
        else {

            drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
            drawLine(canvas, leftHip, rightHip, whitePaint);

            long arm = 0, shoulder = 0, hip = 0, knee = 0, lKnee = 0, rKnee = 0, foot = 0;
            Log.v("poseCode", String.valueOf(poseCode));
            if (poseCode < 2000) {
                switch (poseCode) {
                    case 1000:
                        arm = 75;
                        shoulder = 145;
                        break;
                }
            } else if (poseCode < 3000) {
                switch (poseCode) {
                    case 2000:
                        arm = 175;
                        shoulder = 113;
                        hip = 83;
                        knee = 90;
                        break;
                    case 2001:
                        arm = 175;
                        shoulder = 113;
                        hip = 83;
                        knee = 94;
                        break;
                }
            } else {
                switch (poseCode) {
                    case 3000:
                        hip = 97;
                        lKnee = 150;
                        rKnee = 90;
                        foot = 90;
                        break;
                }
            }


            // 무릎 재활 운동일 때
            if (poseCode < 4000 && poseCode >= 3000) {
                drawLine(canvas, leftShoulder, leftElbow, whitePaint);
                drawLine(canvas, leftElbow, leftWrist, whitePaint);
                drawLine(canvas, rightShoulder, rightElbow, whitePaint);
                drawLine(canvas, rightElbow, rightWrist, whitePaint);
                drawLine(canvas, leftShoulder, leftHip, whitePaint);
                drawLine(canvas, leftElbow, leftShoulder, whitePaint);
                drawLine(canvas, rightShoulder, rightHip, whitePaint);
                drawLine(canvas, rightElbow, rightShoulder, whitePaint);

            } else {
                // 왼쪽 팔 각도
                if (round(leftArmAngle) >= (arm - 15) && round(leftArmAngle) <= (arm + 15)) {
                    drawLine(canvas, leftShoulder, leftElbow, rightPaint);
                    drawLine(canvas, leftElbow, leftWrist, rightPaint);
                } else {
                    drawLine(canvas, leftShoulder, leftElbow, leftPaint);
                    drawLine(canvas, leftElbow, leftWrist, leftPaint);
                }
                // 오른쪽 팔 각도
                if (round(rightArmAngle) >= (arm - 15) && round(rightArmAngle) <= (arm + 15)) {
                    drawLine(canvas, rightShoulder, rightElbow, rightPaint);
                    drawLine(canvas, rightElbow, rightWrist, rightPaint);
                } else {
                    drawLine(canvas, rightShoulder, rightElbow, leftPaint);
                    drawLine(canvas, rightElbow, rightWrist, leftPaint);
                }

                // 왼쪽 어깨 각도
                if (round(leftShoulderAngle) >= (shoulder - 15) && round(leftShoulderAngle) <= (shoulder + 15)) {
                    drawLine(canvas, leftShoulder, leftHip, rightPaint);
                    drawLine(canvas, leftElbow, leftShoulder, rightPaint);
                } else {
                    drawLine(canvas, leftShoulder, leftHip, leftPaint);
                    drawLine(canvas, leftElbow, leftShoulder, leftPaint);
                }
                // 오른쪽 어깨 각도
                if (round(rightShoulderAngle) >= (shoulder - 15) && round(rightShoulderAngle) <= (shoulder + 15)) {
                    drawLine(canvas, rightShoulder, rightHip, rightPaint);
                    drawLine(canvas, rightElbow, rightShoulder, rightPaint);
                } else {
                    drawLine(canvas, rightShoulder, rightHip, leftPaint);
                    drawLine(canvas, rightElbow, rightShoulder, leftPaint);
                }
            }
            if (poseCode >= 2000) {
                // 왼쪽 힙 각도
                if (round(leftHipAngle) >= (hip - 15) && round(leftHipAngle) <= (hip + 15)) {
                    drawLine(canvas, leftShoulder, leftHip, rightPaint);
                    drawLine(canvas, leftHip, leftKnee, rightPaint);
                } else {
                    drawLine(canvas, leftShoulder, leftHip, leftPaint);
                    drawLine(canvas, leftHip, leftKnee, leftPaint);
                }
                // 오른쪽 힙 각도
                if (round(rightHipAngle) >= (hip - 15) && round(rightHipAngle) <= (hip + 15)) {
                    drawLine(canvas, rightShoulder, rightHip, rightPaint);
                    drawLine(canvas, rightHip, rightKnee, rightPaint);
                } else {
                    drawLine(canvas, rightShoulder, rightHip, leftPaint);
                    drawLine(canvas, rightHip, rightKnee, leftPaint);
                }

                // 왼쪽 무릎 각도
                if (round(leftKneeAngle) >= (lKnee - 15) && round(leftKneeAngle) <= (lKnee + 15)) {
                    drawLine(canvas, leftKnee, leftHip, rightPaint);
                    drawLine(canvas, leftKnee, leftAnkle, rightPaint);
                } else {
                    drawLine(canvas, leftKnee, leftHip, leftPaint);
                    drawLine(canvas, leftKnee, leftAnkle, leftPaint);
                }
                // 오른쪽 무릎 각도
                if (round(rightKneeAngle) >= (rKnee - 15) && round(rightKneeAngle) <= (rKnee + 15)) {
                    drawLine(canvas, rightKnee, rightHip, rightPaint);
                    drawLine(canvas, rightKnee, rightAnkle, rightPaint);
                } else {
                    drawLine(canvas, rightKnee, rightHip, leftPaint);
                    drawLine(canvas, rightKnee, rightAnkle, leftPaint);
                }
            }

            Log.v("leftArmAngle", String.valueOf(leftArmAngle));
        }
    }

    void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
        PointF3D point = landmark.getPosition3D();
        maybeUpdatePaintColor(paint, canvas, point.getZ());
        if(poseCode == 101){
            if (landmark.getLandmarkType() == PoseLandmark.RIGHT_EAR || landmark.getLandmarkType() == PoseLandmark.RIGHT_SHOULDER) {
                canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
            }
        }
        else {
            if (landmark.getLandmarkType() == 0 || (landmark.getLandmarkType() >= 11 && landmark.getLandmarkType() <= 16)) {
                canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
            }

            if (poseCode >= 2000) {
                if ((landmark.getLandmarkType() >= 23 && landmark.getLandmarkType() <= 28
                        || landmark.getLandmarkType() == 31 || landmark.getLandmarkType() == 32)) {
                    canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
                }
            }
        }
    }

    void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
        PointF3D start = startLandmark.getPosition3D();
        PointF3D end = endLandmark.getPosition3D();

        // Gets average z for the current body line
        float avgZInImagePixel = (start.getZ() + end.getZ()) / 2;
        maybeUpdatePaintColor(paint, canvas, avgZInImagePixel);

        canvas.drawLine(
                translateX(start.getX()),
                translateY(start.getY()),
                translateX(end.getX()),
                translateY(end.getY()),
                paint);
    }

    private void maybeUpdatePaintColor(Paint paint, Canvas canvas, float zInImagePixel) {
        if (!visualizeZ) {
            return;
        }
    }

    private double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }

    public double getNeckAngle(PoseLandmark startPoint, PoseLandmark endPoint) {
        double dy = endPoint.getPosition().y-startPoint.getPosition().y;
        double dx = endPoint.getPosition().x-startPoint.getPosition().x;
        double angle = Math.atan(dy/dx) * (180.0/Math.PI);
        double degree = Math.toDegrees(Math.atan2(dx, dy));


        if(dx < 0.0) {
            angle += 180.0;
        } else {
            if(dy<0.0) angle += 360.0;
        }

        //return angle;
        return Math.abs(degree);
    }

    public double getTechNeck(){
        return neckAngle;
    }

}
