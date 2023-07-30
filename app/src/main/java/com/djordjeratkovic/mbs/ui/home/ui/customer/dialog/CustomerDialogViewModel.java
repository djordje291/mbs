package com.djordjeratkovic.mbs.ui.home.ui.customer.dialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.djordjeratkovic.mbs.repository.MBSRepository;

public class CustomerDialogViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public CustomerDialogViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }
}
