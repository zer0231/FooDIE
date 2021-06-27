package com.zero.foodie.model;

public class UserDetail {
    public String userName, userAddress, userEmail, userPassword, userProfilePicture,userPhoneNumber;

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public UserDetail(String userName, String userAddress, String userEmail, String userPassword, String userPhoneNumber) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userProfilePicture = "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg";
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public UserDetail() {
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
