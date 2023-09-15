package com.example.hw_8.ui.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.database.Cursor;
import android.os.Bundle;

import com.example.hw_8.R;
import com.example.hw_8.adapter.staff.StaffProductListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.LoginCredentials;
import com.example.hw_8.model.Product;
import com.example.hw_8.util.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StaffDashboardActivity extends AppCompatActivity {
    BottomNavigationView btmNavView;
    LoginCredentials currentLoginCredentials;
    Bundle currentStaffCredentialsBundle;
    DatabaseHelper databaseHelper;
    ArrayList<Product> cartProductList = new ArrayList<>();
    BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        retrieveCurrentStaffDetails();
        showDefaultFragment();
        initViews();
        configureBottomNavigation();
    }

    private void showDefaultFragment() {
        currentStaffCredentialsBundle = new Bundle();
        currentStaffCredentialsBundle.putSerializable(Constants.CURRENT_LOGIN_CREDENTIALS_KEY, currentLoginCredentials);
        // Set up default fragment to be shown
        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, currentStaffCredentialsBundle).commit();
    }

    private void initViews() {
        btmNavView = findViewById(R.id.btmNavView);
    }

    private void retrieveCurrentStaffDetails() {
        currentLoginCredentials = (LoginCredentials) getIntent().getSerializableExtra(Constants.CURRENT_LOGIN_CREDENTIALS_KEY);
    }

    private void checkCartedProductList() {
        cartProductList.clear();
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
                if (product.getAddToCart())
                    cartProductList.add(product);
            }
        }
        cursor.close();

        badgeDrawable = btmNavView.getOrCreateBadge(R.id.cartFragment);

        if (!cartProductList.isEmpty()) {
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(cartProductList.size());
        } else {
            badgeDrawable.setVisible(false);
        }
    }

    private Boolean checkCartedOrUncarted(int isAddedToCart) {
        if (isAddedToCart == Constants.PRODUCT_UNCARTED)
            return false;
        else return true;
    }

    private void configureBottomNavigation() {
        checkCartedProductList();

        btmNavView.setSelectedItemId(R.id.productFragment);

        btmNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.productFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffProductListFragment.class, currentStaffCredentialsBundle).commit();
            } else if (item.getItemId() == R.id.cartFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffCartFragment.class, currentStaffCredentialsBundle).commit();
            } else if (item.getItemId() == R.id.settingFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffSettingFragment.class, currentStaffCredentialsBundle).commit();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // ToDo: Handle badge drawable data
    }
}