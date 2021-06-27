package com.zero.foodie.model;


import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class OrderHistoryModel {
    String date;
    ArrayList<String> productList;
    ArrayList<String> quantity;
    ArrayList<String> price;
    String total;

    public OrderHistoryModel() {

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProductList(ArrayList<String> productList) {
        this.productList = productList;
    }

    public void setQuantity(ArrayList<String> quantity) {
        this.quantity = quantity;
    }

    public void setPrice(ArrayList<String> price) {
        this.price = price;
    }

    public void setTotal(String total) {
        this.total = total.substring(1);
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getProductList() {
        return productList;
    }

    public ArrayList<String> getQuantity() {
        return quantity;
    }

    public ArrayList<String> getPrice() {
        for (int i = 0; i < price.size(); i++) {
            price.set(i, parseInt(quantity.get(i)) * parseInt(price.get(i)) + "");
        }
        return price;
    }

    public String getTotal() {
        ArrayList<String> prices = getPrice();
        int t = 0;
        for (int i = 0; i < prices.size(); i++) {
            t = t + parseInt(prices.get(i));
        }
        total = String.valueOf(t);
        return total;
    }

    public OrderHistoryModel(String date, ArrayList<String> productList, ArrayList<String> quantity, ArrayList<String> price, String total) {
        this.date = date;
        this.productList = productList;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }
}
