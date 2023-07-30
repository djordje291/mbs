package com.djordjeratkovic.mbs.repository.network;

import com.djordjeratkovic.mbs.model.WeatherAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPIService {

    //https://api.openweathermap.org/data/2.5/weather?q=Washington&units=metric&APPID=2fe662002a89fc8db04b171b53b089dd

//    @GET("weather?q={city}&units=metric&APPID={key}")
//    Call<WeatherAPI> getWeatherInfo(@Path("city") String city,
//                                    @Path("key") String key);

    @GET("weather?")
    Call<WeatherAPI> getWeatherInfo(@Query("q") String city,
                                    @Query("units") String unit,
                                    @Query("APPID") String key);
}
