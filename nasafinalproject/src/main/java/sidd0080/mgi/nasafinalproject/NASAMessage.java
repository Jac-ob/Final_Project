package sidd0080.mgi.nasafinalproject;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NASAMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "id" )
    public int id;

    @ColumnInfo(name = "ImageURL")
    protected String imageURL;

    @ColumnInfo( name = "Rover")
    protected String rover;

    @ColumnInfo(name = "Camera")
    protected String camera;

    @ColumnInfo(name = "SendOrReceive")
    protected boolean isSentButton;

    public NASAMessage() {  }

    public NASAMessage(String i, String r, String c, boolean sent){
        imageURL = i;
        rover = r;
        camera = c;
        isSentButton = sent;
    }

    //getter methods
    public String getRover(){
        return  rover;
    }

    public void setRover(String rover){
        this.rover =  rover;
    }

    public String getImageURL(){
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL =  imageURL;
    }

    public String getCamera(){
        return  camera;
    }

    public void setCamera(String camera){
        this.camera =  camera;
    }

    public boolean getIsSentButton(){
        return isSentButton;
    }
    public void setSentButton(boolean isSentButton){
        this.isSentButton =  isSentButton;
    }
}