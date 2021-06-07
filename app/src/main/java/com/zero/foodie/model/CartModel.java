package com.zero.foodie.model;

public class CartModel {
    String productID;
    String prductName;
    int quantity;
    String image;
    String price;
    String subTotal;

    public String getImage() {
        return image;
    }

    public CartModel() {
    }

    public String getProductID() {
        return productID;
    }

    public String getPrductName() {
        return prductName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public CartModel(String productID, String prductName, int quantity, String image, String price, String subTotal) {
        this.productID = productID;
        this.prductName = prductName;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
        this.subTotal = subTotal;
    }
}
