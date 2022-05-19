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
    TextView tv_temp, tv_main, tv_description;
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
        tv_description = (TextView) findViewById(R.id.tv_description);
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
//        if(model.getWeather().get(0).getMain().equals("Clear")){
//            tv_main.setText("맑음");
//        }
        tv_main.setText(model.getWeather().get(0).getMain());

        tv_description.setText(model.getWeather().get(0).getDescription());
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


