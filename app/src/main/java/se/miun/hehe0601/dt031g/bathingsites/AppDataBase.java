package se.miun.hehe0601.dt031g.bathingsites;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Source: ripped most of it from the Dialer equivalent.
@Database(entities = {BathingSite.class}, version = 16, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract BathingSiteDao bathingSiteDao();

    public static AppDataBase getDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context, AppDataBase.class, "bathingSiteDataBase")
                    .enableMultiInstanceInvalidation()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
