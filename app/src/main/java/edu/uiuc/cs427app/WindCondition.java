package edu.uiuc.cs427app;
import com.google.gson.annotations.SerializedName;

public class WindCondition {
    @SerializedName("speed")
    private double speed;

    public double getSpeed() {
        return speed;


    }
}
