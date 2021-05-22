package com.zero.foodie.model;

public class ProductDetail {
    String proName;
    String proImageLink;
    String proItemType;
    String proBriefDescription;

    public ProductDetail(String proName, String proImageLink, String proItemType, String proBriefDescription, String proPrice) {
        this.proName = proName;
        this.proImageLink = proImageLink;
        this.proItemType = proItemType;
        this.proBriefDescription = proBriefDescription;
        this.proPrice = proPrice;
    }

    public ProductDetail() {

    }

    public String getProName() {
        return proName;
    }

    public String getProImageLink() {
        return proImageLink;
    }

    public String getProItemType() {
        return proItemType;
    }

    public String getProBriefDescription() {
        return proBriefDescription;
    }

    public String getProPrice() {
        return proPrice;
    }

    String proPrice;
}
