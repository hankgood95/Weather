package wook.co.weather.models.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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

        //Retrofit 객체 생성 생성
        retrofit = new RetrofitService().getRetroInstance(BASE_URL);

        //인터페이스 객체 생성
        opwAPI = retrofit.create(OpenWeatherAPI.class);

        //API와 통신을 하는 함수 호출
        opw = new OpenWeather();
        MutableLiveData<OpenWeather> data = new MutableLiveData<OpenWeather>();
        callWeatherAPI(data);
        return data;
    }

    private void callWeatherAPI(MutableLiveData<OpenWeather> data) {

        //응답을 하고 받아오는 부분
        Call<OpenWeather> call = opwAPI.getWeather("seoul","d0a0f80f34551266ab6f092780304575","kr");

        call.enqueue(new Callback<OpenWeather>() {
            //아래 함수들은 callback 함수들로써 연결이 완료되면 호출이 되는 부분이다.
            @Override
            public void onResponse(Call<OpenWeather> call, Response<OpenWeather> response) {
                if(response.isSuccessful()){ //연결이 성공적으로 되었을때 진입하는 부분
                    data.postValue(response.body());
                    Log.i(TAG,"API CONNECT SUCCESS");
                }else{ //400번대나 500번대 에러가 나면 여기로 오게됨
                    opw.setCod(response.code());
                    Log.i(TAG,"API CONNECT SUCCESS BUT WRONG PARAMETER");
                    data.postValue(opw);
                }
            }

            @Override
            public void onFailure(Call<OpenWeather> call, Throwable t) {
                Log.d(TAG,"onFailure : "+t.getMessage());
            }
        });
    }
}
