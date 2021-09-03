package wook.co.weather.models.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class OpenWeather implements Serializable {

    @SerializedName("coord")
    private Coord coord;
    @SerializedName("weather")
    private List<Weather> weather;
    @SerializedName("base")
    private String base;
    @SerializedName("main")
    private Main main;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("rain")
    private Rain rain;
    @SerializedName("snow")
    private Snow snow;
    @SerializedName("sys")
    private Sys sys;

    @SerializedName("visibility")
    private int visibility; //가시성
    @SerializedName("dt")
    private long dt; //데이터 계산 시간
    @SerializedName("timezone")
    private int timezone; //UTC에서 초단위로 이동
    @SerializedName("id")
    private long id; //도시 id
    @SerializedName("name")
    private String name; //도시이름
    @SerializedName("cod")
    private int cod; //응답코드

    private String message;

    public String getMessage() {
        return message;
    }

    public void setCod(int cod) {
        this.cod = cod;
        if(cod >= 400){
            this.message = "날씨 정보 요청은 했으나 요청의 오류로 데이터를 받아오지 못했습니다.";
        }
    }

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Rain getRain() {
        return rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public Sys getSys() {
        return sys;
    }

    public int getVisibility() {
        return visibility;
    }

    public long getDt() {
        return dt;
    }

    public int getTimezone() {
        return timezone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    @Override
    public String toString() {
        return "OpenWeather{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", rain=" + rain +
                ", snow=" + snow +
                ", sys=" + sys +
                ", visibility=" + visibility +
                ", dt=" + dt +
                ", timezone=" + timezone +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                ", message='" + message + '\'' +
                '}';
    }
}
