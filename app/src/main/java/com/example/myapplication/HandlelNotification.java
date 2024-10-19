package com.example.myapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class HandlelNotification extends BroadcastReceiver {
    private MyDbSqlite myDbSqlite;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");

        Log.d("kkkkkk",action);
        if (action.equals("cancel")) {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
            AlarmWorker.mediaPlayer.stop();
            AlarmWorker.mediaPlayer.release();
            AlarmWorker.mediaPlayer = null;
        }
        if (action.equals("done")) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
            Log.d("kkkkkk",String.valueOf(intent.getIntExtra("id",0)));
            changeStatus(context,intent);
        }

    }
    private void changeStatus(Context context,Intent intent){
        myDbSqlite = new MyDbSqlite(context);
        myDbSqlite.updateNoteStatus(intent.getIntExtra("id",0),"1");
    }
}