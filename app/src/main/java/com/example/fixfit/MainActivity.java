
package com.example.fixfit;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragHome fragHome = new FragHome();
    private FragGuide fragGuide = new FragGuide();
    private FragSetup fragSetup = new FragSetup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId())
            {
                case R.id.action_home:
                    transaction.replace(R.id.frameLayout, fragHome).commitAllowingStateLoss();
                    break;
                case R.id.action_guide:
                    transaction.replace(R.id.frameLayout, fragGuide).commitAllowingStateLoss();
                    break;
/*
                리스트랑 날씨쪽 xml만들어야해요

                case R.id.action_list:
                    transaction.replace(R.id.frameLayout, fraList).commitAllowingStateLoss();
                    break;
                case R.id.action_weather:
                    transaction.replace(R.id.frameLayout, fragWeather).commitAllowingStateLoss();
                    break;

 */
                case R.id.action_setup:
                    transaction.replace(R.id.frameLayout, fragSetup).commitAllowingStateLoss();
                    break;

            }
            return true;
        }
    }
}
