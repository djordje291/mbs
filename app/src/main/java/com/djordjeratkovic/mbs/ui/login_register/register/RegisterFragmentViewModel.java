package com.djordjeratkovic.mbs.ui.login_register.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.repository.MBSRepository;

public class RegisterFragmentViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public RegisterFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<Boolean> createAccount(String ime, String user, String password) {
        return repository.createAccount(ime, user, password);
    }
}
