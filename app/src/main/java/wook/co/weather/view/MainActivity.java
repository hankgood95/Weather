package wook.co.weather.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import wook.co.weather.R;
import wook.co.weather.models.dto.ShortWeather;

public class MainActivity extends AppCompatActivity {

//    private WeatherViewModel mavm;
//    private OpenWeather opw;

    private ShortWeather sw;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intent 수신받아서
        Intent intent = getIntent();
        sw = (ShortWeather) intent.getSerializableExtra("shortWeather");
        Log.i(TAG,sw.toString());

    }
}