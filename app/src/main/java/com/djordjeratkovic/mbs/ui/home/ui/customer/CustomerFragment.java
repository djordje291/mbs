package com.djordjeratkovic.mbs.ui.home.ui.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.FragmentCustomerBinding;
import com.djordjeratkovic.mbs.model.Customer;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.ui.home.ui.customer.dialog.CustomerDialog;
import com.djordjeratkovic.mbs.ui.home.ui.employee.EmployeeAdapter;
import com.djordjeratkovic.mbs.ui.login_register.LoginActivity;
import com.djordjeratkovic.mbs.util.CommonUtils;
import com.djordjeratkovic.mbs.util.Constants;
import com.djordjeratkovic.mbs.util.ItemTouchHelperEdit;
import com.djordjeratkovic.mbs.util.Sleeper;
import com.djordjeratkovic.mbs.util.SwipeToDeleteAndEditCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;


public class CustomerFragment extends Fragment implements ItemTouchHelperEdit {
    //TODO: set dropdown to null

    private FragmentCustomerBinding binding;
    private List<Employee> employeeList = new ArrayList<>();
    private List<Customer> fullCustomerList = new ArrayList<>();
    private CustomerViewModel viewModel;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList = new ArrayList<>();
    private Context context;

    EmployeeDropdownAdapter employeeDropdownAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();


        setListeners();
        setAdapters();
        setObservers();
        setupMenu();

        CommonUtils.loading(binding.loading, true);

        return root;
    }

    private void setListeners() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!employeeList.isEmpty()) {
                    CustomerDialog customerDialog = new CustomerDialog(null, employeeList);
                    customerDialog.show(getParentFragmentManager(), Constants.CUSTOMER_DIALOG_TAG);
                } else {
                    Toast.makeText(context, getString(R.string.no_employee), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.dropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                changeList(employeeList.get(i));
                CommonUtils.refreshAdapter(customerAdapter, null);
                checkIfEmpty();
            }
        });
    }

    private void checkIfEmpty() {
        if (customerList.isEmpty()) {
            CommonUtils.loading(binding.empty, true);
            CommonUtils.loading(binding.lottieAnim, true);
        } else {
            CommonUtils.loading(binding.empty, false);
            CommonUtils.loading(binding.lottieAnim, false);
        }
    }

    private void changeList(Employee employee) {
        customerList.clear();
        for (Customer customer : fullCustomerList) {
            if (customer.getEmployeeDocRef().equals(employee.getDocRef())) {
                customerList.add(customer);
            }
        }
    }

    private void setAdapters() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.customerRV.setLayoutManager(layoutManager);

        customerAdapter = new CustomerAdapter(customerList, getContext(), viewModel, employeeList);
        binding.customerRV.setAdapter(customerAdapter);

        SwipeToDeleteAndEditCallback callback = new SwipeToDeleteAndEditCallback(context, customerAdapter, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.customerRV);

        setArrayAdapter();
    }

    private void setArrayAdapter() {
        employeeDropdownAdapter = new EmployeeDropdownAdapter(context, employeeList);
        binding.dropDown.setAdapter(employeeDropdownAdapter);
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.customer_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                                .setMessage(context.getResources().getString(R.string.logout_question))
                                .setPositiveButton(R.string.potvrdi, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        viewModel.logout();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton(R.string.otkazi, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        builder.show();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void setObservers() {
        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (!employees.isEmpty()) {
                if (!employeeList.isEmpty()) {
                    employeeList.clear();
                }
                employeeList.addAll(employees);

                employeeDropdownAdapter.notifyDataSetChanged();
            } else {
                employeeList.clear();
                employeeDropdownAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getCustomers().observe(getViewLifecycleOwner(), customers -> {
            if (!customers.isEmpty()) {
                if (!customerList.isEmpty()) {
                    customerList.clear();
                }
                if (!fullCustomerList.isEmpty()) {
                    fullCustomerList.clear();
                }
                customerList.addAll(customers);
                fullCustomerList.addAll(customers);

                CommonUtils.loading(binding.empty, false);
                CommonUtils.loading(binding.lottieAnim, false);
                CommonUtils.loading(binding.loading, false);
                binding.dropDownLayout.setVisibility(View.VISIBLE);
                binding.dropDown.setText("");

                CommonUtils.refreshAdapter(customerAdapter, null);
            } else {
                customerList.clear();
                fullCustomerList.clear();
                binding.dropDownLayout.setVisibility(View.GONE);
                new Sleeper(binding.empty, binding.loading, customers, customerList, binding.lottieAnim).start();
                CommonUtils.refreshAdapter(customerAdapter, null);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.dropDown.setText("");
    }

    @Override
    public void onItemEdit(int position) {
        CommonUtils.refreshAdapter(customerAdapter, position);
        CustomerDialog customerDialog = new CustomerDialog(customerList.get(position), employeeList);
        customerDialog.show(getParentFragmentManager(), Constants.CUSTOMER_DIALOG_TAG);
    }
}