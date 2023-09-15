package com.example.hw_8.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Staff;
import com.example.hw_8.ui.admin.AdminAddStaffActivity;
import com.example.hw_8.util.Constants;

import java.util.List;

public class AdminStaffListAdapter extends RecyclerView.Adapter<AdminStaffListAdapter.StaffViewHolder> {

    Context context;
    List<Staff> staffList;
    DatabaseHelper databaseHelper;
    public AdminStaffListAdapter(Context context, List<Staff> staffList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.staffList = staffList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StaffViewHolder(LayoutInflater.from(context).inflate(R.layout.single_staff_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff currentStaff = staffList.get(position);

        holder.tvStaffId.setText(context.getString(R.string.product_id_suffix, currentStaff.getId()));
        holder.tvStaffName.setText(currentStaff.getName());
        holder.tvStaffEmail.setText(currentStaff.getEmail());
        holder.tvStaffNrc.setText(currentStaff.getNrc());
        holder.tvStaffGender.setText(currentStaff.getGender());
        holder.tvStaffPassword.setText(currentStaff.getPassword());

        holder.ivRemoveStaff.setOnClickListener(view -> {
            staffList.remove(position);
            notifyDataSetChanged();

            databaseHelper.deleteCurrentStaff(currentStaff.getId());
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context.getApplicationContext(), AdminAddStaffActivity.class);
            intent.putExtra(Constants.CURRENT_STAFF_KEY, currentStaff);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {

        TextView tvStaffId, tvStaffName, tvStaffEmail, tvStaffNrc, tvStaffGender, tvStaffPassword;
        ImageView ivRemoveStaff;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStaffId = itemView.findViewById(R.id.tvStaffId);
            tvStaffName = itemView.findViewById(R.id.tvStaffName);
            tvStaffEmail = itemView.findViewById(R.id.tvStaffEmail);
            tvStaffNrc = itemView.findViewById(R.id.tvStaffNrc);
            tvStaffGender = itemView.findViewById(R.id.tvStaffGender);
            tvStaffPassword = itemView.findViewById(R.id.tvStaffPassword);

            ivRemoveStaff = itemView.findViewById(R.id.ivRemoveStaff);
        }
    }
}
