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
import com.djordjeratkovic.mbs.model.Employee;

import java.util.List;

public class EmployeeDropdownAdapter extends ArrayAdapter<Employee> {

    private List<Employee> employees;
    private Context context;


    public EmployeeDropdownAdapter(@NonNull Context context, @NonNull List<Employee> objects) {
        super(context, 0, objects);
        this.employees = objects;
        this.context = context;
        for (Employee employee : employees) {
            Log.d("CIRILO", "EmployeeDropdownAdapter: " + employee.getLastName());
        }
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.dropDownItem);

        Employee employee = getItem(position);

        if (employee != null) {
            textView.setText(employee.getFirstName());
        }

        return convertView;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                results.values = employees;
                results.count = employees.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                addAll((List<Employee>) filterResults.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Employee) resultValue).getFirstName() + " " + ((Employee) resultValue).getLastName();
            }
        };
    }
}
