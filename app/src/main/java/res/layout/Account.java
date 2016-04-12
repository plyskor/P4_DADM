package res.layout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jose.connect3.DatabaseAdapter;
import com.example.jose.connect3.R;

public class Account extends Activity implements account_fr.OnFragmentInteractionListener {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmedPassword;
    private DatabaseAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);
        editTextUsername = (EditText)findViewById(R.id.accountUsername);
        editTextPassword = (EditText)findViewById(R.id.accountPassword);
        editTextConfirmedPassword = (EditText)findViewById(R.id.accountConfirmedPassword);
    }
    private void newAccount(){
        String name = editTextUsername.getText().toString();

        String pass = editTextPassword.getText().toString();
        String confPass = editTextConfirmedPassword.getText().toString();
        if (!pass.equals("") && !name.equals("") && pass.equals(confPass)) {
            db = new DatabaseAdapter(this);
            db.open();
            db.insertUser(name, Login.md5Java(pass));
            db.close();
            Toast.makeText(Account.this, R.string.accountFirstToastMessage,
                    Toast.LENGTH_SHORT).show();
            finish(); }
        else if(pass.equals("") || confPass.equals("") || name.equals("")) Toast.makeText(Account.this,
                R.string.accountSecondToastMessage,Toast.LENGTH_SHORT).show(); else if(!pass.equals(confPass))
            Toast.makeText(Account.this, R.string.accountThirdToastMessage,Toast.LENGTH_SHORT).show();
    }
@Override
public void onBackPressed(){
    startActivity(new Intent("android.intent.action.C3.LOGIN"));
}
    public void acceptAccount(View v){
        newAccount();
        this.onBackPressed();
    }
    public void cancelAccount(View v){
        this.onBackPressed();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
