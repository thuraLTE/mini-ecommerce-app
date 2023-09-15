package com.example.hw_8.adapter.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Product;
import com.example.hw_8.util.Constants;

import java.util.List;

public class StaffCartListAdapter extends RecyclerView.Adapter<StaffCartListAdapter.CartItemViewHolder> {

    Context context;
    List<Product> productList;
    OnRemoveFromCartClicked onRemoveFromCartClickedListener;
    OnSoldQtyChanged onSoldQtyChangedListener;
    DatabaseHelper databaseHelper;

    public StaffCartListAdapter(Context context, List<Product> productList, OnRemoveFromCartClicked onRemoveFromCartClickedListener, OnSoldQtyChanged onSoldQtyChangedListener,DatabaseHelper databaseHelper) {
        this.context = context;
        this.productList = productList;
        this.onRemoveFromCartClickedListener = onRemoveFromCartClickedListener;
        this.onSoldQtyChangedListener = onSoldQtyChangedListener;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(LayoutInflater.from(context).inflate(R.layout.single_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.ivRemoveFromCart.setOnClickListener(view -> {
            productList.remove(position);
            notifyDataSetChanged();

            currentProduct.setAddToCart(false);
            currentProduct.setSoldQty(Constants.PRODUCT_UNCARTED);

            onRemoveFromCartClickedListener.onRemoveFromCartClicked(currentProduct);
        });
        holder.tvCartProductName.setText(currentProduct.getName());
        holder.tvCartProductId.setText(context.getString(R.string.product_id_suffix, currentProduct.getId()));
        holder.tvQty.setText(String.valueOf(currentProduct.getSoldQty()));
        holder.tvCartProductSubtotalPrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(calculateProductSubtotalPrice(currentProduct.getSoldQty(), currentProduct.getSellPrice()))));

        holder.ivIncrease.setOnClickListener(view -> {
            int cartItemSoldQty = Integer.parseInt(holder.tvQty.getText().toString());
            if (cartItemSoldQty < currentProduct.getRemainingQuantity()) {
                cartItemSoldQty++;

                holder.tvQty.setText(String.valueOf(cartItemSoldQty));
                currentProduct.setSoldQty(cartItemSoldQty);

                holder.tvCartProductSubtotalPrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(calculateProductSubtotalPrice(currentProduct.getSoldQty(), currentProduct.getSellPrice()))));

                databaseHelper.updateCurrentProductSoldQty(currentProduct.getId(), cartItemSoldQty);

                onSoldQtyChangedListener.onSoldQtyChanged();
            } else {
                Toast.makeText(context, "Product quantity limit reached", Toast.LENGTH_SHORT).show();
            }
        });

        holder.ivDecrease.setOnClickListener(view -> {
            int cartItemSoldQty = Integer.parseInt(holder.tvQty.getText().toString());
            if (cartItemSoldQty > 1) {
                cartItemSoldQty--;
                holder.tvQty.setText(String.valueOf(cartItemSoldQty));
                currentProduct.setSoldQty(cartItemSoldQty);

                holder.tvCartProductSubtotalPrice.setText(context.getString(R.string.price_suffix, Constants.formatCurrency(calculateProductSubtotalPrice(currentProduct.getSoldQty(), currentProduct.getSellPrice()))));

                databaseHelper.updateCurrentProductSoldQty(currentProduct.getId(), cartItemSoldQty);

                onSoldQtyChangedListener.onSoldQtyChanged();
            }
            else {
                productList.remove(position);
                notifyDataSetChanged();

                currentProduct.setAddToCart(false);
                currentProduct.setSoldQty(Constants.PRODUCT_UNCARTED);

                onRemoveFromCartClickedListener.onRemoveFromCartClicked(currentProduct);
            }
        });
    }

    private String calculateProductSubtotalPrice(int soldQty, String pricePerItem) {
        Double pricePerItemDouble = Double.parseDouble(pricePerItem);
        Double soldQtyDouble = (double) soldQty;
        return String.valueOf(soldQtyDouble * pricePerItemDouble);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRemoveFromCart, ivIncrease, ivDecrease;
        TextView tvCartProductName, tvCartProductId, tvQty, tvCartProductSubtotalPrice;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRemoveFromCart = itemView.findViewById(R.id.ivRemoveFromCart);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            ivDecrease = itemView.findViewById(R.id.ivDecrease);

            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartProductId = itemView.findViewById(R.id.tvCartProductId);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvCartProductSubtotalPrice = itemView.findViewById(R.id.tvCartProductSubtotalPrice);
        }
    }

    public interface OnRemoveFromCartClicked {
        public default void onRemoveFromCartClicked(Product cartedProduct) {
        }
    }

    public interface OnSoldQtyChanged {
        public default void onSoldQtyChanged() {
        }
    }
}
