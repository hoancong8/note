package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {
    private String title,content,date,clock;
    private int id;
    private EditText editTitle,editdetail;
    private TextView selcetday,tvClock;
    private MyDbSqlite myDbSqlite;
    private Button bt;
    private String date1;
    private LocalDate localDate;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        date = intent.getStringExtra("date");
        clock = intent.getStringExtra("clock");
        calendar = Calendar.getInstance();

        myDbSqlite = new MyDbSqlite(this);
        editTitle = findViewById(R.id.editTitle);
        editdetail = findViewById(R.id.detailnote);
        selcetday = findViewById(R.id.selcetday);
        bt = findViewById(R.id.update);
        tvClock = findViewById(R.id.clock);
        calendar = Calendar.getInstance();
        tvClock.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute1 = calendar.get(Calendar.MINUTE);
            TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
                tvClock.setText(hourOfDay+":"+minute);
            };
            new TimePickerDialog(EditNoteActivity.this, timeSetListener, hour, minute1, true).show();
        });
        editTitle.setText(title);
        editdetail.setText(content);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        selcetday.setText(date);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDbSqlite.updateNote(id,editTitle.getText().toString(),editdetail.getText().toString(),selcetday.getText().toString(),tvClock.getText().toString(),0,"0");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("title",editTitle.getText().toString());
        intent.putExtra("detail",editdetail.getText().toString());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    public void calendardialog(View view){
        LocalDate localDate1 = LocalDate.now();
        DatePickerDialog pickerDialog = new DatePickerDialog(this,(view1, year, month, dayOfMonth) -> {
            localDate = LocalDate.of(year,month+1,dayOfMonth);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E,dd/MM/yyyy");
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date1= localDate.format(formatter1);
            selcetday.setText(localDate.format(formatter));
        },2024,localDate1.getMonthValue() - 1, localDate1.getDayOfMonth());
        pickerDialog.show();
    }
    public void back(View view){
        onBackPressed();
    }
}