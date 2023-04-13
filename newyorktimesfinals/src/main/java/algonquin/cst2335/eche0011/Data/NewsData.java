package algonquin.cst2335.eche0011.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class NewsData {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name="headline")
    private String headline;
    /**
     * The Url of news for web page
     */
    @ColumnInfo(name="webUrl")
    private String webUrl;

    /**
     * For the published date of news
     */
    @ColumnInfo(name="pubDate")
    private String pubDate;

    /**
     * the default constructor
     */
    public NewsData(){};

    /**
     * Creates a NewsData object with the given variables
     * @param headline headline of the news
     * @param webUrl url of the news
     * @param pubDate published date of the news
     */
    public NewsData(String headline, String webUrl, String pubDate){
        this.headline = headline;
        this.webUrl = webUrl;
        this.pubDate = pubDate;
    }

    /**
     * Returns the headline of the news
     * @return the headline
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * Returns the url of the news
     * @return the url
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Returns the published date of the news
     * @return the published date
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Sets the headline of the news
     * @param headline the headline
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * Sets the url of the news
     * @param webUrl the url
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * Sets the published date of the news
     * @param pubDate the published date
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
