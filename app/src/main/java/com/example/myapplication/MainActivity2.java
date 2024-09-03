package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    private EditText editTitle,detailnote;
    private MyDbSqlite myDbSqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editTitle = findViewById(R.id.editTitle);
        detailnote = findViewById(R.id.detailnote);

    }
    public void save(View view){
        myDbSqlite = new MyDbSqlite(MainActivity2.this);
        myDbSqlite.addNote(editTitle.getText().toString().trim(),detailnote.getText().toString().trim(),0,0,"0");
        getData();
    }
    public void getData() {
        Cursor cursor = myDbSqlite.readData(); // Gọi phương thức readData()
        if(cursor.getCount()==0){
            Toast.makeText(MainActivity2.this, "not data", Toast.LENGTH_SHORT).show();
        }
        else
        {
//            while(cursor.moveToNext()){
//                list.add(new Word(cursor.getString(1),cursor.getString(2),cursor.getString(3)));
//            }
        }

        // Đóng Cursor sau khi sử dụng
        if (cursor != null) {
            cursor.close();
        }
    }
}