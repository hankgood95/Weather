package wook.co.weather.viewmodels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import wook.co.weather.models.dto.Coord;
import wook.co.weather.models.dto.GeoInfo;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.repository.MAgencyRepo;
import wook.co.weather.view.MainActivity;
import wook.co.weather.view.splash.SplashActivity;

public class MAgencyViewModel extends AndroidViewModel { //ViewModel과 AndroidViewModel의 차이점은 Application의 유무이다.

    private final String TAG = "MAgencyViewModel";

    //이 클래스에서는 Model과 통신하여서 날씨 정보를 받아온다.
    private MutableLiveData<ShortWeather> sw;
    private MutableLiveData<GeoInfo> mldGi;
    private MAgencyRepo maRepo;
    private GeoInfo gi;
    private GpsTransfer gpt;

    private LocationCallback lcb;
    private LocationRequest lr;
    public boolean requestLocationUpdate;

    public MAgencyViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG,"LocationCallBack instance have been made");
        //LocationCallBack 부분 객체 생성하고 그에 LocatinResult 받았을때를 추상화 시켜주는 부분
        gpt = new GpsTransfer();
        lcb = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult == null){
                    Log.d(TAG,"Location information have not been recieved");
                    return;
                }
                Log.d(TAG,"Location information have been recieved");
                //gps를 통하여서 위도와 경도를 입력받는다.
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        gpt.setLon(location.getLongitude());
                        gpt.setLat(location.getLatitude());
                    }
                }

                //gps 연결을 닫는다.
                LocationServices.getFusedLocationProviderClient(getApplication()).removeLocationUpdates(lcb);

                //x,y 좌표로 변환
                gpt.transfer(gpt,0);
                Log.d(TAG, gpt.toString());
                setGeoInfo(gpt); //변환된 정보를 GeoInfo에 넣음
                mldGi.setValue(gi);
            }
        };
    }

    //위치 정보 이용 권한 허가를 받지 못했을떄 호출 하는 부분
    public void defaultLocation() {

        //GpsTransfer 객체 생성
        gpt = new GpsTransfer();

        //GpsTransfer 위도와 경도를 원주로 설정
        gpt.setxLat(76);
        gpt.setyLon(122);
        gpt.transfer(gpt, 1);

        setGeoInfo(gpt);
        callApi(gi);

    }

    @SuppressLint("MissingPermission")//위치권한 체크안해도 된다고 하는 부분 안하는 이유는 SplashActivity에서 이미 했기 때문이다.
    public void requestUpdate(LocationRequest locationRequest){

        Log.d(TAG,"LocationRequest have been request");
        mldGi = new MutableLiveData<GeoInfo>();
        requestLocationUpdate = true;
        LocationServices.getFusedLocationProviderClient(getApplication())
                .requestLocationUpdates(locationRequest,lcb,null);
    }

    public void setGeoInfo(GpsTransfer gpt){
        gi = new GeoInfo();
        gi.setLon(gpt.getyLon());
        gi.setLat(gpt.getxLat());
        getTime();
    }

    public void callApi(GeoInfo geoInfo){
        if (sw != null) {
            return;
        }
        //해당 정보를 API를 호출
        maRepo = MAgencyRepo.getInStance();
        sw = maRepo.getWeather(geoInfo); // this part is calling a weather api
        Log.i(TAG, "API Connection finish");
    }
//머징

    public void getTime() {

        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd"); //년월일 받아오는 부분
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH"); //현재시간 받아오는 부분

        Calendar cal = Calendar.getInstance(); //현재시간을 받아온다.

        gi.setNowDate(dateSdf.format(cal.getTime())); //날짜 세팅
        gi.setNowTime(timeSdf.format(cal.getTime())); //시간 세팅

        /*
         * 하루 전체의 기상예보를 받아오려면 전날 23시에 266개의 날씨정보를 호출해와야 한다.
         * 따라서 호출 값은 현재 날짜보다 1일전으로 세팅해줘야 한다.
         * */

        cal.add(Calendar.DATE, -1); //1일전 날짜를 구하기 위해 현재 날짜에서 -1 시켜주는 부분
        gi.setCallDate(dateSdf.format(cal.getTime())); //1일전 값으로 호출값 생성


        Log.i(TAG, "DATE : " + gi.getNowDate());
        Log.i(TAG, "TIME : " + gi.getNowTime());
        Log.i(TAG, "CALL DATE : " + gi.getCallDate());

    }

    public LiveData<ShortWeather> getWeather() {
        return sw;
    }
    public LiveData<GeoInfo> getGeo(){ return mldGi; }
}
