package com.zero.foodie.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrganizationDetail {

    String orgName;
    String orgAddress;
    String orgPhoneNumber;
    String orgImageLink;
    ArrayList<ProductDetail> products;
   // public HashMap<ProductDetail,String> getProducts() {
        //return products;
 //   }


    public void setProducts(ArrayList<ProductDetail> products) {
        this.products = products;
    }

    public ArrayList<ProductDetail> getProducts() {
        return products;
    }

    public OrganizationDetail(String orgName, String orgAddress, String orgPhoneNumber, String orgImageLink) {
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.orgPhoneNumber = orgPhoneNumber;
        this.orgImageLink = orgImageLink;
        //this.products = products;
    }

    public OrganizationDetail()
    {}

    public String getOrgName() {
        return orgName;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public String getOrgPhoneNumber() {
        return orgPhoneNumber;
    }

    public String getOrgImageLink() {
        return orgImageLink;
    }
}
