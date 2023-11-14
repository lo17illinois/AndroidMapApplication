package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SignupActivitySuccessfulTest {
    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test.
     */
    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule = new ActivityScenarioRule<>(RegisterActivity.class);
    private FirebaseAuth auth;
    @Before
    public void setUp() {
        auth = FirebaseAuth.getInstance();
    }


    @Test
    public void signUpSuccessTest() throws InterruptedException {
        // Prepare
        String rightUsername = UUID.randomUUID().toString();
        String rightPassword = "123456";
        String rightEmail = rightUsername + "@example.com";


        onView(withId(R.id.username))
                .perform(typeText(rightUsername), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(rightPassword), closeSoftKeyboard());


        // Act
        onView(withId(R.id.button_register)).perform(click());
        TimeUnit.SECONDS.sleep(1);

        // Assert
        auth.signInWithEmailAndPassword(rightEmail, rightPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Assert.assertTrue(task.isSuccessful());
            }
        });

    }

}