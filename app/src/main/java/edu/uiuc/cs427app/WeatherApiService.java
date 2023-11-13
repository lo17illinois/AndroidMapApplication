package edu.uiuc.cs427app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for defining the Weather API service using Retrofit.
 */

public interface WeatherApiService {
        /**
     * Gets weather data for a specific city.
     *
     * @param city    The name of the city for which weather data is requested.
     * @param apiKey  The API key for accessing the weather data.
     * @return        A Retrofit Call object containing the WeatherData for the specified city.
     */
    @GET("weather")
    Call<WeatherData> getWeatherData(@Query("q") String city, @Query("appid") String apiKey);
}

