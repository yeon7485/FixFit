package com.example.fixfit.Weather;

import com.google.gson.annotations.SerializedName;

public class WeatherWeatherModel {

    @SerializedName("main")
    String main = "";  //날씨

    @SerializedName("id")
    String id = "";  //아이디

    @SerializedName("description")
    String description = "";   //상세 날씨 설명

    @SerializedName("icon")
    String icon = "";  //날씨 이미지

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}