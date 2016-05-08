package res.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;

import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.JugadorHumano;

public class Board extends AppCompatActivity {
    protected JugadorAleatorio jugadorAleatorio = new JugadorAleatorio("Maquina");
    protected JugadorHumano jugadorHumano ;
    protected ArrayList<Jugador> jugadores = new ArrayList<>();
    private final int ids []={R.id.button_0,R.id.button_1,R.id.button_2,R.id.button_3,
            R.id.button_4,R.id.button_5,R.id.button_6,R.id.button_7,R.id.button_8};
    private TextView roundidtextview;
    private boolean joined=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        jugadorHumano= new JugadorHumano(C3Preference.getPlayerName(this));
        roundidtextview=(TextView)findViewById(R.id.roundIdTextView);
        roundidtextview.setText("ID Partida:"+C3Preference.getPartidaId(this));
    }

    public static void joinedGame(Intent intent) {
        
    }
}
