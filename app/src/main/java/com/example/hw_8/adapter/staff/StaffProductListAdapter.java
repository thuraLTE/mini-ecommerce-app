package com.example.hw_8.adapter.staff;

import android.content.Context;
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
import com.example.hw_8.util.Constants;

import java.util.List;

public class StaffProductListAdapter extends RecyclerView.Adapter<StaffProductListAdapter.ProductViewHolder> {

    Context context;
    List<Product> productList;
    OnItemClicked onItemClickedListener;
    OnCartItemClicked onCartItemClickedListener;
    DatabaseHelper databaseHelper;

    public StaffProductListAdapter(Context context, List<Product> productList, OnItemClicked onItemClickedListener, OnCartItemClicked onCartItemClickedListener, DatabaseHelper databaseHelper) {
        this.context = context;
        this.productList = productList;
        this.onItemClickedListener = onItemClickedListener;
        this.onCartItemClickedListener = onCartItemClickedListener;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.single_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.tvProductId.setText(context.getString(R.string.product_id_suffix, currentProduct.getId()));
        holder.tvProductName.setText(currentProduct.getName());
        holder.tvProductTotalQuantity.setText(String.valueOf(currentProduct.getTotalQuantity()));
        holder.tvProductRemainingQuantity.setText(String.valueOf(currentProduct.getRemainingQuantity()));
        holder.tvProductBuyPrice.setText(context.getString(R.string.bought_price, Constants.formatCurrency(currentProduct.getBuyPrice())));
        holder.tvProductSellPrice.setText(context.getString(R.string.sold_price, Constants.formatCurrency(currentProduct.getSellPrice())));

        if (currentProduct.getRemainingQuantity() == 0) {
            holder.ivCartOption.setVisibility(View.GONE);
            holder.ivCartOption.setEnabled(false);
        } else {
            if (!currentProduct.getAddToCart()) {
                holder.ivCartOption.setImageResource(R.drawable.ic_cart);
            } else {
                holder.ivCartOption.setImageResource(R.drawable.ic_remove_from_cart);
            }

            holder.ivCartOption.setOnClickListener(view -> {
                if (!currentProduct.getAddToCart()) {
                    holder.ivCartOption.setImageResource(R.drawable.ic_remove_from_cart);
                    currentProduct.setAddToCart(true);
                    currentProduct.setSoldQty(Constants.PRODUCT_CARTED);

                    databaseHelper.updateCurrentProductSoldQtyAndCartedOption(currentProduct.getId(), Constants.PRODUCT_ADDED_TO_CART_QTY, Constants.convertCartOptionBooleanToInteger(currentProduct.getAddToCart()));
                } else {
                    holder.ivCartOption.setImageResource(R.drawable.ic_cart);
                    currentProduct.setAddToCart(false);
                    currentProduct.setSoldQty(Constants.PRODUCT_UNCARTED);

                    databaseHelper.updateCurrentProductSoldQtyAndCartedOption(currentProduct.getId(), Constants.PRODUCT_DEFAULT_SOLD_QTY, Constants.convertCartOptionBooleanToInteger(currentProduct.getAddToCart()));
                }
                onCartItemClickedListener.onCartItemClicked(currentProduct);
            });
        }

        holder.itemView.setOnClickListener(view -> {
            onItemClickedListener.onItemClicked(productList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductId, tvProductName, tvProductRemainingQuantity, tvProductTotalQuantity, tvProductBuyPrice, tvProductSellPrice;
        ImageView ivCartOption;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductRemainingQuantity = itemView.findViewById(R.id.tvProductRemainingQuantity);
            tvProductTotalQuantity = itemView.findViewById(R.id.tvProductTotalQuantity);
            tvProductBuyPrice = itemView.findViewById(R.id.tvProductBuyPrice);
            tvProductSellPrice = itemView.findViewById(R.id.tvProductSellPrice);

            ivCartOption = itemView.findViewById(R.id.ivCartOption);
        }
    }

    public interface OnItemClicked {
        public default void onItemClicked(Product product) {
        }
    }

    public interface OnCartItemClicked {
        public default void onCartItemClicked(Product product) {
        }
    }
}
