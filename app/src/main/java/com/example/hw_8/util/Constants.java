package com.example.hw_8.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "admin12345";
    public static final String CURRENT_LOGIN_CREDENTIALS_KEY = "current_login_credentials_key";
    public static final String CURRENT_PRODUCT_KEY = "current_product_key";
    public static final String CURRENT_STAFF_KEY = "current_staff_key";
    public static final String CURRENT_SALES_RECORD_KEY = "current_sales_record_key";

    public static final int PRODUCT_UNCARTED = 0;
    public static final int PRODUCT_CARTED = 1;
    public static final int PRODUCT_DEFAULT_SOLD_QTY = 0;
    public static final int PRODUCT_ADDED_TO_CART_QTY = 1;

    public static final int STAFF_ID_COLUMN_POSITION = 0;
    public static final int STAFF_NAME_COLUMN_POSITION = 1;
    public static final int STAFF_EMAIL_COLUMN_POSITION = 2;
    public static final int STAFF_NRC_COLUMN_POSITION = 3;
    public static final int STAFF_GENDER_COLUMN_POSITION = 4;
    public static final int STAFF_PASSWORD_COLUMN_POSITION = 5;

    public static final int PRODUCT_ID_COLUMN_POSITION = 0;
    public static final int PRODUCT_NAME_COLUMN_POSITION = 1;
    public static final int PRODUCT_TOTAL_QUANTITY_COLUMN_POSITION = 2;
    public static final int PRODUCT_REMAINING_QUANTITY_COLUMN_POSITION = 3;
    public static final int PRODUCT_BUY_PRICE_COLUMN_POSITION = 4;
    public static final int PRODUCT_SELL_PRICE_COLUMN_POSITION = 5;
    public static final int PRODUCT_SOLD_QUANTITY_COLUMN_POSITION = 6;
    public static final int PRODUCT_IS_ADDED_TO_CART_COLUMN_POSITION = 7;

    public static final int SALES_RECORD_ID_COLUMN_POSITION = 0;
    public static final int SALES_RECORD_PRODUCT_ID_COLUMN_POSITION = 1;
    public static final int SALES_RECORD_STAFF_ID_COLUMN_POSITION = 2;
    public static final int SALES_RECORD_PRODUCT_SOLD_QUANTITY_COLUMN_POSITION = 3;
    public static final int SALES_RECORD_PRODUCT_SUBTOTAL_PRICE_COLUMN_POSITION = 4;
    public static final int SALES_RECORD_DISCOUNT_COLUMN_POSITION = 5;
    public static final int SALES_RECORD_TAX_COLUMN_POSITION = 6;
    public static final int SALES_RECORD_SALES_TOTAL_PRICE_COLUMN_POSITION = 7;

    public static String formatCurrency(String price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        Double priceDouble = Double.parseDouble(price);
        return formatter.format(priceDouble);
    }

    public static int convertCartOptionBooleanToInteger(Boolean isAddedToCart) {
        if (!isAddedToCart)
            return Constants.PRODUCT_UNCARTED;
        else
            return Constants.PRODUCT_CARTED;
    }

    public static double calculateProductSubtotal(int soldQty, String singlePrice) {
        double singlePriceDouble = Double.parseDouble(singlePrice);

        return ((double) soldQty * singlePriceDouble);
    }

    public static String calculateDiscountPercent(int soldQty) {
        String discountPercent;

        if (soldQty > 5 && soldQty <= 10) {
            discountPercent = "- 5%";
        } else if (soldQty > 10 && soldQty <= 20) {
            discountPercent = "- 7.5%";
        } else if (soldQty > 20) {
            discountPercent = "- 10%";
        } else {
            discountPercent = null;
        }
        return discountPercent;

    }

    public static double calculateProductDiscount(int soldQty, String subTotalPrice) {
        double discount;
        double originalPrice = Double.parseDouble(subTotalPrice);

        if (soldQty > 5 && soldQty <= 10) {
            discount = 0.05;
            return originalPrice * discount;

        } else if (soldQty > 10 && soldQty <= 20) {
            discount = 0.075;
            return originalPrice * discount;

        } else if (soldQty > 20) {
            discount = 0.1;
            return originalPrice * discount;

        } else {
            discount = 0;
            return originalPrice * discount;

        }
    }

    public static double calculateTax(double totalPrice) {
        double taxRate = 0.04;

        return totalPrice * taxRate;
    }

    public static String toJsonString(@NonNull Gson gson, ArrayList<Integer> list) {
        return gson.toJson(list);
    }

    public static ArrayList<Integer> fromJsonString(@NonNull Gson gson, String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<List<Integer>>() {}.getType());
    }
}
