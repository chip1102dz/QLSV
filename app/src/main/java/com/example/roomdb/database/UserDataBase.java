package com.example.roomdb.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.roomdb.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "User1.db";
    private static UserDataBase instance;

    public static synchronized UserDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, UserDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract UserDAO userDAO();

}
