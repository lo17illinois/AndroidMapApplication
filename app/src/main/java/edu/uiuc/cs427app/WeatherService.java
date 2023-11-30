package edu.uiuc.cs427app;

import java.util.Arrays;

import retrofit2.Call;


public class WeatherService {
    private WeatherApiService weatherApiService;

    public WeatherService(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    public Call<WeatherData> getWeatherData(String city, String apiKey) {
        return weatherApiService.getWeatherData(city, apiKey);
    }

    private WeatherData createDefaultWeatherData() {
        MainData mainData = new MainData();
        mainData.setTemperature(500.0);
        mainData.setHumidity(500.0);
        WeatherCondition weatherCondition = new WeatherCondition();
        weatherCondition.setMain("Mocking");
        WindCondition windCondition = new WindCondition();
        windCondition.setSpeed(500.0);

        WeatherData mockWeather = new WeatherData();

        mockWeather.setMainData(mainData);
        mockWeather.setWeatherConditions(Arrays.asList(weatherCondition));
        mockWeather.setWindCondition(windCondition);

        return mockWeather;
    }
}