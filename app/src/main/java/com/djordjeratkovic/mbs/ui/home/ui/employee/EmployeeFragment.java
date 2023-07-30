package com.djordjeratkovic.mbs.ui.home.ui.employee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.FragmentEmployeeBinding;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.ui.home.ui.employee.dialog.EmployeeDialog;
import com.djordjeratkovic.mbs.ui.login_register.LoginActivity;
import com.djordjeratkovic.mbs.util.CommonUtils;
import com.djordjeratkovic.mbs.util.Constants;
import com.djordjeratkovic.mbs.util.Sleeper;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFragment extends Fragment {

    private FragmentEmployeeBinding binding;
    private Context context;
    private EmployeeViewModel viewModel;

    private List<Employee> employeeList = new ArrayList<>();
    private EmployeeAdapter employeeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        setAdapter();
        setListeners();
        setObservers();
        setMenu();

        CommonUtils.loading(binding.loading, true);

        return root;
    }

    private void setListeners() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeDialog employeeDialog = new EmployeeDialog();
                employeeDialog.show(getParentFragmentManager(), Constants.EMPLOYEE_DIALOG_TAG);
            }
        });
    }

    private void setObservers() {
        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (!employees.isEmpty()) {
                if (!employeeList.isEmpty()) {
                    employeeList.clear();
                }
                employeeList.addAll(employees);

                CommonUtils.loading(binding.empty, false);
                CommonUtils.loading(binding.lottieAnim, false);
                CommonUtils.loading(binding.loading, false);

                CommonUtils.refreshAdapter(employeeAdapter, null);
            } else {
                employeeList.clear();
                new Sleeper(binding.empty, binding.loading, employees, employeeList, binding.lottieAnim).start();
                CommonUtils.refreshAdapter(employeeAdapter, null);
            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.employeeRV.setLayoutManager(layoutManager);

        employeeAdapter = new EmployeeAdapter(employeeList, getContext(), viewModel);
        binding.employeeRV.setAdapter(employeeAdapter);

    }

    private void setMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.employee_menu, menu);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}