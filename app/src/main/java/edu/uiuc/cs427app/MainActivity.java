package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


//     Main Page
//     * location selector, will use firestore
//     * location adder
//     * settings

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Spinner spinner = findViewById(R.id.spinner);

        // Initialize Firestore data
        db.collection("users")
                .document("QfCRXh6NUkPslOmDX6KAojqNCL42")
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
                                ArrayAdapter<String> adapter = createSpinnerAdapter(fav_cities);
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

    private ArrayAdapter<String> createSpinnerAdapter(List<GeoPoint> geoPoints) {
        // Convert the GeoPoint data to a format suitable for the spinner
        List<String> cityNames = new ArrayList<>();
        cityNames.add("Select a City");
        for (GeoPoint geoPoint : geoPoints) {
            cityNames.add(formatGeoPoint(geoPoint)); // Implement formatGeoPoint to format GeoPoint as a string
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private String formatGeoPoint(GeoPoint geoPoint) {

        // Need to implement geopoint to city api
        if ((geoPoint.getLatitude() == 41.88231727399064) && (geoPoint.getLongitude() == -87.64613325836507))
            return "Chicago";
        else if ((geoPoint.getLatitude() == 40.116964839819616) && (geoPoint.getLongitude() == -88.24483958291965))
            return "Champaign";
        else
            return "UNKOWN";
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
//            case R.id.buttonChampaign:
//                intent = new Intent(this, DetailsActivity.class);
//                intent.putExtra("city", "Champaign");
//                startActivity(intent);
//                break;
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
//            case R.id.buttonAddLocation:
//                // Implement this action to add a new location to the list of locations
//                break;
        }
    }
}

