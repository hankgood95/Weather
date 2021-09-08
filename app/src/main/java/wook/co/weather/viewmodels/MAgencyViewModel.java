package wook.co.weather.viewmodels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wook.co.weather.models.dto.Coord;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.repository.MAgencyRepo;
import wook.co.weather.models.repository.OpenWeatherRepos;

public class MAgencyViewModel extends ViewModel {

    private final String TAG = "MAgencyViewModel";

    //이 클래스에서는 Model과 통신하여서 날씨 정보를 받아온다.
    private MutableLiveData<ShortWeather> sw;
    private MAgencyRepo maRepo;
    private Coord coord;

    private LocationManager lm; //핸드폰에 있는 gps관련 기기와 상호작용하기 위해서

    public void init(GpsTransfer gpt){
        if(sw != null){
            return;
        }
        maRepo = MAgencyRepo.getInStance();
        sw = maRepo.getWeather(gpt);
        Log.i(TAG,"API Connection finish");
    }

    public LiveData<ShortWeather> getWeather(){
        return sw;
    }

}
