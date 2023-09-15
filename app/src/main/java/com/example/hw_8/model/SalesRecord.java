package com.example.hw_8.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesRecord implements Serializable {

    int id;
    ArrayList<Integer> productIdList;
    int staffId;
    int productSoldQty;
    String productSubTotalPrice;
    String discount;
    String tax;
    String salesTotalPrice;

    public SalesRecord(int id, int staffId, int productSoldQty, String salesTotalPrice) {
        this.id = id;
        this.staffId = staffId;
        this.productSoldQty = productSoldQty;
        this.salesTotalPrice = salesTotalPrice;
    }

    public SalesRecord(int id, ArrayList<Integer> productIdList, int staffId, int productSoldQty, String productSubTotalPrice, String discount, String tax, String salesTotalPrice) {
        this.id = id;
        this.productIdList = productIdList;
        this.staffId = staffId;
        this.productSoldQty = productSoldQty;
        this.productSubTotalPrice = productSubTotalPrice;
        this.discount = discount;
        this.tax = tax;
        this.salesTotalPrice = salesTotalPrice;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getProductIdList() {
        return productIdList;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getProductSoldQty() {
        return productSoldQty;
    }

    public String getProductSubTotalPrice() {
        return productSubTotalPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public String getTax() {
        return tax;
    }

    public String getSalesTotalPrice() {
        return salesTotalPrice;
    }
}
