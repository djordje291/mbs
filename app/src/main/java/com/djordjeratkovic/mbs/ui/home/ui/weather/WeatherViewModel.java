package com.djordjeratkovic.mbs.ui.home.ui.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.model.Weather;
import com.djordjeratkovic.mbs.model.WeatherAPI;
import com.djordjeratkovic.mbs.repository.MBSRepository;

public class WeatherViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public void checkWeather(String city) {
        repository.checkWeather(city);
    }

    public LiveData<WeatherAPI> getWeather() {
        return repository.getWeather();
    }

    public void logout() {
        repository.logOut();
    }
}