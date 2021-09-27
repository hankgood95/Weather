package wook.co.weather.viewmodels;

import android.location.LocationManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wook.co.weather.models.dto.Coord;
import wook.co.weather.models.dto.GeoInfo;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.repository.MAgencyRepo;

public class MAgencyViewModel extends ViewModel {

    private final String TAG = "MAgencyViewModel";

    //이 클래스에서는 Model과 통신하여서 날씨 정보를 받아온다.
    private MutableLiveData<ShortWeather> sw;
    private MAgencyRepo maRepo;

    private LocationManager lm; //핸드폰에 있는 gps관련 기기와 상호작용하기 위해서

    public void init(GeoInfo gi){
        if(sw != null){
            return;
        }
        maRepo = MAgencyRepo.getInStance();
        sw = maRepo.getWeather(gi);
        Log.i(TAG,"API Connection finish");
    }


    public LiveData<ShortWeather> getWeather(){
        return sw;
    }

}
