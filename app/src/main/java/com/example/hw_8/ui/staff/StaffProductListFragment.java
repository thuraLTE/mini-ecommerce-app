package com.example.hw_8.ui.staff;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw_8.R;
import com.example.hw_8.adapter.staff.StaffProductListAdapter;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.LoginCredentials;
import com.example.hw_8.model.Product;
import com.example.hw_8.ui.LoginActivity;
import com.example.hw_8.util.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StaffProductListFragment extends Fragment implements StaffProductListAdapter.OnItemClicked, StaffProductListAdapter.OnCartItemClicked {

    public static final String TAG = "StaffProductListFragment";
    TextView tvWelcomeBack, tvEmptyProductList;
    ImageView ivLogout;
    RecyclerView rvProductList;
    StaffProductListAdapter staffProductListAdapter;
    FloatingActionButton fabAddProduct;
    DatabaseHelper databaseHelper;
    ArrayList<Product> productList = new ArrayList<>();
    LoginCredentials currentStaffCredentials;
    ArrayList<Product> cartProductList = new ArrayList<>();
    BadgeDrawable badgeDrawable;

    public StaffProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        retrieveCurrentStaffCredentials();
        retrieveProducts();
        initEvents();
        populateProductList();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveProducts();
        populateProductList();
    }

    private void initViews(View view) {
        tvWelcomeBack = view.findViewById(R.id.tvStaffCartScreenMessage);
        tvEmptyProductList = view.findViewById(R.id.tvEmptyProductList);
        rvProductList = view.findViewById(R.id.rvProductList);
        fabAddProduct = view.findViewById(R.id.fabAddProduct);
        ivLogout = view.findViewById(R.id.ivLogout);
    }

    private void retrieveCurrentStaffCredentials() {
        if (getArguments() != null) {
            currentStaffCredentials = (LoginCredentials) getArguments().getSerializable(Constants.CURRENT_LOGIN_CREDENTIALS_KEY);

            tvWelcomeBack.setText(getString(R.string.welcome_current_staff, currentStaffCredentials.getName()));
        }
    }

    private void retrieveProducts() {
        productList.clear();
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
                productList.add(product);

                if (product.getAddToCart())
                    cartProductList.add(product);
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
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffAddProductFragment.class, null).commit();
        });

        ivLogout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void populateProductList() {
        if (productList.isEmpty()) {
            rvProductList.setVisibility(View.GONE);
            tvEmptyProductList.setVisibility(View.VISIBLE);
        } else {
            rvProductList.setVisibility(View.VISIBLE);
            tvEmptyProductList.setVisibility(View.INVISIBLE);

            staffProductListAdapter = new StaffProductListAdapter(requireContext(), productList, this, this, databaseHelper);
            rvProductList.setAdapter(staffProductListAdapter);
            rvProductList.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }

    @Override
    public void onItemClicked(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CURRENT_PRODUCT_KEY, product);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, StaffAddProductFragment.class, bundle).commit();
    }

    @Override
    public void onCartItemClicked(Product product) {
        if (getActivity() != null) {
            BottomNavigationView btmNavView = getActivity().findViewById(R.id.btmNavView);
            badgeDrawable = btmNavView.getOrCreateBadge(R.id.cartFragment);

            if (product.getAddToCart()) {
                cartProductList.add(product);
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(cartProductList.size());

            } else {
                cartProductList.remove(product);
                if (cartProductList.isEmpty())
                    badgeDrawable.setVisible(false);
                else {
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(cartProductList.size());
                }
            }
        }
    }
}