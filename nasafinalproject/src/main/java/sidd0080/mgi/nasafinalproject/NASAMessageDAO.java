package sidd0080.mgi.nasafinalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// class for doing CRUD
@Dao
public interface NASAMessageDAO {

    @Insert
    long insertMessage(NASAMessage m);

    @Query("Select * from NASAMessage")
    List<NASAMessage> getAllMessages();

    @Delete
    void deleteMessage(NASAMessage m);

}
