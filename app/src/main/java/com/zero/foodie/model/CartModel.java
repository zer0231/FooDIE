package com.zero.foodie.model;

public class CartModel {
    String productID;
    String userID;
    String prductName;
    int quantity;
    String image;
    String deliveryLatitude;
    String deliveryLongitude;

    public String getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(String deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public String getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(String deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public CartModel(String productID, String prductName, int quantity, String image, String price, String subTotal, String userID) {
        this.productID = productID;
        this.prductName = prductName;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
        this.subTotal = subTotal;
        this.userID = userID;
    }
}
