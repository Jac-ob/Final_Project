package algonquin.cst2335.eche0011.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This is the interface class that will interact with the database
 */
@Dao
public interface NewsDataDAO {
    /**
     * For inserting object into database
     * @param nd class object
     * @return id for inserted row
     */

    @Insert
    public long insertList(NewsData nd);

    /**
     * To return all lists in database
     * @return all lists in database
     */
    @Query("select * from NewsData")
    public List<NewsData> getAllList();

    /**
     * To delete list in database
     * @param nd object to delete
     */
    @Delete
    void deleteList(NewsData nd);
}
