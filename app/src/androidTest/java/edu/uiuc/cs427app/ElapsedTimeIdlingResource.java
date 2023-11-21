package edu.uiuc.cs427app;

import androidx.test.espresso.IdlingResource;

//public class for adding idling time in between test steps to allow loading time
public class ElapsedTimeIdlingResource implements IdlingResource {

    private final long startTime;
    private final long waitingTime;
    private ResourceCallback resourceCallback;

    //Constructor
    public ElapsedTimeIdlingResource(long waitingTime) {
        this.startTime = System.currentTimeMillis();
        this.waitingTime = waitingTime;
    }

    //get name of the resource
    @Override
    public String getName() {
        return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
    }

    //checks whether elapsed time since the creation of idling resource is > waiting time, if idle it notifies Espresso
    @Override
    public boolean isIdleNow() {
        long elapsed = System.currentTimeMillis() - startTime;
        boolean idle = (elapsed >= waitingTime);
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    //notifies when resource becomes idle
    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
