package wook.co.weather.models.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import wook.co.weather.interfaces.OpenWeatherAPI;
import wook.co.weather.models.dto.OpenWeather;
import wook.co.weather.models.retrofit.RetrofitService;

public class OpenWeatherRepos {
    //이 클래스에서는 API 통신을 통해서 데이터를 가져와야 한다.
    private final String TAG = "OpenWeatherRepository";
    private final static String BASE_URL = "https://api.openweathermap.org/data/2.5/"; //api의 baseURL을 상수로 적어서 올려놨다.
    private static OpenWeatherRepos instance;
    private Retrofit retrofit;
    private OpenWeatherAPI opwAPI;
    private OpenWeather opw;

    //OpenWeatherRepos 인스턴스 반환
    public static OpenWeatherRepos getInStance() {
        if(instance == null){
            instance = new OpenWeatherRepos();
        }
        return instance;
    }

    //날씨 정보를 직접적으러 받아와야 하는 부분
    public MutableLiveData<OpenWeather> getWeather() {

        //Retrofit 인스턴스 생성
        retrofit = new RetrofitService().getRetroInstance(BASE_URL);

        //인터페이스 객체 생성
        opwAPI = retrofit.create(OpenWeatherAPI.class);

        //API와 통신을 하는 함수 호출
        MutableLiveData<OpenWeather> data = new MutableLiveData<OpenWeather>();
        opw = new OpenWeather();
        callWeatherAPI();
        Log.i(TAG,opw.toString());
        data.setValue(opw);
        return data;
    }

    private void callWeatherAPI() {

        //응답을 받아오는 부분
        Call<OpenWeather> call = opwAPI.getWeather("seoul","d0a0f80f34551266ab6f092780304575","kr");

        call.enqueue(new Callback<OpenWeather>() {
            @Override
            public void onResponse(Call<OpenWeather> call, Response<OpenWeather> response) {
                opw = response.body();
                Log.i(TAG,"API CONNECT SUCCESS");
            }

            @Override
            public void onFailure(Call<OpenWeather> call, Throwable t) {
                Log.d(TAG,"onFailure : "+t.getMessage());
            }
        });
    }

}
