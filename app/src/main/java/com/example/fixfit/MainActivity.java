
package com.example.fixfit;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragHome fragHome = new FragHome();
    private FragGuide fragGuide = new FragGuide();
    private FragSetup fragSetup = new FragSetup();

    TextView tName, tSex, tHeight, tBirth;
    ImageView ImgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
           
        ImgProfile = (ImageView) findViewById(R.id.ImgProfile);

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

                case R.id.action_list:
                    Intent intent2 = new Intent(MainActivity.this, ToDoRoutineActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.action_weather:
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    startActivity(intent);
                    break;

                case R.id.action_setup:
                    transaction.replace(R.id.frameLayout, fragSetup).commitAllowingStateLoss();
                    break;

            }
            return true;
        }
    }
    
    public void getTextData(String name, String sex, String height, String birth){
        tName = findViewById(R.id.tName);
        tSex = findViewById(R.id.tSex);
        tHeight = findViewById(R.id.tHeight);
        tBirth = findViewById(R.id.tBirth);

        tName.setText(name);
        tSex.setText(sex);
        tHeight.setText(height + " cm");
        tBirth.setText(birth);
    }
}
