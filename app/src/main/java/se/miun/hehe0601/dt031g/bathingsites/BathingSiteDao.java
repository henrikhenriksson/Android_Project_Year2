package se.miun.hehe0601.dt031g.bathingsites;


import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BathingSiteDao {

    @Query("SELECT * FROM bathingsite")
    List<BathingSite> getAll();

    @Insert
    void addBathingSite(BathingSite bathingSite) throws SQLiteConstraintException;


}
