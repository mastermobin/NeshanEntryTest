package ir.mobin.test.model;

import org.neshan.core.LngLat;

public class Place {

    private String title;
    private String address;
    private LngLat location;

    public Place(String title, String address, LngLat location) {
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
