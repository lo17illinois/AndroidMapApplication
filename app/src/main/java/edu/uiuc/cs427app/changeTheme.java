

package edu.uiuc.cs427app;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class changeTheme {
    private static int themeId;
    public static String localTheme;
    public static void setTheme(Activity activity, String themeName) {
        if (themeName == null){
            themeName = "theme1";
        }
        localTheme = themeName;
        switch (themeName) {
            case "theme1":
                themeId = R.style.Theme_MyFirstApp;
                break;
            case "theme2":
                themeId = R.style.Theme_MyFirstApp2;
                break;
            case "theme3":
                themeId =  R.style.Theme_MyFirstApp3;
                break;
            case "theme4":
                themeId =  R.style.Theme_MyFirstApp4;
                break;
            default:
                themeId =  R.style.Theme_MyFirstApp;
                break;
        }
        activity.setTheme(themeId);
        Log.i("changeTheme_ThemeChanged",themeName);
    }
    public static void changetoTheme(Activity activity, String themeName){
        if (themeName == null){
            themeName = "theme1";
        }
        localTheme = themeName;
        changeTheme themeChanger = new changeTheme();
        themeChanger.setTheme(activity, themeName); // Call the non-static method using an instance
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

}
