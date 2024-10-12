package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;


// AlarmReceiver.java
public class AlarmService extends Service {
    private String title = "  ";
    private int sl = 0;
    public static boolean isService = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        title =
        startForeground(1,createNotification1(intent.getStringExtra("title"),NotificationManager.IMPORTANCE_LOW).build());
        if (!intent.getStringExtra("title").equals("0")){
        startForeground(1,createNotification(intent.getStringExtra("title"),NotificationManager.IMPORTANCE_HIGH).build());
        }


//        Log.d("LOG3333333",intent.getStringExtra("title1"));

//        Log.d("TAG77777",title);
        // Nếu cần, bạn có thể dừng dịch vụ tại đây hoặc để nó chạy trong nền
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isService = false;
    }






    private NotificationCompat.Builder createNotification(String t,int i) {

//        t = "Thông báo không có tiêu đề"; // Hoặc một giá trị khác hợp lý


        String channelId = "alarm_channel";
        String channelName = "Alarm Notification";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Kiểm tra xem kênh thông báo đã tồn tại chưa
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
                if (channel == null) { // Nếu kênh chưa được tạo, tạo mới
                    channel = new NotificationChannel(channelId, channelName, i);
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelNotificationIntent = new Intent(this, CancelNotification.class);
        cancelNotificationIntent.putExtra("title", t);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, cancelNotificationIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(t)
                .setContentText(t)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.baseline_airplanemode_active_24, "đánh dấu đã xong", pendingIntent1)
                .addAction(R.drawable.baseline_airplanemode_active_24, "hủy", pendingIntent1);

        return builder;
        // Chạy thông báo trong Foreground

    }
    private NotificationCompat.Builder createNotification1(String t, int i) {
        String channelId = "alarm_channel";
        String channelName = "Alarm Notification";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
                if (channel == null) {
                    channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW); // Đảm bảo IMPORTANCE_LOW
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(t)
                .setContentText(t) // Hạn chế nội dung nếu có thể
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW) // Giảm mức độ chú ý
                .setSound(null) // Tắt âm thanh
                .setVibrate(new long[]{0}) // Tắt rung
                ;

        return builder;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null; // Không cần phải triển khai cho trường hợp này
    }
}
