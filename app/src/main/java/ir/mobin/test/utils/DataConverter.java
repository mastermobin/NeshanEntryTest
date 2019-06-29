package ir.mobin.test.utils;

import androidx.room.TypeConverter;

import org.neshan.core.LngLat;

import java.util.Date;

public class DataConverter {

    @TypeConverter
    public Date toDate(Long dateLong) {
        if (dateLong == null)
            return null;
        else
            return new Date(dateLong);
    }

    @TypeConverter
    public Long fromDate(Date date)
    {
        if (date == null)
            return null;
        else
            return date.getTime();
    }

    @TypeConverter
    public LngLat toLngLat(String lngLatString){
        if(lngLatString == null){
            return null;
        }
        String[] pos = lngLatString.split(":");
        double x = Double.parseDouble(pos[0]);
        double y = Double.parseDouble(pos[1]);

        return new LngLat(x, y);
    }

    @TypeConverter
    public String fromLngLat(LngLat lngLat){
        if(lngLat == null){
            return null;
        }

        return lngLat.getX() + ":" + lngLat.getY();
    }
}