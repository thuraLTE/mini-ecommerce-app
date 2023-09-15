package com.example.hw_8.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_8.R;
import com.example.hw_8.model.SalesRecord;
import com.example.hw_8.ui.LoginActivity;
import com.example.hw_8.ui.admin.AdminSalesRecordInvoiceDetailsActivity;
import com.example.hw_8.util.Constants;

import java.util.List;

public class AdminSalesRecordListAdapter extends RecyclerView.Adapter<AdminSalesRecordListAdapter.SalesRecordViewHolder> {

    Context context;
    List<SalesRecord> salesRecordList;

    public AdminSalesRecordListAdapter(Context context, List<SalesRecord> salesRecordList) {
        this.context = context;
        this.salesRecordList = salesRecordList;
    }

    @NonNull
    @Override
    public SalesRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.single_sales_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesRecordViewHolder holder, int position) {
        SalesRecord salesRecord = salesRecordList.get(position);

        holder.tvSalesRecordId.setText(context.getString(R.string.product_id_suffix, salesRecord.getId()));
        holder.tvApprovedBy.setText(context.getString(R.string.approved_by_placeholder, salesRecord.getStaffId()));
        holder.tvProductTypeQty.setText(context.getString(R.string.product_type_qty_placeholder, salesRecord.getProductSoldQty()));
        holder.tvTotalPrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(salesRecord.getSalesTotalPrice())));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context.getApplicationContext(), AdminSalesRecordInvoiceDetailsActivity.class);
            intent.putExtra(Constants.CURRENT_SALES_RECORD_KEY, salesRecord);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return salesRecordList.size();
    }

    static class SalesRecordViewHolder extends RecyclerView.ViewHolder {

        TextView tvSalesRecordId, tvApprovedBy, tvProductTypeQty, tvTotalPrice;

        public SalesRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSalesRecordId = itemView.findViewById(R.id.tvSalesRecordId);
            tvApprovedBy = itemView.findViewById(R.id.tvApprovedBy);
            tvProductTypeQty = itemView.findViewById(R.id.tvProductTypeQty);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
