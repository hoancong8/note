package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

// AlarmReceiver.java
public class AlarmService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();
        Log.d("TAG77777","service");
        // Nếu cần, bạn có thể dừng dịch vụ tại đây hoặc để nó chạy trong nền
        return START_STICKY;
    }

    private void createNotification() {
        String channelId = "alarm_channel";
        String channelName = "Alarm Notification";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Kiểm tra xem kênh thông báo đã tồn tại chưa
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
                if (channel == null) { // Nếu kênh chưa được tạo, tạo mới
                    channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm")
                .setContentText("It's time!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Chạy thông báo trong Foreground
        startForeground(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Không cần phải triển khai cho trường hợp này
    }
}
