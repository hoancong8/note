package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteListActivity extends AppCompatActivity implements iSelectListener.onItemClickListNote2 {
    private TextView emptyText,tvDay;
    private RecyclerView recyclerView;
    private MyDbSqlite myDbSqlite;
    private LocalDate localDate;
    private DateTimeFormatter formatter;
    private ImageView imageView;
    private NoteListAdapter2 noteListAdapter2;
    private FloatingActionButton floatingActionButton;
    private List<Integer> ids1;
    private Button trash;
    private HashMap<Integer,Boolean> ids2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        myDbSqlite = new MyDbSqlite(this);
        recyclerView = findViewById(R.id.recycler);
        imageView = findViewById(R.id.imageView);
        emptyText = findViewById(R.id.emptyText);
        tvDay = findViewById(R.id.tvDay);
        floatingActionButton = findViewById(R.id.addNote);
        ids1 = new ArrayList<>();
        trash = findViewById(R.id.trash);
        ids2 = new HashMap<>();


        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Map.Entry<Integer, Boolean> entry : ids2.entrySet()) {
                    if (entry.getValue()) { // Kiểm tra nếu giá trị là true
                        myDbSqlite.deleteById(entry.getKey());
                    }
                }

                checkRCV();
                noteListAdapter2.notifyDataSetChanged();
            }
        });

        Intent intent = getIntent();
        String value1 = intent.getStringExtra("key1");
        int value2 = intent.getIntExtra("key2",0);
        int value3 = intent.getIntExtra("key3",0);
        localDate = LocalDate.of(value3,value2,Integer.parseInt(value1));
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tvDay.setText("Lich biểu "+localDate.format(formatter));
        checkRCV();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivity(intent1);
            }
        });
    }
    public void checkRCV(){
        if (sreachByDay(localDate.format(formatter))!= null){
            noteListAdapter2 = new NoteListAdapter2(this,sreachByDay(localDate.format(formatter)),this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(noteListAdapter2);
        }
    }
    public List<Note> sreachByDay(String date){
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = myDbSqlite.sreachByDay(date);
        if(cursor.getCount()==0){
            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "not data"+localDate.format(formatter), Toast.LENGTH_SHORT).show();
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
//            Toast.makeText(this, "get date", Toast.LENGTH_SHORT).show();
            while(cursor.moveToNext()){
                noteList.add(new Note(cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(6),
                        cursor.getInt(0),
                        cursor.getInt(3)
                        ));
            }
        }
        return noteList;
    }
    public void back(View view){
        onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkRCV();
    }

    @Override
    public void onItemClickNoteList2(Note note) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", note.getId());
        intent.putExtra("date", note.getDate());
        intent.putExtra("content", note.getContentTitle());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("clock", note.getClock());
        startActivity(intent);

    }

    @Override
    public void onItemCheckClick(int ids, boolean isCheck) {
        Log.d("k1234",String.valueOf(ids)+ "  " + isCheck);
        ids2.put(ids,isCheck);
    }

    @Override
    public void onItemCheckClick1(boolean isCheck) {
        if (isCheck){
            trash.setVisibility(View.VISIBLE);
        }
    }

}