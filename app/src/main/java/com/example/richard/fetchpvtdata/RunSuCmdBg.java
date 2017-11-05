package com.example.richard.fetchpvtdata;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.richard.fetchpvtdata.utils.Constants;
import com.example.richard.fetchpvtdata.utils.SuCmd;

/**
 * Created this AsyncTask in order to do execute the copy asynchronously with the
 * main thread, since the file could take some minutes to copy.
 */
class RunSuCmdBg extends AsyncTask<SuCmd, Void, Boolean> {
    private SuCmd suCmd;
    @SuppressLint("StaticFieldLeak")
    private Context appContext;
    RunSuCmdBg(Context c) {
        appContext = c.getApplicationContext();
    }

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
            text = appContext.getString(R.string.copy_success);
            title = String.format(
                    appContext.getString(R.string.copy_notification_title_success),
                    suCmd.getTargetFile().getName());
        } else {
            text = appContext.getString(R.string.copy_fail);
            title = String.format(
                    appContext.getString(R.string.copy_notification_title_fail),
                    suCmd.getTargetFile().getName());
        }


        notifyUser(title, text);
        Log.d(Constants.TAG, "onPostExecute: Ended file copy. Success: " + wasSuccessful);
    }

    /**
     * Creates a simple notification
     * @param title Title of notification
     * @param notificationText Text of notification
     */
    private void notifyUser(String title, String notificationText) {
        Notification.Builder noteBuilder = new Notification.Builder(appContext);
        noteBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        noteBuilder.setContentTitle(title);
        noteBuilder.setContentText(notificationText);

        NotificationManager notificationManager =
                (NotificationManager)appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, noteBuilder.build());
    }
}