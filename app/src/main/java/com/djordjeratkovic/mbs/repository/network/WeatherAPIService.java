package com.djordjeratkovic.mbs.repository.network;

import com.djordjeratkovic.mbs.model.WeatherAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherAPIService {

    @GET("onecall?lat={lat}&lon={lon}&exclude={part}&apiid={key}")
    Call<WeatherAPI> getWeatherInfo(@Path("lat") float lat,
                                    @Path("lon") float lon,
                                    @Path("part") String part,
                                    @Path("key") String key);
}
