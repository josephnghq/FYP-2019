package com.example.joseph.fyp;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Joseph on 1/18/2019.
 */

public class RoomSingleton {

    static RoomMyDatabase db ;

    public static void buildDb(Context context){
        db = Room.databaseBuilder(context, RoomMyDatabase.class, "finalyp2019").build();

    }

}
