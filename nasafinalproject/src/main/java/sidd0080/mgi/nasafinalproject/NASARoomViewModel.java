package sidd0080.mgi.nasafinalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class NASARoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<NASAMessage>> roverNames = new MutableLiveData< >();
    public MutableLiveData<NASAMessage> selectedMessage = new MutableLiveData<>();
    public MutableLiveData<MainActivity.MyRowHolder> selectedRow = new MutableLiveData<>();
}