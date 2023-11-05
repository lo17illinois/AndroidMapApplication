package edu.uiuc.cs427app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    String name;
    EditText nameInput;
    Button button;
    Button backButton;
    private static int themeColor1;
    private static int themeColor2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    FirebaseUser user;
    String userTheme;

    //Starts on activity start up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SearchActivity","onCreate");
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

    // initializes specific features
    private void setupUI() {
        setContentView(R.layout.activity_addcity);
        nameInput = (EditText) findViewById(R.id.nameInput);
        button = (Button) findViewById(R.id.button);
        backButton = (Button) findViewById(R.id.button9);
        backButton.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameInput.getText().toString();
                addLocationFirestore(name); // fill grab the user input and add it onto the locations database
                showToast(name);
            }
        });
    }

    private void addLocationFirestore(String city) {
        Log.i("SearchActivity","addLocationFirestore");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        new GeocodeAsyncTask(city).execute();
                        //userDocRef.update("locations", FieldValue.arrayUnion(city)).addOnCompleteListener(updateTask -> {
                            //TODO: Update userDocRef with location coordinates
                            //if (updateTask.isSuccessful()) {
                            //    Log.d("Firestore", "User locations successfully updated.");
                            //} else {
                            //    Log.e("Firestore", "Error updating user locations", updateTask.getException());
                            //}
                        //});
                    }
                } else {
                    Log.e("Firestore", "Error getting user document", task.getException());
                }
            });
        } else {
            Log.e("Firestore", "User not logged in");
        }
    }
// AsyncTask to retrieve location coordinates using Geocoder
private class GeocodeAsyncTask extends AsyncTask<Void, Void, List<Address>> {
    private String cityName;

    GeocodeAsyncTask(String cityName) {
        this.cityName = cityName;
    }

    @Override
    protected List<Address> doInBackground(Void... voids) {
        Geocoder geocoder = new Geocoder(SearchActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(cityName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            Log.i("SearchActivity", String.valueOf(latitude));
            Log.i("SearchActivity", String.valueOf(longitude));
            // Update userDocRef with location coordinates
            DocumentReference userDocRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            //userDocRef.update("locations", FieldValue.arrayUnion(city)).addOnCompleteListener(updateTask -> {
            userDocRef.update("locations", FieldValue.arrayUnion(cityName))
                    .addOnSuccessListener(aVoid -> {
                        // Coordinates retrieved and user locations successfully updated
                        Log.d("Firestore", "User locations successfully updated.");
                    })
                    .addOnFailureListener(e -> {
                        // Error updating user locations
                        Log.e("Firestore", "Error updating user locations", e);
                    });
            userDocRef.update("coordinateX", FieldValue.arrayUnion(latitude))
                    .addOnSuccessListener(aVoid -> {
                        // Coordinates retrieved and user locations successfully updated
                        Log.d("Firestore", "User locations successfully updated.");
                    })
                    .addOnFailureListener(e -> {
                        // Error updating user locations
                        Log.e("Firestore", "Error updating user locations", e);
                    });
            userDocRef.update("coordinateY", FieldValue.arrayUnion(longitude))
                    .addOnSuccessListener(aVoid -> {
                        // Coordinates retrieved and user locations successfully updated
                        Log.d("Firestore", "User locations successfully updated.");
                    })
                    .addOnFailureListener(e -> {
                        // Error updating user locations
                        Log.e("Firestore", "Error updating user locations", e);
                    });
        } else {
            // City not found or Geocoder service unavailable
            Log.e("Geocoder", "City not found or Geocoder service unavailable");
        }
    }
}


    // currently just shows inputted text, should be mapped to add city to user's info in firestore
    private void showToast (String text) {
        Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    // function that deals with button presses
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //Create button functionality to takes the user back to MainActivity
            case R.id.button9:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
