package com.example.hw_8.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hw_8.R;
import com.example.hw_8.ui.LoginActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout linearStaff, linearProducts, linearSales;
    ImageView ivLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initViews();
        initEvents();
    }

    private void initViews() {
        linearStaff = findViewById(R.id.linearStaff);
        linearProducts = findViewById(R.id.linearProducts);
        linearSales = findViewById(R.id.linearSales);

        ivLogout = findViewById(R.id.ivLogout);
    }

    private void initEvents() {
        linearStaff.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminStaffListActivity.class);
            startActivity(intent);
        });

        linearProducts.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminProductListActivity.class);
            startActivity(intent);
        });

        linearSales.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminSalesRecordListActivity.class);
            startActivity(intent);
        });

        ivLogout.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}