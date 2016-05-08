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

    public final static String PLAYER_NAME_KEY = "playername";
    public final static String PLAYER_NAME_DEFAULT = "unknownuser";
    public final static String PLAYER_ID_KEY = "playerid";
    public final static String GAME_ID_DEFAULT = "77";
    public final static String GAME_ID_KEY = "gameid";
    public final static String PARTIDA_ID_DEFAULT = "partidaid";
    public final static String PARTIDA_ID_KEY = "roundid";
    public final static String GCM_ID_DEFAULT = "gcmregid";
    public final static String GCM_ID_KEY = "gcmregid";
    public final static String PLAYER_ID_DEFAULT = "playerid";
    public final static String PLAYER_PASSWORD_KEY = "playerpassword";
    public final static String PLAYER_PASSWORD_DEFAULT = "pass";
    public final static String FIGURE_NAME_KEY = "figurename";
    public final static String FIGURE_NAME_DEFAULT = "completo";
    public final static String FIGURE_CODE_KEY = "figurecode";
    public final static String FIGURE_CODE_DEFAULT =
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
    public static void setFigureCode(Context context, String figurecode) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.FIGURE_CODE_KEY, figurecode);
        editor.apply();
    }
    public static String getPlayerName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYER_NAME_KEY, PLAYER_NAME_DEFAULT);
    }
    public static String getGCMid(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GCM_ID_KEY, GCM_ID_DEFAULT);
    }
    public static void setGCMId(Context login, String gcmid) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.GCM_ID_KEY, gcmid);
        editor.apply();
    }
    public static String getGameId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GAME_ID_KEY, GAME_ID_DEFAULT);
    }
    public static void setGameId(Context login, String gameid) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.GAME_ID_KEY, gameid);
        editor.apply();
    }
    public static String getPartidaId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PARTIDA_ID_KEY, PARTIDA_ID_DEFAULT);
    }
    public static void setPartidaId(Context login, String partidaid) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.PARTIDA_ID_KEY, partidaid);
        editor.apply();
    }
    public static void setPlayerName(Context login, String username) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.PLAYER_NAME_KEY, username);
        editor.apply();
    }
    public static void setPlayerId(Context login, String playerid) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.PLAYER_ID_KEY, playerid);
        editor.apply();
    }
    public static String getPlayerId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYER_ID_KEY, PLAYER_ID_DEFAULT);
    }
    public static void setPlayerPass(Context login, String pass) {
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(C3Preference.PLAYER_PASSWORD_KEY, pass);
        editor.apply();
    }
    public static String getPlayerPassword(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYER_PASSWORD_KEY, PLAYER_PASSWORD_DEFAULT);
    }
    public static void setPlayerPassword(Context login, String password) {

    }


}