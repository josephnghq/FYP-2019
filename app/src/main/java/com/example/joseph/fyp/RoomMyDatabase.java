package com.example.joseph.fyp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * Created by Joseph on 1/17/2019.
 */


@Database(entities = {RoomSynthData.class , RoomNotesArrayList.class} , version = 1)
@TypeConverters({Converters.class})
public abstract class RoomMyDatabase extends RoomDatabase{

    public abstract RoomDAO roomDAO();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE RoomSynthData "
                    + " ADD" + " COLUMN DisableSaw2 BOOLEAN"
                    + " COLUMN DisableSine2 BOOLEAN"
                    + " COLUMN DisableSqr2 BOOLEAN"
                    + " COLUMN DisableTri2 BOOLEAN"
                    + " COLUMN ENABLE_PHASER BOOLEAN"
                    + " COLUMN PHASER_feedback DOUBLE"
                    + " COLUMN PHASER_freq DOUBLE"
                    + " COLUMN PHASER_amp DOUBLE"
                    + " COLUMN PHASER_depth DOUBLE"
            );



        }
    };


}


