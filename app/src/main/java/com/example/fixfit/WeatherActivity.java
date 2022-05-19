package com.example.fixfit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.fixfit.Weather.APIClient;
import com.example.fixfit.Weather.APIService;
import com.example.fixfit.Weather.WeatherInfoModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    public static String TAG = "[" + WeatherActivity.class.getSimpleName() + "] ";
    Context context = WeatherActivity.this;
    TextView tv_name, tv_country;
    ImageView iv_weather;
    TextView tv_temp, tv_main;
    TextView tv_wind, tv_cloud, tv_humidity;
    APIService apiInterface = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        requestNetwork();
    }


    /* view 를 설정하는 메소드 */
    private void initView() {

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_country = (TextView) findViewById(R.id.tv_country);
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_main = (TextView) findViewById(R.id.tv_main);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_cloud = (TextView) findViewById(R.id.tv_cloud);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
    }


    /* retrofit 을 통해 통신을 요청하기 위한 메소드 */
    private void requestNetwork() {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String lat;
        String lon;
        double latitude = 0, longitude = 0;
        //retrofit 객체와 인터페이스 연결
        apiInterface = APIClient.getClient(getString(R.string.weather_url)).create(APIService.class);

        //gps 연결
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( WeatherActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        lat = String.valueOf(latitude);
        lon = String.valueOf(longitude);
        Log.i("위도", lat);
        Log.i("경도", lon);
        //통신 요청
        Call<WeatherInfoModel> call = apiInterface.doGetJsonData("weather",lat , lon, getString(R.string.weather_app_id));

        //응답 콜백 구현
        call.enqueue(new Callback<WeatherInfoModel>() {

            @Override
            public void onResponse(Call<WeatherInfoModel> call, Response<WeatherInfoModel> response) {
                WeatherInfoModel resource = response.body();
                if (response.isSuccessful()) {
                    setWeatherData(resource);  //UI 업데이트
                } else {
                    showFailPop();
                }
            }

            @Override
            public void onFailure(Call<WeatherInfoModel> call, Throwable t) {
                call.cancel();
                showFailPop();
            }

        });

    }


    /* 통신하여 받아온 날씨 데이터를 통해 UI 업데이트 메소드 */
    @SuppressLint("SetTextI18n")
    private void setWeatherData(WeatherInfoModel model) {
        tv_name.setText(model.getName());
        tv_country.setText(model.getSys().getCountry());
        Glide.with(context).load(getString(R.string.weather_url) + "img/w/" + model.getWeather().get(0).getIcon() + ".png")  //Glide 라이브러리를 이용하여 ImageView 에 url 로 이미지 지정
                .placeholder(R.drawable.icon_image)
                .error(R.drawable.icon_image)
                .into(iv_weather);
        tv_temp.setText(doubleToStrFormat(2, model.getMain().getTemp() - 273.15) + " 'C");  //소수점 2번째 자리까지 반올림하기

        switch (model.getWeather().get(0).getId()){
            case  "200":
                tv_main.setText("가벼운 비를 동반한 천둥구름");
                break;
            case  "201":
                tv_main.setText("비를 동반한 천둥구름");
                break;
            case  "202":
                tv_main.setText("폭우를 동반한 천둥구름");
                break;
            case  "210":
                tv_main.setText("약한 천둥구름");
                break;
            case  "211":
                tv_main.setText("천둥구름");
                break;
            case  "212":
                tv_main.setText("강한 천둥구름");
                break;
            case  "221":
                tv_main.setText("불규칙적 천둥구름");
                break;
            case  "230":
                tv_main.setText("약한 연무를 동반한 천둥구름");
                break;
            case  "231":
                tv_main.setText("연무를 동반한 천둥구름");
                break;
            case  "232":
                tv_main.setText("강한 안개비를 동반한 천둥구름");
                break;
            case  "300":
                tv_main.setText("가벼운 안개비");
                break;
            case  "302":
                tv_main.setText("강한 안개비");
                break;
            case  "310":
                tv_main.setText("가벼운 적은비");
                break;
            case  "311":
                tv_main.setText("적은비");
                break;
            case  "312":
                tv_main.setText("강한 적은비");
                break;
            case  "313":
                tv_main.setText("소나기와 안개비");
                break;
            case  "314":
                tv_main.setText("강한 소나기와 안개비");
                break;
            case  "321":
                tv_main.setText("소나기");
                break;
            case  "500":
                tv_main.setText("악한 비");
                break;
            case  "501":
                tv_main.setText("중간 비");
                break;
            case  "502":
                tv_main.setText("강한 비");
                break;
            case  "503":
                tv_main.setText("매우 강한 비");
                break;
            case  "504":
                tv_main.setText("극심한 비");
                break;
            case  "511":
                tv_main.setText("우박");
                break;
            case  "520":
                tv_main.setText("약한 소나기 비");
                break;
            case  "521":
                tv_main.setText("소나기 비");
                break;
            case  "522":
                tv_main.setText("강한 소나기 비");
                break;
            case  "531":
                tv_main.setText("불규칙적 소나기 비");
                break;
            case  "600":
                tv_main.setText("가벼운 눈");
                break;
            case  "601":
                tv_main.setText("눈");
                break;
            case  "602":
                tv_main.setText("강한 눈");
                break;
            case  "611":
                tv_main.setText("진눈깨비");
                break;
            case  "612":
                tv_main.setText("소나기 진눈깨비");
                break;
            case  "615":
                tv_main.setText("약한 비와 눈");
                break;
            case  "616":
                tv_main.setText("비와 눈");
                break;
            case  "620":
                tv_main.setText("약한 소나기 눈");
                break;
            case  "621":
                tv_main.setText("소나기 눈");
                break;
            case  "622":
                tv_main.setText("강한 소나기 눈");
                break;
            case  "701":
                tv_main.setText("박무");
                break;
            case  "711":
                tv_main.setText("연기");
                break;
            case  "721":
                tv_main.setText("연무");
                break;
            case  "731":
                tv_main.setText("모래 먼지");
                break;
            case  "741":
                tv_main.setText("안개");
                break;
            case  "751":
                tv_main.setText("모래");
                break;
            case  "761":
                tv_main.setText("먼지");
                break;
            case  "762":
                tv_main.setText("화산재");
                break;
            case  "771":
                tv_main.setText("돌풍");
                break;
            case  "781":
                tv_main.setText("토네이도");
                break;
            case  "800":
                tv_main.setText("구름 한 점 없는 맑은 하늘");
                break;
            case  "801":
                tv_main.setText("약간의 구름이 낀 하늘");
                break;
            case  "802":
                tv_main.setText("드문드문 구름이 낀 하늘");
                break;
            case  "803":
                tv_main.setText("구름이 거의 없는 하늘");
                break;
            case  "804":
                tv_main.setText("구름으로 뒤덮인 흐린 하늘");
                break;
            case  "900":
                tv_main.setText("토네이도");
                break;
            case  "901":
                tv_main.setText("태풍");
                break;
            case  "902":
                tv_main.setText("허리케인");
                break;
            case  "903":
                tv_main.setText("한랭");
                break;
            case  "904":
                tv_main.setText("고온");
                break;
            case  "905":
                tv_main.setText("바람부는");
                break;
            case  "906":
                tv_main.setText("우박");
                break;
            case  "951":
                tv_main.setText("바람이 거의 없는");
                break;
            case  "952":
                tv_main.setText("약한 바람");
                break;
            case  "953":
                tv_main.setText("부드러운 바람");
                break;
            case  "954":
                tv_main.setText("중간 세기 바람");
                break;
            case  "955":
                tv_main.setText("신선한 바람");
                break;
            case  "956":
                tv_main.setText("센 바람");
                break;
            case  "957":
                tv_main.setText("돌풍에 가까운 센 바람");
                break;
            case  "958":
                tv_main.setText("돌풍");
                break;
            case  "959":
                tv_main.setText("심각한 돌풍");
                break;
            case  "960":
                tv_main.setText("폭풍");
                break;
            case  "961":
                tv_main.setText("강한 폭풍");
                break;
            case  "962":
                tv_main.setText("허리케인");
                break;
        }

        tv_wind.setText(doubleToStrFormat(2, model.getWind().getSpeed()) + " m/s");
        tv_cloud.setText(doubleToStrFormat(2, model.getClouds().getAll()) + " %");
        tv_humidity.setText(doubleToStrFormat(2, model.getMain().getHumidity()) + " %");
    }


    /* 통신 실패시 AlertDialog 표시하는 메소드 */
    private void showFailPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title").setMessage("통신실패");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /* 소수점 n번째 자리까지 반올림하기 */
    private String doubleToStrFormat(int n, double value) {
        return String.format("%." + n + "f", value);
    }


}


