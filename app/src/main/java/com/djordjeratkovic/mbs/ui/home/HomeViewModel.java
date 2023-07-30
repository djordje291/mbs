package com.djordjeratkovic.mbs.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.repository.MBSRepository;

public class HomeViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return repository.getLoggedOutLiveData();
    }
}
