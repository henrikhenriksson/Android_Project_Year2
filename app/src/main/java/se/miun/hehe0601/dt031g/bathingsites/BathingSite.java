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

        StringBuilder sb = new StringBuilder();
        sb.append("Site: ")
                .append(bathingSiteName)
                .append("\n");
        if (bathingSiteAddress != null) {
            sb.append("Location: ")
                    .append(bathingSiteAddress)
                    .append("\n");
        }
        if (this.longitude != null) {
            sb.append("Coordinates: ")
                    .append(longitude)
                    .append(" , ")
                    .append(latitude)
                    .append("\n");
        }

        if (this.bathingSiteDescription != null) {
            sb.append("Description: ")
                    .append(bathingSiteDescription)
                    .append("\n");
        }

        if (this.grade != null) {
            sb.append("Rating: ")
                    .append(grade).append("\n");
        }
        if (this.waterTemp != null) {
            sb.append("Temperature: ")
                    .append(waterTemp)
                    .append("\n");
        }
        if (this.tempDate != null) {
            sb.append("Bathing site Saved: ").append(tempDate).append("\n");
        }

        return sb.toString();

    }

    public String getBathingSiteName() {
        return bathingSiteName;
    }
}
