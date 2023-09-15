package com.example.hw_8.ui.staff;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Product;
import com.example.hw_8.util.Constants;
import com.google.android.material.textfield.TextInputLayout;

public class StaffAddProductFragment extends Fragment {

    AppCompatButton btnAddProduct;
    ImageView ivBack;
    TextInputLayout txtFieldName, txtFieldTotalQty, txtFieldRemainingQty, txtFieldBoughtPrice, txtFieldSoldPrice;
    DatabaseHelper databaseHelper;
    Product currentProduct;

    public StaffAddProductFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        if (getArguments() != null)
            initUpdateProductEvents();
        else
            initAddProductEvents();
    }

    private void initViews(View view) {
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        ivBack = view.findViewById(R.id.ivBack);

        txtFieldName = view.findViewById(R.id.txtFieldName);
        txtFieldTotalQty = view.findViewById(R.id.txtFieldTotalQty);
        txtFieldRemainingQty = view.findViewById(R.id.txtFieldRemainingQty);
        txtFieldBoughtPrice = view.findViewById(R.id.txtFieldBoughtPrice);
        txtFieldSoldPrice = view.findViewById(R.id.txtFieldSoldPrice);
    }

    private void initUpdateProductEvents() {
        btnAddProduct.setText(R.string.update);
        currentProduct = (Product) getArguments().getSerializable(Constants.CURRENT_PRODUCT_KEY);

        txtFieldName.getEditText().setText(currentProduct.getName());
        txtFieldTotalQty.getEditText().setText(String.valueOf(currentProduct.getTotalQuantity()));
        txtFieldRemainingQty.getEditText().setText(String.valueOf(currentProduct.getRemainingQuantity()));
        txtFieldBoughtPrice.getEditText().setText(currentProduct.getBuyPrice());
        txtFieldSoldPrice.getEditText().setText(currentProduct.getSellPrice());

        btnAddProduct.setOnClickListener(view -> {

            String name = txtFieldName.getEditText().getText().toString().trim();
            int totalQty = Integer.parseInt(txtFieldTotalQty.getEditText().getText().toString());
            int remainingQty = Integer.parseInt(txtFieldRemainingQty.getEditText().getText().toString());
            String boughtPrice = txtFieldBoughtPrice.getEditText().getText().toString().trim();
            String soldPrice = txtFieldSoldPrice.getEditText().getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                txtFieldTotalQty.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);

                txtFieldName.setErrorEnabled(true);
                txtFieldName.setError("Empty Name!");

            } else if (txtFieldTotalQty.getEditText().getText() == null) {
                txtFieldName.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);

                txtFieldTotalQty.setErrorEnabled(true);
                txtFieldTotalQty.setError("Empty Total Quantity!");

            } else if (txtFieldRemainingQty.getEditText().getText() == null) {
                txtFieldName.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);

                txtFieldRemainingQty.setErrorEnabled(true);
                txtFieldRemainingQty.setError("Empty Remaining Quantity!");

            } else if (TextUtils.isEmpty(boughtPrice)) {
                txtFieldName.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);

                txtFieldBoughtPrice.setErrorEnabled(true);
                txtFieldBoughtPrice.setError("Empty Bought Price!");

            } else if (TextUtils.isEmpty(soldPrice)) {
                txtFieldName.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);

                txtFieldSoldPrice.setErrorEnabled(true);
                txtFieldSoldPrice.setError("Empty Sold Price!");

            } else if (totalQty < remainingQty) {
                txtFieldName.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);

                txtFieldTotalQty.setErrorEnabled(true);
                txtFieldTotalQty.setError("Total quantity cannot be less than remaining!");

            } else {
                txtFieldName.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);

                Toast.makeText(requireContext(), "Product Updated Successfully", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(requireContext());
                databaseHelper.updateCurrentProduct(currentProduct.getId(), name, totalQty, remainingQty, boughtPrice, soldPrice, Constants.PRODUCT_DEFAULT_SOLD_QTY, Constants.PRODUCT_UNCARTED);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, null).commit();
            }
        });

        ivBack.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, null).commit();
        });
    }

    private void initAddProductEvents() {
        btnAddProduct.setOnClickListener(view -> {

            String name = txtFieldName.getEditText().getText().toString().trim();
            int totalQty = Integer.parseInt(txtFieldTotalQty.getEditText().getText().toString());
            int remainingQty = Integer.parseInt(txtFieldRemainingQty.getEditText().getText().toString());
            String boughtPrice = txtFieldBoughtPrice.getEditText().getText().toString().trim();
            String soldPrice = txtFieldSoldPrice.getEditText().getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                txtFieldTotalQty.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);

                txtFieldName.setErrorEnabled(true);
                txtFieldName.setError("Empty Name!");

            } else if (txtFieldTotalQty.getEditText().getText() == null) {
                txtFieldName.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);

                txtFieldTotalQty.setErrorEnabled(true);
                txtFieldTotalQty.setError("Empty Total Quantity!");

            } else if (txtFieldRemainingQty.getEditText().getText() == null) {
                txtFieldName.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);

                txtFieldRemainingQty.setErrorEnabled(true);
                txtFieldRemainingQty.setError("Empty Remaining Quantity!");

            } else if (TextUtils.isEmpty(boughtPrice)) {
                txtFieldName.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);

                txtFieldBoughtPrice.setErrorEnabled(true);
                txtFieldBoughtPrice.setError("Empty Bought Price!");

            } else if (TextUtils.isEmpty(soldPrice)) {
                txtFieldName.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);

                txtFieldSoldPrice.setErrorEnabled(true);
                txtFieldSoldPrice.setError("Empty Sold Price!");

            } else if (totalQty < remainingQty) {
                txtFieldName.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);

                txtFieldTotalQty.setErrorEnabled(true);
                txtFieldTotalQty.setError("Total quantity cannot be less than remaining!");

            } else {
                txtFieldName.setErrorEnabled(false);
                txtFieldRemainingQty.setErrorEnabled(false);
                txtFieldBoughtPrice.setErrorEnabled(false);
                txtFieldSoldPrice.setErrorEnabled(false);
                txtFieldTotalQty.setErrorEnabled(false);

                Toast.makeText(requireContext(), "Product Added Successfully", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(requireContext());
                databaseHelper.insertNewProduct(name, totalQty, remainingQty, boughtPrice, soldPrice, Constants.PRODUCT_DEFAULT_SOLD_QTY, Constants.PRODUCT_UNCARTED);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, null).commit();
            }
        });

        ivBack.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, null).commit();
        });
    }
}
