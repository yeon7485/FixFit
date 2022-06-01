package com.example.fixfit;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Animation anim_Shake;
    ImageView splash_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash_img = (ImageView) findViewById(R.id.splash_img);
        anim_Shake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
        anim_Shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        splash_img.startAnimation(anim_Shake);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, R.anim.anim_fadeout);
            }
        }, 2000);
    }

    public void onBackPressed() {
    } // 뒤로가기 버튼 막기

}