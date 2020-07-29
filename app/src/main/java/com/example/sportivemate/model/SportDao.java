package com.example.sportivemate.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SportDao {
    @Query("SELECT * FROM Sports")
    LiveData<List<Sport>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Sport... sports);

    @Query("SELECT * FROM Sports WHERE name LIKE :spotName")
    LiveData<Sport> getSport(String spotName);
}
