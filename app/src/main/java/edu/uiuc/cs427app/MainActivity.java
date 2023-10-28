package edu.uiuc.cs427app;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


//     Main Page
//     * location selector, will use firestore
//     * location adder
//     * settings

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            username = user.getDisplayName();

            if(username!=null){
                setTitle("Team 10-" + username);

                textView.setText("Welcome, " + username);
            }
        else {
            textView.setText(user.getEmail());
        }}
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Spinner spinner = findViewById(R.id.spinner);

        // Initialize Firestore data
        db.collection("users")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<GeoPoint> fav_cities = (List<GeoPoint>) document.get("locations");
                                String fav_theme = document.getString("theme");

                                // Firestore data received
                                ArrayAdapter<String> adapter = null;
                                try {
                                    adapter = createSpinnerAdapter(fav_cities);
                                } catch (ExecutionException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                spinner.setAdapter(adapter);
                                spinner.setPrompt("Select a city");

                                Button buttonNew = findViewById(R.id.buttonAddLocation);
//                                buttonNew.setOnClickListener(this);

                                // Continue initializing other UI components here
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<GeoPoint> geoPoints) throws ExecutionException, InterruptedException {
        // Convert the GeoPoint data to a format suitable for the spinner
        List<String> cityNames = new ArrayList<>();
        cityNames.add("Select a City");
        for (GeoPoint geoPoint : geoPoints) {
            String city = "";
            try {
                city = formatGeoPoint(this, geoPoint); // Implement formatGeoPoint to format GeoPoint as a string
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (city != null)
                cityNames.add(city);
            else
                cityNames.add("Invalid");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private String formatGeoPoint(Context context, GeoPoint geoPoint) throws ExecutionException, InterruptedException {
        double latitude = geoPoint.getLatitude();
        double longitude = geoPoint.getLongitude();
        String apiKey = "AIzaSyAWShdyunaphUFSlfem19ZvYPJalS8td1A";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String cityName = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }


        @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.spinner:
                intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("city", "Champaign");
                startActivity(intent);
                break;
            //case R.id.buttonChampaign:
                //intent = new Intent(this, SearchActivity.class);
                //intent.putExtra("city", "Champaign");
                //startActivity(intent);
                //break;
//            case R.id.buttonChicago:
//                intent = new Intent(this, DetailsActivity.class);
//                intent.putExtra("city", "Chicago");
//                startActivity(intent);
//                break;
//            case R.id.buttonLA:
//                intent = new Intent(this, DetailsActivity.class);
//                intent.putExtra("city", "Los Angeles");
//                startActivity(intent);
//                break;
            case R.id.buttonAddLocation:
                // Implement this action to add a new location to the list of locations
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("city", "Add a Location");
                startActivity(intent);
                break;
        }
    }
}

