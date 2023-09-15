package com.example.hw_8.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw_8.R;
import com.example.hw_8.adapter.admin.AdminSalesRecordDetailsListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Product;
import com.example.hw_8.model.SalesRecord;
import com.example.hw_8.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdminSalesRecordInvoiceDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AdminSalesRecordInvoiceDetailsActivity";
    AppCompatButton btnDeleteSalesRecord;
    ImageView ivBack;
    TextView tvAdminSalesRecordDetailsScreenMessage, tvSalesRecordSubTotalPrice, tvSalesRecordDiscountPrice, tvSalesRecordTaxPrice, tvSalesRecordTotalPrice;
    RecyclerView rvSalesProductRecordList;
    AdminSalesRecordDetailsListAdapter adminSalesRecordDetailsListAdapter;
    SalesRecord currentSalesRecord;
    ArrayList<Product> salesProductList;
    DatabaseHelper databaseHelper;
    double salesProductsSubTotalPrice = 0.0, discountPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sales_record_invoice_details);

        databaseHelper = new DatabaseHelper(this);
        salesProductList = new ArrayList<>();

        initViews();
//        retrieveCurrentSalesRecord();
//        populateSalesProductRecordList();
        initDeleteSalesRecordEvents();
        initBackEvents();
    }

    private void initViews() {
        btnDeleteSalesRecord = findViewById(R.id.btnDeleteSalesRecord);
        ivBack = findViewById(R.id.ivBack);
        rvSalesProductRecordList = findViewById(R.id.rvSalesProductRecordList);

        tvAdminSalesRecordDetailsScreenMessage = findViewById(R.id.tvAdminSalesRecordDetailScreenMessage);
        tvSalesRecordSubTotalPrice = findViewById(R.id.tvSalesRecordSubTotalPrice);
        tvSalesRecordDiscountPrice = findViewById(R.id.tvSalesRecordDiscountPrice);
        tvSalesRecordTaxPrice = findViewById(R.id.tvSalesRecordTaxPrice);
        tvSalesRecordTotalPrice = findViewById(R.id.tvSalesRecordTotalPrice);
    }

    private void retrieveCurrentSalesRecord() {
        if (getIntent().getSerializableExtra(Constants.CURRENT_SALES_RECORD_KEY) != null) {
            currentSalesRecord = (SalesRecord) getIntent().getSerializableExtra(Constants.CURRENT_SALES_RECORD_KEY);
            Log.d(TAG, "Current Sales Record Id: Staff #" + currentSalesRecord.getId());

            tvAdminSalesRecordDetailsScreenMessage.setText(getString(R.string.admin_invoice_placeholder, currentSalesRecord.getId()));
        }
    }

    private void populateSalesProductRecordList() {
        salesProductList = databaseHelper.readSalesProductsFromCurrentSalesRecord(currentSalesRecord);

        if (!salesProductList.isEmpty()) {
            adminSalesRecordDetailsListAdapter = new AdminSalesRecordDetailsListAdapter(this, salesProductList);
            rvSalesProductRecordList.setAdapter(adminSalesRecordDetailsListAdapter);
            rvSalesProductRecordList.setLayoutManager(new LinearLayoutManager(this));

            showOrderDetailsSummary();
        }
    }

    private void showOrderDetailsSummary() {
        for (int i = 0; i < salesProductList.size(); i++) {
            double productSubTotal = Constants.calculateProductSubtotal(salesProductList.get(i).getSoldQty(), salesProductList.get(i).getSellPrice());
            salesProductsSubTotalPrice += productSubTotal;

            double productDiscountPrice = Constants.calculateProductDiscount(salesProductList.get(i).getSoldQty(), String.valueOf(productSubTotal));
            discountPrice += productDiscountPrice;
        }

        double totalPrice = salesProductsSubTotalPrice - discountPrice;
        double taxPrice = Constants.calculateTax(totalPrice);
        double totalFinalizedPrice = totalPrice + taxPrice;

        tvSalesRecordSubTotalPrice.setText(getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(salesProductsSubTotalPrice))));
        tvSalesRecordDiscountPrice.setText(getString(R.string.discount_price_placeholder, Constants.formatCurrency(String.valueOf(discountPrice))));
        tvSalesRecordTaxPrice.setText(getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(taxPrice))));
        tvSalesRecordTotalPrice.setText(getString(R.string.price_suffix, Constants.formatCurrency(String.valueOf(totalFinalizedPrice))));
    }

    private void initDeleteSalesRecordEvents() {
        btnDeleteSalesRecord.setOnClickListener(view -> {
            if (currentSalesRecord != null) {
                databaseHelper.deleteCurrentSalesRecord(currentSalesRecord.getId());

                Intent intent = new Intent(this, AdminSalesRecordListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initBackEvents() {
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveCurrentSalesRecord();
        populateSalesProductRecordList();
    }
}