package com.djordjeratkovic.mbs.ui.home.ui.employee;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.EmployeeCardBinding;
import com.djordjeratkovic.mbs.databinding.EmployeeCardBindingImpl;
import com.djordjeratkovic.mbs.model.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{

    private List<Employee> employees;
    private Context context;
    private EmployeeViewModel viewModel;

    private WarehouseAdapter warehouseAdapter;

    Animation dropdown;
    Animation slideUp;

    public EmployeeAdapter(List<Employee> employees, Context context, EmployeeViewModel viewModel) {
        this.employees = employees;
        this.context = context;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmployeeCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.employee_card, parent, false);

        dropdown = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.dropdown);
        slideUp = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.bind(employee);

        setAdapter(holder.binding, employee);

        holder.binding.warehousesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeRVVisibility(holder.binding);
            }
        });
        holder.binding.docRef.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String textToCopy = ((TextView) view).getText().toString();
                copyToClipboard(textToCopy.substring(1));
                Toast.makeText(context, context.getString(R.string.copied), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.employee_id), text);
        clipboard.setPrimaryClip(clip);
    }

    private void changeRVVisibility(EmployeeCardBinding binding) {
        if (binding.warehousesRV.getVisibility() == View.VISIBLE) {
            binding.warehousesRV.startAnimation(slideUp);
            binding.warehousesRV.setVisibility(View.GONE);
        } else {
            binding.warehousesRV.startAnimation(dropdown);
            binding.warehousesRV.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(EmployeeCardBinding binding, Employee employee) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.warehousesRV.setLayoutManager(layoutManager);

        warehouseAdapter = new WarehouseAdapter(employee.getWarehouses(), context);
        binding.warehousesRV.setAdapter(warehouseAdapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EmployeeCardBinding binding;

        public ViewHolder(EmployeeCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Employee employee) {
            binding.setEmployee(employee);
            binding.executePendingBindings();
        }
    }
}
