package com.example.richard.fetchpvtdata;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.richard.fetchpvtdata.com.example.richard.fetchpvtdata.utils.Constants;
import com.example.richard.fetchpvtdata.com.example.richard.fetchpvtdata.utils.SuCmd;
import com.example.richard.fetchpvtdata.com.example.richard.fetchpvtdata.utils.SuCmdJava;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    RunSuCmdBg runSuCmdBg;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * Created this AsyncTask in order to do execute the copy operation asynchronously
     */
    private class RunSuCmdBg extends AsyncTask<SuCmd, Void, Boolean> {
        //SuCmd targetFile;
        SuCmd suCmd;
        @Override
        protected Boolean doInBackground(SuCmd... suCmds) {

            //BuildConfig.DEBUG(suCmds.length == 1);
            suCmd = suCmds[0];
            return suCmd.suCopyFile();
        }

        @Override
        protected void onPostExecute(Boolean wasSuccessful) {
            String text, title;
            if (wasSuccessful) {
                text = getString(R.string.copy_success);
                title = String.format(getString(R.string.copy_notification_title_success),
                        suCmd.getTargetFile().getName());
            } else {
                text = getString(R.string.copy_fail);
                title = String.format(getString(R.string.copy_notification_title_fail),
                        suCmd.getTargetFile().getName());
            }


            notifyUser(title, text);
            Log.d(Constants.TAG, "onPostExecute: Ended file copy. Success: " + wasSuccessful);
        }
    }

    protected void notifyUser(String title, String notificationText) {
        Notification.Builder noteBuilder = new Notification.Builder(this);
        noteBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        noteBuilder.setContentTitle(title);
        noteBuilder.setContentText(notificationText);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, noteBuilder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runSuCmdBg = new RunSuCmdBg();

        // Listener for the Java button
        Button b = findViewById(R.id.java_copy);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File srcFile = new File(
                        "/data/data/com.android.messaging/databases/bugle_db");
                // Instantiates
                SuCmd suCmd = new SuCmdJava(srcFile,
                        new File(getExternalFilesDir(null),
                                srcFile.getName() + "_stolen"));

                runSuCmdBg.execute(suCmd);
            }
        });



        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
