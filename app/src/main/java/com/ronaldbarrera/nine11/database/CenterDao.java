package com.ronaldbarrera.nine11.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ronaldbarrera.nine11.ui.center.Center;

import java.util.List;

@Dao
public interface CenterDao {

    @Query("SELECT * FROM center ORDER BY id")
    LiveData<List<Center>> getAllCenters();

    @Query("SELECT * FROM center")
    List<Center> getAllCentersList();

    @Query("SELECT * FROM center WHERE id = :id")
    Center getCenterById(String id);

    @Insert
    void insertCenter(Center entry);

    @Delete
    void deleteCenter(Center entry);
}
