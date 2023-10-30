

package edu.uiuc.cs427app;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class changeTheme {
    private static int themeId;
    public static String localTheme;

    //a public function that other activity classes call
    //it sets the UI theme of the current activity page to the provided themeName
    public static void setTheme(Activity activity, String themeName) {
        //makes sure themeName is not null
        if (themeName == null){
            themeName = "theme1";
        }
        localTheme = themeName;
        switch (themeName) {
            //set UI theme to theme1
            case "theme1":
                themeId = R.style.Theme_MyFirstApp;
                break;
            //set UI theme to theme2
            case "theme2":
                themeId = R.style.Theme_MyFirstApp2;
                break;
            //set UI theme to theme3
            case "theme3":
                themeId =  R.style.Theme_MyFirstApp3;
                break;
            //set UI theme to theme4
            case "theme4":
                themeId =  R.style.Theme_MyFirstApp4;
                break;
            //set UI theme to default = theme1
            default:
                themeId =  R.style.Theme_MyFirstApp;
                break;
        }
        activity.setTheme(themeId);
        Log.i("changeTheme_ThemeChanged",themeName);
    }

    //a public function that ChooseUIDisplayActivity calls
    //it resets the current activity page to reflect any changes to UI theme selections
    public static void changetoTheme(Activity activity, String themeName){
        //makes sure themeName is not null
        if (themeName == null){
            themeName = "theme1";
        }
        localTheme = themeName;
        //sets UI theme
        changeTheme themeChanger = new changeTheme();
        themeChanger.setTheme(activity, themeName); // Call the non-static method using an instance
        //restarts the activity
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

}
