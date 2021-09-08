package wook.co.weather.view.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import wook.co.weather.R;
import wook.co.weather.models.dto.Coord;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.view.MainActivity;
import wook.co.weather.viewmodels.MAgencyViewModel;
import wook.co.weather.viewmodels.WeatherViewModel;

public class SplashActivity extends AppCompatActivity implements LocationListener {

    private ShortWeather sw;
    private MAgencyViewModel mavm;
    private final String TAG = "SplashActivity";
    private LocationManager lm;
    private Coord coord;
    private GpsTransfer gpt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //MAgencyViewModel 객체 생성
        mavm = new ViewModelProvider(this).get(MAgencyViewModel.class);

        coord = new Coord();

        //LocationManager 객체 생성
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //위치정보 권한 허용되어 있는지 아닌지를 확인하는 부분
        if(ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //위치정보 권한 허용되어 있지 않다면 실행하는 코드, 여기서 request에 대한 응답이 나오면 아래에 있는 onRequestPermissionsResult() 함수를 콜백하게 됨
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},200); //위치정보 권한을 요청한다.

        }else {
            //위치정보 권한이 허용되어 있을때 실행하는 코드
            Log.d(TAG, "위치정보 허용됨");
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this); //위치정보를 update 한다.
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){ //permissionCode가 200이고
            if(grantResults[0] == 0){ // 그중 가장 첫번째 result가 0 즉 승인된경우 진입
                Toast.makeText(getApplicationContext(),"위치정보 승인됨",Toast.LENGTH_SHORT).show(); //위치정보 승인됐다고 알리고
                //위치정보 권한을 받았다면 진입
                if(ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this); //위치정보를 update하는 함수
                }
            }else{ //위치정보를 허가받지 못했을경우 진입
                Toast.makeText(getApplicationContext(),"위치정보를 승인하지 않으면 현재위치 기반으로 \n날씨정보를 알려드릴수 없습니다.",Toast.LENGTH_LONG).show();
                Log.i(TAG,"위치정보를 허가 안해줌");
                //허가를 받지 못했을땐 default로 원주시의 날씨를 받아오게하기 위해서 만들었다.
                gpt = new GpsTransfer();
                gpt.setxLat(76);
                gpt.setyLon(122);
                gpt.transfer(gpt,1);
                getWeather(mavm);
            }
        }
    }

    public void getWeather(MAgencyViewModel mavm){
        mavm.init(gpt);
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
                },1000);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "위치정보 업데이트중");
        coord.setLat(location.getLatitude()); //위도를 입력
        coord.setLon(location.getLongitude()); //경도를 입력
        Log.i(TAG,coord.toString()); //입력한 위도와 경두를 출력

        gpt = new GpsTransfer(coord.getLat(),coord.getLon());
        gpt.transfer(gpt,0);
        Log.i(TAG,gpt.toString()); //입력한 위도와 경두를 출력
        //이건 이제 한번 update하고 나서 위치를 업데이트 다시 안시키기 위해서 하는것임
        //이걸 하지 않으면 위치정보를 계속해서 update하기때문에 배터리 소모하게됨
        lm.removeUpdates(this);

        getWeather(mavm); //전달받은 위치정보로 날씨정보 API를 호출
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
