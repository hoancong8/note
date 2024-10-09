package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmService.class);
        Log.d("TAG77777","BroadcastReceiver");
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
        context.startForegroundService(serviceIntent);
    }
}
