package wook.co.weather.models.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import wook.co.weather.interfaces.MeteorologicalAgencyAPI;
import wook.co.weather.models.dto.GeoInfo;
import wook.co.weather.models.dto.GpsTransfer;
import wook.co.weather.models.dto.ShortWeather;
import wook.co.weather.models.retrofit.RetrofitService;

public class MAgencyRepo {

    //이 클래스에서는 API 통신을 통해서 데이터를 가져와야 한다.
    private final String TAG = "MAgencyRepo";
    private final static String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"; //api의 baseURL을 상수로 적어서 올려놨다.
    private static MAgencyRepo instance;
    private Retrofit retrofit;
    private MeteorologicalAgencyAPI MaAPI;
    private ShortWeather sw;

    //OpenWeatherRepos 인스턴스 반환
    public static MAgencyRepo getInStance() {
        if(instance == null){
            instance = new MAgencyRepo();
        }
        return instance;
    }

    //날씨 정보를 직접적으러 받아와야 하는 부분
    public MutableLiveData<ShortWeather> getWeather(GeoInfo gi) {

        //Retrofit 객체 생성 생성
        retrofit = new RetrofitService().getRetroInstance(BASE_URL);

        //인터페이스 객체 생성
        MaAPI = retrofit.create(MeteorologicalAgencyAPI.class);

        //API와 통신을 하는 함수 호출
        sw = new ShortWeather();
        MutableLiveData<ShortWeather> data = new MutableLiveData<ShortWeather>();
        callWeatherAPI(data, gi);
        return data;
    }

    private void callWeatherAPI(MutableLiveData<ShortWeather> data, GeoInfo gi) {

        int nx = (int) gi.getLat();
        int ny = (int) gi.getLon();

        String baseDate = gi.getCallDate();


        //응답을 하고 받아오는 부분
        Call<ShortWeather> call = MaAPI.getShortWeather("0gr5uJKYx6b+lhmOt+Dm+fbcxVdiG7U407njrJ3YSFLlrckPeysX5fHfT0dwQRHHs4m0z5ELtDx9jcZ/Z3qoVA==",
                266,1,"JSON",baseDate,"2300",nx,ny);

        call.enqueue(new Callback<ShortWeather>() {
            @Override
            public void onResponse(Call<ShortWeather> call, Response<ShortWeather> response) {
                if(response.isSuccessful()){ //연결이 성공적으로 되었을때 진입하는 부분
                    //해당 api는 오류가 생기더라도 여기로 오고 대신에 들어고는 resultCode값에 따라서 오류인것과 아닌것을 나눈다.
                    data.postValue(response.body());
                    Log.i(TAG,"API CONNECT SUCCESS");
                    Log.i(TAG,response.body().toString());
                }
            }

            //인증키 관련해서 잘못 된 부분만 onFailure에 오게 됨
            @Override
            public void onFailure(Call<ShortWeather> call, Throwable t) {
                Log.d(TAG,"onFailure : "+t.getMessage());
            }
        });
    }

}
