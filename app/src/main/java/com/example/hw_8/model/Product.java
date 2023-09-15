package com.example.hw_8.model;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String name;
    int totalQuantity;
    int remainingQuantity;
    String buyPrice;
    String sellPrice;
    int soldQty;
    Boolean isAddedToCart;

    public Product(int id, String name, int totalQuantity, int remainingQuantity, String buyPrice, String sellPrice, int soldQty, Boolean isAddToCart) {
        this.id = id;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = remainingQuantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.soldQty = soldQty;
        this.isAddedToCart = isAddToCart;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public int getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(int soldQty) {
        this.soldQty = soldQty;
    }

    public Boolean getAddToCart() {
        return isAddedToCart;
    }

    public void setAddToCart(Boolean addToCart) {
        isAddedToCart = addToCart;
    }
}
