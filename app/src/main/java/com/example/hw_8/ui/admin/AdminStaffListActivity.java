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
import android.widget.Toast;

import com.example.hw_8.R;
import com.example.hw_8.adapter.admin.AdminStaffListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Staff;
import com.example.hw_8.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminStaffListActivity extends AppCompatActivity {

    RecyclerView rvStaffList;
    TextView tvEmptyStaffList;
    AdminStaffListAdapter staffListAdapter;
    FloatingActionButton fabAddStaff;
    ImageView ivBack;
    DatabaseHelper databaseHelper;
    ArrayList<Staff> staffList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff_list);

        initViews();
        retrieveStaff();
        initEvents();
        populateStaffList();
    }

    private void initViews() {
        rvStaffList = findViewById(R.id.rvStaffList);
        tvEmptyStaffList = findViewById(R.id.tvEmptyStaffList);
        fabAddStaff = findViewById(R.id.fabAddStaff);
        ivBack = findViewById(R.id.ivBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveStaff();
        populateStaffList();
    }

    private void retrieveStaff() {
        staffList.clear();
        databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.readAllStaff();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Staff staff = new Staff(
                        cursor.getInt(Constants.STAFF_ID_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_NAME_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_EMAIL_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_NRC_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_GENDER_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_PASSWORD_COLUMN_POSITION));
                staffList.add(staff);
            }
        }
        cursor.close();
    }

    private void initEvents() {
        fabAddStaff.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminAddStaffActivity.class);
            startActivity(intent);
            finish();
        });
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void populateStaffList() {
        if (staffList.isEmpty()) {
            rvStaffList.setVisibility(View.GONE);
            tvEmptyStaffList.setVisibility(View.VISIBLE);
        } else {
            rvStaffList.setVisibility(View.VISIBLE);
            tvEmptyStaffList.setVisibility(View.INVISIBLE);

            staffListAdapter = new AdminStaffListAdapter(this, staffList, databaseHelper);
            rvStaffList.setAdapter(staffListAdapter);
            rvStaffList.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}