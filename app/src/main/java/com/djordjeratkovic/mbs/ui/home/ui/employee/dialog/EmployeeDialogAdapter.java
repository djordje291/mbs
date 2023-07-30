package com.djordjeratkovic.mbs.ui.home.ui.employee.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.DialogEmployeeBinding;
import com.djordjeratkovic.mbs.databinding.WarehouseDialogBinding;
import com.djordjeratkovic.mbs.model.Warehouse;

import java.util.List;

public class EmployeeDialogAdapter extends RecyclerView.Adapter<EmployeeDialogAdapter.ViewHolder> {

    private List<Warehouse> warehouses;
    private Context context;

    public EmployeeDialogAdapter(List<Warehouse> warehouses, Context context) {
        this.warehouses = warehouses;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WarehouseDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.warehouse_dialog, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeDialogAdapter.ViewHolder holder, int position) {
        if (warehouses.size() == 1) {
            holder.binding.delete.setVisibility(View.GONE);
        }
        Warehouse warehouse = warehouses.get(position);
        holder.bind(warehouse);
        holder.binding.warehouseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && !TextUtils.isEmpty(charSequence)) {
                    warehouse.setName(charSequence.toString());
                    holder.binding.warehouse.setError(null);
                } else {
                    holder.binding.warehouse.setError(context.getString(R.string.ne_moze_biti_prazno));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warehouses.remove(warehouse);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return warehouses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        WarehouseDialogBinding binding;

        public ViewHolder(WarehouseDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Warehouse warehouse) {
            binding.setWarehouse(warehouse);
        }
    }
}
