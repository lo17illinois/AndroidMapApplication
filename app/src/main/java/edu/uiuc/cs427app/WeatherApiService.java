package edu.uiuc.cs427app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather")
    Call<WeatherData> getWeatherData(@Query("q") String city, @Query("appid") String apiKey);
}

