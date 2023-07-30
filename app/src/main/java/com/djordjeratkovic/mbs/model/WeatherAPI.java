package com.djordjeratkovic.mbs.model;

import com.google.gson.annotations.SerializedName;

public class WeatherAPI {

    @SerializedName("main")
    private Weather weather;

    public WeatherAPI(Weather weather) {
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
