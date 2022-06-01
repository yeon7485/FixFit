
package com.example.fixfit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragHome fragHome = new FragHome();
    private FragGuide fragGuide = new FragGuide();
    private FragSetup fragSetup = new FragSetup();

    public final String PREFERENCE = "com.example.fixfit";
    public final String image = "image";
    String themeColor;

    TextView tName, tSex, tHeight, tBirth;
    CircleImageView ImgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String code = intent.getStringExtra("noFirst");
        if (code == null) {
            startActivity(new Intent(this, SplashActivity.class));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    firstSetting();
                }
            }, 1000);
        } else {
            firstSetting();
        }

    }

    private void firstSetting() {
        setContentView(R.layout.activity_main);
        themeColor = ThemeUtil.modLoad(getApplicationContext());
        ThemeUtil.applyTheme(themeColor);

        tName = findViewById(R.id.tName);
        tSex = findViewById(R.id.tSex);
        tHeight = findViewById(R.id.tHeight);
        tBirth = findViewById(R.id.tBirth);

        tName.setText(getPreferenceString("name"));
        tSex.setText(getPreferenceString("sex"));
        tHeight.setText(getPreferenceString("height"));
        tBirth.setText(getPreferenceString("birth"));

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        ImgProfile = (CircleImageView) findViewById(R.id.ImgProfile);
        ImgProfile.setImageBitmap(getBitmap(image));
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
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

    public void getTextData(String name, String sex, String height, String birth) {
        setPreference("name", name);
        setPreference("sex", sex);
        setPreference("height", height + " cm");
        setPreference("birth", birth);

        tName.setText(getPreferenceString("name"));
        tSex.setText(getPreferenceString("sex"));
        tHeight.setText(getPreferenceString("height"));
        tBirth.setText(getPreferenceString("birth"));
    }

    public void setPreference(String key, String value) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public void setBitmap(String key, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        setPreference(key, temp);
    }

    public Bitmap getBitmap(String key) {
        String temp = getPreferenceString(key);
        try {
            byte[] encodeByte = Base64.decode(temp, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
