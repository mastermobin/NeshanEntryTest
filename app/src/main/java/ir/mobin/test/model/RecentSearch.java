package ir.mobin.test.model;

import org.neshan.core.LngLat;

//@Entity(tableName = "recent_search")
public class RecentSearch {
    private String title;
    private String address;
    private LngLat location;

    public RecentSearch(String title, String address, LngLat location) {
        this.title = title;
        this.address = address;
        this.location = location;
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
}
