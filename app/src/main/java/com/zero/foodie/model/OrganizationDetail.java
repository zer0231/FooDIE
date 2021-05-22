package com.zero.foodie.model;

import java.util.HashMap;
import java.util.List;

public class OrganizationDetail {

    String orgName;
    String orgAddress;
    String orgPhoneNumber;
    String orgImageLink;
    List<ProductDetail> products;
   // public HashMap<ProductDetail,String> getProducts() {
        //return products;
 //   }


    public void setProducts(List<ProductDetail> products) {
        this.products = products;
    }

    public List<ProductDetail> getProducts() {
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
