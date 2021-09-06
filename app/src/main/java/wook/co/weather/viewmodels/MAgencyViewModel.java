package wook.co.weather.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.repository.MAgencyRepo;
import wook.co.weather.models.repository.OpenWeatherRepos;

public class MAgencyViewModel extends ViewModel {

    private final String TAG = "MAgencyViewModel";

    //이 클래스에서는 Model과 통신하여서 날씨 정보를 받아온다.
    private MutableLiveData<ShortWeather> sw;
    private MAgencyRepo maRepo;

    public void init(){
        if(sw != null){
            return;
        }
        maRepo = MAgencyRepo.getInStance();
        sw = maRepo.getWeather();
        Log.i(TAG,"API Connection finish");
    }

    public LiveData<ShortWeather> getWeather(){
        return sw;
    }

}
