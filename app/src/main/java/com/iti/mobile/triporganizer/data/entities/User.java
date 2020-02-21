package com.iti.mobile.triporganizer.data.entities;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String profilePicUrl;
    private String email;
    private String id;
    private String password;


    //login-method
    private String provider_id;

    public User() {
    }

    public User(String userName, String profilePicUrl, String email, String id,String provider_id) {
        this.userName = userName;
        this.profilePicUrl = profilePicUrl;
        this.email = email;
        this.id = id;
        this.provider_id=provider_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
