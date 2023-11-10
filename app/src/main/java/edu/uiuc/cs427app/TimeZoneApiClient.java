package edu.uiuc.cs427app;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TimeZoneApiClient {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/timezone/json";
    private static final String API_KEY = "AIzaSyAWShdyunaphUFSlfem19ZvYPJalS8td1A";

    public static String getTimeZoneId(double latitude, double longitude) {
        try {
            long timestamp = System.currentTimeMillis() / 1000; // Current Unix timestamp

            // Build the URL for the API request
            String apiUrl = BASE_URL + "?location=" + URLEncoder.encode(latitude + "," + longitude) +
                    "&timestamp=" + timestamp +
                    "&key=" + API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read the API response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the response to get the time zone ID
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("timeZoneId")) {
                    return jsonResponse.getString("timeZoneId");
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

