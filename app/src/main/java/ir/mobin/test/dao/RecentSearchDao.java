package ir.mobin.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.mobin.test.model.RecentSearch;

@Dao
public interface RecentSearchDao {

    @Insert
    void insert(RecentSearch recentSearch);

    @Query("SELECT * FROM recent_search ORDER BY date DESC")
    List<RecentSearch> getRecent();

}
