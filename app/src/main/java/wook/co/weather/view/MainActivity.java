package wook.co.weather.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import wook.co.weather.R;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mavm;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mavm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mavm.init();
        Log.i(TAG,"API Connection finish");
        OpenWeather opw = mavm.getWeather().getValue();
        Log.i(TAG,opw.toString());
    }
}