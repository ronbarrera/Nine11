package com.ronaldbarrera.nine11.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    LiveData<UserEntry> getUser();

    @Query("SELECT COUNT(*) FROM user")
    int getRowCount();

    @Query("UPDATE user SET pictureUri=:uri WHERE id=:id")
    void updatePhotoUri(String uri, int id);

    @Insert
    void insertUser(UserEntry user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(UserEntry user);
}
