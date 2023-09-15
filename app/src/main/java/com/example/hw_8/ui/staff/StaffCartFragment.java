package com.example.hw_8.ui.staff;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hw_8.R;
import com.example.hw_8.adapter.staff.StaffCartListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.LoginCredentials;
import com.example.hw_8.model.Product;
import com.example.hw_8.ui.LoginActivity;
import com.example.hw_8.util.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StaffCartFragment extends Fragment implements StaffCartListAdapter.OnRemoveFromCartClicked, StaffCartListAdapter.OnSoldQtyChanged {

    private static final String TAG = "StaffCartFragment";
    AppCompatButton btnAddToSalesRecord;
    ImageView ivLogout;
    TextView tvEmptyCartList, tvSalesRecordSubTotalPrice, tvSalesRecordDiscountPrice, tvSalesRecordTaxPrice, tvSalesRecordTotalPrice;
    LinearLayout linearCartSummary;
    RecyclerView rvCartList;
    StaffCartListAdapter staffCartListAdapter;
    ArrayList<Product> cartProductList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    BadgeDrawable badgeDrawable;
    LoginCredentials currentStaffLoginCredentials;

    public StaffCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        retrieveCartItemList();
        populateCartItemList();
        initLogoutEvents();
    }

    private void initViews(View view) {
        btnAddToSalesRecord = view.findViewById(R.id.btnAddToSalesRecord);
        ivLogout = view.findViewById(R.id.ivLogout);

        tvEmptyCartList = view.findViewById(R.id.tvEmptyCartList);
        linearCartSummary = view.findViewById(R.id.linearCartSummary);
        rvCartList = view.findViewById(R.id.rvCartList);

        tvSalesRecordSubTotalPrice = view.findViewById(R.id.tvSalesRecordSubTotalPrice);
        tvSalesRecordDiscountPrice = view.findViewById(R.id.tvSalesRecordDiscountPrice);
        tvSalesRecordTaxPrice = view.findViewById(R.id.tvSalesRecordTaxPrice);
        tvSalesRecordTotalPrice = view.findViewById(R.id.tvSalesRecordTotalPrice);
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveCartItemList();
        populateCartItemList();
    }

    private void retrieveCartItemList() {
        cartProductList.clear();
        databaseHelper = new DatabaseHelper(requireContext());
        Cursor cursor = databaseHelper.readAllProducts();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Product product = new Product(
                        cursor.getInt(Constants.PRODUCT_ID_COLUMN_POSITION),
                        cursor.getString(Constants.PRODUCT_NAME_COLUMN_POSITION),
                        cursor.getInt(Constants.PRODUCT_TOTAL_QUANTITY_COLUMN_POSITION),
                        cursor.getInt(Constants.PRODUCT_REMAINING_QUANTITY_COLUMN_POSITION),
                        cursor.getString(Constants.PRODUCT_BUY_PRICE_COLUMN_POSITION),
                        cursor.getString(Constants.PRODUCT_SELL_PRICE_COLUMN_POSITION),
                        cursor.getInt(Constants.PRODUCT_SOLD_QUANTITY_COLUMN_POSITION),
                        checkCartedOrUncarted(cursor.getInt(Constants.PRODUCT_IS_ADDED_TO_CART_COLUMN_POSITION)));
                if (product.getAddToCart()) {
                    cartProductList.add(product);
                    Log.d(TAG, "CartedProductTypeCount: " + cartProductList.size());
                }
            }
        }
        cursor.close();
    }

    private Boolean checkCartedOrUncarted(int isAddedToCart) {
        if (isAddedToCart == Constants.PRODUCT_UNCARTED)
            return false;
        else return true;
    }

    private void populateCartItemList() {
        if (cartProductList.isEmpty()) {
            linearCartSummary.setVisibility(View.GONE);
            tvEmptyCartList.setVisibility(View.VISIBLE);
            btnAddToSalesRecord.setVisibility(View.GONE);

        } else {
            linearCartSummary.setVisibility(View.VISIBLE);
            tvEmptyCartList.setVisibility(View.INVISIBLE);
            btnAddToSalesRecord.setVisibility(View.VISIBLE);

            staffCartListAdapter = new StaffCartListAdapter(requireContext(), cartProductList, this, this, databaseHelper);
            rvCartList.setAdapter(staffCartListAdapter);
            rvCartList.setLayoutManager(new LinearLayoutManager(requireContext()));

            showOrderSummaryDetails();
        }
    }

    private void showOrderSummaryDetails() {
        Double subTotalPrice = 0.0, discountedPrice = 0.0;

        for (int i = 0; i < cartProductList.size(); i++) {
            int productSoldQuantity = cartProductList.get(i).getSoldQty();
            String productSubTotalPrice = cartProductList.get(i).getSellPrice();
            Log.d(TAG, "ProductSoldQty: " + productSoldQuantity);
            Log.d(TAG, "ProductSubTotalPrice: " + productSubTotalPrice);

            double productSellPricePerItem = Double.parseDouble(productSubTotalPrice);
            double productSubTotalPriceDouble = (double) productSoldQuantity * productSellPricePerItem;
            subTotalPrice += productSubTotalPriceDouble;

            double productDiscountedPrice = Constants.calculateProductDiscount(productSoldQuantity, String.valueOf(productSubTotalPriceDouble));
            discountedPrice += productDiscountedPrice;
        }

        double taxPrice = Constants.calculateTax(subTotalPrice - discountedPrice);
        double totalPrice = (subTotalPrice - discountedPrice) + taxPrice;

        tvSalesRecordSubTotalPrice.setText(getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(subTotalPrice))));
        tvSalesRecordDiscountPrice.setText(getString(R.string.discount_price_placeholder, Constants.formatCurrency(String.valueOf(discountedPrice))));
        tvSalesRecordTaxPrice.setText(getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(taxPrice))));
        tvSalesRecordTotalPrice.setText(getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(totalPrice))));

        initAddToSalesRecordEvents(subTotalPrice, discountedPrice, taxPrice, totalPrice);
    }

    private void initAddToSalesRecordEvents(double subTotalPrice, double discountedPrice, double taxPrice, double totalPrice) {
        btnAddToSalesRecord.setOnClickListener(view -> {
            ArrayList<Integer> productIdList = new ArrayList<>();
            int totalSoldQty = 0;

            if (!cartProductList.isEmpty()) {
                for (int i = 0; i < cartProductList.size(); i++) {
                    int cartedProductId = cartProductList.get(i).getId();
                    productIdList.add(cartedProductId);

                    int cartedProductSoldQty = cartProductList.get(i).getSoldQty();
                    int productRemainingQty = cartProductList.get(i).getRemainingQuantity() - cartedProductSoldQty;

                    databaseHelper.updateCurrentProductRemainingQty(cartedProductId, productRemainingQty);
                    databaseHelper.updateCurrentProductCartOption(cartedProductId, Constants.PRODUCT_UNCARTED);

                    totalSoldQty += cartProductList.get(i).getSoldQty();
                }
            }

            if (getArguments() != null) {
                currentStaffLoginCredentials = (LoginCredentials) getArguments().getSerializable(Constants.CURRENT_LOGIN_CREDENTIALS_KEY);

                Gson gson = new Gson();
                String productIdListAsString = Constants.toJsonString(gson, productIdList);

                databaseHelper.insertNewSalesRecord(
                        productIdListAsString,
                        currentStaffLoginCredentials.getId(),
                        totalSoldQty,
                        String.valueOf(subTotalPrice),
                        String.valueOf(discountedPrice),
                        String.valueOf(taxPrice),
                        String.valueOf(totalPrice));

                cartProductList.clear();
                updateCartBadge();
            }
        });
    }

    private void updateCartBadge() {
        BottomNavigationView btmNavView = getActivity().findViewById(R.id.btmNavView);
        badgeDrawable = btmNavView.getOrCreateBadge(R.id.cartFragment);

        badgeDrawable.setVisible(false);
        tvEmptyCartList.setVisibility(View.VISIBLE);
        rvCartList.setVisibility(View.GONE);
        linearCartSummary.setVisibility(View.GONE);
        btnAddToSalesRecord.setVisibility(View.GONE);

        Bundle currentStaffCredentialsBundle = new Bundle();
        currentStaffCredentialsBundle.putSerializable(Constants.CURRENT_LOGIN_CREDENTIALS_KEY, currentStaffLoginCredentials);

        btmNavView.setSelectedItemId(R.id.productFragment);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, currentStaffCredentialsBundle).commit();
    }

    @Override
    public void onRemoveFromCartClicked(Product cartedProduct) {
        if (getActivity() != null) {
            BottomNavigationView btmNavView = getActivity().findViewById(R.id.btmNavView);

            cartProductList.remove(cartedProduct);

            badgeDrawable = btmNavView.getOrCreateBadge(R.id.cartFragment);

            if (cartProductList.isEmpty()) {
                badgeDrawable.setVisible(false);
                tvEmptyCartList.setVisibility(View.VISIBLE);
                rvCartList.setVisibility(View.GONE);
                linearCartSummary.setVisibility(View.GONE);
                btnAddToSalesRecord.setVisibility(View.GONE);
            } else {
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(cartProductList.size());

                showOrderSummaryDetails();
            }

            databaseHelper.updateCurrentProductSoldQtyAndCartedOption(cartedProduct.getId(), Constants.PRODUCT_DEFAULT_SOLD_QTY, Constants.convertCartOptionBooleanToInteger(cartedProduct.getAddToCart()));
        }
    }

    private void initLogoutEvents() {
        ivLogout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onSoldQtyChanged() {
        retrieveCartItemList();
        showOrderSummaryDetails();
    }
}