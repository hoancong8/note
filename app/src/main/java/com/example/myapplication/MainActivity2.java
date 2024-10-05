package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class MainActivity2 extends AppCompatActivity {
    private EditText editTitle, detailnote;
    private MyDbSqlite myDbSqlite;
    private TextView date,clock;
    private String date1;
    private LocalDate localDate;
    private Calendar calendar;
    private boolean updateUI = false;
    private DateTimeFormatter formatter,formatter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editTitle = findViewById(R.id.editTitle);
        detailnote = findViewById(R.id.detailnote);
        clock = findViewById(R.id.clock);
        date = findViewById(R.id.selcetday);
        calendar = Calendar.getInstance();
        localDate = LocalDate.now();
        formatter = DateTimeFormatter.ofPattern("E,dd/MM/yyyy");
        formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date.setText(localDate.format(formatter));
        date1= localDate.format(formatter1);



        clock.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute1 = calendar.get(Calendar.MINUTE);
            TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
                clock.setText(hourOfDay+":"+minute);
            };
            new TimePickerDialog(MainActivity2.this, timeSetListener, hour, minute1, true).show();
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "High Priority Channel";
            String description = "This channel is used for important notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Đặt mức độ quan trọng cao
            NotificationChannel channel = new NotificationChannel("HIGH_PRIORITY_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
    public void back(View view){
        onBackPressed();
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
    }


    public void calendardialog(View view){
        LocalDate localDate1 = LocalDate.now();
        DatePickerDialog pickerDialog = new DatePickerDialog(this,(view1, year, month, dayOfMonth) -> {
//            Calendar selectedDate = Calendar.getInstance();
//            selectedDate.set(year, month, dayOfMonth);
            localDate = LocalDate.of(year,month+1,dayOfMonth);
//            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date1= localDate.format(formatter1);
            date.setText(localDate.format(formatter));
        },2024,localDate1.getMonthValue() - 1, localDate1.getDayOfMonth());
        pickerDialog.show();
    }

    public void save(View view) {
        myDbSqlite = new MyDbSqlite(MainActivity2.this);
        if (date1.isEmpty() || date1.equals("")){
//            myDbSqlite.addNote(editTitle.getText().toString().trim(), detailnote.getText().toString().trim(), 0, 0, "0","0");
            Toast.makeText(this, "khoong cos dux lieu", Toast.LENGTH_SHORT).show();
        }
        else {
            myDbSqlite.addNote(editTitle.getText().toString().trim(), detailnote.getText().toString().trim(), 0, 0,clock.getText().toString().trim(),date1);
            updateUI = true;
        }
//        getData();
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "HIGH_PRIORITY_CHANNEL_ID")
                .setSmallIcon(R.drawable.baseline_airplanemode_active_24)  // Biểu tượng nhỏ hiển thị trên thanh thông báo
                .setContentTitle("Tiêu đề thông báo")        // Tiêu đề của thông báo
                .setContentText("Nội dung của thông báo")    // Nội dung chi tiết của thông báo
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Độ ưu tiên cao
                .setDefaults(Notification.DEFAULT_ALL)       // Áp dụng tất cả các kiểu mặc định (âm thanh, rung, đèn)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //  ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    public void clockDialog(){

    }

}