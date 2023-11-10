package edu.uiuc.cs427app;

import android.content.Context;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeocodingApiClient {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "AIzaSyAWShdyunaphUFSlfem19ZvYPJalS8td1A";

    public static double[] getLatLngForCity(String city) {
        try {
            // Build the URL for the API request
            String apiUrl = BASE_URL + "?address=" + URLEncoder.encode(city, "UTF-8") +
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

                // Parse the response to get the LatLng for the city
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("results")) {
                    JSONObject firstResult = jsonResponse.getJSONArray("results").optJSONObject(0);
                    if (firstResult != null) {
                        JSONObject geometry = firstResult.optJSONObject("geometry");
                        if (geometry != null) {
                            JSONObject location = geometry.optJSONObject("location");
                            if (location != null) {
                                double lat = location.optDouble("lat");
                                double lng = location.optDouble("lng");
                                return new double[] {lat, lng};
                            }
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
