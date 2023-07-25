package com.djordjeratkovic.mbs.ui.login_register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.repository.MBSRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return repository.getUserLiveData();
    }

    public void login(String user, String password) {
        repository.login(user, password);
    }
}
