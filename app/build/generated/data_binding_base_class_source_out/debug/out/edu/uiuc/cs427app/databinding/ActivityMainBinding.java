// Generated by view binder compiler. Do not edit!
package edu.uiuc.cs427app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import edu.uiuc.cs427app.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button button6;

  @NonNull
  public final Button buttonAddLocation;

  @NonNull
  public final ImageButton buttonRemoveLocation;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final Button logout;

  @NonNull
  public final ImageButton settingsButton;

  @NonNull
  public final Spinner spinner;

  @NonNull
  public final TextView textView15;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView userDetails;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button button6,
      @NonNull Button buttonAddLocation, @NonNull ImageButton buttonRemoveLocation,
      @NonNull LinearLayout linearLayout, @NonNull Button logout,
      @NonNull ImageButton settingsButton, @NonNull Spinner spinner, @NonNull TextView textView15,
      @NonNull TextView textView3, @NonNull TextView userDetails) {
    this.rootView = rootView;
    this.button6 = button6;
    this.buttonAddLocation = buttonAddLocation;
    this.buttonRemoveLocation = buttonRemoveLocation;
    this.linearLayout = linearLayout;
    this.logout = logout;
    this.settingsButton = settingsButton;
    this.spinner = spinner;
    this.textView15 = textView15;
    this.textView3 = textView3;
    this.userDetails = userDetails;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button6;
      Button button6 = ViewBindings.findChildViewById(rootView, id);
      if (button6 == null) {
        break missingId;
      }

      id = R.id.buttonAddLocation;
      Button buttonAddLocation = ViewBindings.findChildViewById(rootView, id);
      if (buttonAddLocation == null) {
        break missingId;
      }

      id = R.id.buttonRemoveLocation;
      ImageButton buttonRemoveLocation = ViewBindings.findChildViewById(rootView, id);
      if (buttonRemoveLocation == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.logout;
      Button logout = ViewBindings.findChildViewById(rootView, id);
      if (logout == null) {
        break missingId;
      }

      id = R.id.settingsButton;
      ImageButton settingsButton = ViewBindings.findChildViewById(rootView, id);
      if (settingsButton == null) {
        break missingId;
      }

      id = R.id.spinner;
      Spinner spinner = ViewBindings.findChildViewById(rootView, id);
      if (spinner == null) {
        break missingId;
      }

      id = R.id.textView15;
      TextView textView15 = ViewBindings.findChildViewById(rootView, id);
      if (textView15 == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.user_details;
      TextView userDetails = ViewBindings.findChildViewById(rootView, id);
      if (userDetails == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, button6, buttonAddLocation,
          buttonRemoveLocation, linearLayout, logout, settingsButton, spinner, textView15,
          textView3, userDetails);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
