package res.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.InterfazConServidor;
import com.example.jose.connect3.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.JugadorHumano;
import es.uam.eps.multij.JugadorRemoto;
import es.uam.eps.multij.Movimiento3Raya;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;
import es.uam.eps.multij.Tablero3Raya;

public class Board extends AppCompatActivity {
    protected JugadorRemoto jugadorRemoto ;
    protected JugadorHumano jugadorHumano ;
    protected ArrayList<Jugador> jugadores = new ArrayList<>();
    private final int ids []={R.id.button_0,R.id.button_1,R.id.button_2,R.id.button_3,
            R.id.button_4,R.id.button_5,R.id.button_6,R.id.button_7,R.id.button_8};
    private TextView roundidtextview;
    private TextView roundinfotextview;
    public static final String TIPO_UNIDO="unido";
    public static final String TIPO_HOST="host";
    private EditText messageToSend;
    private String adversario,tipo;
    public int cronoparado=1;
    public Boolean esMiTurno=false;
    private static boolean enPrimerPlano = false;
    BroadcastReceiver recibidorJOINED;
    BroadcastReceiver recibidorMOVEMENT;
    public static final String FILTRO_JOINED = "com.example.jose.connect3.JOINEDGAME";
    public static final String FILTRO_MOVEMENT = "com.example.jose.connect3.MOVEMENT";
    private Partida partida;
    private Chronometer chr;
    private Button buttonAux;
    private Response.Listener<String> listeneraddresult;
    private Response.ErrorListener errorlisteneraddresult;
    private int elapsedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();
        tipo = intent.getExtras().getString("tipo");
        if(tipo.equals(Board.TIPO_UNIDO)){
            //el jugador se ha unido
            adversario=new String(intent.getExtras().getString("adversario"));
            C3Preference.setAdversario(this,adversario);
            initialize(tipo);
            joinGame();
        }else{
            initialize(tipo);
        }

    }

    public void initialize(String tipo){
        messageToSend=(EditText)findViewById(R.id.normalMessage);
        messageToSend.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            sendNormalMessage(null);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        adversario=C3Preference.getAdversario(this);
        recibidorJOINED = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //FUNCION QUE SE EJECUTA INSTANTANEAMENTE SI ALGUIEN SE UNE A NUESTRA PARTIDA
                Bundle extras=intent.getExtras();
                joinedGame(extras);
            }
        };
        recibidorMOVEMENT = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //FUNCION QUE SE EJECUTA INSTANTANEAMENTE SI SE RECIBE UN MOVIMIENTO

                Bundle extras=intent.getExtras();
                try {
                    movementReceived(extras);
                    //esMiTurno();
                } catch (ExcepcionJuego excepcionJuego) {
                    excepcionJuego.printStackTrace();
                }
            }
        };
        listeneraddresult = new Response.Listener<String>(){ @Override
        public void onResponse(String response) {
            if(response.equals("-1")) Toast.makeText(Board.this, "Error al sincronizar la puntuación con el servidor.",Toast.LENGTH_SHORT).show();
            else{
                Log.d("ResponseADDRESULT",response);
            }
        } };
        errorlisteneraddresult = new Response.ErrorListener(){ @Override
        public void onErrorResponse(VolleyError error) {
        } };
        roundidtextview=(TextView)findViewById(R.id.roundIdTextView);

        roundinfotextview=(TextView)findViewById(R.id.roundInfo);
        chr = (Chronometer)findViewById(R.id.chrono);
        roundidtextview.setText("ID Partida:"+C3Preference.getPartidaId(this));
        this.tipo=tipo;

        jugadorHumano= new JugadorHumano(C3Preference.getPlayerName(this));
        jugadorRemoto=new JugadorRemoto(adversario);
        if(tipo.equals(TIPO_UNIDO)){
            jugadores.add(jugadorRemoto);
            jugadores.add(jugadorHumano);
        }else{
            jugadores.add(jugadorHumano);
            jugadores.add(jugadorRemoto);
        }
        partida = new Partida(new Tablero3Raya(), jugadores, this);
        partida.addObservador(new JugadorHumano("Observador"));
    }
    private void movementReceived(Bundle extras) throws ExcepcionJuego {
        //Funcion a ejecutar cuando se recibe un movimiento
        //el bundle es el GCM de tipo 1
        try {
            partida.getTablero().stringToTablero(extras.getString("tablero"));
            //Log.i("TABLERITO", extras.getString("tablero"));
            actualizaTablero();
            roundinfotextview.setText(adversario+" ha movido, es tu turno.");
            if(partida.getTablero().getEstado()!=Tablero.EN_CURSO){
                stopChronometer();
                cronoparado=1;
                finalPartida(partida.getTablero().getGanador(),true);
            }else{
                Toast.makeText(Board.this,"¡Te toca!",Toast.LENGTH_SHORT).show();
            }
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
    esMiTurno=true;
    }
    @Override public void onSaveInstanceState(Bundle savedInstanceState){
        if(partida!=null) {
            String estado = partida.getTablero().tableroToString();
            savedInstanceState.putString("estadoPartida", estado);
            Log.i("tresenraya", "guardado estado " + estado);
            savedInstanceState.putLong("ChronoTime", chr.getBase());
            savedInstanceState.putInt("cronoparado",cronoparado);
            savedInstanceState.putString("ChronoText",chr.getText().toString());
            savedInstanceState.putString("tipo",tipo);
            savedInstanceState.putString("pendingMsg",messageToSend.getText().toString());
            savedInstanceState.putBoolean("turnito",esMiTurno);
        }
    }
    @Override public void onRestoreInstanceState(Bundle savedInstanceState){
        //int flag=0;
        initialize(savedInstanceState.getString("tipo"));
        messageToSend.setText(savedInstanceState.getString("pendingMsg"));
        esMiTurno=savedInstanceState.getBoolean("turnito");
        try {
            String estado = savedInstanceState.getString("estadoPartida");
            if(estado!=null){
                partida.getTablero().stringToTablero(estado);
               // flag=1;
                Log.i("tresenraya", "cargado estado " + estado);}
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
       // if(flag==1) {
           actualizaTablero();
          //  }
            if((savedInstanceState !=null) && savedInstanceState.containsKey("ChronoTime")) {
                if(savedInstanceState.getInt("cronoparado")==0) {
                    chr.setBase(savedInstanceState.getLong("ChronoTime"));
                    chr.start();
                }else{
                    chr.setText(savedInstanceState.getString("ChronoText"));
                }
            }

        }

    //}
    private void actualizaTablero() {
        if(esMiTurno){
            roundinfotextview.setText(adversario+" ha movido, es tu turno.");
        }else{
            roundinfotextview.setText("Esperando a que mueva "+adversario+" ...");

        }
        for (int i = 0; i < 9; i++) {
            buttonAux = (Button) findViewById(ids[i]);
            if(partida.getTablero().getEstado()!=Tablero.EN_CURSO){
                finalPartida(partida.getTablero().getGanador(),false);
            }
            switch (((Tablero3Raya) partida.getTablero()).getCasilla(i)) {
                case 0:
                    buttonAux.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    if(tipo.equals(TIPO_UNIDO)){
                        buttonAux = (Button) findViewById(ids[i]);
                        buttonAux.setVisibility(View.VISIBLE);
                        buttonAux.setText(R.string.O);
                    }else {
                        buttonAux = (Button) findViewById(ids[i]);
                        buttonAux.setVisibility(View.VISIBLE);
                        buttonAux.setText(R.string.X);
                    }

                    break;
                case 2:
                    if(tipo.equals(TIPO_UNIDO)){
                        buttonAux = (Button) findViewById(ids[i]);
                        buttonAux.setVisibility(View.VISIBLE);
                        buttonAux.setText(R.string.X);
                    }else {
                        buttonAux = (Button) findViewById(ids[i]);
                        buttonAux.setVisibility(View.VISIBLE);
                        buttonAux.setText(R.string.O);
                    }
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Cada vez que pasamos al primer plano mostramos los datos actualizados

        roundidtextview=(TextView)findViewById(R.id.roundIdTextView);
        roundinfotextview=(TextView)findViewById(R.id.roundInfo);

        // y registramos el recibidor para recibir respuestas
        registerReceiver(recibidorJOINED, new IntentFilter(FILTRO_JOINED));
        registerReceiver(recibidorMOVEMENT, new IntentFilter(FILTRO_MOVEMENT));

        // Truco sencillo para saber si la actividad está en primer plano
        Board.enPrimerPlano = true;
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Quitamos el recibidor del registro para evitar que sea llamado
        // una vez destruida la actividad
        unregisterReceiver(recibidorJOINED);
        unregisterReceiver(recibidorMOVEMENT);
        // Decimos que estamos en segundo plano
        Board.enPrimerPlano = false;
    }
    public void joinedGame(Bundle data){
        //SI TU HAS CREADO Y ALGUIEN SE UNE
        adversario= new String(data.getString("sender"));
        C3Preference.setAdversario(this,adversario);
        Toast.makeText(this,"¡"+adversario+" se ha unido!",Toast.LENGTH_SHORT).show();
        roundinfotextview.setText("La partida contra "+adversario+" va a comenzar");
        esMiTurno=true;
        startGame();
    }
    public void joinGame(){
        //SI TÚ TE HAS UNIDO
        roundinfotextview.setText("La partida contra "+adversario+" va a comenzar");
        startGame();
    }
    public void startGame(){
        //Creamos el tablero, etc etc
        chr.setBase(SystemClock.elapsedRealtime());
        chr.start();
        cronoparado=0;
        Tablero3Raya tablero = (Tablero3Raya) partida.getTablero();
        for (int i = 0; i < 9; i++) {
            buttonAux = (Button) findViewById(ids[i]);
            buttonAux.setVisibility(View.VISIBLE);
        }
        if(tipo.equals(Board.TIPO_UNIDO)){
            this.partida.getTablero().cambiaTurno();
            this.partida.getTablero().numJugadas--;
        }
//esMiTurno();

    }
    public void sendNormalMessage(View view){

        Response.Listener<String> msglistener = new Response.Listener<String>(){ @Override
        public void onResponse(String response) {
            if(response.equals("-1")){
                Toast.makeText(Board.this, "Error enviando mensaje",Toast.LENGTH_SHORT).show();
        }else{
            messageToSend.setText("");}
        }};
        Response.ErrorListener msgerrlistener = new Response.ErrorListener(){ @Override
        public void onErrorResponse(VolleyError error) {
        } };
        String msg = messageToSend.getText().toString();
        if(msg.isEmpty()){
            Toast.makeText(this,"¡Mensaje vacío!",Toast.LENGTH_SHORT).show();
        }else if(msg.length()>=140){
            Toast.makeText(this,"¡Mensaje mayor de 140 caracteres!",Toast.LENGTH_SHORT).show();
        }else{
            InterfazConServidor.getServer(this).sendMessageToUser(adversario,msg,C3Preference.getPlayerId(this),msglistener,msgerrlistener);
        }

    }

   /* public Boolean esMiTurno(){
        //mirar si me toca a mí, PERO EN EL SERVIDOR
        Response.Listener<JSONObject> listenerESMITURNO = new Response.Listener<JSONObject>() {
            @Override public void onResponse(JSONObject response) {
                    try {
                        String turno = response.getString("turn");
                        String codeboard = response.getString("codedboard");
                        Log.d("codeboard(ismyturn)", codeboard);
                        if(turno.equals("0")){
                            esMiTurno=false;
                        }else if(turno.equals("1")){
                            esMiTurno=true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Log.d("ResponseIsmyTurn", response.toString());
            }
        };
        Response.ErrorListener errorlistenerESMITURNO=
                new Response.ErrorListener()
                {
                    @Override public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                };
        InterfazConServidor.getServer(this).ismyturn(C3Preference.getPlayerId(this),C3Preference.getPartidaId(this),listenerESMITURNO,errorlistenerESMITURNO);
        return esMiTurno;
    }*/
    public void onPress(View view){
        //cuando tocamos una tecla
       // Log.d("DEBUG","esmiturnoantes:"+esMiTurno.toString());
        //esMiTurno();
       // Log.d("DEBUG","esmiturnodespues:"+esMiTurno.toString());
        if(partida.getTablero().getEstado()!=Tablero.EN_CURSO) {
            Toast.makeText(this,"La partida ya ha terminado",Toast.LENGTH_SHORT).show();
            return;
        }
        if(esMiTurno==false){

            //no se puede mover si no te toca
            Toast.makeText(this,"¡Es el turno de "+adversario+"!",Toast.LENGTH_SHORT).show();

        }else{
            //hacer el movimiento porque me toca
           // Log.d("DEBUG","esmiturnoELSE:"+esMiTurno.toString());
            int a;
            for (int i = 0; i < 9; i++) {
                if (view.getId() == ids[i]) {
                    try {
                        if(tipo.equals(TIPO_UNIDO)){
                            a=1;
                        }else{
                            a=0;
                        }
                        esMiTurno=false;
                        if(partida.getTablero().getEstado()== Tablero.EN_CURSO) {
                            partida.realizaAccion(new AccionMover(partida.getJugador(a), new Movimiento3Raya(i)));
                            //Toast.makeText(this,partida.getJugador(a).getNombre(),Toast.LENGTH_SHORT).show();
                            buttonAux = (Button) findViewById(view.getId());
                            buttonAux.setText(R.string.X);
                            roundinfotextview.setText("Esperando a que mueva "+adversario+" ...");
                            if(partida.getTablero().getEstado()!=Tablero.EN_CURSO){
                                stopChronometer();
                                cronoparado=1;
                                finalPartida(partida.getTablero().getGanador(),true);
                            }

                        }

                    } catch (ExcepcionJuego excepcionJuego) {
                        if (excepcionJuego.getError() == -2) ;
                        {
                            Toast.makeText(this.getApplicationContext(),R.string.error2String,Toast.LENGTH_SHORT).show();
                            esMiTurno=true;
                        }
                    }
                }
            }
        }
    }

  //  static boolean estaEnPrimerPlano() {
   //     return enPrimerPlano;
   // }
  private void stopChronometer (){
      chr.stop();
      String chronometerText = chr.getText().toString(); String array[] = chronometerText.split(":");
      if (array.length == 2){
          elapsedTime = Integer.parseInt(array[0]) * 60 +
                  Integer.parseInt(array[1]); } else if (array.length == 3){
          elapsedTime = Integer.parseInt(array[0]) * 60 * 60 + Integer.parseInt(array[1]) * 60 + Integer.parseInt(array[2]);

      }
  }
    public void finalPartida(int ganador,Boolean toast) {
        if(partida.getTablero().getEstado()==Tablero.TABLAS){
            roundinfotextview.setText("Ha habido un empate.");
            if(toast){
                Toast.makeText(this,"Ha habido un empate",Toast.LENGTH_SHORT).show();
                InterfazConServidor.getServer(this).addresult(C3Preference.getPlayerId(this), C3Preference.getGameId(this), String.valueOf(elapsedTime),"0",listeneraddresult,errorlisteneraddresult);
            }
        }else{
        if(tipo.equals(TIPO_UNIDO)){
            if(partida.getTablero().getGanador()==1){
                //he perdido

                roundinfotextview.setText("Has perdido. Otra vez será");
                if(toast){
                    Toast.makeText(this,"Has perdido, otra vez será",Toast.LENGTH_SHORT).show();
                    InterfazConServidor.getServer(this).addresult(C3Preference.getPlayerId(this), C3Preference.getGameId(this), String.valueOf(elapsedTime),"0",listeneraddresult,errorlisteneraddresult);
                }
            }else{
                //he ganado

                roundinfotextview.setText("Has ganado. Bien jugado");
                if(toast){
                    Toast.makeText(this,"Has ganado. Bien jugado",Toast.LENGTH_SHORT).show();
                    InterfazConServidor.getServer(this).addresult(C3Preference.getPlayerId(this), C3Preference.getGameId(this),  String.valueOf(elapsedTime),"1",listeneraddresult,errorlisteneraddresult);
                }
            }
        }else{
            if(partida.getTablero().getGanador()==1){
                //he ganado
               roundinfotextview.setText("Has ganado. Bien jugado");
                if(toast){
                    Toast.makeText(this,"Has ganado. Bien jugado",Toast.LENGTH_SHORT).show();
                    InterfazConServidor.getServer(this).addresult(C3Preference.getPlayerId(this), C3Preference.getGameId(this),  String.valueOf(elapsedTime),"1",listeneraddresult,errorlisteneraddresult);
                }
            }else{
                //he perdido

                roundinfotextview.setText("Has perdido. Otra vez será");
                if(toast){
                    Toast.makeText(this,"Has perdido, otra vez será",Toast.LENGTH_SHORT).show();
                    InterfazConServidor.getServer(this).addresult(C3Preference.getPlayerId(this), C3Preference.getGameId(this),  String.valueOf(elapsedTime),"0",listeneraddresult,errorlisteneraddresult);
                }

            }
        }
    }}
}
