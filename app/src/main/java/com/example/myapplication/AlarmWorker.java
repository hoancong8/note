package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlarmWorker extends Worker {
    public static MediaPlayer mediaPlayer;
    Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sena_alarm);

    // Cấu hình các thuộc tính âm thanh
    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
//        soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sena_alarm);
    }
    @NonNull
    @Override
    public Result doWork() {
        // Công việc cần thực hiện khi báo thức kêu
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);

        // Yêu cầu WakeLock để bật màn hình và giữ nó sáng trong một thời gian
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE,
                "MyApp::AlarmWakeLock");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sms); // Thay R.raw.alarm_sound bằng âm thanh của bạn
        mediaPlayer.setLooping(true); // lặp lại âm thanh
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

        // Kích hoạt WakeLock (màn hình sẽ sáng lên)
        wakeLock.acquire(3000);
        showAlarmNotification();
        return Result.success();
    }


    private void showAlarmNotification() {
        // Hiển thị thông báo hoặc thực hiện một tác vụ gì đó
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent handleEvent = new Intent(context, HandlelNotification.class);
        Intent handleEvent1 = new Intent(context, HandlelNotification.class);

        handleEvent.setAction("cancel_alarm_action");
        handleEvent.putExtra("action", "cancel");
        PendingIntent cancel = PendingIntent.getBroadcast(context, 0, handleEvent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        handleEvent1.setAction("done_alarm_action");
        handleEvent1.putExtra("action", "done");
        handleEvent1.putExtra("id", getInputData().getInt("id", 0));
        PendingIntent done = PendingIntent.getBroadcast(context, 0, handleEvent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Tạo thông báo
        Notification notification = new NotificationCompat.Builder(context, "alarm_channel")
                .setContentTitle(getInputData().getString("title"))
                .setContentText("Báo thức của bạn đã đến giờ!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher_foreground, "Hủy", cancel) // Thêm nút "Dismiss"
                .addAction(R.drawable.ic_launcher_foreground, "Đánh dấu đã xong", done) // Thêm nút "Done"
                .setAutoCancel(true) // Tự động hủy thông báo khi người dùng nhấn vào
                .build();

        // Hiển thị thông báo
        notificationManager.notify(1, notification);
    }

    private void showAlarmNotification1() {
        // Hiển thị thông báo hoặc thực hiện một tác vụ gì đó
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent handleEvent = new Intent(context, HandlelNotification.class);
        Intent handleEvent1 = new Intent(context, HandlelNotification.class);

        handleEvent.setAction("cancel_alarm_action");
        handleEvent.putExtra("action", "cancel");
        PendingIntent cancel = PendingIntent.getBroadcast(context, 0, handleEvent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        handleEvent1.setAction("done_alarm_action");
        handleEvent1.putExtra("action", "done");
        Log.d("kkkkk","ji   "  +String.valueOf(getInputData().getInt("id",0)));
        handleEvent1.putExtra("id",getInputData().getInt("id",0));
        PendingIntent done = PendingIntent.getBroadcast(context, 0, handleEvent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);



        // Tạo thông báo
        Notification notification = new NotificationCompat.Builder(context, "alarm_channel")
                .setContentTitle(getInputData().getString("title"))
                .setContentText("Báo thức của bạn đã đến giờ!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher_foreground, "hủy", cancel) // Thêm nút "Dismiss"
                .addAction(R.drawable.ic_launcher_foreground, "đánh dấu đã xong", done) // Thêm nút "Dismiss"
                .setAutoCancel(true) // Tự động hủy thông báo khi người dùng nhấn vào
                .build();


        Log.d("jjjjj","ok");

        // Hiển thị thông báo
        notificationManager.notify(1, notification);
    }
}
