package edu.uiuc.cs427app;

import android.os.Handler;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.ListenerRegistration;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.SetOptions;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.Collections;

public class ChooseUIDisplayActivity extends AppCompatActivity implements View.OnClickListener {

    public static String userSelectedTheme = "theme1";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Button backButton;


    //Runs on activity start up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if there exists a savedInstanceState, app will restore itself to a previous state
        if (savedInstanceState != null) {
            Log.i("onCreateIf",userSelectedTheme);
            userSelectedTheme = savedInstanceState.getString("userSelectedTheme", userSelectedTheme);
        //otherwise, read variables from local variables
        } else {
            Log.i("onCreateElse",userSelectedTheme);
            userSelectedTheme = changeTheme.localTheme;
            changeTheme.setTheme(this, userSelectedTheme);
        }
        //Initializes all the UI features
        setupUI();
    }

    //Initializes all the UI features
    private void setupUI() {
        setContentView(R.layout.activity_chooseui);
        String titleChooseUI = "Please select a UI theme";
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        welcomeMessage.setText(titleChooseUI);
        Button themeButton1 = findViewById(R.id.button2);
        themeButton1.setOnClickListener(this);
        Button themeButton2 = findViewById(R.id.button3);
        themeButton2.setOnClickListener(this);
        Button themeButton3 = findViewById(R.id.button4);
        themeButton3.setOnClickListener(this);
        Button themeButton4 = findViewById(R.id.button5);
        themeButton4.setOnClickListener(this);
        backButton = findViewById(R.id.button);
        backButton.setOnClickListener(this);
    }

    //onClick function that deals with button presses
    @Override
    public void onClick(View view) {
        //switch case to deal with different button presses
        String themeOnclick = "theme1";
        switch (view.getId()) {
            case R.id.button:
                //Takes the user back to MainActivity page
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                //Selects theme1 as the user favorite and applies it and saves it to user profile
                userSelectedTheme = "theme1";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
            case R.id.button3:
                //Selects theme2 as the user favorite and applies it and saves it to user profile
                userSelectedTheme = "theme2";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
            case R.id.button4:
                //Selects theme3 as the user favorite and applies it and saves it to user profile
                userSelectedTheme = "theme3";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
            case R.id.button5:
                //Selects theme4 as the user favorite and applies it and saves it to user profile
                userSelectedTheme = "theme4";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
        }
    }

    //Saves the selected theme to their user profile
    private void saveUserThemeInFirestore(String theme) {
        //sets backButton to be off until it finishes saving user theme, otherwise mainactivity page wont reflect theme changes
        backButton.setEnabled(false);
        //instantiates the firebase user profile
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //creates/updates a user theme field for the firebase user profile
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //if theme field already exists -> update it
                    if (document.exists()) {
                        userDocRef.update("userTheme", theme).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Log.d("Firestore", "User theme successfully updated.");
                            } else {
                                Log.e("Firestore", "Error updating user theme", updateTask.getException());
                            }
                            //enable backButton now that the profile is updated
                            backButton.setEnabled(true);
                        });
                    //if theme field didnt exist -> create it
                    } else {
                        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
                        Map<String, Object> data = new HashMap<>();
                        data.put("userTheme", theme);
                        data.put("locations", geoPoints);
                        userDocRef.set(data).addOnCompleteListener(createTask -> {
//                        userDocRef.set(Collections.singletonMap("userTheme", theme)).addOnCompleteListener(createTask -> {
                            if (createTask.isSuccessful()) {
                                Log.d("Firestore", "User theme successfully set.");
                            } else {
                                Log.e("Firestore", "Error setting user theme", createTask.getException());
                            }
                            backButton.setEnabled(true);
                        });
                    }
                } else {
                    Log.e("Firestore", "Error getting user document", task.getException());
                    backButton.setEnabled(true);
                }
            });
        } else {
            Log.e("Firestore", "User not logged in");
            backButton.setEnabled(true);
        }
    }

    //saveInstanceState to allow app to restore itself to a previous state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userSelectedTheme", userSelectedTheme);
    }
}
