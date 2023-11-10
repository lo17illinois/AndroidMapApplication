package edu.uiuc.cs427app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/"; // Replace with your API's base URL
    private static final String API_KEY = "e70c05c6351eee727a7e8d183d749e3e"; // Replace with your API key

    FirebaseAuth auth;
    FirebaseUser user;
    String userTheme;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static int themeColor1;
    private static int themeColor2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            //reads in the user profile, specifically the saved user theme
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Retrieve user theme from the document
                        userTheme = document.getString("userTheme");
                        if (userTheme != null) {
                            Log.i("RetrievedUser", userTheme);
                            //set the UI theme to the user theme saved onto the users profile
                            changeTheme.setTheme(this, userTheme);
                            //The ActionBar and StatusBar doesn't change with the setTheme functionality so they are manually changed below
                            Window window1 = getWindow();
                            switch (userTheme) {
                                case "theme1":
                                    //Intializes the colors for theme1 for ActionBar + StatusBar
                                    themeColor1 = Color.parseColor("#FF6200EE");
                                    themeColor2 = Color.parseColor("#FF3700B3");
                                    break;
                                case "theme2":
                                    //Intializes the colors for theme2 for ActionBar + StatusBar
                                    themeColor1 = Color.parseColor("#FF903A");
                                    themeColor2 = Color.parseColor("#FFBB86");
                                    break;
                                case "theme3":
                                    //Intializes the colors for theme3 for ActionBar + StatusBar
                                    themeColor1 = Color.parseColor("#b46b41");
                                    themeColor2 = Color.parseColor("#cd9575");
                                    break;
                                case "theme4":
                                    //Intializes the colors for theme4 for ActionBar + StatusBar
                                    themeColor1 = Color.parseColor("#3aa9ff");
                                    themeColor2 = Color.parseColor("#86caff");
                                    break;
                                default:
                                    //Intializes the colors for default=theme1 for ActionBar + StatusBar
                                    themeColor1 = Color.parseColor("#FF6200EE");
                                    themeColor2 = Color.parseColor("#FF3700B3");
                                    break;
                            }
                            //Manually changes the ActionBar + StatusBar colors
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(themeColor1));
                            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window1.setStatusBarColor(themeColor2);

                            getSupportActionBar().setDisplayShowTitleEnabled(false);
                            getSupportActionBar().setDisplayShowTitleEnabled(true);
                        } else {
                        }
                    } else {
                    }
                } else {
                    Log.e("Firestore", "Error getting user document", task.getException());
                }
            });
        } else {
            Log.e("Firestore", "User not logged in");
        }

        setContentView(R.layout.activity_weather);

        // Retrieve the selected city from the Intent
        String selectedCity = getIntent().getStringExtra("selectedCity");

        // Retrieve the currentTime using api calls
        Date currentTime = null;
        try {
            currentTime = new DateTimeInfoAsyncTask().execute(selectedCity).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Update the ui based on what the current time was
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