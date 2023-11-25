package edu.uiuc.cs427app;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingResource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddRemoveTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    //Allows time for UI to be setup
    @Before
    public void setUp() {
        IdlingPolicies.setMasterPolicyTimeout(20, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(20, TimeUnit.SECONDS);
    }
    @Test
    public void addRemoveTest() {
        // type in credentials and login
        onView(withId(R.id.username)).perform(typeText("add_remove_test"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("add_remove_test"), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());

        // click on the locations drop down
        IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource1);
        onView(withId(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource1);

        // assert that all list items (just "Select a City") are present, and click on select a city
        IdlingResource idlingResource2 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource2);
        List<String> expectedEmptyList = Arrays.asList("Select a City");
        for (String item : expectedEmptyList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Select a City")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource2);

        // click on add location button
        IdlingResource idlingResource3 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource3);
        onView(withId(R.id.buttonAddLocation)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource3);

        // type in chicago, click add button, and then click back button
        // type chicago
        IdlingResource idlingResource4 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource4);
        onView(withId(R.id.nameInput)).perform(typeText("Chicago"), closeSoftKeyboard());
        Espresso.unregisterIdlingResources(idlingResource4);
        // add
//        IdlingResource idlingResource5 = new ElapsedTimeIdlingResource(20);
//        Espresso.registerIdlingResources(idlingResource5);
        onView(withId(R.id.button)).perform(click());
//        Espresso.unregisterIdlingResources(idlingResource5);
        // back
        IdlingResource idlingResource6 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource6);
        onView(withId(R.id.button9)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource6);

        // click on location drop down
//        IdlingResource idlingResource7 = new ElapsedTimeIdlingResource(20);
//        Espresso.registerIdlingResources(idlingResource7);
        onView(withId(R.id.spinner)).perform(click());
//        Espresso.unregisterIdlingResources(idlingResource7);

        // assert that all locations are present and select chicago
        IdlingResource idlingResource8 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource8);
        List<String> expectedChicagoList = Arrays.asList("Select a City", "Chicago");
        for (String item : expectedChicagoList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Chicago")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource8);

        // click on delete for chicago
        IdlingResource idlingResource9 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource9);
        onView(withId(R.id.buttonRemoveLocation)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource9);

        // click on locations drop down
        IdlingResource idlingResource10 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource10);
        onView(withId(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource10);

        // assert that the list items are correct (no chicago) and click on select a city
        IdlingResource idlingResource11 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource11);
        for (String item : expectedEmptyList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Select a City")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource11);

        // click on add location and add Urbana and Champaign
//        IdlingResource idlingResource12 = new ElapsedTimeIdlingResource(20);
//        Espresso.registerIdlingResources(idlingResource12);
        onView(withId(R.id.buttonAddLocation)).perform(click());
//        Espresso.unregisterIdlingResources(idlingResource12);
        // add urbana
//        IdlingResource idlingResource13 = new ElapsedTimeIdlingResource(20);
//        Espresso.registerIdlingResources(idlingResource13);
        onView(withId(R.id.nameInput)).perform(typeText("Urbana"), closeSoftKeyboard());
//        Espresso.unregisterIdlingResources(idlingResource13);
//        IdlingResource idlingResource14 = new ElapsedTimeIdlingResource(20);
//        Espresso.registerIdlingResources(idlingResource14);
        onView(withId(R.id.button)).perform(click());
//        Espresso.unregisterIdlingResources(idlingResource14);
        // add champaign
        IdlingResource idlingResource15 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource15);
        onView(withId(R.id.nameInput)).perform(replaceText("Champaign"), closeSoftKeyboard());
        Espresso.unregisterIdlingResources(idlingResource15);
        IdlingResource idlingResource16 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource16);
        onView(withId(R.id.button)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource16);
        // go back
        IdlingResource idlingResource17 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource17);
        onView(withId(R.id.button9)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource17);

        // click on the locations drop down
        IdlingResource idlingResource18 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource18);
        onView(withId(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource18);

        // check that all list items are there and click on urbana
        IdlingResource idlingResource19 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource19);
        List<String> expectedUrbanaChampaignList = Arrays.asList("Select a City", "Urbana", "Champaign");
        for (String item : expectedUrbanaChampaignList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Urbana")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource19);

        // remove urbana
        IdlingResource idlingResource20 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource20);
        onView(withId(R.id.buttonRemoveLocation)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource20);

        // show list items
        IdlingResource idlingResource21 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource21);
        onView(withId(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource21);

        // assert that only urbana was removed and click on champaign
        IdlingResource idlingResource22 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource22);
        List<String> expectedChampaignList = Arrays.asList("Select a City", "Champaign");
        for (String item : expectedChampaignList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Champaign")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource22);

        // remove champaign
        IdlingResource idlingResource23 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource23);
        onView(withId(R.id.buttonRemoveLocation)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource23);

        // show list
        IdlingResource idlingResource24 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource24);
        onView(withId(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource24);

        // assert that champaign was removed, should just be the prompt now. Click on select a city
        IdlingResource idlingResource25 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource25);
        for (String item : expectedEmptyList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Select a City")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource25);

        // try to remove select a city
        IdlingResource idlingResource26 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource26);
        onView(withId(R.id.buttonRemoveLocation)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource26);

        // show list
        IdlingResource idlingResource27 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource27);
        onView(withId(R.id.spinner)).perform(click());
        Espresso.unregisterIdlingResources(idlingResource27);

        // assert that select a city was NOT removed, and click on it
        IdlingResource idlingResource28 = new ElapsedTimeIdlingResource(20);
        Espresso.registerIdlingResources(idlingResource28);
        for (String item : expectedEmptyList) {
            onData(allOf(is(instanceOf(String.class)), is(item))).inRoot(isPlatformPopup()).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                assertThat(view, notNullValue());
            });
        }
        onData(allOf(is(instanceOf(String.class)), is("Select a City")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.unregisterIdlingResources(idlingResource28);
    }
}