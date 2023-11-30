package edu.uiuc.cs427app;
import com.google.gson.annotations.SerializedName;

public class MainData {
    @SerializedName("temp")
    private double temperature;

    @SerializedName("humidity")
    private double humidity;

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }
    public void setTemperature(double temperature){
        this.temperature = temperature;
    }

    public void setHumidity(double humidity){
        this.humidity = humidity;
    }
}
