package edu.uiuc.cs427app;

import com.google.gson.annotations.SerializedName;

public class WeatherCondition {
    @SerializedName("main")
    private String main;

    public String getMain() {
        return main;
    }
}

