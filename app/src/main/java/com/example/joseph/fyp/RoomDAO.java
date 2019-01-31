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

    @Query("SELECT * FROM RoomSynthData")
    List<RoomSynthData> getAll();


    @Query("SELECT * FROM RoomNotesArrayList")
    List<RoomNotesArrayList> getAllNotes();

    @Query("SELECT * FROM RoomSynthData WHERE uid IN (:userIds)")
    List<RoomSynthData> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(RoomSynthData... users);

    @Insert
    void insertAllNotes(RoomNotesArrayList... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(RoomSynthData... users);



    @Delete
    void delete(RoomSynthData user);







}
