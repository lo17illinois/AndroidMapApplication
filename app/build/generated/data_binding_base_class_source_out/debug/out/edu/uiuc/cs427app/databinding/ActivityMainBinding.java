// Generated by view binder compiler. Do not edit!
package edu.uiuc.cs427app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
  public final Button buttonAddLocation;

  @NonNull
  public final Button buttonRemoveLocation;

  @NonNull
  public final ImageButton settingsButton;

  @NonNull
  public final Spinner spinner;

  @NonNull
  public final TextView textView3;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button buttonAddLocation,
      @NonNull Button buttonRemoveLocation, @NonNull ImageButton settingsButton,
      @NonNull Spinner spinner, @NonNull TextView textView3) {
    this.rootView = rootView;
    this.buttonAddLocation = buttonAddLocation;
    this.buttonRemoveLocation = buttonRemoveLocation;
    this.settingsButton = settingsButton;
    this.spinner = spinner;
    this.textView3 = textView3;
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
      id = R.id.buttonAddLocation;
      Button buttonAddLocation = ViewBindings.findChildViewById(rootView, id);
      if (buttonAddLocation == null) {
        break missingId;
      }

      id = R.id.buttonRemoveLocation;
      Button buttonRemoveLocation = ViewBindings.findChildViewById(rootView, id);
      if (buttonRemoveLocation == null) {
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

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, buttonAddLocation,
          buttonRemoveLocation, settingsButton, spinner, textView3);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
