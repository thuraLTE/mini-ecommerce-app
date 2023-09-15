package com.example.hw_8.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw_8.R;
import com.example.hw_8.adapter.admin.AdminProductListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Product;
import com.example.hw_8.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminProductListActivity extends AppCompatActivity {

    FloatingActionButton fabAddProduct;
    ImageView ivBack;
    RecyclerView rvProductList;
    TextView tvEmptyProductList;
    AdminProductListAdapter adminProductListAdapter;
    ArrayList<Product> productList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);

        initViews();
        retrieveProducts();
        initEvents();
        populateProductList();
    }

    private void initViews() {
        fabAddProduct = findViewById(R.id.fabAddProduct);
        rvProductList = findViewById(R.id.rvProductList);

        tvEmptyProductList = findViewById(R.id.tvEmptyProductList);
        ivBack = findViewById(R.id.ivBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveProducts();
        populateProductList();
    }

    private void retrieveProducts() {
        productList.clear();
        databaseHelper = new DatabaseHelper(this);
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
                productList.add(product);
            }
        }
        cursor.close();
    }

    private Boolean checkCartedOrUncarted(int isAddedToCart) {
        if (isAddedToCart == Constants.PRODUCT_UNCARTED)
            return false;
        else return true;
    }

    private void initEvents() {
        fabAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminAddProductActivity.class);
            startActivity(intent);
            finish();
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void populateProductList() {
        if (productList.isEmpty()) {
            rvProductList.setVisibility(View.GONE);
            tvEmptyProductList.setVisibility(View.VISIBLE);
        } else {
            rvProductList.setVisibility(View.VISIBLE);
            tvEmptyProductList.setVisibility(View.INVISIBLE);

            adminProductListAdapter = new AdminProductListAdapter(this, productList, databaseHelper);
            rvProductList.setAdapter(adminProductListAdapter);
            rvProductList.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}