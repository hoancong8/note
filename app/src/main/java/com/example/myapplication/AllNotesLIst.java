package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllNotesLIst extends AppCompatActivity implements iSelectListener.onItemClickListNote {
    private RecyclerView recyclerView;
    private MyDbSqlite myDbSqlite;
    private List<Note> list;
    private AllNotesListAdapter notesListAdapter;
    private List<Integer> ids1;
    private boolean isCheck = false;
    private Button trash;
    private ImageView imageView;
    private TextView textView;
    private boolean checkTrash = false;
    private HashMap<Integer, Boolean> ids2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allnoteslist);
        list = new ArrayList<>();
        ids1 = new ArrayList<>();
        trash = findViewById(R.id.trash);
        textView = findViewById(R.id.emptyText);
        imageView = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.notelist);
        ids2 = new HashMap<>();


        handle();
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<Integer, Boolean> entry : ids2.entrySet()) {
                    if (entry.getValue()) { // Kiểm tra nếu giá trị là true
                        myDbSqlite.deleteById(entry.getKey());
                    }
                }
                handle();
                if (textView.getVisibility() == View.VISIBLE && imageView.getVisibility() == View.VISIBLE) {
                    checkTrash = !checkTrash;
                }
                notesListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void handle() {
        HashMap<String, List<Note>> noteMap = new HashMap<>();
        myDbSqlite = new MyDbSqlite(this);
        list.clear();
        getData();
        if (list.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        } else {
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //add list on hashmap
            for (Note e : list) {
                if (!noteMap.containsKey(e.getDate())) {
                    noteMap.put(e.getDate(), new ArrayList<Note>());
                }
                //f
                // Thêm Note vào danh sách của ngày đó
                noteMap.get(e.getDate()).add(e);

//            Log.d("TAG777777777",String.valueOf(e.getDate()));
            }


            List<Map.Entry<String, List<Note>>> noteList = new ArrayList<>(noteMap.entrySet());

            for (Map.Entry<String, List<Note>> entry : noteList) {
                String date = entry.getKey(); // Lấy ngày
                List<Note> notesForDate = entry.getValue(); // Lấy danh sách ghi chú cho ngày đó

                // Bây giờ bạn có thể lặp qua từng ghi chú trong notesForDate
                for (Note note : notesForDate) {
                    // Lấy các thông tin từ ghi chú
                    int noteId = note.getId();

                    // Xử lý hoặc hiển thị thông tin ghi chú ở đây
                    Log.d("TAG999999", String.valueOf(noteId));
                }
            }


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Sắp xếp danh sách dựa trên khóa (ngày tháng)
            noteList.sort((entry1, entry2) -> {
                LocalDate date1 = LocalDate.parse(entry1.getKey(), formatter);
                LocalDate date2 = LocalDate.parse(entry2.getKey(), formatter);
                return date1.compareTo(date2);  // Sắp xếp theo thứ tự tăng dần
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            notesListAdapter = new AllNotesListAdapter(this, noteList, this, checkTrash);
            recyclerView.setAdapter(notesListAdapter);
        }

    }

    public void getData() {
        Cursor cursor = myDbSqlite.readData(); // Gọi phương thức readData()
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "đéo có dữ liệu mờ xem", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                list.add(new Note(cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(6),
                        cursor.getInt(0),
                        cursor.getInt(3))
                );
            }
        }

    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!checkTrash) {
            super.onBackPressed();
        }
        else {
            trash.setVisibility(View.GONE);
            checkTrash = false;
            handle();
        }



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handle();
//        notesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickNoteList(Note note, String date) {
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
        ids2.put(ids, isCheck);
        Log.d("k123", "ids  " + String.valueOf(ids));
        Log.d("k123", "ids  " + String.valueOf(isCheck));
    }

    @Override
    public void onItemCheckClick1(boolean isCheck) {
        checkTrash = isCheck;
        if (isCheck) {
            trash.setVisibility(View.VISIBLE);
        }

    }
}
