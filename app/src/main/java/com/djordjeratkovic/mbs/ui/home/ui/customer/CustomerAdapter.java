package com.djordjeratkovic.mbs.ui.home.ui.customer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.CustomerCardBinding;
import com.djordjeratkovic.mbs.databinding.DialogCustomerBinding;
import com.djordjeratkovic.mbs.model.Customer;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.ui.home.ui.customer.dialog.CustomerDialog;
import com.djordjeratkovic.mbs.ui.home.ui.employee.EmployeeAdapter;
import com.djordjeratkovic.mbs.ui.login_register.LoginActivity;
import com.djordjeratkovic.mbs.util.ItemTouchHelperDelete;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements ItemTouchHelperDelete {

    private List<Customer> customers;
    private Context context;
    private CustomerViewModel viewModel;
    private List<Employee> employees;

    public CustomerAdapter(List<Customer> customers, Context context, CustomerViewModel viewModel, List<Employee> employees) {
        this.customers = customers;
        this.context = context;
        this.viewModel = viewModel;
        this.employees = employees;
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomerCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()
        ), R.layout.customer_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        Customer customer = customers.get(position);
        holder.bind(customer, getEmployee(customer));
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
        return customers.size();
    }

    @Override
    public void onItemDelete(int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setMessage(context.getResources().getString(R.string.delete_question))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.deleteCustomer(customers.get(position));
                        notifyItemRemoved(position);
                    }
                })
                .setNegativeButton(R.string.otkazi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyItemChanged(position);
                    }
                });
        builder.show();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.employee_id), text);
        clipboard.setPrimaryClip(clip);
    }

    private String getEmployee(Customer customer) {
        for (Employee employee : employees) {
            if (employee.getDocRef().equals(customer.getEmployeeDocRef())) {
                return employee.getFirstName() + " " + employee.getLastName();
            }
        }
        return context.getString(R.string.error);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomerCardBinding binding;

        public ViewHolder(CustomerCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Customer customer, String employee) {
            binding.setCustomer(customer);
            binding.setEmployee(employee);
        }
    }
}
