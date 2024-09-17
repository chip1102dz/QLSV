package com.example.roomdb.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomdb.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void InsertUser(User user);

    @Query("SELECT * FROM User1")
    List<User> getListUser();

    @Update
    void UpdateUser(User user);

    @Delete
    void DeleteUser(User user);

    @Query("SELECT * FROM user1 where name= :name")
    List<User> checkUser(String name);

    @Query("DELETE FROM User1")
    void DeleteAll();

    @Query("SELECT * FROM user1 WHERE name LIKE '%' || :name || '%'")
    List<User> searchUser(String name);
}
