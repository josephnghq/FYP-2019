package com.example.joseph.fyp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Joseph on 1/30/2019.
 */

@Entity()
public class RoomNotesArrayList {


    @PrimaryKey()
    public int uid = 1;

    @ColumnInfo(name = "nal")
    public String nal;



}
