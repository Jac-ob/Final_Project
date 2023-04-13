package algonquin.cst2335.eche0011.Data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
//
//import com.example.finalproject.databinding.DetailsLayoutBinding;

import algonquin.cst2335.eche0011.databinding.DetailsLayoutBinding;


public class NewsDetailsFragment extends Fragment {

    NewsData selected;
    public NewsDetailsFragment(NewsData n){
        selected = n;
    }

    /**
     * @param inflater is for the layoutInflater object
     * @param container the parent view that the fragment's UI is going to be attached to
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
