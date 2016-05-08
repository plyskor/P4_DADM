
package com.example.jose.connect3;
import java.util.HashMap;
        import java.util.Map;
import org.json.JSONArray;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
        import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
        import android.content.Context;
import android.util.Log;
public class InterfazConServidor {
    private static final String BASE_URL = "http://ptha.ii.uam.es/dadm2016/";
    private static final String ACCOUNT_PHP = "account.php";
    private static final String TOPTEN_PHP = "topten.php";
    private static final String ADD_SCORE_PHP = "addscore.php";
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
    public void topten(String figurename, Listener<JSONArray> callback,
                       ErrorListener errorCallback) {
        String url = BASE_URL + TOPTEN_PHP + "?"
                + C3Preference.FIGURE_NAME_KEY + "="
                + figurename + "&json"; Log.d(DEBUG_TAG, url);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, callback, errorCallback);
        queue.add(jsObjRequest); }
    public void addscore(final String playerid, String figurename,
                         String duration, String numberoftiles, String date,
                         Listener<String> callback, ErrorListener errorCallback) {
        /*COMMENT O PETA
        String url = BASE_URL + ADD_SCORE_PHP + "?"
                + C3Preference.PLAYER_ID_KEY + "=" + playerid + "&"
                + C3Preference.FIGURE_NAME_KEY + "=" + figurename + "&"
                + C3Preference.DURATION_KEY + "=" + duration + "&"
                + C3Preference.NUMBER_OF_TILES_KEY + "=" + numberoftiles + "&" + C3Preference.DATE_KEY + "=" + date;
        Log.d(DEBUG_TAG, url);
        StringRequest r = new StringRequest(Request.Method.GET, url, callback, errorCallback);
        queue.add(r);*/
    }
}