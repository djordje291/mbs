package com.djordjeratkovic.mbs.ui.home.ui.customer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.DialogCustomerBinding;
import com.djordjeratkovic.mbs.databinding.DialogEmployeeBinding;
import com.djordjeratkovic.mbs.model.Customer;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.ui.home.ui.employee.dialog.EmployeeDialogViewModel;

import java.util.List;

public class CustomerDialog extends DialogFragment {

    private DialogCustomerBinding binding;
    private CustomerDialogViewModel viewModel;
    private Customer customer;
    private List<Employee> employees;
    private Context context;

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
        
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbar.setTitle(getString(R.string.new_employee));
        binding.toolbar.inflateMenu(R.menu.dialog_menu);
        binding.toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (addCustomer()) {
                dismiss();
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
        return false;
    }

}
