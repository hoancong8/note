package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private MyDbSqlite myDbSqlite;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmService.class);
        String title = intent.getStringExtra("title");
        serviceIntent.putExtra("title",title);
        String action;
        if (intent.getAction()!=null){
            action = intent.getAction();
            serviceIntent.setAction(action);
        }
        else{
            action = " ";
        }
//        Log.d("TAG77777",title);
        if(!action.equals("ACTION_UPDATE_WIDGET")){
//            updateWidget(context);
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            // Yêu cầu WakeLock để bật màn hình và giữ nó sáng trong một thời gian
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE,
                    "MyApp::AlarmWakeLock");
            mediaPlayer = MediaPlayer.create(context, R.raw.sena_alarm); // Thay R.raw.alarm_sound bằng âm thanh của bạn
            mediaPlayer.setLooping(true); // Nếu muốn lặp lại âm thanh cho đến khi người dùng tắt
            mediaPlayer.start();
            // Kích hoạt WakeLock (màn hình sẽ sáng lên)
            wakeLock.acquire(3000);
            Log.d("LOG99999","chay den đay");
//            updateWidget(context);
        }

        context.startForegroundService(serviceIntent);
    }

    public List<Note> sreachByDay(String date,Context context){
        myDbSqlite = new MyDbSqlite(context);
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = myDbSqlite.sreachByDay(date);
        if(cursor.getCount()==0){

        }
        else
        {
            Toast.makeText(context, "get date", Toast.LENGTH_SHORT).show();
            while(cursor.moveToNext()){
                noteList.add(new Note(cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(0),
                        cursor.getString(6)));
            }
        }
        return noteList;
    }
    private void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, NewAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent);

        // Cập nhật nội dung widget
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            // Giả sử bạn muốn cập nhật ngày vào widget
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String currentDate = localDate.format(formatter);
            views.setTextViewText(R.id.textViewOutput, currentDate);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
