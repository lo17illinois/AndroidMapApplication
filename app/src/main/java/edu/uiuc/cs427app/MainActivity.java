package edu.uiuc.cs427app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.navigation.ui.AppBarConfiguration;
import edu.uiuc.cs427app.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.GeoPoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    int position = 0;
    //    List<GeoPoint> fav_cities  = new ArrayList<GeoPoint>();
    List<String> fav_cities = new ArrayList<>(); // this is temporary long term goal is to have it geopoint
    FirebaseAuth auth;
    FirebaseUser user;
    String userTheme;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static int themeId;
    private static int themeColor1;
    private static int themeColor2;
    private Spinner spinner;
    String username = "";


    //Starts on activity start up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity","onCreate");
        super.onCreate(savedInstanceState);
        //loads in firebase user in preparation to read in user theme
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
                            Log.i("RetrievedUser",userTheme);
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
                            //Initialize MainActivity-specific UI features
                            setupUI();
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
        setupUI();
    }

    //Initialize MainActivity-specific UI features
    private void setupUI() {
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.logout);
        Button buttonNew = findViewById(R.id.buttonAddLocation);
        Button buttonChangeUI = findViewById(R.id.button6);
        Button getweather = findViewById(R.id.buttonCheckWeather);

        buttonNew.setOnClickListener(this);
        buttonChangeUI.setOnClickListener(this);
        getweather.setOnClickListener(this);

        //Create button functionality to go to LoginActivity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //Create text welcoming the Team + username
        TextView textView = findViewById(R.id.user_details);
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String username = user.getDisplayName();
            if (username != null) {
                setTitle("Team 10-" + username);
                textView.setText("Welcome, " + username);
            } else {
                textView.setText(user.getEmail());
            }
        }
         spinner = findViewById(R.id.spinner);

        // Initialize Firestore data
        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {                     // when we get the data back we want to load up the locations list
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //fav_cities = (List<GeoPoint>) document.get("locations");                 // there should always be a location array, even if it's empty
                                fav_cities = (List<String>) document.get("locations");
                                String fav_theme = document.getString("userTheme");

                                // Firestore data received
                                ArrayAdapter<String> adapter = null;
                                try {
                                    adapter = createSpinnerAdapter(fav_cities);                          // turning the geopoint array into strings to be used in spinner
                                } catch (ExecutionException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                spinner.setAdapter(adapter);
                                spinner.setPrompt("Select a city");

                                // Continue initializing other UI components here
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {                        // whenever an item is selected, we want to update the position so we know what item is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                position = 0;
            }
        });

        Button buttonLocationAdd = findViewById(R.id.buttonAddLocation);

        //Create button functionality to go to remove locations from the cities list
        ImageButton buttonLocationRemove = findViewById(R.id.buttonRemoveLocation);                                // trash button uses position to determine which location to remove
        buttonLocationRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != 0) {                                                                              // if it was on position 0 then we don't want to do anything since that is the prompt location
                    DocumentReference docRef = db.collection("users").document(user.getUid());               // otherwise, we update the firestore to show that the location was removed
                    fav_cities.remove(position-1);
                    docRef.update("locations", fav_cities)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document was successfully deleted
                                    try {
                                        spinner.setAdapter(createSpinnerAdapter(fav_cities));
                                    } catch (ExecutionException e) {
                                        throw new RuntimeException(e);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that occurred during the deletion
                                }
                            });
                }
            }
        });
    }
    //    private ArrayAdapter<String> createSpinnerAdapter(List<GeoPoint> geoPoints) throws ExecutionException, InterruptedException { // Convert the GeoPoint data to a format suitable for the spinner
    private ArrayAdapter<String> createSpinnerAdapter(List<String> cities) throws ExecutionException, InterruptedException {
        List<String> cityNames = new ArrayList<>();
        cityNames.add("Select a City");
        //        for (GeoPoint geoPoint : geoPoints) {
//            String city = "";
//            try {
//                city = formatGeoPoint(this, geoPoint);                                                                             // formats the geopoint into a string of a city if found otherwise Invalid.
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//                cityNames.add(city);
//            else
//                cityNames.add("Invalid");
//        }
        if (cities != null) {
            for (String city : cities) {
                cityNames.add(city);
            }
        } else {
            cityNames.add("Invalid");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNames);                    // put the citynames into adapter for the spinner to use
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private String formatGeoPoint(Context context, GeoPoint geoPoint) throws ExecutionException, InterruptedException {                // use a geocoder to convert geopoints back into cities
        double latitude = geoPoint.getLatitude();
        double longitude = geoPoint.getLongitude();
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


    //Create button functionality to add locations to cities list
        @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //Create button functionality to add locations to cities list
            case R.id.buttonAddLocation:
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("city", "Add a Location");
                startActivity(intent);
                break;
            //Create button functionality to go to ChooseUIDisplayActivity
            case R.id.button6:
                intent = new Intent(this, ChooseUIDisplayActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonCheckWeather:
                // Retrieve the selected city from the Spinner
                System.out.println("test");

                String selectedCity = spinner.getSelectedItem().toString();
                System.out.println(selectedCity);
                // Create an Intent to launch the WeatherActivity
                Intent weatherIntent = new Intent(this, WeatherActivity.class);

                // Pass the selected city as an extra to the WeatherActivity
                weatherIntent.putExtra("selectedCity", selectedCity);

                // Start the WeatherActivity with the selected city
                startActivity(weatherIntent);
                // TODO: Make an API request to fetch weather data for the selected city
                // Ensure you handle the request asynchronously.

                // TODO: Update the UI to display weather information once data is fetched.
                // Use a TextView or other UI element to display the weather details.
                break;
        }
    }
}

