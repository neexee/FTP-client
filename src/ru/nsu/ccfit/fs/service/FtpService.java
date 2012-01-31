package ru.nsu.ccfit.fs.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ru.nsu.ccfit.fs.ftp.Ftp;
import ru.nsu.ccfit.fs.ftp.Login;
import ru.nsu.ccfit.fs.command.Command;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: violetta
 * Date: 12/11/11
 * Time: 7:29 PM
 */
public class FtpService extends Service {
    private final static String TAG = "FtpService";
    public final static String COMMAND = "COMMAND";
    private final IBinder mBinder = new LocalBinder();
    Ftp ftp = null;
    public class LocalBinder extends Binder {
       public FtpService getService() {
           Log.w(TAG, FtpService.this.toString());
           return FtpService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
       // Log.w(TAG, "Binded.") ;
        return null;
    }


   public int onStartCommand(final Intent intent, int flags, int startId)
   {
       Bundle command_ = intent.getExtras();
               if (ftp == null)
               {

                 //  Bundle loginWrap = intent.getExtras();
                   Login  login = (Login)command_.get("LOGIN");

                   ftp = new Ftp(login);
               }



                Command command = (Command)command_.get("COMMAND");
                String args = (String)command_.get("ARG");
                  try
                  {
                        Serializable result = command.run(ftp,args);
                        Intent answer = new Intent(COMMAND);
                        Bundle resultWrap = new Bundle();
                        resultWrap.putSerializable("RESULT", result);
                        answer.putExtras(resultWrap);
                        answer.putExtra("CURRENT_DIR", ftp.getCurrentDir()) ;
                        Log.d(TAG, "Broadcast sended!");
                        sendBroadcast(answer);
                  }
                  catch (IOException e)
                  {
                         e.printStackTrace();
                  }



       return START_STICKY;
   }



 
    @Override
	public void onDestroy() {
        super.onDestroy();
		Toast.makeText(this, "Stopping", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
         try
          {
            ftp.stop();
          }
          catch (IOException e)
          {
              e.printStackTrace();
          }

	}
    @Override
	public void onCreate() {
        super.onCreate();
		Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");

	}

}
