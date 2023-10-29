package edu.uiuc.cs427app;

import android.content.Intent;
import android.nfc.Tag;
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
import java.util.Collections;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth auth;
    FirebaseUser user;
    String userTheme;
    private static int themeId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("DetailsActivity","onCreate");
        //super.onCreate(savedInstanceState);
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
                            switch (userTheme) {
                                case "theme1":
                                    Log.i("changeTheme","theme1");
                                    themeId = R.style.Theme_MyFirstApp;
                                    break;
                                case "theme2":
                                    Log.i("changeTheme","theme2");
                                    themeId = R.style.Theme_MyFirstApp2;
                                    break;
                                case "theme3":
                                    Log.i("changeTheme","theme3");
                                    themeId =  R.style.Theme_MyFirstApp3;
                                    break;
                                case "theme4":
                                    Log.i("changeTheme","theme4");
                                    themeId =  R.style.Theme_MyFirstApp4;
                                    break;
                                default:
                                    Log.i("changeTheme","default");
                                    themeId =  R.style.Theme_MyFirstApp;
                                    break;
                            }
                            this.setTheme(themeId);
                            Log.i("DetailssetTheme",userTheme);
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
    }

    private void setupUI() {
        setContentView(R.layout.activity_details);
        String cityName = getIntent().getStringExtra("city").toString();
        String welcome = "Welcome to the "+cityName;
        String cityWeatherInfo = "Detailed information about the weather of "+cityName;
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        TextView cityInfoMessage = findViewById(R.id.cityInfo);
        welcomeMessage.setText(welcome);
        cityInfoMessage.setText(cityWeatherInfo);
        Button buttonMap = findViewById(R.id.mapButton);
        buttonMap.setOnClickListener(this);
    }

    public void retrieveUserThemeAndSet() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userTheme = document.getString("userTheme");
                        if (userTheme != null) {
                            Log.i("RetrievedUser",userTheme);
                            changeTheme.setTheme(this, userTheme);
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
    }
    @Override
    public void onClick(View view) {
        //Implement this (create an Intent that goes to a new Activity, which shows the map)
    }
}

