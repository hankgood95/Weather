package wook.co.weather.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.models.repository.OpenWeatherRepos;

public class MainActivityViewModel extends ViewModel {

    private final String TAG = "MainActivityViewModel";

    //이 클래스에서는 Model과 통신하여서 날씨 정보를 받아온다.
    private MutableLiveData<OpenWeather> weather;
    private OpenWeatherRepos opwRepo;

    public void init(){
        if(weather != null){
            return;
        }
        opwRepo = OpenWeatherRepos.getInStance();
        weather = opwRepo.getWeather();
        Log.i(TAG,"API Connection finish");
    }

    public LiveData<OpenWeather> getWeather(){
        return weather;
    }
}
