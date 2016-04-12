package res.layout;
import java.util.ArrayList;
import android.app.ListActivity;
import android.media.Rating;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jose.connect3.DatabaseAdapter;
import com.example.jose.connect3.R;

import java.util.ArrayList;

public class rating extends ListActivity implements rating_fr.OnFragmentInteractionListener{
    private DatabaseAdapter db;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, rating_fr.newInstance()).commit();
        list = fromDatabaseToStringArray();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.listrating,R.id.newslist_text,list);
        setListAdapter(adapter);
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
            Toast.makeText(rating.this, ((TextView) arg1).getText(),
                    Toast.LENGTH_SHORT).show();
        } });
    }


    private ArrayList<String> fromDatabaseToStringArray (){
        ArrayList<String> list;
        db = new DatabaseAdapter(this);
        db.open();
        list = db.getAllRounds();
        db.close();
        return list;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
