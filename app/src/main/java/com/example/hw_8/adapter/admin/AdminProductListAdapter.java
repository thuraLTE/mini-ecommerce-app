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
import com.example.hw_8.model.Product;
import com.example.hw_8.ui.admin.AdminAddProductActivity;
import com.example.hw_8.util.Constants;

import java.util.List;

public class AdminProductListAdapter extends RecyclerView.Adapter<AdminProductListAdapter.AdminProductViewHolder> {

    Context context;
    List<Product> productList;
    DatabaseHelper databaseHelper;

    public AdminProductListAdapter(Context context, List<Product> productList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.productList = productList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminProductViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_single_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.tvProductId.setText(context.getString(R.string.product_id_suffix, currentProduct.getId()));
        holder.tvProductName.setText(currentProduct.getName());
        holder.tvProductTotalQuantity.setText(String.valueOf(currentProduct.getTotalQuantity()));
        holder.tvProductRemainingQuantity.setText(String.valueOf(currentProduct.getRemainingQuantity()));
        holder.tvProductBuyPrice.setText(context.getString(R.string.bought_price, Constants.formatCurrency(currentProduct.getBuyPrice())));
        holder.tvProductSellPrice.setText(context.getString(R.string.sold_price, Constants.formatCurrency(currentProduct.getSellPrice())));

        holder.ivDeleteProduct.setOnClickListener(view -> {
            productList.remove(position);
            notifyDataSetChanged();

            databaseHelper.deleteCurrentProduct(currentProduct.getId());
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context.getApplicationContext(), AdminAddProductActivity.class);
            intent.putExtra(Constants.CURRENT_PRODUCT_KEY, currentProduct);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class AdminProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductId, tvProductName, tvProductRemainingQuantity, tvProductTotalQuantity, tvProductBuyPrice, tvProductSellPrice;
        ImageView ivDeleteProduct;
        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductRemainingQuantity = itemView.findViewById(R.id.tvProductRemainingQuantity);
            tvProductTotalQuantity = itemView.findViewById(R.id.tvProductTotalQuantity);
            tvProductBuyPrice = itemView.findViewById(R.id.tvProductBuyPrice);
            tvProductSellPrice = itemView.findViewById(R.id.tvProductSellPrice);

            ivDeleteProduct = itemView.findViewById(R.id.ivDeleteProduct);
        }
    }
}
