package com.iti.mobile.triporganizer.data.room.dao;

import com.iti.mobile.triporganizer.data.entities.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM USERS WHERE email = :email")
    LiveData<User> getUserByEmail(String email);

    @Query("SELECT * FROM USERS")
    List<User> getAllUsers();
}
