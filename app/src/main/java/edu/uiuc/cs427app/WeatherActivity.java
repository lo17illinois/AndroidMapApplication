package edu.uiuc.cs427app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.HttpException;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/"; // Replace with your API's base URL
    private static final String API_KEY = "e70c05c6351eee727a7e8d183d749e3e"; // Replace with your API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Retrieve the selected city from the Intent
        String selectedCity = getIntent().getStringExtra("selectedCity");
        Date currentTime = null;
        try {
            currentTime = new DateTimeInfoAsyncTask().execute(selectedCity).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (currentTime != null) {
            TextView dateTimeTextView = findViewById(R.id.textViewDateTime);
            dateTimeTextView.setText(currentTime.toString());
        } else {
            TextView dateTimeTextView = findViewById(R.id.textViewDateTime);
            dateTimeTextView.setText("Unable to determine time");
        }

        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the WeatherApi service
        WeatherApiService weatherApi = retrofit.create(WeatherApiService.class);

        //  API request using the selected city
        Call<WeatherData> call = weatherApi.getWeatherData(selectedCity, API_KEY);
        Log.d(TAG, "Request URL: " + call.request().url());


        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

                if (response.isSuccessful()) {
                    //  successful response handled here
                    WeatherData weatherData = response.body();
                    Log.d(TAG, "WeatherData: " + weatherData); // Log the response
                    System.out.println("no response");

                    if (weatherData != null) {
                        Log.d(TAG, "Temperature: " + weatherData.getMainData().getTemperature() + "°K");
                        Log.d(TAG, "Humidity: " + weatherData.getMainData().getHumidity() + "%");
                        Log.d(TAG, "Weather: " + weatherData.getWeatherConditions().get(0).getMain());
                        Log.d(TAG, "Wind Speed: " + weatherData.getWindCondition().getSpeed() + " m/s");

                        TextView cityTextView = findViewById(R.id.textViewCityName);

                        TextView temperatureTextView = findViewById(R.id.textViewTemperature);
                        TextView humidityTextView = findViewById(R.id.textViewHumidity);
                        TextView weatherConditionTextView = findViewById(R.id.textViewWeatherCondition);
                        TextView windSpeedTextView = findViewById(R.id.textViewWindCondition);

                        cityTextView.setText("City: " + selectedCity);
                        temperatureTextView.setText("Temperature: " + weatherData.getMainData().getTemperature() + "°K");
                        humidityTextView.setText("Humidity: " + weatherData.getMainData().getHumidity() + "%");
                        weatherConditionTextView.setText("Weather: " + weatherData.getWeatherConditions().get(0).getMain());
                        windSpeedTextView.setText("Wind Speed: " + weatherData.getWindCondition().getSpeed() + " m/s");  }
                } else {
                    // Handle the error response here

                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

                // Handle the request failure here
                if (t instanceof HttpException) {
                    HttpException httpException = (HttpException) t;
                    int statusCode = httpException.code();
                    Log.e(TAG, "Request failed with status code: " + statusCode);
                    System.out.println(statusCode);
                } else {
                    System.out.println("failed");

                    Log.e(TAG, "Request failed: " + t.getMessage());
                }
            }




        });
        Button backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                startActivity(intent);
            }

    });
    }

    private class DateTimeInfoAsyncTask extends AsyncTask<String, Void, Date> {
        @Override
        protected Date doInBackground(String... params) {
            String selectedCity = params[0];
            double[] latLng = GeocodingApiClient.getLatLngForCity(selectedCity);
            if (latLng != null) {
                Log.i(TAG, "Successfully retrieved LatLng for " + selectedCity + latLng);
                double latitude = latLng[0];
                double longitude = latLng[1];
                String timeZoneId = TimeZoneApiClient.getTimeZoneId(latitude, longitude);
                if (timeZoneId != null) {
                    Log.i(TAG, "Successfully retrieved timeZoneID for " + selectedCity + timeZoneId);
                    TimeZone timeZone = TimeZone.getTimeZone("America/New_York"); // Replace with the appropriate time zone
                    Calendar calendar = Calendar.getInstance(timeZone);
                    Date currentTime = calendar.getTime();
                    Log.i(TAG, "The current time for " + selectedCity + "is: " + currentTime);
                    return currentTime;
                } else {
                    Log.i(TAG, "Failed to retrieve timeZoneID for " + selectedCity);
                }
            } else {
                Log.i(TAG, "Failed to retrieve LatLng for " + selectedCity);
            }
            return null;
        }
    }
}