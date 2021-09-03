package wook.co.weather.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import wook.co.weather.R;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.view.MainActivity;
import wook.co.weather.viewmodels.WeatherViewModel;

public class SplashActivity extends AppCompatActivity {

    private WeatherViewModel mavm;
    private final String TAG = "SplashActivity";
    private OpenWeather opw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mavm = new ViewModelProvider(this).get(WeatherViewModel.class);
        mavm.init();
        //여기서 바뀐 값들이 뭐가 있는지 확인하는 부분

        //ViewModel로부터 값을 LiveData를 가져온후 해당 값에 변경사항이 있을때 아래 함수를 호출하게 된다.
        mavm.getWeather().observe(this, new Observer<OpenWeather>() {
            //onChanged 됐을때 다른 화면으로 넘어가면 됨, 그리고 넘어갈때 날씨 데이터를 같이 가지고 가야함
            @Override
            public void onChanged(OpenWeather openWeather) {
                opw = mavm.getWeather().getValue();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("openWeather",opw);
                Log.i(TAG,opw.toString());
                startActivity(intent);
            }
        });

    }

}
