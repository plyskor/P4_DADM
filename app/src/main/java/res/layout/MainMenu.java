package res.layout;

import android.content.Intent;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.ComponentName;
import android.os.IBinder;
import android.content.Context;
import android.preference.Preference;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.R;
import com.example.jose.connect3.MusicService;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainMenu extends AppCompatActivity {
    public static boolean mIsBound = false;
    private MusicService mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (!MainMenu.mIsBound) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);

        }

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals(C3Preference.PLAY_MUSIC_KEY)){
                    Boolean play = false;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (sharedPreferences.contains(C3Preference.PLAY_MUSIC_KEY))
                        play = sharedPreferences.getBoolean(C3Preference.PLAY_MUSIC_KEY,
                                C3Preference.PLAY_MUSIC_DEFAULT);
                    if (play == true)
                    {if (!mIsBound){
                        doBindService();
                    }
                        mServ.resumeMusic();
                    }else{
                        mServ.pauseMusic();
                    }
                }
                if(key.equals(C3Preference.PLAYER_NAME_KEY)){


                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

    }

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        MainMenu.mIsBound = true;
    }

    void doUnbindService() {
        if (MainMenu.mIsBound) {
            unbindService(Scon);
            MainMenu.mIsBound = false;
        }
    }

    protected void onPause() {
        super.onPause();





    }

    public void startLogin(View view) {

            Intent intent = new Intent("android.intent.action.C3.LOGIN");
            startActivity(intent);

    }

    public void startRatings(View view) {
        Intent intent = new Intent("android.intent.action.C3.RATINGS");
        startActivity(intent);
    }

    public void startTopTen(View view) {
        Intent intent = new Intent("android.intent.action.C3.TOPTEN");
        startActivity(intent);
    }
    public void startGame(View view) {
        String aux=C3Preference.getPlayerName(this);
        if(aux.equals("unknownuser")){
            Toast.makeText(this, "Inicia Sesión para jugar",
                    Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent("android.intent.action.C3.MAINACTIVITY");
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    public void onWindowFocusChanged(boolean winFocus) {
        super.onWindowFocusChanged(winFocus);
        if (winFocus) {
            if (!mIsBound) {
                doBindService();

        }}else{
                doUnbindService();
            }
    }
    public void onRestart(){
        super.onRestart();
        if(!mIsBound){
            doBindService();
        }
    }
        public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.menuPreferences:
                startActivity(new Intent(this, C3Preference.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        if(!mIsBound){
            doBindService();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        if (isFinishing()) {
                Intent music = new Intent();
                music.setClass(this, MusicService.class);
                mServ.stopService(music);
        }
    }
}