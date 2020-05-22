package se.miun.hehe0601.dt031g.bathingsites;

import android.location.Address;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// source: https://developer.android.com/training/data-storage/room/defining-data

@Entity(indices = {@Index(value = {"longitude", "latitude"}, unique = true)})
public class BathingSite {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "bathing_site_name")
    public String bathingSiteName;

    @ColumnInfo(name = "bathing_site_description")
    public String bathingSiteDescription;

    @ColumnInfo(name = "bathing_site_address")
    public String bathingSiteAddress;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "grade")
    public Double grade;

    @ColumnInfo(name = "water_temp")
    public Double waterTemp;

    @ColumnInfo(name = "temp_date")
    public String tempDate;

    public BathingSite(String bathingSiteName, String bathingSiteDescription,
                       String bathingSiteAddress, @Nullable Double latitude, @Nullable
                       Double longitude, @Nullable Double grade,
                       @Nullable Double waterTemp, String tempDate) {
        this.bathingSiteName = bathingSiteName;
        this.bathingSiteDescription = bathingSiteDescription;
        this.bathingSiteAddress = bathingSiteAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.grade = grade;
        this.waterTemp = waterTemp;
        this.tempDate = tempDate;
    }


    @Override
    public String toString() {
        return "BathingSite{" +
                "uid=" + uid +
                "\n bathingSiteName='" + bathingSiteName + '\'' +
                "\n bathingSiteDescription='" + bathingSiteDescription + '\'' +
                "\n bathingSiteAddress='" + bathingSiteAddress + '\'' +
                "\n latitude=" + latitude +
                "\n longitude=" + longitude +
                "\n grade=" + grade +
                "\n waterTemp=" + waterTemp +
                "\n tempDate='" + tempDate + '\'' +
                '}';
    }

    public String getBathingSiteName() {
        return bathingSiteName;
    }
}
