package wook.co.weather.models.dto;

import com.google.gson.annotations.SerializedName;

public class GeoInfo {
    @SerializedName("lon")
    private double lon;
    @SerializedName("lat")
    private double lat;

    private String nowDate;
    private String nowTime;
    private String callDate;

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "GeoInfo{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", nowDate='" + nowDate + '\'' +
                ", nowTime='" + nowTime + '\'' +
                ", callDate='" + callDate + '\'' +
                '}';
    }
}
