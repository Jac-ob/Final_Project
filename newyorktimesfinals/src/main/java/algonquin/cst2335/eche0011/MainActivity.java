package algonquin.cst2335.eche0011;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.eche0011.UI.NewYorkPage;
import algonquin.cst2335.eche0011.UI.NewYorkTimes;

import algonquin.cst2335.eche0011.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigation();
    }
    void navigation(){
        binding.newYork.setOnClickListener(clk->{
            Intent NewYorkPage=new Intent(this, NewYorkTimes.class);
            startActivity(NewYorkPage);
        });

    }
}