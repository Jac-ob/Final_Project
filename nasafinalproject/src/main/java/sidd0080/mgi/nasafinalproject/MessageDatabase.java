package sidd0080.mgi.nasafinalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NASAMessage.class}, version = 2)
public abstract class MessageDatabase extends RoomDatabase {

    // methods for interacting with the database
    public abstract NASAMessageDAO cmDAO();

}
