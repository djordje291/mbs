package com.djordjeratkovic.mbs.ui.home.ui.customer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.DialogCustomerBinding;
import com.djordjeratkovic.mbs.databinding.DialogEmployeeBinding;
import com.djordjeratkovic.mbs.model.Customer;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.ui.home.ui.customer.EmployeeDropdownAdapter;
import com.djordjeratkovic.mbs.ui.home.ui.employee.dialog.EmployeeDialogViewModel;
import com.djordjeratkovic.mbs.util.CommonUtils;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class CustomerDialog extends DialogFragment {

    private DialogCustomerBinding binding;
    private CustomerDialogViewModel viewModel;
    private Customer customer;
    private List<Employee> employees;
    private Employee selectedEmployee;
    private Context context;
    EmployeeDropdownAdapter employeeDropdownAdapter;

    public CustomerDialog(Customer customer, List<Employee> employees) {
        this.customer = customer;
        this.employees = employees;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        viewModel = new ViewModelProvider(this).get(CustomerDialogViewModel.class);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCustomerBinding.inflate(getLayoutInflater());

        if (customer != null) {
            bind();
        }

        setAdapter();
        setListener();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer != null) {
            binding.toolbar.setTitle(getString(R.string.update_customer));
        } else {
            binding.toolbar.setTitle(getString(R.string.new_customer));
        }
        binding.toolbar.inflateMenu(R.menu.dialog_menu);
        binding.toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (customer != null) {
                if (updateCustomer()) {
                    dismiss();
                }
            } else {
                if (addCustomer()) {
                    dismiss();
                }
            }
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private boolean addCustomer() {
        if (CommonUtils.checkField(binding.nameEditText, binding.name, context) &&
                CommonUtils.checkField(binding.PIBEditText, binding.PIB, context) &&
                checkDropDownSelected()) {
            Customer customer = new Customer();
            customer.setName(binding.nameEditText.getText().toString().trim());
            customer.setPIB(Long.parseLong(binding.PIBEditText.getText().toString().trim()));
            customer.setEmployeeDocRef(selectedEmployee.getDocRef());

            viewModel.addCustomer(customer);
            return true;
        } else {
            return false;
        }
    }

    private boolean updateCustomer() {
        if (CommonUtils.checkField(binding.nameEditText, binding.name, context) &&
                CommonUtils.checkField(binding.PIBEditText, binding.PIB, context) &&
                checkDropDownSelected()) {
            customer.setName(binding.nameEditText.getText().toString().trim());
            customer.setPIB(Long.parseLong(binding.PIBEditText.getText().toString().trim()));
            customer.setEmployeeDocRef(selectedEmployee.getDocRef());

            viewModel.updateCustomer(customer);
            return true;
        } else {
            return false;
        }
    }

    private void bind() {
        binding.setCustomer(customer);
        binding.dropDown.setText(getEmployeePosition());
    }

    private String getEmployeePosition() {
        for (Employee employee : employees) {
            if (employee.getDocRef().equals(customer.getEmployeeDocRef())) {
                selectedEmployee = employee;
                return employee.toString();
            }
        }
        return "";
    }

    private boolean checkDropDownSelected() {
        return selectedEmployee != null;
    }

    private void setAdapter() {
        employeeDropdownAdapter = new EmployeeDropdownAdapter(context, employees);
        binding.dropDown.setAdapter(employeeDropdownAdapter);
    }

    private void setListener() {
        binding.dropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEmployee = employees.get(i);
            }
        });
    }

}
