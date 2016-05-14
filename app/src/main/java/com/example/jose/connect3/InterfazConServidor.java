
package com.example.jose.connect3;
import java.util.HashMap;
        import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
        import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
        import android.content.Context;
import android.util.Log;
public class InterfazConServidor {
    private static final String BASE_URL = "http://ptha.ii.uam.es/dadm2016/";
    private static final String ACCOUNT_PHP = "account.php";
    private static final String TOPTEN_PHP = "topten.php";
    private static final String NEWROUND_PHP = "newround.php";
    private static final String ADDPLAYER_PHP = "addplayertoround.php";
    private static final String SENDMSG_PHP = "sendmsg.php";
    private static final String ISMYTURN_PHP = "ismyturn.php";
    private static final String NEWMOVEMENT_PHP = "newmovement.php";
    private static final String REMOVEPLAYER_PHP = "removeplayerfromround.php";
    private static final String OPENROUND_PHP = "openrounds.php";
    private static final String ADDRESULT_PHP = "addresult.php";
    private static final String GETRESULTS_PHP = "getresults.php";
    private static final String DEBUG_TAG = "InterfazConServidor";
    private RequestQueue queue;
    private static InterfazConServidor serverInterface;
    private InterfazConServidor(Context context) {
        queue = Volley.newRequestQueue(context); }
    public static InterfazConServidor getServer(Context context) { if (serverInterface == null) {
        serverInterface = new InterfazConServidor(context); }
        return serverInterface; }
    public void account(final String gcmregid,final String playername, final String playerpassword,
                        Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + ACCOUNT_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(C3Preference.PLAYER_NAME_KEY, playername);
                params.put(C3Preference.PLAYER_PASSWORD_KEY, playerpassword);
                params.put(C3Preference.GCM_ID_KEY, gcmregid);
                return params;
            }
        };
        queue.add(request); }
    public void login(final String playername,final String gcmregid, final String playerpassword,
                      Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + ACCOUNT_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(C3Preference.PLAYER_NAME_KEY, playername);
                params.put(C3Preference.PLAYER_PASSWORD_KEY, playerpassword);
                params.put("login", "");
                params.put(C3Preference.GCM_ID_KEY, gcmregid);
                return params;
            }
        };
        queue.add(request); }
    public void newround(final String playerid, final String gameid ,Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + NEWROUND_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(C3Preference.PLAYER_ID_KEY, playerid);
                params.put(C3Preference.GAME_ID_KEY, gameid);
                return params;
            }
        };
        queue.add(request); }
    public void addplayertoround(final String roundid, final String playerid ,Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + ADDPLAYER_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(C3Preference.PLAYER_ID_KEY, playerid);
                params.put(C3Preference.PARTIDA_ID_KEY, roundid);
                return params;
            }
        };
        queue.add(request); }
    public void removeplayerfromround(final String roundid, final String playerid ,Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + REMOVEPLAYER_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(C3Preference.PLAYER_ID_KEY, playerid);
                params.put(C3Preference.PARTIDA_ID_KEY, roundid);
                return params;
            }
        };
        queue.add(request); }

    public void sendMessageToUser(final String to, final String msg,final String playerid ,Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + SENDMSG_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("to", to);
                params.put("msg", msg);
                params.put(C3Preference.PLAYER_ID_KEY, playerid);
                return params;
            }
        };
        queue.add(request); }
    public void sendMessageToRound(final String to, final String msg,final String playerid ,Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + SENDMSG_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("toround", to);
                params.put("msg", msg);
                params.put(C3Preference.PLAYER_ID_KEY, playerid);
                return params;
            }
        };
        queue.add(request); }
    public void newmovement(String playerid,String roundid,String codeboard, Listener<JSONObject> callback, ErrorListener errorCallback) {
        String url = BASE_URL + NEWMOVEMENT_PHP + "?"
                + C3Preference.PLAYER_ID_KEY + "="
                + playerid +"&"
                +C3Preference.PARTIDA_ID_KEY+"="+roundid
                +"&codedboard="+codeboard+
                "&json";
        Log.d(DEBUG_TAG, url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url,null, callback, errorCallback);
        queue.add(jsObjRequest);
    }
    public void getresults(String playerid,String gameid,Boolean groupbyuser, Listener<JSONArray> callback, ErrorListener errorCallback) {
        String url = BASE_URL + GETRESULTS_PHP + "?";
        if(groupbyuser){
            url=url+"groupbyuser&";
        }
        url=url+ C3Preference.PLAYER_ID_KEY + "="
                + playerid +"&"
                +C3Preference.GAME_ID_KEY+"="+gameid
        +"&json";
        Log.d(DEBUG_TAG, url);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, callback, errorCallback);
        queue.add(jsObjRequest);
    }
    public void ismyturn(String playerid, String roundid, Listener<JSONObject> callback, ErrorListener errorCallback) {
        String url = BASE_URL + ISMYTURN_PHP + "?"
                + C3Preference.PLAYER_ID_KEY + "="
                +playerid +"&"+C3Preference.PARTIDA_ID_KEY+"="+roundid+"&json";
        Log.d(DEBUG_TAG, url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url, null,callback, errorCallback);
        queue.add(jsObjRequest);
    }
    public void topten(String figurename, Listener<JSONArray> callback,
                       ErrorListener errorCallback) {
        String url = BASE_URL + TOPTEN_PHP + "?"
                + C3Preference.FIGURE_NAME_KEY + "="
                + figurename + "&json"; Log.d(DEBUG_TAG, url);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, callback, errorCallback);
        queue.add(jsObjRequest); }
    public void openrounds(String gameid,String playerid, Listener<JSONArray> callback,
                           ErrorListener errorCallback) {
        String url = BASE_URL + OPENROUND_PHP + "?"
                + C3Preference.GAME_ID_KEY + "="
                + gameid +"&"+ C3Preference.PLAYER_ID_KEY +"="+ playerid; Log.d(DEBUG_TAG, url);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, callback, errorCallback);
        queue.add(jsObjRequest);
    }

    public void addresult(final String playerid, final String gameid,final String roundtime,final String points ,Listener<String> callback, ErrorListener errorCallback) {
        String url = BASE_URL + ADDRESULT_PHP; Log.d(DEBUG_TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(C3Preference.PLAYER_ID_KEY, playerid);
                params.put("gameid", gameid);
                params.put("roundtime", roundtime);

                params.put("points", points);
                return params;
            }
        };
        queue.add(request); }
}