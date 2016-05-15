package res.layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.InterfazConServidor;
import com.example.jose.connect3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Play extends AppCompatActivity {
    Response.Listener<String> listener;
    Response.ErrorListener errorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        listener = new Response.Listener<String>(){ @Override
        public void onResponse(String response) {
            if(response.equals("-1")){
                Toast.makeText(Play.this, "Error del servidor al crear la nueva partida",Toast.LENGTH_SHORT).show();
            }else{

                C3Preference.setPartidaId(Play.this, response.trim());
                Intent intent = new Intent("android.intent.action.C3.BOARD");
                intent.putExtra("tipo",Board.TIPO_HOST);
                startActivity(intent);
            }
        } };
        errorListener = new Response.ErrorListener(){ @Override
        public void onErrorResponse(VolleyError error) {
        } };
    }
    public void newGameClick(View view) {
        InterfazConServidor.getServer(this).newround(C3Preference.getPlayerId(this),C3Preference.GAME_ID_DEFAULT,listener,errorListener);
    }
    public void localGameClick(View view) {
        Intent intent = new Intent("android.intent.action.C3.BOARD");
        intent.putExtra("tipo",Board.TIPO_LOCAL);
        startActivity(intent);
    }
    public void joinGameClick(View view) {
        Intent intent = new Intent("android.intent.action.C3.GAMESELECTION");
        startActivity(intent);
    }
}
