package wook.co.weather.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import wook.co.weather.R;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.view.MainActivity;
import wook.co.weather.viewmodels.MAgencyViewModel;
import wook.co.weather.viewmodels.WeatherViewModel;

public class SplashActivity extends AppCompatActivity {

//    private OpenWeather opw;
//    private WeatherViewModel mavm;
    private ShortWeather sw;
    private MAgencyViewModel mavm;
    private final String TAG = "SplashActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mavm = new ViewModelProvider(this).get(MAgencyViewModel.class);
        mavm.init();

        mavm.getWeather().observe(this, new Observer<ShortWeather>() {
            @Override
            public void onChanged(ShortWeather shortWeather) {
                sw = mavm.getWeather().getValue();
                Log.i(TAG,sw.toString());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //intent 형성한다.
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //해당 intent에 객체를 실어서 보낸다.
                        intent.putExtra("shortWeather",sw);
                        startActivity(intent);
                    }
                },2500);
            }
        });

    }

}
