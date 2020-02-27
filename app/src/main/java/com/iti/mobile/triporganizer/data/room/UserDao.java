package com.iti.mobile.triporganizer.data.room;

import com.iti.mobile.triporganizer.data.room_entity.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM USERS WHERE email = :email")
    LiveData<User> getUserByEmail(String email);
}
