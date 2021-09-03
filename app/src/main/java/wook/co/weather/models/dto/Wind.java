package wook.co.weather.models.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind implements Serializable {
    @SerializedName("speed")
    private float speed;
    @SerializedName("deg")
    private float deg;

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
