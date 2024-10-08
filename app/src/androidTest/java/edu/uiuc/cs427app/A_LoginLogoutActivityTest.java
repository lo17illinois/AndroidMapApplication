package edu.uiuc.cs427app;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

//Tests the Logout functionality
@LargeTest
@RunWith(AndroidJUnit4.class)
public class A_LoginLogoutActivityTest {

    //Launches the LoginActivity at start of test
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    private FirebaseAuth auth;

    //Allows time for UI to be setup
    @Before
    public void setUp() {
        FirebaseAuth.getInstance().signOut();
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(60, TimeUnit.SECONDS);
        auth = FirebaseAuth.getInstance();
    }

    //Test steps
    @Test
    public void logoutTest() {
        //An existing user account/password just for the sake of login
        String rightUsername = "hadilhelaly";
        String rightPassword = "hadil00";
        String rightEmail = rightUsername + "@example.com";

        // Input the existing user account/password into their respective fields in the Login page
        onView(withId(R.id.username))
                .perform(typeText(rightUsername), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(rightPassword), closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.button_login)).perform(click());

        // Wait for some time
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Assert
        auth.signInWithEmailAndPassword(rightEmail, rightPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println(task.getResult().getUser());
                        Assert.assertTrue(task.isSuccessful());

                    }
                });

        // Assertion that the logout button is displayed at current page
        onView(withId(R.id.logout)).check(matches(isDisplayed()));

        // Wait for some time
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        // Press logout (to LoginActivity page)
        onView(withId(R.id.logout)).perform(click());

        // Wait for some time
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Assertion that the login message is displayed at current page
        onView(withId(R.id.loginText)).check(matches(isDisplayed()));
        // Assertion that current user is logged out
        final FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // User has been successfully signed out
                    Assert.assertTrue(true); // Assertion for successful logout
                } else {
                    Assert.fail(); // Assertion fails if the user is still logged in
                }
            }
        };

        auth.getInstance().addAuthStateListener(authStateListener);

        // Wait for some time
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Unregister the AuthStateListener to avoid interference with subsequent tests
        auth.getInstance().removeAuthStateListener(authStateListener);
        // Wait for some time
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}