package com.example.jose.connect3;
import android.net.Uri;
import android.os.SystemClock;
import android.view.MenuItem;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import res.layout.About;
import java.util.ArrayList;

import es.uam.eps.multij.*;
import res.layout.login_fr;

public class MainActivity extends AppCompatActivity implements res.layout.main_fragment.OnFragmentInteractionListener {

    protected JugadorAleatorio jugadorAleatorio = new JugadorAleatorio("Maquina");
    protected JugadorHumano jugadorHumano = new JugadorHumano("Humano");
    protected ArrayList<Jugador> jugadores = new ArrayList<>();
    private final int ids []={R.id.button_0,R.id.button_1,R.id.button_2,R.id.button_3,
            R.id.button_4,R.id.button_5,R.id.button_6,R.id.button_7,R.id.button_8};

    private Button buttonAux;
    private TextView title;
    private Partida partida;
    private boolean already=false;
    private RelativeLayout mainFrame;
public Chronometer chr;
    private int elapsedTime;
    public static DatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, res.layout.main_fragment.newInstance()).commit();
        chr = (Chronometer)findViewById(R.id.chrono);
    }
    public void newGame(View view){
        if(already==false) {
            already = true;
            mainFrame = (RelativeLayout) findViewById(R.id.mainFrame);

            jugadores.add(jugadorAleatorio);
            jugadores.add(jugadorHumano);
            partida = new Partida(new Tablero3Raya(), jugadores, this);
            partida.addObservador(new JugadorHumano("Observador"));
            chr.start();
            Tablero3Raya tablero = (Tablero3Raya) partida.getTablero();
            for (int i = 0; i < 9; i++) {
                buttonAux = (Button) findViewById(ids[i]);
                buttonAux.setVisibility(View.VISIBLE);

            }

            title = (TextView) findViewById(R.id.textView_title);
        }else{
            partida.getTablero().reset();
            chr.setBase(SystemClock.elapsedRealtime());
            chr.start();
            mainFrame.setBackgroundResource(R.color.colorNormal);
            for (int i = 0; i < 9; i++) {
                buttonAux = (Button) findViewById(ids[i]);
                buttonAux.setText(R.string.nul);

            }
        }
    }

    public TextView getViewTitle(){
        return title;
    }
    public void setViewTitleText(int i){
        title.setText(i);
    }

    public void onPress(View v){
        boolean alerta=false;
        if(partida.getTablero().getEstado()==Tablero.FINALIZADA||partida.getTablero().getEstado()==Tablero.TABLAS){
            alerta=true;
            switch(partida.getTablero().getGanador()){
                case 0:
                    mainFrame.setBackgroundResource(R.color.colorX);
                    setViewTitleText(R.string.xString);
                   stopChronometer();
                    break;
                case 1:
                    mainFrame.setBackgroundResource(R.color.colorWin);
                    setViewTitleText(R.string.winString);
stopChronometer();
                    break;
                case 2:
                    mainFrame.setBackgroundResource(R.color.colorLose);
                    setViewTitleText(R.string.loseString);
                    stopChronometer();
                    break;
            }
        }
        int turno;
        turno=partida.getTablero().getTurno();
        if(alerta==false){

        if(turno==0) {
            for (int i = 0; i < 9; i++) {
                if (v.getId() == ids[i]) {
                    try {
                        partida.realizaAccion(new AccionMover(partida.getJugador(turno), new Movimiento3Raya(i)));
                        buttonAux = (Button) findViewById(v.getId());
                        buttonAux.setText(R.string.X);
                    } catch (ExcepcionJuego excepcionJuego) {
                        if (excepcionJuego.getError() == -2) ;
                        {
                            Toast.makeText(this.getApplicationContext(),R.string.error2String,Toast.LENGTH_SHORT).show();
                            alerta = true;
                        }
                    }
                }
            }


        }}
            if(partida.getTablero().getEstado()==Tablero.FINALIZADA||partida.getTablero().getEstado()==Tablero.TABLAS){
                alerta=true;
                switch(partida.getTablero().getGanador()){
                    case 0:
                        mainFrame.setBackgroundResource(R.color.colorX);
                        setViewTitleText(R.string.xString);
                        stopChronometer();
                        break;
                    case 1:
                        mainFrame.setBackgroundResource(R.color.colorWin);
                        setViewTitleText(R.string.winString);
                        stopChronometer();
                        break;
                    case 2:
                        mainFrame.setBackgroundResource(R.color.colorLose);
                        setViewTitleText(R.string.loseString);
                        stopChronometer();
                        break;
                }
            }
            if(alerta==false) {


                int r = (int) (Math.random() * partida.getTablero().movimientosValidos().size());
                Movimiento3Raya m = (Movimiento3Raya) partida.getTablero().movimientosValidos().get(r);

                try {
                    partida.realizaAccion(new AccionMover(
                            partida.getJugador(turno), partida.getTablero().movimientosValidos().get(r)));
                    buttonAux = (Button) findViewById(ids[m.getCasilla()]);
                    buttonAux.setText(R.string.O);
                } catch (Exception e) {
                    //NONPOSSIBLE?
                }


            }
        if(partida.getTablero().getEstado()==Tablero.FINALIZADA||partida.getTablero().getEstado()==Tablero.TABLAS){

            switch(partida.getTablero().getGanador()){
                case 0:
                    mainFrame.setBackgroundResource(R.color.colorX);
                    setViewTitleText(R.string.xString);
                    break;
                case 1:
                    mainFrame.setBackgroundResource(R.color.colorWin);
                    setViewTitleText(R.string.winString);
                    stopChronometer();
                    break;
                case 2:
                    mainFrame.setBackgroundResource(R.color.colorLose);
                    setViewTitleText(R.string.loseString);
                    stopChronometer();
                    break;
            }
        }
            }

    @Override public void onSaveInstanceState(Bundle savedInstanceState){
        if(partida!=null) {
            String estado = partida.getTablero().tableroToString();
            savedInstanceState.putString("estadoPartida", estado);
            Log.i("tresenraya", "guardado estado " + estado);
            savedInstanceState.putLong("ChronoTime", chr.getBase());

        }
    }
    @Override public void onRestoreInstanceState(Bundle savedInstanceState){
        jugadores.add(jugadorAleatorio);
        jugadores.add(jugadorHumano);
        int flag=0;
        partida = new Partida(new Tablero3Raya(), jugadores, this);
        partida.addObservador(new JugadorHumano("Observador"));
        try {
            String estado = savedInstanceState.getString("estadoPartida");
            if(estado!=null){
            partida.getTablero().stringToTablero(estado);
                flag=1;
            Log.i("tresenraya", "cargado estado " + estado);}
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
        if(flag==1) {
            already = true;
            mainFrame = (RelativeLayout) findViewById(R.id.mainFrame);
            Tablero3Raya tablero = (Tablero3Raya) partida.getTablero();
            for (int i = 0; i < 9; i++) {
                buttonAux = (Button) findViewById(ids[i]);
                buttonAux.setVisibility(View.VISIBLE);
                switch (((Tablero3Raya) partida.getTablero()).getCasilla(i)) {
                    case 0:
                        break;
                    case 1:
                        buttonAux = (Button) findViewById(ids[i]);
                        buttonAux.setText(R.string.X);
                        break;
                    case 2:
                        buttonAux = (Button) findViewById(ids[i]);
                        buttonAux.setText(R.string.O);
                        break;
                }
            }

            title = (TextView) findViewById(R.id.textView_title);
            if((savedInstanceState !=null) && savedInstanceState.containsKey("ChronoTime")) {
                chr.setBase(savedInstanceState.getLong("ChronoTime"));
                chr.start();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public int[] getIds() {
        return ids;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
            startActivity(new Intent(this, About.class));
            return true;

            case R.id.menuPreferences:
                startActivity(new Intent(this, C3Preference.class));
                return true; }
        return super.onOptionsItemSelected(item);
    }
    private void stopChronometer (){
        chr.stop();
        String chronometerText = chr.getText().toString(); String array[] = chronometerText.split(":");
        if (array.length == 2){
            elapsedTime = Integer.parseInt(array[0]) * 60 +
                    Integer.parseInt(array[1]); } else if (array.length == 3){
            elapsedTime = Integer.parseInt(array[0]) * 60 * 60 + Integer.parseInt(array[1]) * 60 + Integer.parseInt(array[2]);

        }
    insertRound(C3Preference.getPlayerName(this),partida.getTablero().movimientosValidos().size());
    }
    private void insertRound(String username, int numberOfTilesLeft){
        db = new DatabaseAdapter(this);
        db.open();
        db.insertData(username, elapsedTime, numberOfTilesLeft); db.close();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}






