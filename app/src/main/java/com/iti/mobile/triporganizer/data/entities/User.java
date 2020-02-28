package com.iti.mobile.triporganizer.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User implements Parcelable {

    @NonNull
    @PrimaryKey
    private String id;
    private String userName;
    private String profilePicUrl;
    private String email;
    @Ignore
    private String password;

    //login-method
    private String provider_id;

    public User() {
    }

    @Ignore
    public User(String userName, String profilePicUrl, String email, String id, String provider_id) {
        this.userName = userName;
        this.profilePicUrl = profilePicUrl;
        this.email = email;
        this.id = id;
        this.provider_id=provider_id;
    }

    protected User(Parcel in) {
        userName = in.readString();
        profilePicUrl = in.readString();
        email = in.readString();
        id = in.readString();
        password = in.readString();
        provider_id = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(profilePicUrl);
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(password);
        dest.writeString(provider_id);
    }
}

