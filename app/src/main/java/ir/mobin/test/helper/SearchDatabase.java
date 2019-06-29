package ir.mobin.test.helper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ir.mobin.test.converter.DataConverter;
import ir.mobin.test.dao.RecentSearchDao;
import ir.mobin.test.model.RecentSearch;

@Database(entities = RecentSearch.class, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class SearchDatabase extends RoomDatabase {

    abstract RecentSearchDao recentSearchDao();

    private static SearchDatabase i;

    synchronized SearchDatabase getInstance(Context context){
        if(i == null){
            i = Room.databaseBuilder(context, SearchDatabase.class, "search_database")
                    .fallbackToDestructiveMigration().build();
        }
        return i;
    }
}
