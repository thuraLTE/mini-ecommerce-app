package com.example.hw_8.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_8.R;
import com.example.hw_8.model.Product;
import com.example.hw_8.util.Constants;

import java.util.List;

public class AdminSalesRecordDetailsListAdapter extends RecyclerView.Adapter<AdminSalesRecordDetailsListAdapter.SalesRecordDetailsViewHolder> {

    Context context;
    List<Product> productList;

    public AdminSalesRecordDetailsListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public SalesRecordDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesRecordDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.single_invoice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesRecordDetailsViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.tvProductName.setText(currentProduct.getName());
        holder.tvProductId.setText(String.valueOf(currentProduct.getId()));
        holder.tvProductQty.setText(context.getString(R.string.quantity_placeholder, currentProduct.getSoldQty()));
        holder.tvProductSinglePrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(currentProduct.getSellPrice())));

        double productSubtotalPrice = Constants.calculateProductSubtotal(currentProduct.getSoldQty(), currentProduct.getSellPrice());
        holder.tvProductSubtotalPrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(productSubtotalPrice))));

        String discountPercent = Constants.calculateDiscountPercent(currentProduct.getSoldQty());
        double discountedPrice = Constants.calculateProductDiscount(currentProduct.getSoldQty(), String.valueOf(productSubtotalPrice));
        if (discountPercent != null) {
            holder.tvDiscountPercentGained.setVisibility(View.VISIBLE);
            holder.tvDiscountPercentGained.setText(discountPercent);
        } else {
            holder.tvDiscountPercentGained.setVisibility(View.GONE);
        }

        double productFinalSubtotalPrice = productSubtotalPrice - discountedPrice;
        holder.tvProductFinalSubtotalPrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(productFinalSubtotalPrice))));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class SalesRecordDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvProductId, tvProductQty, tvProductSinglePrice, tvProductSubtotalPrice, tvDiscountPercentGained, tvProductFinalSubtotalPrice;

        public SalesRecordDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            tvProductSinglePrice = itemView.findViewById(R.id.tvProductSinglePrice);
            tvProductSubtotalPrice = itemView.findViewById(R.id.tvProductSubtotalPrice);
            tvDiscountPercentGained = itemView.findViewById(R.id.tvDiscountPercentGained);
            tvProductFinalSubtotalPrice = itemView.findViewById(R.id.tvProductFinalSubtotalPrice);
        }
    }

}
