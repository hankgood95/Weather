package wook.co.weather.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import wook.co.weather.R;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.viewmodels.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel mavm;
    private final String TAG = "MainActivity";
    private OpenWeather opw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intent 수신받아서
        Intent intent = getIntent();

        //intent에 실은 OpenWeather 객체를 가져옴
        opw = (OpenWeather)intent.getSerializableExtra("openWeather");
        Log.i(TAG,opw.toString());

//        mavm = new ViewModelProvider(this).get(WeatherViewModel.class);
//        mavm.init();
//        //여기서 바뀐 값들이 뭐가 있는지 확인하는 부분
//
//        //ViewModel로부터 값을 LiveData를 가져온후 해당 값에 변경사항이 있을때 아래 함수를 호출하게 된다.
//        mavm.getWeather().observe(this, new Observer<OpenWeather>() {
//            @Override
//            public void onChanged(OpenWeather openWeather) {
//                opw = mavm.getWeather().getValue();
//                Log.i(TAG,opw.toString());
//            }
//        });
    }
}