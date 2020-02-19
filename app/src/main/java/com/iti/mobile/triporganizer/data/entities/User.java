package com.iti.mobile.triporganizer.data.entities;

public class User {

    private String userName;
    private String profilePic;
    private String email;
    private String id;

    public User() {
    }

    public User(String userName, String profilePic, String email, String id) {
        this.userName = userName;
        this.profilePic = profilePic;
        this.email = email;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
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
}
