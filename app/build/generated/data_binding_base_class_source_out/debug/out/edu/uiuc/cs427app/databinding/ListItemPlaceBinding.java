// Generated by view binder compiler. Do not edit!
package edu.uiuc.cs427app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import edu.uiuc.cs427app.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListItemPlaceBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView txtLongAddress;

  @NonNull
  public final TextView txtShortAddress;

  private ListItemPlaceBinding(@NonNull LinearLayout rootView, @NonNull TextView txtLongAddress,
      @NonNull TextView txtShortAddress) {
    this.rootView = rootView;
    this.txtLongAddress = txtLongAddress;
    this.txtShortAddress = txtShortAddress;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItemPlaceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItemPlaceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item_place, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItemPlaceBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.txtLongAddress;
      TextView txtLongAddress = ViewBindings.findChildViewById(rootView, id);
      if (txtLongAddress == null) {
        break missingId;
      }

      id = R.id.txtShortAddress;
      TextView txtShortAddress = ViewBindings.findChildViewById(rootView, id);
      if (txtShortAddress == null) {
        break missingId;
      }

      return new ListItemPlaceBinding((LinearLayout) rootView, txtLongAddress, txtShortAddress);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
