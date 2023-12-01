package edu.uiuc.cs427app;

import com.google.gson.annotations.SerializedName;

public class WeatherCondition {
    // The main weather condition description
    @SerializedName("main")
    private String main;

    public String getMain() {
        return main;
    }

    public void setMain(String main){
        this.main = main;
    }
}


