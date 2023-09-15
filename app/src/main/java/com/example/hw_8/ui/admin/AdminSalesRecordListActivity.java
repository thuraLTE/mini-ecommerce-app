package com.example.hw_8.ui.admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw_8.R;
import com.example.hw_8.adapter.admin.AdminSalesRecordListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.SalesRecord;
import com.example.hw_8.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdminSalesRecordListActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvSalesRecordList;
    AdminSalesRecordListAdapter adminSalesRecordListAdapter;
    TextView tvEmptySalesRecordList;
    DatabaseHelper databaseHelper;
    ArrayList<SalesRecord> salesRecordList = new ArrayList<>();
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sales_record_list);

        initViews();
        retrieveSalesRecordList();
        initEvents();
        populateSalesRecordList();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        rvSalesRecordList = findViewById(R.id.rvSalesRecordList);
        tvEmptySalesRecordList = findViewById(R.id.tvEmptySalesRecordList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveSalesRecordList();
        populateSalesRecordList();
    }

    private void retrieveSalesRecordList() {
        salesRecordList.clear();
        gson = new Gson();
        databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.readAllSalesRecords();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ArrayList<Integer> productIdListFromDB = Constants.fromJsonString(gson, cursor.getString(Constants.SALES_RECORD_PRODUCT_ID_COLUMN_POSITION));

                SalesRecord salesRecord = new SalesRecord(
                        cursor.getInt(Constants.SALES_RECORD_ID_COLUMN_POSITION),
                        productIdListFromDB,
                        cursor.getInt(Constants.SALES_RECORD_STAFF_ID_COLUMN_POSITION),
                        cursor.getInt(Constants.SALES_RECORD_PRODUCT_SOLD_QUANTITY_COLUMN_POSITION),
                        cursor.getString(Constants.SALES_RECORD_PRODUCT_SUBTOTAL_PRICE_COLUMN_POSITION),
                        cursor.getString(Constants.SALES_RECORD_DISCOUNT_COLUMN_POSITION),
                        cursor.getString(Constants.SALES_RECORD_TAX_COLUMN_POSITION),
                        cursor.getString(Constants.SALES_RECORD_SALES_TOTAL_PRICE_COLUMN_POSITION));
                salesRecordList.add(salesRecord);
            }
        }
        cursor.close();
    }

    private void initEvents() {
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void populateSalesRecordList() {
        if (salesRecordList.isEmpty()) {
            rvSalesRecordList.setVisibility(View.GONE);
            tvEmptySalesRecordList.setVisibility(View.VISIBLE);
        } else {
            rvSalesRecordList.setVisibility(View.VISIBLE);
            tvEmptySalesRecordList.setVisibility(View.GONE);

            adminSalesRecordListAdapter = new AdminSalesRecordListAdapter(this, salesRecordList);
            rvSalesRecordList.setAdapter(adminSalesRecordListAdapter);
            rvSalesRecordList.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}