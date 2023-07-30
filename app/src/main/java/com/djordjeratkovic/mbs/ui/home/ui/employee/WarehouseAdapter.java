package com.djordjeratkovic.mbs.ui.home.ui.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.WarehouseCardBinding;
import com.djordjeratkovic.mbs.databinding.WarehouseCardBindingImpl;
import com.djordjeratkovic.mbs.databinding.WarehouseDialogBinding;
import com.djordjeratkovic.mbs.model.Employee;
import com.djordjeratkovic.mbs.model.Warehouse;

import java.util.List;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.ViewHolder>{

    private List<Warehouse> warehouses;
    private Context context;

    public WarehouseAdapter(List<Warehouse> warehouses, Context context) {
        this.warehouses = warehouses;
        this.context = context;
    }

    @NonNull
    @Override
    public WarehouseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WarehouseCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.warehouse_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WarehouseAdapter.ViewHolder holder, int position) {
        Warehouse warehouse = warehouses.get(position);
        holder.bind(warehouse);
    }

    @Override
    public int getItemCount() {
        return warehouses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WarehouseCardBinding binding;

        public ViewHolder(WarehouseCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Warehouse warehouse) {
            binding.setWarehouse(warehouse);
        }
    }
}
