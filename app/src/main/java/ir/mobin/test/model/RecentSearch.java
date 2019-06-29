package ir.mobin.test.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.neshan.core.LngLat;

import java.util.Date;

@Entity(tableName = "recent_search")
public class RecentSearch {

    @PrimaryKey(autoGenerate = true)
    private int index;
    private String title;
    private String address;
    private LngLat location;
    private Date date;

    public RecentSearch(String title, String address, LngLat location, Date date) {
        this.title = title;
        this.address = address;
        this.location = location;
        this.date = date;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public LngLat getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }
}
