package algonquin.cst2335.eche0011.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import algonquin.cst2335.eche0011.R;
import algonquin.cst2335.eche0011.databinding.ActivityNewYorkPageBinding;


public class NewYorkPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityNewYorkPageBinding binding=ActivityNewYorkPageBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
    }
}