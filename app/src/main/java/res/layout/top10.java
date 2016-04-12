package res.layout;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jose.connect3.R;
public class top10 extends AppCompatActivity implements top10_fr.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_top10);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, top10_fr.newInstance()).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
