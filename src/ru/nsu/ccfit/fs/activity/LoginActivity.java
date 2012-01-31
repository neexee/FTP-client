package ru.nsu.ccfit.fs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ru.nsu.ccfit.fs.R;
import ru.nsu.ccfit.fs.ftp.Login;

public class LoginActivity extends Activity
{   private static final String   TAG ="LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final EditText usernameEdit = (EditText) findViewById(R.id.username_edit);
        final EditText hostEdit = (EditText) findViewById(R.id.host_edit);
        final EditText passwordEdit  = (EditText) findViewById(R.id.password_edit);
        final EditText portEdit = (EditText) findViewById(R.id.port_edit);
        Button okButton = (Button) findViewById( R.id.ok_button);

        okButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent main = new Intent(view.getContext(),AuthorizedMainActivity.class);
                Log.d(TAG,hostEdit.getText().toString());
                Login login = new Login(
                         usernameEdit.getText().toString(),
                         passwordEdit.getText().toString(),
                         hostEdit.getText().toString(),
                         Integer.parseInt(portEdit.getText().toString(), 10));

                Bundle bundle = new Bundle();
                bundle.putSerializable("LOGIN",login);
                main.putExtras(bundle);
                startActivity(main);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //this.appendMenuItemText(item);
        if (item.getItemId() == R.menu.login){
            //и тут НАНОДЕСУ!
        }
        else {
            //ou
        }

        return true;
    }

    public void onStop(){
      super.onStop();
    }

}
