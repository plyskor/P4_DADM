package res.layout;

import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.widget.Toast;

import java.io.IOException;


public class MainMenu extends AppCompatActivity {
    /**
     * Este es el numero de proyecto. Ya esta creado y debes usar exactamente el numero
     *  indicado aqui abajo
     */
    String SENDERIDGCM = "125442492416";
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static boolean mIsBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (checkPlayServices()){
           // Log.d("jose","HOLI");
// If id de registro (guardado en prefs) está vacío o si ha cambiado // de versión la app se llama a registrarse
            if ( C3Preference.getGCMid(this).equals("gcmregid") ) {
              //  Log.d("jose","HOLI");
                registerInBackground();
            }
        }
    }




    protected void onPause() {
        super.onPause();





    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("ERROR", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true; }

    public void startLogin(View view) {
            Intent intent = new Intent("android.intent.action.C3.LOGIN");
            startActivity(intent);
    }
    public void startHelp(View view) {
        Intent intent = new Intent("android.intent.action.C3.ABOUT");
        startActivity(intent);
    }
    public void startRatings(View view) {
        String aux=C3Preference.getPlayerId(this);
        if(aux.equals(C3Preference.PLAYER_ID_DEFAULT)){
            Toast.makeText(this, "Inicia Sesión para ver las puntuaciones",
                    Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent("android.intent.action.C3.RATINGS");
            startActivity(intent);
        }
    }


    public void startGame(View view) {
        String aux=C3Preference.getPlayerId(this);
        if(aux.equals(C3Preference.PLAYER_ID_DEFAULT)){
            Toast.makeText(this, "Inicia Sesión para jugar",
                    Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent("android.intent.action.C3.PLAY");
            startActivity(intent);
        }
    }

    /**
     * Registramos la aplicación en los servidores GCM asincronamente.
     *
     * Guardamos la información del id en preferencias. También de la versión
     *   de la app
     */
    private void registerInBackground() { new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            String id = "";
            try {

                GoogleCloudMessaging gcm = GoogleCloudMessaging .getInstance(MainMenu.this);
                // Nos registramos en los servidores de GCM

                id = gcm.register(SENDERIDGCM);
                // Este id hay que guardarlo de forma persistente
                //   Guardamos el id en las preferencias junto con
                //   la versión de la app

               C3Preference.setGCMId(MainMenu.this,id);

            } catch (IOException ex) {
                Log.d("GCM REGISTER:", "Error registro en GCM:" + ex.getMessage());
            }
            return id; }
    }.execute(null, null, null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
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




    }
