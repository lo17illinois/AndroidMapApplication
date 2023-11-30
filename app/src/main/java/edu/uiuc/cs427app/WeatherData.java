package edu.uiuc.cs427app;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents weather data containing information about weather conditions, and wind conditions.
 */

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

    public void setMainData(MainData mainData) {
        this.mainData = mainData;
    }

    public void setWeatherConditions(List<WeatherCondition> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public void setWindCondition(WindCondition windCondition) {
        this.windCondition = windCondition;
    }
}