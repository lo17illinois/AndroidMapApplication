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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Log.i("onCreateIf",userSelectedTheme);
            userSelectedTheme = savedInstanceState.getString("userSelectedTheme", userSelectedTheme);
        } else {
            Log.i("onCreateElse",userSelectedTheme);
            userSelectedTheme = changeTheme.localTheme;
            changeTheme.setTheme(this, userSelectedTheme);
        }
        //super.onCreate(savedInstanceState);
        setupUI();
    }


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


    @Override
    public void onClick(View view) {
        String themeOnclick = "theme1";
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                userSelectedTheme = "theme1";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
            case R.id.button3:
                userSelectedTheme = "theme2";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
            case R.id.button4:
                userSelectedTheme = "theme3";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
            case R.id.button5:
                userSelectedTheme = "theme4";
                changeTheme.changetoTheme(this, userSelectedTheme);
                saveUserThemeInFirestore(userSelectedTheme);
                break;
        }
    }


    private void saveUserThemeInFirestore(String theme) {
        backButton.setEnabled(false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userDocRef.update("userTheme", theme).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Log.d("Firestore", "User theme successfully updated.");
                            } else {
                                Log.e("Firestore", "Error updating user theme", updateTask.getException());
                            }
                            backButton.setEnabled(true);
                        });
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
                            userSelectedTheme = userTheme;
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


    /*
    public static void retrieveUserThemeAndSet(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //DocumentReference userDocRef = db.collection("users").document(user.getUid());
            db.collection("users").document(user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    userSelectedTheme = document.getString("userTheme");
                                } else {
                                    // Set default theme if no theme is found in Firestore
                                    userSelectedTheme = "theme1";
                                }
                            } else {
                                // Log error and set default theme if Firestore retrieval fails
                                Log.e("Firestore", "Error retrieving user theme", task.getException());
                                userSelectedTheme = "theme1";
                            }
                            // Proceed with the rest of onCreate logic
                            if (savedInstanceState != null) {
                                // If there's a saved instance state, prefer that
                                userSelectedTheme = savedInstanceState.getString("userSelectedTheme", userSelectedTheme);
                            }
                            changeTheme.setTheme(ChooseUIDisplayActivity.this, userSelectedTheme);
                            setContentView(R.layout.activity_chooseui);
                            setupUI();
                        }
                    });
        } else {
            Log.e("Firestore", "User not logged in");
            // Set default theme and proceed if the user is not logged in
            changeTheme.setTheme(this, "theme1");
            setContentView(R.layout.activity_chooseui);
            setupUI();
        }
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userSelectedTheme", userSelectedTheme);
    }
}
