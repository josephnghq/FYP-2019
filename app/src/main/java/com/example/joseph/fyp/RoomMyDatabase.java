package com.example.joseph.fyp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Joseph on 1/17/2019.
 */


@Database(entities = {RoomEntity.class} , version = 1)
public abstract class RoomMyDatabase extends RoomDatabase{

    public abstract RoomDAO roomDAO();

}
