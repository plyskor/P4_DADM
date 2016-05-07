package res.layout;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.R;

import android.widget.Toast;



public class MainMenu extends AppCompatActivity {
    public static boolean mIsBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }




    protected void onPause() {
        super.onPause();





    }

    public void startLogin(View view) {

            Intent intent = new Intent("android.intent.action.C3.LOGIN");
            startActivity(intent);

    }

    public void startRatings(View view) {
        Intent intent = new Intent("android.intent.action.C3.RATINGS");
        startActivity(intent);
    }

    public void startTopTen(View view) {
        Intent intent = new Intent("android.intent.action.C3.TOPTEN");
        startActivity(intent);
    }
    public void startGame(View view) {
        String aux=C3Preference.getPlayerName(this);
        if(aux.equals("unknownuser")){
            Toast.makeText(this, "Inicia Sesión para jugar",
                    Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent("android.intent.action.C3.MAINACTIVITY");
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


        public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.menuPreferences:
                startActivity(new Intent(this, C3Preference.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    }
