package res.layout;
import java.util.ArrayList;
import android.app.ListActivity;
import android.media.Rating;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.DatabaseAdapter;
import com.example.jose.connect3.InterfazConServidor;
import com.example.jose.connect3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class rating extends AppCompatActivity implements rating_fr.OnFragmentInteractionListener{
    private DatabaseAdapter db;
    private ArrayList<String> list;
    private TableLayout tableLayoutRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_rating);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, rating_fr.newInstance()).commit();
        tableLayoutRatings = (TableLayout)findViewById(R.id.TableLayoutRatings);
        headerRow(tableLayoutRatings);
        topten();

    }

    private void headerRow(TableLayout table) {
        TableRow header = new TableRow(this);
        int textColor = getResources().getColor(R.color.header_top_scores_color);
        float textSize = getResources().getDimension(R.dimen.header_top_scores_size);
        addTextView(header, getResources().getString(R.string.playername), textColor, textSize,true);
        addTextView(header, getResources().getString(R.string.timeround), textColor, textSize,true);
        addTextView(header, getResources().getString(R.string.points), textColor, textSize,true);
        table.addView(header);
    }
    private void addTextView(TableRow tableRow, String text, int textColor,float textSize,Boolean estilo)
    {
        TextView textView = new TextView(this);
        if(estilo){
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
        }
        textView.setText(text);
        tableRow.addView(textView);
    }
    public void topten() {
        InterfazConServidor is = InterfazConServidor.getServer(this);// String figurename = CCCPreference.getFigureName(TopScores.this);
        is.getresults(C3Preference.getPlayerId(this),C3Preference.getGameId(this),true,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
// TODO Auto-generated method stub
                        try { processJSONArray(response);
                        } catch (JSONException e) { e.printStackTrace();
                        } }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("ErrorGetResults", volleyError.getMessage());
                    }


                });
    }

    private void processJSONArray (JSONArray response) throws JSONException{ for (int i=0; i<response.length(); i++){
        JSONObject score = (JSONObject) response.get(i);
        String usuario = score.getString("playername");
        String npiezas = score.getString("timeround");
        String duracion = score.getString("points");
        insertRow(tableLayoutRatings, usuario, npiezas, duracion);
    } }

    private void insertRow(TableLayout tableLayoutRatings, String usuario, String time, String points) {
        TableRow row = new TableRow(this);
        addTextView(row,usuario, 0, 0,false);
        addTextView(row, time, 0, 0,false);
        int pointsint=Integer.parseInt(points);
        points=String.valueOf(pointsint);
        addTextView(row, points, 0, 0,false);
        tableLayoutRatings.addView(row);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
