package algonquin.cst2335.eche0011.Data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {NewsData.class}, version=2)
public abstract class NewsDatabase extends RoomDatabase {

    /**
     * This Abstract method is used to return a NewsDataDAO object
     * @return NewsDataDAO object
     */
    public abstract NewsDataDAO ndDAO();
}
