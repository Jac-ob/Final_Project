package algonquin.cst2335.eche0011.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class NewYorkTimesViewModel extends ViewModel {
    /**
     * This MutableLiveData object holds the ArrayList of NewsData for the titles
     */
    public MutableLiveData<ArrayList<NewsData>> titles = new MutableLiveData< >();
    /**
     * This mutableLiveData object holds a single NewsData object for a selected article
     */
    public MutableLiveData<NewsData> selectedArticle = new MutableLiveData< >();
}
