package res.layout;
import android.app.Activity;

import com.example.jose.connect3.C3Preference;
import com.example.jose.connect3.C3PreferenceFragment;
import com.example.jose.connect3.DatabaseAdapter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jose.connect3.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Login extends AppCompatActivity implements login_fr.OnFragmentInteractionListener,account_fr.OnFragmentInteractionListener {


    private static final int REQUEST_READ_CONTACTS = 0;
    public static DatabaseAdapter db;
    public static EditText usernameEditText;
    public static EditText passwordEditText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, login_fr.newInstance(),"LOGIN").commit();
        // Set up the login form.
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);




    }
    public static String md5Java(String message){
        String digest = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        try {
            hash = md.digest(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder(2*hash.length);
        for(byte b : hash){
            sb.append(String.format("%02x", b&0xff));
        }
        digest = sb.toString();
      return digest;
    }


    private void check() {
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        db=new DatabaseAdapter(this);
        db.open();


        boolean in = db.isRegistered(username, md5Java(password));
        db.close();
        if (in){
            C3Preference.setPlayerName(Login.this, username);
            C3Preference.setPlayerPassword(Login.this, password);
            startActivity(new Intent(this, com.example.jose.connect3.MainActivity.class)); finish();
        }
        else {
            new AlertDialog.Builder(this) .setTitle(R.string.loginAlertDialogTitle) .setMessage(R.string.loginAlertDialogMessage) .setNeutralButton(R.string.loginAlertDialogNeutralButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    }).show();
        }
    }



public void registerButton(View v){
    startActivity(new Intent("android.intent.action.C3.ACCOUNT"));
    setContentView(R.layout.fragment_account);
}
    public void loginButton(View v){
        check();
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent("android.intent.action.C3.MENU"));
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}