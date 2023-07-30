package com.djordjeratkovic.mbs.ui.home.ui.employee;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.repository.MBSRepository;

import java.util.List;

public class EmployeeViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public void logout() {
        repository.logOut();
    }

    public LiveData<List<Employee>> getEmployees() {
        return repository.getEmployees();
    }
}