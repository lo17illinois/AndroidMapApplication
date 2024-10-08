//package edu.uiuc.cs427app;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//
//public class RegisterActivity extends AppCompatActivity {
//    TextInputEditText editTextEmail, editTextPassword, editTextUsername;
//    Button buttonRegister;
//    FirebaseAuth mAuth;
//    ProgressBar progressBar;
//    TextView textView;
//    public void onStart() {
//        // Check if user is signed in (non-null) and update UI accordingly.
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        editTextEmail = findViewById(R.id.email);
//        editTextPassword = findViewById(R.id.password);
//        editTextUsername = findViewById(R.id.username);
//        buttonRegister = findViewById(R.id.button_register);
//        mAuth = FirebaseAuth.getInstance();
//        progressBar = findViewById(R.id.progressBar);
//        textView = findViewById(R.id.loginNow);
//
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email, password,username;
//                email = String.valueOf(editTextEmail.getText());
//                password = String.valueOf(editTextPassword.getText());
//                username = String.valueOf(editTextUsername.getText());
//                progressBar.setVisibility(View.VISIBLE);
//
//
//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if(TextUtils.isEmpty(password)){
//                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if(TextUtils.isEmpty(username)){
//                    Toast.makeText(RegisterActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
////                Documentation can be found at here
////                https://firebase.google.com/docs/auth/android/password-auth#java_5
//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//                                    FirebaseUser user = mAuth.getCurrentUser();
//
//                                    // Update the user's display name with the provided username
//                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
//                                    user.updateProfile(profileUpdates);
//
//
//
//                                    Toast.makeText(RegisterActivity.this, "Account created.",
//                                            Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(RegisterActivity.this, "Registration failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//    }
//}


package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText editTextUsername, editTextPassword;
    Button buttonRegister;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    public void onStart() {
        // Check if the user is signed in (non-null) and update the UI accordingly.
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonRegister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password;
                username = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                String email = username + "@example.com"; // Create a placeholder email

                // Use the placeholder email to register with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Update the user's display name with the provided username
                                    String username = String.valueOf(editTextUsername.getText());
                                    Log.d("RegisterActivity", "Username: " + username); // Add this line to print the username
                                    System.out.println(username);
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Display name updated successfully
                                                        // Proceed to the main activity
                                                        Intent intent = new Intent(getApplicationContext(), ChooseUIDisplayActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        // Handle the case where updating the display name failed
                                                        Toast.makeText(RegisterActivity.this, "Error updating display name", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
