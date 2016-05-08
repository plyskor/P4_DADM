package res.layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.InterfazConServidor;
import com.example.jose.connect3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameSelection extends AppCompatActivity {
    Response.Listener<JSONArray> listenerJSON;
    Response.ErrorListener errorListenerJSON;
    ListView list;
    private List<String> List_file;
    private List<String> listaRondas;
    private List<String> ListaJugadores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        List_file =new ArrayList<String>();
        listaRondas =new ArrayList<String>();
        ListaJugadores =new ArrayList<String>();
        list = (ListView)findViewById(R.id.gamelist);
        final Response.Listener<String> listener = new Response.Listener<String>(){ @Override
        public void onResponse(String response) {


            if(response.equals("-1")){

                Toast.makeText(GameSelection.this, " Falido",Toast.LENGTH_SHORT).show();
            }else{

            }

        } };
        final Response.ErrorListener errorListener = new Response.ErrorListener(){ @Override
        public void onErrorResponse(VolleyError error) {
        } };
        listenerJSON =  new Response.Listener<JSONArray>()
        {
            @Override public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    JSONObject jresponse = null;
                    try {
                        jresponse = response.getJSONObject(i);
                        String partidaid = jresponse.getString(C3Preference.PARTIDA_ID_KEY);
                        String playername = jresponse.getString("playernames");
                        if(playername.equals(C3Preference.getPlayerName(GameSelection.this))){
                            continue;
                        }
                        List_file.add("ID:"+partidaid+"\t"+"Nombre Jugador: "+playername);
                        ListaJugadores.add(playername);
                        listaRondas.add(partidaid);
                        String numPlayers=jresponse.getString(C3Preference.NPLAYERS_KEY);
                        Log.d("partidaid", partidaid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Log.d("Response", response.toString());
                list.setAdapter(new ArrayAdapter<String>(GameSelection.this, android.R.layout.simple_list_item_1,List_file));
            }
        };
        errorListenerJSON=
                new Response.ErrorListener()
                {
                    @Override public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                };
        InterfazConServidor.getServer(this).openrounds(C3Preference.GAME_ID_DEFAULT,C3Preference.getPlayerId(this),listenerJSON,errorListenerJSON);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
            String roundid,adversary;
                int auxint;
                roundid=listaRondas.get(arg2);
                adversary=ListaJugadores.get(arg2);

                InterfazConServidor.getServer(GameSelection.this).addplayertoround(roundid,C3Preference.getPlayerId(GameSelection.this),listener,errorListener);
                InterfazConServidor.getServer(GameSelection.this).sendMessageToUser(adversary,"JOINED",C3Preference.getPlayerId(GameSelection.this),listener,errorListener);
            }
        });
    }
}
