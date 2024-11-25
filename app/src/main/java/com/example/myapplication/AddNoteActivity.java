package com.example.myapplication;


import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;


import android.database.Cursor;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import java.util.concurrent.TimeUnit;


public class AddNoteActivity extends AppCompatActivity {
    private EditText editTitle, detailnote;
    private MyDbSqlite myDbSqlite;
    private TextView date,clock;
    private String date1;
    private LocalDate localDate;
    private Calendar calendar;
    private boolean updateUI = false;
    private DateTimeFormatter formatter,formatter1;
    private Switch aSwitch;
    private Note note;
    private int dd,mm,yyyy,h,m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
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
        aSwitch = findViewById(R.id.switch1);




        yyyy =  calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd =  calendar.get(Calendar.DAY_OF_MONTH);
        h =  calendar.get(Calendar.HOUR_OF_DAY);
        m =  calendar.get(Calendar.MINUTE);


        clock.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) + ":"+String.valueOf(calendar.get(Calendar.MINUTE))));


        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                clock.setText("0");
                clock.setVisibility(View.GONE);
            }
            else {
                clock.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) + ":"+String.valueOf(calendar.get(Calendar.MINUTE))));
                clock.setVisibility(View.VISIBLE);
            }
        });

        clock.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute1 = calendar.get(Calendar.MINUTE);
            TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
                clock.setText(hourOfDay+":"+minute);
                h=hourOfDay;
                m=minute;
            };
            new TimePickerDialog(AddNoteActivity.this, timeSetListener, hour, minute1, true).show();
        });
    }



    public void back(View view){
        onBackPressed();
    }

    public void calendardialog(View view){
        LocalDate localDate1 = LocalDate.now();
        DatePickerDialog pickerDialog = new DatePickerDialog(this,(view1, year, month, dayOfMonth) -> {
            yyyy = year;
            mm = month;
            dd = dayOfMonth;
            localDate = LocalDate.of(year,month+1,dayOfMonth);
//            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date1= localDate.format(formatter1);
            date.setText(localDate.format(formatter));
        },2024,localDate1.getMonthValue() - 1, localDate1.getDayOfMonth());
        pickerDialog.show();
    }



    //save
    public void save(View view) {
        myDbSqlite = new MyDbSqlite(AddNoteActivity.this);
        if (date1.isEmpty() || date1.equals("")){
            Toast.makeText(this, "khoong cos dux lieu", Toast.LENGTH_SHORT).show();
        }
        else {
            myDbSqlite.addNote(editTitle.getText().toString().trim(), detailnote.getText().toString().trim(), 0, 0,clock.getText().toString().trim(),date1);
            updateUI = true;
            Log.d("LOG00000",String.valueOf(h+" "+m+" "+dd+" "+mm));
            scheduleAlarmWork(yyyy,mm,dd,h,m,getDataRecent().getId());


            //update widget
            Intent intent = new Intent(this, NewAppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Gửi cùng với danh sách ID của widget để cập nhật
            int[] ids = AppWidgetManager.getInstance(getApplication())
                    .getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            // send Broadcast
            sendBroadcast(intent);
        }
    }



    //get note final list
    public Note getDataRecent(){
        Cursor cursor = myDbSqlite.dataRecent();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "not data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                note = new Note(cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(0),
                        cursor.getString(6));
            }
        }
        Log.d("LOG00000",String.valueOf(note.getId()));
        return note;
    }



    //set alarm
    private void scheduleAlarmWork(int yyyy, int mm, int dd, int h, int m, int id) {
        Calendar calendar = Calendar.getInstance();

        // Đặt giá trị ngày, tháng, năm
        calendar.set(Calendar.YEAR, yyyy);
        calendar.set(Calendar.MONTH, mm); // mm đã được truyền vào từ DatePicker (đảm bảo mm chính xác từ DatePicker)
        calendar.set(Calendar.DAY_OF_MONTH, dd);

        // Đặt giá trị giờ và phút
        calendar.set(Calendar.HOUR_OF_DAY, h); // h lấy từ TimePicker
        calendar.set(Calendar.MINUTE, m); // m lấy từ TimePicker
        calendar.set(Calendar.SECOND, 0);
        long time = calendar.getTimeInMillis()-System.currentTimeMillis();


        Data data = new Data.Builder()
                .putInt("id",id).putString("title",getDataRecent().getTitle())
                .build();


        WorkManager workManager = WorkManager.getInstance(this);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();
        workManager.enqueue(workRequest);
    }
}