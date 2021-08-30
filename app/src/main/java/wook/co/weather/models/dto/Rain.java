package wook.co.weather.models.dto;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private float h3;
    @SerializedName("1h")
    private float h1;

    public float getH1() {
        return h1;
    }

    public float getH3() {
        return h3;
    }

    @Override
    public String toString() {
        return "Rain{" +
                "h3=" + h3 +
                ", h1=" + h1 +
                '}';
    }
}
