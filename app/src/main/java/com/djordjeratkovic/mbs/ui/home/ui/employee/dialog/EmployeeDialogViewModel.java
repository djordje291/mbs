package com.djordjeratkovic.mbs.ui.home.ui.employee.dialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.repository.MBSRepository;

public class EmployeeDialogViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public EmployeeDialogViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public void addEmployee(Employee employee) {
        repository.addEmployee(employee);
    }
}
