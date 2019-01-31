package com.example.joseph.fyp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by Joseph on 1/17/2019.
 */


@Database(entities = {RoomSynthData.class , RoomNotesArrayList.class} , version = 1)
@TypeConverters({Converters.class})
public abstract class RoomMyDatabase extends RoomDatabase{

    public abstract RoomDAO roomDAO();

}
