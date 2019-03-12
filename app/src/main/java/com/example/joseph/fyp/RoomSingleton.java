package com.example.joseph.fyp;

import android.arch.persistence.room.Room;
import android.content.Context;

import static com.example.joseph.fyp.RoomMyDatabase.MIGRATION_1_2;

/**
 * Created by Joseph on 1/18/2019.
 */

public class RoomSingleton {

    static RoomMyDatabase db ;

    public static void buildDb(Context context){
        db = Room.databaseBuilder(context, RoomMyDatabase.class, "finalyp2019").addMigrations(MIGRATION_1_2).build();

    }

}
