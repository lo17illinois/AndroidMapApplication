package edu.uiuc.cs427app;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private PlacesAdapter adapter;
    private EditText editSearch;
    private ProgressBar progressBar;
    private static int themeColor1;
    private static int themeColor2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    FirebaseUser user;
    String userTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity","onCreate");
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Retrieve user theme from the document
                        userTheme = document.getString("userTheme");
                        if (userTheme != null) {
                            Log.i("RetrievedUser",userTheme);
                            changeTheme.setTheme(this, userTheme);
                            Window window1 = getWindow();
                            switch (userTheme) {
                                case "theme1":
                                    themeColor1 = Color.parseColor("#FF6200EE");
                                    themeColor2 = Color.parseColor("#FF3700B3");
                                    break;
                                case "theme2":
                                    themeColor1 = Color.parseColor("#FF903A");
                                    themeColor2 = Color.parseColor("#FFBB86");
                                    break;
                                case "theme3":
                                    themeColor1 = Color.parseColor("#b46b41");
                                    themeColor2 = Color.parseColor("#cd9575");
                                    break;
                                case "theme4":
                                    themeColor1 = Color.parseColor("#3aa9ff");
                                    themeColor2 = Color.parseColor("#86caff");
                                    break;
                                default:
                                    themeColor1 = Color.parseColor("#FF6200EE");
                                    themeColor2 = Color.parseColor("#FF3700B3");
                                    break;
                            }
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(themeColor1));
                            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window1.setStatusBarColor(themeColor2);
                            getSupportActionBar().setDisplayShowTitleEnabled(false);
                            getSupportActionBar().setDisplayShowTitleEnabled(true);
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

    private void setupUI() {
        setContentView(R.layout.activity_search);
        String apiKey = BuildConfig.PLACES_API_KEY;

        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey);
        }
        Button backButton = findViewById(R.id.button9);
        backButton.setOnClickListener(this);

        placesClient = Places.createClient(this);
        sessionToken = AutocompleteSessionToken.newInstance();

        editSearch = findViewById(R.id.editSearch);
        progressBar = findViewById(R.id.progressBar);
        ListView listPlaces = findViewById(R.id.listPlaces);

        progressBar.setVisibility(View.GONE);
        adapter = new PlacesAdapter(this);
        listPlaces.setAdapter(adapter);
        listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getCount() > 0) {
                    detailPlace(adapter.predictions.get(i).getPlaceId());
                }
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (editSearch.length() > 0) {
                        searchPlaces();
                    }
                }
                return false;
            }
        });
    }
    private void searchPlaces() {

        progressBar.setVisibility(View.VISIBLE);

        //RectangularBounds bounds = RectangularBounds.newInstance(
                //new LatLng(41.8781, 87.6298),
                //new LatLng(40.730671, 88.524896)
        //);

        List<String> test = new ArrayList();
        test.add("CITIES");

        FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest.builder()
                //.setSessionToken(sessionToken)
                //.setTypesFilter(test)
                //.setLocationBias(bounds)
                .setCountries("US")
                .setTypesFilter(Arrays.asList(PlaceTypes.CITIES))
                .setSessionToken(sessionToken)
                .setQuery(editSearch.getText().toString())
                .build();
                //.setLocationBias(bias)
                //.setCountries("ID")
                //.build();

        int bp = 0;

        /*placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                List<AutocompletePrediction> predictions = findAutocompletePredictionsResponse.getAutocompletePredictions();
                adapter.setPredictions(predictions);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e("SearchActivity", "Place not found: " + apiException.getStatusCode());
                }
            }
        });*/

        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found : " + apiException.getStatusCode());
            }
        });
    }

    private void detailPlace(String placeId) {

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    Toast.makeText(SearchActivity.this, "LatLng: " + latLng, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e("SearchActivity", "Place not found: " + e.getMessage());
                    final int statusCode = apiException.getStatusCode();
                }
            }
        });
    }

    private static class PlacesAdapter extends BaseAdapter {
        private final List<AutocompletePrediction> predictions = new ArrayList<>();
        private final Context context;

        public PlacesAdapter(Context context) {
            this.context = context;
        }

        public void setPredictions(List<AutocompletePrediction> predictions) {
            this.predictions.clear();
            this.predictions.addAll(predictions);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return predictions.size();
        }

        @Override
        public Object getItem(int i) {
            return predictions.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = LayoutInflater.from(context).inflate(R.layout.list_item_place, viewGroup, false);
            TextView txtShortAddress = v.findViewById(R.id.txtShortAddress);
            TextView txtLongAddress = v.findViewById(R.id.txtLongAddress);

            txtShortAddress.setText(predictions.get(i).getPrimaryText(null));
            txtLongAddress.setText(predictions.get(i).getSecondaryText(null));

            return v;
        }

    }
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button9:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
