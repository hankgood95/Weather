package wook.co.weather.view.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import wook.co.weather.R;
import wook.co.weather.models.dto.GeoInfo;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.repository.MAgencyRepo;
import wook.co.weather.view.MainActivity;
import wook.co.weather.viewmodels.MAgencyViewModel;

public class SplashActivity extends AppCompatActivity{


    private final String TAG = "SplashActivity";
    private final int DEFAULT_LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY; //배터리와 정확도를 밸런스있게 맞춰주는애
    private final long DEFAULT_LOCATION_REQUEST_INTERVAL = 2000L;
    private final long DEFAULT_LOCATION_REQUEST_FAST_INTERVAL = 10000L;

    private LocationRequest lr; //위치정보를 사용하기 위해서 사용하는 변수
    private ShortWeather sw;
    private MAgencyViewModel mavm; //ViewModel 변수


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        //MAgencyViewModel 객체 생성
        mavm = new ViewModelProvider(this).get(MAgencyViewModel.class);
        //LocationRequest 객체 생성
        lr = LocationRequest.create();
        if(isNetworkAvailable(this)){
            checkLocationPermission();
        }else{
            AlertDialog.Builder tryAgain = new AlertDialog.Builder(this);
            tryAgain.setMessage("서버와 연결이 불안정합니다.\n 다시 시도해 주세요");
            tryAgain.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            tryAgain.show();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if(context == null)  return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); //ConnectivityManager 객체 생성

        if (connectivityManager != null) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    //네트워크 연결되어 있는지 확인
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Check InterNet","Internet connection Available");
                        return true;
                    }
                }
            }
            else { //특정 버전 이하라면 아래진입
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo(); //네트워크 연결 정보 받아옴
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) { //네트워크 연결되어 있다면 진입
                        Log.i("Check InterNet","Internet connection Available");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("Check InterNet","No internet connection");
                }
            }
        }
        Log.i("Check InterNet","No internet connection");
        return false;
    }

    public void checkLocationPermission(){
        //위치정보 권한 허용되어 있는지 아닌지를 확인하는 부분
        if(ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //위치정보 권한 허용되어 있지 않다면 실행하는 코드, 여기서 request에 대한 응답이 나오면 아래에 있는 onRequestPermissionsResult() 함수를 콜백하게 됨
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},200); //위치정보 권한을 요청한다.
        }else {
            //위치정보 권한이 허용되어 있을때 실행하는 코드
            Log.d(TAG, "위치정보 허용됨");
            //구글 플레이 서비스 위치정보 권한 승인된건지 확인해야함
            checkLocationSetting();
        }
    }

    //권한요청되었을때 콜백되어지는 함수 -> 이부분은 ViewModel로 옮겨져야 함
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){ //permissionCode가 200이고
            if(grantResults[0] == 0){ // 그중 가장 첫번째 result가 0 즉 승인된경우 진입
                Toast.makeText(getApplicationContext(),"위치정보 권한 승인됨",Toast.LENGTH_SHORT).show(); //위치정보 승인됐다고 알리고
                //위치정보 권한을 받았다면 진입
                if(ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    //구글 플레이 서비스 위치정보 권한 승인된건지 확인해야함
                    checkLocationSetting();
                }
            }else{ //위치정보를 허가받지 못했을경우 진입
                Toast.makeText(getApplicationContext(),"위치정보를 승인하지 않으면 현재위치 기반으로 \n날씨정보를 알려드릴수 없습니다.",Toast.LENGTH_LONG).show();
                Log.i(TAG,"위치정보 권한 없음 : 위치정보를 허가 안해줌");

                //허가를 받지 못했을때의 결과를 받았을때 ViewModel의 메소드를 호출하면됨
                mavm.defaultLocation();// I call ViewModel at this part - This works
                obeserveAPI();
            }
        }
    }

    public void checkLocationSetting() {
        Log.d(TAG,"LocationReqeust is in setting");
        lr.setPriority(DEFAULT_LOCATION_REQUEST_PRIORITY);
        lr.setInterval(DEFAULT_LOCATION_REQUEST_INTERVAL);
        lr.setFastestInterval(DEFAULT_LOCATION_REQUEST_FAST_INTERVAL);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(lr).setAlwaysShow(true);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //구글플레이 위치 서비스 사용할수 있게 됐을때 진입
                        mavm.requestUpdate(lr);
                        observeGps();
                    }
                })
                .addOnFailureListener(SplashActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode){
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: //충분히 설정 변경하는것으로 변경이 가능할떄
                                ResolvableApiException rae = (ResolvableApiException) e;
                                try {
                                    rae.startResolutionForResult(SplashActivity.this, 200);
                                } catch (IntentSender.SendIntentException ex) {
                                    Log.w(TAG,"LocationService approval canceled");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: //GPS가 아예 없거나 연결자체가 불가능하여 사용을 물리적으로 사용하지 못할때
                                Log.w(TAG,"No way to change setting");
                                Toast.makeText(getApplicationContext(),"GPS 사용을 하지 못하여 위치정보를 받아오지 못하고 있습니다.\n GPS 연결을 해주세요.",
                                        Toast.LENGTH_SHORT).show();
                                mavm.defaultLocation();
                                obeserveAPI();
                                break;
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requstCode,int resultCode,Intent data) {

        super.onActivityResult(requstCode, resultCode, data);
        if(requstCode == 200) {
            if (resultCode == RESULT_OK) {
                checkLocationSetting();
            } else {
                Toast.makeText(getApplicationContext(), "위치정보를 승인하지 않으면 현재위치 기반으로 \n날씨정보를 알려드릴수 없습니다.", Toast.LENGTH_LONG).show();
                Log.i(TAG, "구글 플레이 : 위치정보를 허가 안해줌");

                //허가를 받지 못했을때의 결과를 받았을때 ViewModel의 메소드를 호출하면됨
                mavm.defaultLocation();// I call ViewModel at this part - This works
                obeserveAPI();
            }
        }
    }

    public void obeserveAPI(){
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                        //해당 intent에 객체를 실어서 보낸다.
                        intent.putExtra("shortWeather",sw);
                        startActivity(intent);
                    }
                },2000);
            }
        });
    }
    public void observeGps(){
        mavm.getGeo().observe(this, new Observer<GeoInfo>() {
            @Override
            public void onChanged(GeoInfo geoInfo) {
                Log.i(TAG,mavm.getGeo().getValue().toString());
                mavm.callApi(mavm.getGeo().getValue());
                obeserveAPI();
            }
        });
    }
}
