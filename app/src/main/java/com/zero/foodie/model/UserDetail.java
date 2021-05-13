package com.zero.foodie.model;

public class UserDetail {
    public String userId, userName, userAddress, userEmail, userPassword, userProfilePicture;

    public UserDetail(String userName, String userAddress, String userEmail, String userPassword) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userProfilePicture = "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg";
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getUserId() {
        return userId;
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
