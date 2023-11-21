package edu.uiuc.cs427app;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Spinner;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
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
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingResource;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

//Tests the ShowMap functionality by showing two cities and asserts that the correct city and coordinates are shown accordingly
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

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
        String rightUsername = "milestone5test1";
        String rightPassword = "milestone5test1";

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
        IdlingResource idlingResource2 = new ElapsedTimeIdlingResource(100); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource2);

        // Select the spinner entry with "Chicago"
        onData(allOf(is(instanceOf(String.class)), is("Chicago")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource2);

        // Wait for some time
        IdlingResource idlingResource3 = new ElapsedTimeIdlingResource(150); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource3);

        // Click the button ViewMap
        onView(withId(R.id.buttonViewMap)).perform(click());

        // Wait for some time
        IdlingResource idlingResource4 = new ElapsedTimeIdlingResource(200); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource4);
        Espresso.unregisterIdlingResources(idlingResource3);

        // Assertion that the R.id.location_name displays "Chicago"
        onView(withId(R.id.location_name))
                .check(matches(withText("Chicago")));

        // Assertion that the R.id.locationGeoCoordinate displays the correct coordinates for Chicago
        onView(withId(R.id.locationGeoCoordinate))
                .check(matches(withText("41.8781136, -87.6297982")));

        // Press back (to MainActivity page)
        pressBackUnconditionally();

        // Click the spinner
        onView(withSpinner(R.id.spinner)).perform(click());

        // Wait for some time
        IdlingResource idlingResource5 = new ElapsedTimeIdlingResource(100); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource5);
        Espresso.unregisterIdlingResources(idlingResource4);

        // Select the spinner entry with "New York"
        onData(allOf(is(instanceOf(String.class)), is("New York")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource5);

        // Wait for some time
        IdlingResource idlingResource6 = new ElapsedTimeIdlingResource(150); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource6);

        // Click the button ViewMap
        onView(withId(R.id.buttonViewMap)).perform(click());

        // Wait for some time
        IdlingResource idlingResource7 = new ElapsedTimeIdlingResource(200); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource7);
        Espresso.unregisterIdlingResources(idlingResource6);

        // Assertion that the R.id.location_name displays "New York"
        onView(withId(R.id.location_name))
                .check(matches(withText("New York")));

        // Assertion that the R.id.locationGeoCoordinate displays the correct coordinates for New York
        onView(withId(R.id.locationGeoCoordinate))
                .check(matches(withText("40.7127753, -74.0059728")));

        // Press back (to MainActivity page)
        pressBackUnconditionally();
        Espresso.unregisterIdlingResources(idlingResource7);

        // Wait for some time
        IdlingResource idlingResource8 = new ElapsedTimeIdlingResource(150); // Adjust the waiting time as needed
        Espresso.registerIdlingResources(idlingResource8);
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
