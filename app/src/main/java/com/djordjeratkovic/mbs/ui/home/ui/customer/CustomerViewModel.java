package com.djordjeratkovic.mbs.ui.home.ui.customer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.mbs.model.Customer;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.repository.MBSRepository;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {

    private MBSRepository repository;

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        repository = MBSRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<List<Employee>> getEmployees() {
        return  repository.getEmployees();
    }

    public LiveData<List<Customer>> getCustomers() {
        return repository.getCustomers();
    }

    public void deleteCustomer(Customer customer) {
        repository.deleteCustomer(customer);
    }

    public void logout() {
        repository.logOut();
    }

}