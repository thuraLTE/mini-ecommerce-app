package com.example.hw_8.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.hw_8.model.Product;
import com.example.hw_8.model.SalesRecord;
import com.example.hw_8.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    public static final String DATABASE_NAME = "pos.db";
    public static final int DATABASE_VERSION = 1;

    public static final String STAFF_TABLE_NAME = "staff_table";
    public static final String STAFF_ID_COLUMN = "staff_id";
    public static final String STAFF_NAME_COLUMN = "staff_name";
    public static final String STAFF_EMAIL_COLUMN = "staff_email";
    public static final String STAFF_NRC_COLUMN = "staff_nrc";
    public static final String STAFF_GENDER_COLUMN = "staff_gender";
    public static final String STAFF_PASSWORD_COLUMN = "staff_password";

    public static final String PRODUCT_TABLE_NAME = "product_table";
    public static final String PRODUCT_ID_COLUMN = "product_id";
    public static final String PRODUCT_NAME_COLUMN = "product_name";
    public static final String PRODUCT_TOTAL_QUANTITY_COLUMN = "product_total_quantity";
    public static final String PRODUCT_REMAINING_QUANTITY_COLUMN = "product_remaining_quantity";
    public static final String PRODUCT_BUY_PRICE_COLUMN = "product_buy_price";
    public static final String PRODUCT_SELL_PRICE_COLUMN = "product_sell_price";
    public static final String PRODUCT_SOLD_QUANTITY_COLUMN = "product_sold_qty";
    public static final String PRODUCT_IS_ADDED_TO_CART_COLUMN = "product_add_to_cart";

    public static final String SALES_RECORD_TABLE_NAME = "sales_record_table";
    public static final String SALES_RECORD_ID_COLUMN = "sales_record_id";
    public static final String PRODUCT_TOTAL_SOLD_QUANTITY_COLUMN = "product_total_sold_quantity";
    public static final String PRODUCT_SUBTOTAL_PRICE_COLUMN = "product_subtotal_price";
    public static final String SALES_RECORD_DISCOUNT_COLUMN = "sales_record_discount";
    public static final String SALES_RECORD_TAX_COLUMN = "sales_record_tax";
    public static final String SALES_RECORD_TOTAL_PRICE_COLUMN = "sales_record_total_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createStaffTableQuery = "CREATE TABLE " + STAFF_TABLE_NAME + " (" +
                STAFF_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STAFF_NAME_COLUMN + " TEXT NOT NULL, " +
                STAFF_EMAIL_COLUMN + " TEXT NOT NULL, " +
                STAFF_NRC_COLUMN + " TEXT NOT NULL, " +
                STAFF_GENDER_COLUMN + " TEXT NOT NULL, " +
                STAFF_PASSWORD_COLUMN + " TEXT NOT NULL);";

        String createProductTableQuery = "CREATE TABLE " + PRODUCT_TABLE_NAME + " (" +
                PRODUCT_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_NAME_COLUMN + " TEXT NOT NULL, " +
                PRODUCT_TOTAL_QUANTITY_COLUMN + " INTEGER NOT NULL, " +
                PRODUCT_REMAINING_QUANTITY_COLUMN + " INTEGER NOT NULL, " +
                PRODUCT_BUY_PRICE_COLUMN + " TEXT NOT NULL, " +
                PRODUCT_SELL_PRICE_COLUMN + " TEXT NOT NULL, " +
                PRODUCT_SOLD_QUANTITY_COLUMN + " INTEGER NOT NULL, " +
                PRODUCT_IS_ADDED_TO_CART_COLUMN + " INTEGER NOT NULL);";

        String createSalesRecordTableQuery = "CREATE TABLE " + SALES_RECORD_TABLE_NAME + " (" +
                SALES_RECORD_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_ID_COLUMN + " TEXT NOT NULL, " +
                STAFF_ID_COLUMN + " INTEGER NOT NULL, " +
                PRODUCT_TOTAL_SOLD_QUANTITY_COLUMN + " INTEGER NOT NULL, " +
                PRODUCT_SUBTOTAL_PRICE_COLUMN + " TEXT NOT NULL, " +
                SALES_RECORD_DISCOUNT_COLUMN + " TEXT, " +
                SALES_RECORD_TAX_COLUMN + " TEXT NOT NULL, " +
                SALES_RECORD_TOTAL_PRICE_COLUMN + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + PRODUCT_ID_COLUMN + ")" +
                "REFERENCES " + PRODUCT_TABLE_NAME + " (" + PRODUCT_ID_COLUMN + ")" +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY (" + STAFF_ID_COLUMN + ")" +
                "REFERENCES " + STAFF_TABLE_NAME + " (" + STAFF_ID_COLUMN + ")" +
                "ON UPDATE CASCADE ON DELETE CASCADE);";

        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL(createStaffTableQuery);
            sqLiteDatabase.execSQL(createProductTableQuery);
            sqLiteDatabase.execSQL(createSalesRecordTableQuery);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropStaffTableQuery = "DROP TABLE IF EXISTS " + STAFF_TABLE_NAME + ";";
        String dropProductTableQuery = "DROP TABLE IF EXISTS " + PRODUCT_TABLE_NAME + ";";
        String dropSalesRecordTableQuery = "DROP TABLE IF EXISTS " + SALES_RECORD_TABLE_NAME + ";";

        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL(dropStaffTableQuery);
            sqLiteDatabase.execSQL(dropProductTableQuery);
            sqLiteDatabase.execSQL(dropSalesRecordTableQuery);

            onCreate(sqLiteDatabase);
        }
    }

    public void insertNewStaff(String name, String email, String nrc, String gender, String password) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(STAFF_NAME_COLUMN, name);
        cv.put(STAFF_EMAIL_COLUMN, email);
        cv.put(STAFF_NRC_COLUMN, nrc);
        cv.put(STAFF_GENDER_COLUMN, gender);
        cv.put(STAFF_PASSWORD_COLUMN, password);

        long id = sqLiteDatabase.insert(STAFF_TABLE_NAME, null, cv);
        if (id == -1) {
            Toast.makeText(context, "Staff insertion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Staff insertion success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCurrentStaff(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        int result = sqLiteDatabase.delete(STAFF_TABLE_NAME, STAFF_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Staff deletion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Staff deletion success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllStaff() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + STAFF_TABLE_NAME);
    }

    public void updateCurrentStaff(int id, String name, String email, String nrc, String gender, String password) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(STAFF_NAME_COLUMN, name);
        cv.put(STAFF_EMAIL_COLUMN, email);
        cv.put(STAFF_NRC_COLUMN, nrc);
        cv.put(STAFF_GENDER_COLUMN, gender);
        cv.put(STAFF_PASSWORD_COLUMN, password);

        int result = sqLiteDatabase.update(STAFF_TABLE_NAME, cv, STAFF_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Staff update failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Staff update success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllStaff() {
        Cursor cursor = null;
        String selectAllQuery = "SELECT * FROM " + STAFF_TABLE_NAME + ";";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(selectAllQuery, null);
        }
        return cursor;
    }

    public void insertNewProduct(String name, int totalQuantity, int remainingQuantity, String buyPrice, String sellPrice, int soldQty, int isAddedToCart) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_NAME_COLUMN, name);
        cv.put(PRODUCT_TOTAL_QUANTITY_COLUMN, totalQuantity);
        cv.put(PRODUCT_REMAINING_QUANTITY_COLUMN, remainingQuantity);
        cv.put(PRODUCT_BUY_PRICE_COLUMN, buyPrice);
        cv.put(PRODUCT_SELL_PRICE_COLUMN, sellPrice);
        cv.put(PRODUCT_SOLD_QUANTITY_COLUMN, soldQty);
        cv.put(PRODUCT_IS_ADDED_TO_CART_COLUMN, isAddedToCart);

        long id = sqLiteDatabase.insert(PRODUCT_TABLE_NAME, null, cv);
        if (id == -1) {
            Toast.makeText(context, "Product insertion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Product insertion success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCurrentProduct(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        int result = sqLiteDatabase.delete(PRODUCT_TABLE_NAME, PRODUCT_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Product deletion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Product deletion success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllProducts() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + PRODUCT_TABLE_NAME);
    }

    public void updateCurrentProduct(int id, String name, int totalQuantity, int remainingQuantity, String buyPrice, String sellPrice, int soldQty, int isAddedToCart) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_NAME_COLUMN, name);
        cv.put(PRODUCT_TOTAL_QUANTITY_COLUMN, totalQuantity);
        cv.put(PRODUCT_REMAINING_QUANTITY_COLUMN, remainingQuantity);
        cv.put(PRODUCT_BUY_PRICE_COLUMN, buyPrice);
        cv.put(PRODUCT_SELL_PRICE_COLUMN, sellPrice);
        cv.put(PRODUCT_SOLD_QUANTITY_COLUMN, soldQty);
        cv.put(PRODUCT_IS_ADDED_TO_CART_COLUMN, isAddedToCart);

        int result = sqLiteDatabase.update(PRODUCT_TABLE_NAME, cv, PRODUCT_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Product update failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Product update success", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCurrentProductSoldQty(int id, int soldQty) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_SOLD_QUANTITY_COLUMN, soldQty);

        sqLiteDatabase.update(PRODUCT_TABLE_NAME, cv, PRODUCT_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
    }

    public void updateCurrentProductSoldQtyAndCartedOption(int id, int soldQty, int isAddedToCart) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_SOLD_QUANTITY_COLUMN, soldQty);
        cv.put(PRODUCT_IS_ADDED_TO_CART_COLUMN, isAddedToCart);

        sqLiteDatabase.update(PRODUCT_TABLE_NAME, cv, PRODUCT_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
    }

    public void updateCurrentProductRemainingQty(int id, int remainingQty) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_REMAINING_QUANTITY_COLUMN, remainingQty);

        sqLiteDatabase.update(PRODUCT_TABLE_NAME, cv, PRODUCT_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
    }

    public void updateCurrentProductCartOption(int id, int isAddToCart) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_IS_ADDED_TO_CART_COLUMN, isAddToCart);

        sqLiteDatabase.update(PRODUCT_TABLE_NAME, cv, PRODUCT_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor readAllProducts() {
        Cursor cursor = null;
        String selectAllQuery = "SELECT * FROM " + PRODUCT_TABLE_NAME + ";";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(selectAllQuery, null);
        }
        return cursor;
    }

    public ArrayList<Product> readSalesProductsFromCurrentSalesRecord(SalesRecord salesRecord) {
        ArrayList<Product> salesProductList = new ArrayList<>();

        if (salesRecord != null) {
            ArrayList<Integer> salesProductIdList = new ArrayList<>(salesRecord.getProductIdList());

            for (int i = 0; i < salesProductIdList.size(); i++) {
                salesProductList.add(readCurrentSalesProduct(salesProductIdList.get(i)));
            }
        }

        return salesProductList;
    }

    public Product readCurrentSalesProduct(int productId) {
        Product product = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = null;
        String selectSalesProductQuery = "SELECT * FROM " + PRODUCT_TABLE_NAME + " WHERE " + PRODUCT_ID_COLUMN + "=" + productId + ";";
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(selectSalesProductQuery, null);

            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    product = new Product(
                            cursor.getInt(Constants.PRODUCT_ID_COLUMN_POSITION),
                            cursor.getString(Constants.PRODUCT_NAME_COLUMN_POSITION),
                            cursor.getInt(Constants.PRODUCT_TOTAL_QUANTITY_COLUMN_POSITION),
                            cursor.getInt(Constants.PRODUCT_REMAINING_QUANTITY_COLUMN_POSITION),
                            cursor.getString(Constants.PRODUCT_BUY_PRICE_COLUMN_POSITION),
                            cursor.getString(Constants.PRODUCT_SELL_PRICE_COLUMN_POSITION),
                            cursor.getInt(Constants.PRODUCT_SOLD_QUANTITY_COLUMN_POSITION),
                            checkCartedOrUncarted(cursor.getInt(Constants.PRODUCT_IS_ADDED_TO_CART_COLUMN_POSITION)));
                }
            }
            cursor.close();
        }
        return product;
    }

    private Boolean checkCartedOrUncarted(int isAddedToCart) {
        if (isAddedToCart == Constants.PRODUCT_UNCARTED)
            return false;
        else return true;
    }

    public void insertNewSalesRecord(String productIdList, int staffId, int totalSoldQty, String productSubTotalPrice, String discount, String tax, String salesTotalPrice) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_ID_COLUMN, productIdList);
        cv.put(STAFF_ID_COLUMN, staffId);
        cv.put(PRODUCT_TOTAL_SOLD_QUANTITY_COLUMN, totalSoldQty);
        cv.put(PRODUCT_SUBTOTAL_PRICE_COLUMN, productSubTotalPrice);
        cv.put(SALES_RECORD_DISCOUNT_COLUMN, discount);
        cv.put(SALES_RECORD_TAX_COLUMN, tax);
        cv.put(SALES_RECORD_TOTAL_PRICE_COLUMN, salesTotalPrice);

        long id = sqLiteDatabase.insert(SALES_RECORD_TABLE_NAME, null, cv);
        if (id == -1) {
            Toast.makeText(context, "Sales record insertion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sales record insertion success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCurrentSalesRecord(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        int result = sqLiteDatabase.delete(SALES_RECORD_TABLE_NAME, SALES_RECORD_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Sales record deletion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sales record deletion success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllSalesRecords() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + SALES_RECORD_TABLE_NAME);
    }

    public void updateCurrentSalesRecord(int id, List<Integer> productIdList, int staffId, int totalSoldQty, String productSubTotalPrice, String discount, String tax, String salesTotalPrice) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        cv.put(PRODUCT_ID_COLUMN, productIdList.toString());
        cv.put(STAFF_ID_COLUMN, staffId);
        cv.put(PRODUCT_TOTAL_SOLD_QUANTITY_COLUMN, totalSoldQty);
        cv.put(PRODUCT_SUBTOTAL_PRICE_COLUMN, productSubTotalPrice);
        cv.put(SALES_RECORD_DISCOUNT_COLUMN, discount);
        cv.put(SALES_RECORD_TAX_COLUMN, tax);
        cv.put(SALES_RECORD_TOTAL_PRICE_COLUMN, salesTotalPrice);

        int result = sqLiteDatabase.update(SALES_RECORD_TABLE_NAME, cv, SALES_RECORD_ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Sales record update failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sales record update success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllSalesRecords() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = null;
        String selectAllQuery = "SELECT * FROM " + SALES_RECORD_TABLE_NAME + ";";
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(selectAllQuery, null);
        }
        return cursor;
    }
}
