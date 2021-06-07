package com.zero.foodie.model;

public class ProductDetail {
    String proName;
    String proImageLink;
    String proItemType;
    String proId;
    String proBriefDescription;
    String proPrice;
    boolean inCart = false;
    boolean isFavourite = false;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
// String orgId;

    public ProductDetail(String proName, String proImageLink, String proItemType, String proBriefDescription, String proPrice, String proId) {
        this.proName = proName;
        this.proImageLink = proImageLink;
        this.proItemType = proItemType;
        this.proBriefDescription = proBriefDescription;
        this.proPrice = proPrice;
        this.proId = proId;
    }

    public String getProId() {
        return proId;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
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


}
