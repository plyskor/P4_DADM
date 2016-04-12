package com.example.jose.connect3;
import android.app.Activity;
import android.app.FragmentManager;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.CompoundButton;
import android.os.Bundle;
import android.widget.CheckBox;
import android.content.SharedPreferences;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import res.layout.Login;

public class C3Preference extends Activity {
    public final static String PLAY_MUSIC_KEY = "music";
    public final static boolean PLAY_MUSIC_DEFAULT = true;
    public final static String PLAYER_NAME_KEY = "playername"; public final static String PLAYER_NAME_DEFAULT = "unknownuser"; public final static String FIGURE_NAME_KEY = "figurename"; public final static String FIGURE_NAME_DEFAULT = "completo"; public final static String FIGURE_CODE_KEY = "figurecode"; public final static String FIGURE_CODE_DEFAULT =
            "0011100001110011111111110111111111100111000011100";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_void);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        C3PreferenceFragment fragment = new C3PreferenceFragment(); fragmentTransaction.replace(android.R.id.content, fragment); fragmentTransaction.commit();


    }
    public static String getFigureName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(FIGURE_NAME_KEY, FIGURE_NAME_DEFAULT);
    }
    public static void setFigureName(Context context, String figurename) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit(); editor.putString(C3Preference.FIGURE_NAME_KEY, figurename); editor.apply();
    }
    public static String getFigureCode(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FIGURE_CODE_KEY, FIGURE_CODE_DEFAULT);
    }
    public static String getPlayerName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYER_NAME_KEY, "");
    }
    public static void setFigureCode(Context context, String figurecode) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.FIGURE_CODE_KEY, figurecode);
        editor.apply();
    }

    public static void setPlayerName(Context login, String username) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.PLAYER_NAME_KEY, username);
        editor.apply();
    }

    public static void setPlayerPassword(Context login, String password) {
    }


}