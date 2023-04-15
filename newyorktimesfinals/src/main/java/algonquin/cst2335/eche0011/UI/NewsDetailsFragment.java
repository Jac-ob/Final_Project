package algonquin.cst2335.eche0011.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

//import com.example.finalproject.Data.NewsData;
//import com.example.finalproject.databinding.DetailsLayoutBinding;

import algonquin.cst2335.eche0011.Data.NewsData;
import algonquin.cst2335.eche0011.databinding.DetailsLayoutBinding;


public class NewsDetailsFragment extends Fragment {
    /** Declaration of NewsData */
    NewsData selected;

    /**
     * Creates a new NewsDetailsFragment object with the specified news article
     * @param n the news article to display details
     */
    public NewsDetailsFragment(NewsData n){
        selected = n;
    }


    /**
     * To create and inflate fragment view and to set text to views on fragment
     * @param inflater the LayoutInflater object
     * @param container the parent view that the fragment's UI should be attached
     * @param savedInstanceState the saved state of the fragment
     * @return the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.nameId.setText("Title : " + selected.getHeadline());
        binding.dateId.setText("Date : " + selected.getPubDate());
        binding.urlId.setText("URL : " + selected.getWebUrl());

        return binding.getRoot();
    }
}
