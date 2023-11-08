package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    FrameLayout map;
    FirebaseAuth auth;
    FirebaseUser user;
    String userTheme;
    List<String> fav_cities = new ArrayList<>();
    List <Double> X_coords = new ArrayList<>();
    List <Double> Y_coords = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static int themeColor1;
    private static int themeColor2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ShowMapActivity","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

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

                        // ADDED: Retrieve user fav cities and coordinates
                        fav_cities = (List<String>) document.get("locations");
                        X_coords = (List<Double>) document.get("coordinateX");
                        Y_coords = (List<Double>) document.get("coordinateX");
                        //Log.i("fav_cities", fav_cities.toString());
                        //Log.i("X_coords", X_coords.toString());
                        //Log.i("Y_coords", Y_coords.toString());
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
        setupUI();
    }

    //Initialize ShowMapActivity specific UI features
    private void setupUI() {

        //Retrieve the top city from the intent
        Intent intent = getIntent();
        String topCity = intent.getStringExtra("selected_city");
        Double topCity_Xcoord = intent.getDoubleExtra("selected_city_XCoord", 0.0);
        Double topCity_Ycoord = intent.getDoubleExtra("selected_city_YCoord", 0.0);

        //Display city name
        TextView cityNametTextView = findViewById(R.id.location_name);
        cityNametTextView.setText(topCity);

        //Display latitude & longitude
        TextView cityCoordinatetextView = findViewById(R.id.locationGeoCoordinate);

        // set text to city coordinate
        cityCoordinatetextView.setText(topCity_Xcoord + ", " + topCity_Ycoord);

        map = findViewById(R.id.google_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        //Retrieve the top city from the intent
        Intent intent = getIntent();
        Double topCity_Xcoord = intent.getDoubleExtra("selected_city_XCoord", 0.0);
        Double topCity_Ycoord = intent.getDoubleExtra("selected_city_YCoord", 0.0);
        // Add a marker in Sydney and move the camera
        LatLng selectedCity = new LatLng(topCity_Xcoord, topCity_Ycoord);
        gMap.addMarker(new MarkerOptions().position(selectedCity).title("Selected City"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(selectedCity));
        // Enable zoom controls
        gMap.getUiSettings().setZoomControlsEnabled(true);
    }
}