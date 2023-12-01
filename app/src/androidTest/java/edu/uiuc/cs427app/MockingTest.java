package edu.uiuc.cs427app;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.widget.Spinner;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

//Tests the ShowMap functionality by showing two cities and asserts that the correct city and coordinates are shown accordingly
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MockingTest {

    //Launches the LoginActivity at start of test
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    //Allows time for UI to be setup
    @Before
    public void setUp() {
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(60, TimeUnit.SECONDS);
    }

    //Test steps
    @Test
    public void loginActivityTest() {
        //An existing user account/password just for the sake of login
        String rightUsername = "milestone5test3";
        String rightPassword = "milestone5test3";

        // Input the existing user account/password into their respective fields in the Login page
        onView(withId(R.id.username))
                .perform(typeText(rightUsername), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(rightPassword), closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.button_login)).perform(click());
        // Wait for some time
        IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(300); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource1);
        // Click the spinner
        onView(withSpinner(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource1);
        // Wait for some time
        IdlingResource idlingResource2 = new ElapsedTimeIdlingResource(15); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource2);
        // Select the spinner entry with "Chicago"
        onData(allOf(is(instanceOf(String.class)), is("Champaign")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource2);
        // Wait for some time
        IdlingResource idlingResource3 = new ElapsedTimeIdlingResource(15); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource3);
        // Click the button ViewWeather
        onView(withId(R.id.buttonViewWeather)).perform(click());
        // Wait for some time
        IdlingResource idlingResource4 = new ElapsedTimeIdlingResource(150); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource4);
        Espresso.unregisterIdlingResources(idlingResource3);
        // Assertion that the R.id.textViewCityName displays "Chicago"
        onView(withId(R.id.textViewCityName))
                .check(matches(withText("Champaign")));
        // Press back (to MainActivity page)
        pressBackUnconditionally();
        Espresso.unregisterIdlingResources(idlingResource4);
        // Wait for some time
        IdlingResource idlingResource15 = new ElapsedTimeIdlingResource(50); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource15);
        // Press logout (to LoginActivity page)
        onView(withId(R.id.logout)).perform(click());
        // Wait for some time
        IdlingResource idlingResource16 = new ElapsedTimeIdlingResource(100); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource16);
        Espresso.unregisterIdlingResources(idlingResource15);
        Espresso.unregisterIdlingResources(idlingResource16);
    }

    //A custom matcher for identifying the spinner
    public static Matcher<View> withSpinner(final int spinnerId) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return item.getId() == spinnerId && item instanceof Spinner;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + spinnerId + " and is a Spinner");
            }
        };
    }
}
