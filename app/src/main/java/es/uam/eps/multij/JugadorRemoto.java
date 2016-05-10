package es.uam.eps.multij;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.InterfazConServidor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import res.layout.Board;
import res.layout.GameSelection;

/**
 * Created by Jose on 9/5/16.
 */
public class JugadorRemoto implements Jugador {
    private String nombre;
    public JugadorRemoto(String name){
        this.nombre=name;
    }
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return false;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

            final Response.Listener<String> listener = new Response.Listener<String>(){ @Override
            public void onResponse(String response) {
                if(response.equals("-1")){

                }else{

                }
            }};
        final Response.ErrorListener errorlistener = new Response.ErrorListener(){ @Override
        public void onErrorResponse(VolleyError error) {
        } };
        Response.Listener<JSONObject> listenerJSON =  new Response.Listener<JSONObject>()
        {
            @Override public void onResponse(JSONObject response) {
                    try {
                        String codedboard = response.getString("codedboard");
                        String turn = response.getString("turn");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Log.d("newmovementResponse", response.toString());

            }
        };
        Response.ErrorListener errorListenerJSON=
                new Response.ErrorListener()
                {
                    @Override public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                };
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                if(evento.getPartida().getTablero().getEstado()!=Tablero.EN_CURSO) {
                    InterfazConServidor.getServer(evento.getPartida().getActivity()).newmovement(C3Preference.getPlayerId(evento.getPartida().getActivity()), C3Preference.getPartidaId(evento.getPartida().getActivity()), evento.getPartida().getTablero().tableroToString(), listenerJSON, errorListenerJSON);
                    InterfazConServidor.getServer(evento.getPartida().getActivity()).sendMessageToUser(C3Preference.getAdversario(evento.getPartida().getActivity()), "$MOV$" + evento.getPartida().getTablero().tableroToString(), C3Preference.getPlayerId(evento.getPartida().getActivity()), listener, errorlistener);
                    ((Board) evento.getPartida().getActivity()).finalPartida(evento.getPartida().getTablero().getGanador(), true);
                }
                break;
            case Evento.EVENTO_CONFIRMA:
                break;
            case Evento.EVENTO_TURNO:
                //Mandar movimiento al servidor.
                InterfazConServidor.getServer(evento.getPartida().getActivity()).newmovement(C3Preference.getPlayerId(evento.getPartida().getActivity()),C3Preference.getPartidaId(evento.getPartida().getActivity()),evento.getPartida().getTablero().tableroToString(),listenerJSON,errorListenerJSON);
                InterfazConServidor.getServer(evento.getPartida().getActivity()).sendMessageToUser(C3Preference.getAdversario(evento.getPartida().getActivity()),"$MOV$"+evento.getPartida().getTablero().tableroToString(),C3Preference.getPlayerId(evento.getPartida().getActivity()),listener,errorlistener);
                //evento.getPartida().getTablero().cambiaTurno();
                ((Board)evento.getPartida().getActivity()).esMiTurno=false;

                break;
        }
    }
}
