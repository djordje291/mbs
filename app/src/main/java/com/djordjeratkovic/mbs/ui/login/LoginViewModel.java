package com.djordjeratkovic.mbs.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.djordjeratkovic.mbs.repository.MBSRepository;

public class LoginViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }
}
