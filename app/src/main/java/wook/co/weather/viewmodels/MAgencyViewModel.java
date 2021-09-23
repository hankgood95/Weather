package wook.co.weather.viewmodels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import wook.co.weather.models.dto.Coord;
import wook.co.weather.models.dto.GeoInfo;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.repository.MAgencyRepo;
import wook.co.weather.view.MainActivity;
import wook.co.weather.view.splash.SplashActivity;

public class MAgencyViewModel extends ViewModel implements LocationListener {

    private final String TAG = "MAgencyViewModel";

    //이 클래스에서는 Model과 통신하여서 날씨 정보를 받아온다.
    private MutableLiveData<ShortWeather> sw;
    private MAgencyRepo maRepo;
    private GeoInfo gi;
    private GpsTransfer gpt;

//    public void init(GeoInfo gi) {
//        if (sw != null) {
//            return;
//        }
//
//        maRepo = MAgencyRepo.getInStance();
//        sw = maRepo.getWeather(gi); // this part is calling a weather api
//        Log.i(TAG, "API Connection finish");
//    }

    public LiveData<ShortWeather> getWeather() {
        return sw;
    }

    @SuppressLint("MissingPermission")
    //이부분은 권한 재확인 안하게 해주는 부분이다. 따로 재확인을 안하는 이유는 Activity단에서 이미 확인을 거쳤기 때문이다.
    public void locationUpdate(LocationManager lm) {
        Log.i(TAG, "locationUpdate()");
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this); //위치정보를 update하는 함수 이건 실제 기기용
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); //After this nothing is happening
    }

    //여기서는 이제 위치 정보가 변경 되었을때 진입하는 부분
    @Override
    public void onLocationChanged(Location location) { //This CallBack method is not working
        Log.i(TAG, "onLocationChanged()");
    }

    //위치 정보 이용 권한 허가를 받지 못했을떄 호출 하는 부분
    public void defaultLocation() {


        //GpsTransfer 객체 생성
        gpt = new GpsTransfer();

        //GpsTransfer 위도와 경도를 원주로 설정
        gpt.setxLat(76);
        gpt.setyLon(122);
        gpt.transfer(gpt, 1);

        gi = new GeoInfo();

        gi.setLon(gpt.getyLon());
        gi.setLat(gpt.getxLat());

        getTime();

        if (sw != null) {
            return;
        }

        //해당 정보를 API를 호출
        maRepo = MAgencyRepo.getInStance();
        sw = maRepo.getWeather(gi); // this part is calling a weather api
        Log.i(TAG, "API Connection finish");
    }

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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
