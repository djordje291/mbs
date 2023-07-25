package com.djordjeratkovic.mbs.ui.login_register.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.repository.MBSRepository;

public class LoadingFragmentViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public LoadingFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<Boolean> login(String user, String password) {
        return repository.login(user, password);
    }
}
