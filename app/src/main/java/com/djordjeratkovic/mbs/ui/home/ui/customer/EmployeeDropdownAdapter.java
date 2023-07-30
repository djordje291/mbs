package com.djordjeratkovic.mbs.ui.home.ui.customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.DropdownItemBinding;
import com.djordjeratkovic.mbs.model.Employee;

import java.util.List;

public class EmployeeDropdownAdapter extends ArrayAdapter<Employee> {

    private List<Employee> employees;
    private Context context;
    private DropdownItemBinding binding;


    public EmployeeDropdownAdapter(@NonNull Context context, @NonNull List<Employee> objects) {
        super(context, R.layout.dropdown_item, objects);
        this.employees = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Nullable
    @Override
    public Employee getItem(int position) {
        return employees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            binding = DropdownItemBinding.inflate(LayoutInflater.from(context), parent, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (DropdownItemBinding) view.getTag();
        }

        binding.dropDownItem.setText(employees.get(position).toString());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
