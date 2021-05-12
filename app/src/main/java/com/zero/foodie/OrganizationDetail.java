package com.zero.foodie;

public class OrganizationDetail {
    String id;
    String name;
    String image_url;
    String address;

    public OrganizationDetail(String id, String name, String image_url, String address) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getAddress() {
        return address;
    }
}
