package com.example.hw_8.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Product;
import com.example.hw_8.util.Constants;
import com.google.android.material.textfield.TextInputLayout;

public class AdminAddProductActivity extends AppCompatActivity {

    AppCompatButton btnAddProduct;
    ImageView ivBack;
    TextInputLayout txtFieldName, txtFieldTotalQty, txtFieldRemainingQty, txtFieldBoughtPrice, txtFieldSoldPrice;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        initViews();
        if (getIntent().getSerializableExtra(Constants.CURRENT_PRODUCT_KEY) != null)
            initUpdateProductEvents();
        else
            initAddProductEvents();
    }
    private void initViews() {
        btnAddProduct = findViewById(R.id.btnAddProduct);

        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldTotalQty = findViewById(R.id.txtFieldTotalQty);
        txtFieldRemainingQty = findViewById(R.id.txtFieldRemainingQty);
        txtFieldBoughtPrice = findViewById(R.id.txtFieldBoughtPrice);
        txtFieldSoldPrice = findViewById(R.id.txtFieldSoldPrice);

        ivBack = findViewById(R.id.ivBack);
    }

    private void initUpdateProductEvents() {
        btnAddProduct.setText(R.string.update);
        Product product = (Product) getIntent().getSerializableExtra(Constants.CURRENT_PRODUCT_KEY);

        txtFieldName.getEditText().setText(product.getName());
        txtFieldTotalQty.getEditText().setText(String.valueOf(product.getTotalQuantity()));
        txtFieldRemainingQty.getEditText().setText(String.valueOf(product.getRemainingQuantity()));
        txtFieldBoughtPrice.getEditText().setText(product.getBuyPrice());
        txtFieldSoldPrice.getEditText().setText(product.getSellPrice());

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

                Toast.makeText(this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(this);
                databaseHelper.updateCurrentProduct(product.getId(), name, totalQty, remainingQty, boughtPrice, soldPrice, Constants.PRODUCT_DEFAULT_SOLD_QTY, Constants.PRODUCT_UNCARTED);

                Intent intent = new Intent(this, AdminProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
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

                Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(this);
                databaseHelper.insertNewProduct(name, totalQty, remainingQty, boughtPrice, soldPrice, Constants.PRODUCT_DEFAULT_SOLD_QTY, Constants.PRODUCT_UNCARTED);

                Intent intent = new Intent(this, AdminProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}