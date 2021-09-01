package wook.co.weather.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.icu.util.Measure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import wook.co.weather.R;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mavm;
    private final String TAG = "MainActivity";
    private OpenWeather opw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mavm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mavm.init();
        //여기서 바뀐 값들이 뭐가 있는지 확인하는 부분

        //ViewModel로부터 값을 LiveData를 가져온후 해당 값에 변경사항이 있을때 아래 함수를 호출하게 된다.
        mavm.getWeather().observe(this, new Observer<OpenWeather>() {
            @Override
            public void onChanged(OpenWeather openWeather) {
                Log.i(TAG,"API Connection finish");
                opw = mavm.getWeather().getValue();
                Log.i(TAG,opw.toString());
            }
        });
    }
}