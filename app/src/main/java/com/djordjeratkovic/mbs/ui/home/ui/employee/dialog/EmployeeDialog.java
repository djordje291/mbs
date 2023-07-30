package com.djordjeratkovic.mbs.ui.home.ui.employee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.DialogEmployeeBinding;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.model.Warehouse;
import com.djordjeratkovic.mbs.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDialog extends DialogFragment {
    //TODO: prosiri limite za editText i promeni layout

    private DialogEmployeeBinding binding;
    private EmployeeDialogViewModel employeeDialogViewModel;
    private List<Warehouse> warehouses = new ArrayList<>();

    private EmployeeDialogAdapter adapter;

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        employeeDialogViewModel = new ViewModelProvider(this).get(EmployeeDialogViewModel.class);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEmployeeBinding.inflate(getLayoutInflater());

        setWarehouse();
        setAdapter();
        setListeners();

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
            if (addEmployee()) {
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

    private boolean addEmployee() {
        if (CommonUtils.checkField(binding.firstNameEditText, binding.firstName,context) &&
            CommonUtils.checkField(binding.lastNameEditText, binding.lastName, context) &&
            CommonUtils.checkField(binding.cityEditText, binding.city, context) &&
            checkWarehouses()) {
            Log.d("DJOKENAJA", "setWarehouse: desilose se" );

            Employee employee = new Employee();

            employee.setFirstName(binding.firstNameEditText.getText().toString().trim());
            employee.setLastName(binding.lastNameEditText.getText().toString().trim());
            employee.setCity(binding.cityEditText.getText().toString().trim());
            employee.setWarehouses(warehouses);

            employeeDialogViewModel.addEmployee(employee);
            return true;
        } else {
            Log.d("DJOKENAJA", "setWarehouse: nije se desilo" );
            return false;
        }
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.warehousesRV.setLayoutManager(layoutManager);
        adapter = new EmployeeDialogAdapter(warehouses, getContext());
        binding.warehousesRV.setAdapter(adapter);
    }

    private void setListeners() {
        binding.addWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Warehouse warehouse = new Warehouse();
                warehouses.add(warehouse);
                adapter.notifyItemInserted(warehouses.size() - 1);
            }
        });
    }

    private boolean checkWarehouses() {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getName() == null || warehouse.getName().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void setWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouses.add(warehouse);
        Log.d("DJOKENAJA", "setWarehouse: " + warehouse.toString());
    }
}
