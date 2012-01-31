package ru.nsu.ccfit.fs.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import ru.nsu.ccfit.fs.R;
import ru.nsu.ccfit.fs.command.ChangeDir;
import ru.nsu.ccfit.fs.command.Command;
import ru.nsu.ccfit.fs.command.Connect;
import ru.nsu.ccfit.fs.command.GetDirList;
import ru.nsu.ccfit.fs.file.AbstractFile;
import ru.nsu.ccfit.fs.service.FtpService;

import java.io.Serializable;
import java.util.ArrayList;

public class AuthorizedMainActivity extends Activity {
    private final static String TAG = "AuthorizedMainActivity";
    FtpReceiver ftpReceiver;
    TableLayout filetable;

    ProgressDialog dialog;
    /*for IPC, maybe
    private ServiceConnection mConnection;
    FtpService mService;
    boolean mBound = false;
   */


    private class FtpReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context arg0, Intent arg)
        {
            Toast.makeText(AuthorizedMainActivity.this, "Ftp answered!", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            Bundle bundle = arg.getExtras();
            Serializable result = bundle.getSerializable("RESULT");
            if(result.getClass().equals(ArrayList.class))
            {
                redraw(arg);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorized_main_activity);
        ftpReceiver = new FtpReceiver();

    }

    @Override
    public void onStart()
    {
        super.onStart();
        filetable = (TableLayout)findViewById(R.id.filetable);
        Log.d(TAG, "Starting service");

        /*registering receiver from FtpService*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FtpService.COMMAND);
        registerReceiver(ftpReceiver, intentFilter);

        Intent forServiceIntent = new Intent(AuthorizedMainActivity.this,FtpService.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("COMMAND",new Connect());
        bundle.putString("ARG", "");
        bundle.putSerializable("LOGIN",getIntent().getExtras().getSerializable("LOGIN"));
        forServiceIntent.putExtras(bundle);

        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        //startService(forServiceIntent);
        startServiceInAnotherThread(forServiceIntent);
    }
    private void startServiceInAnotherThread(final Intent intent)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startService(intent);
            }
        });
        thread.start();
    }
    private void serverWork(Command command, String arg)
    {
       final Intent intent = new Intent(AuthorizedMainActivity.this,FtpService.class);

       Bundle bundle = new Bundle();
       bundle.putSerializable("COMMAND",command);
       bundle.putString("ARG", arg);
       intent.putExtras(bundle);

       //startService(intent);
        startServiceInAnotherThread(intent);
    }

    private void redraw(Intent arg)
    {
       Bundle listWrap = arg.getExtras();
       ArrayList <AbstractFile> list = (ArrayList<AbstractFile>) listWrap.get("RESULT");
       LayoutInflater inflater = getLayoutInflater();
       filetable.removeAllViews();

        for(final AbstractFile i:list)
        {
            Log.d(TAG,i+ "Added to list!");
            TableRow row;
            TextView file = new TextView(this);
            file.setText(i.getName() +"\n" + i.getPermissions());

            ImageView image = new ImageView(this);
               if(i.getType() == AbstractFile.REGULARTYPE)
                {

                    row = (TableRow)inflater.inflate(R.layout.regular_file_row, filetable, false);
                    image.setImageResource(R.drawable.text_plain);
                    row.setTag("File");
                }
                else
                {
                    image.setImageResource(R.drawable.folder);
                     row = (TableRow)inflater.inflate(R.layout.folder_row, filetable, false);
                    final String name = new String( i.getName());
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            serverWork(new ChangeDir(), name);
                            serverWork(new GetDirList(), name);
                        }
                    });
                    row.setTag("Folder");

                }

            row.addView(image);
            row.addView(file);
            registerForContextMenu(row);
            filetable.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));






        }
     
           TextView currDir= (TextView)findViewById(R.id.current_dir);
           currDir.setText("Current dir: " + listWrap.getString("CURRENT_DIR"));

    }

 @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
   {
      super.onCreateContextMenu(menu, v, menuInfo);
      MenuInflater inflater = getMenuInflater();
     // inflater.inflate(R.menu.regular_file_context_menu, menu);
      if(v.getTag().equals("Folder"))
      {
          inflater.inflate(R.menu.folfer_context_menu,menu);
          
      }
      else
      {
          inflater.inflate(R.menu.regular_file_context_menu, menu);
      }
   }

    @Override
   public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
               switch (item.getItemId())
               {
                         case R.id.download_dir: Toast.makeText(this, "DOWNLOAD DIR!", Toast.LENGTH_LONG).show();
                                           return true;
                         case R.id.download_file:  Toast.makeText(this, "DOWNLOAD FILE!", Toast.LENGTH_LONG).show();
                                       return true;
                         default:
                                       return super.onContextItemSelected(item);
               }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
         //this.appendMenuItemText(item);
        if (item.getItemId() == R.id.refresh){
           serverWork(new GetDirList(), "");
        }
        else {
            //ou
        }


        return true;
    }
}



