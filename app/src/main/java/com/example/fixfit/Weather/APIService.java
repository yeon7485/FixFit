package com.example.fixfit.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("data/2.5/{path}")
    Call<WeatherInfoModel> doGetJsonData(
            @Path("path") String path,
            @Query("lat") String lat,
            @Query("lon") String lon,
            //@Query("q") String q,
            @Query("appid") String appid
    );
}
