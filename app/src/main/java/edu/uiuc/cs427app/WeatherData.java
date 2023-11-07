package edu.uiuc.cs427app;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherData {
    @SerializedName("main")
    private MainData mainData;

    @SerializedName("weather")
    private List<WeatherCondition> weatherConditions; // Change this to a List

    @SerializedName("wind")
    private WindCondition windCondition;

    public MainData getMainData() {
        return mainData;
    }

    public List<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    public WindCondition getWindCondition() {
        return windCondition;
    }
}
