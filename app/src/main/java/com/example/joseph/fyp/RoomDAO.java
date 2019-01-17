package com.example.joseph.fyp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Joseph on 1/17/2019.
 */


@Dao
public interface RoomDAO {

    @Query("SELECT * FROM RoomEntity")
    List<RoomEntity> getAll();

    @Query("SELECT * FROM RoomEntity WHERE uid IN (:userIds)")
    List<RoomEntity> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(RoomEntity... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(RoomEntity... users);



    @Delete
    void delete(RoomEntity user);







}
