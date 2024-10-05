package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    TextView tvTitle,tvContent;
    ImageButton btEdittext;
    String title,content,date,clock;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        date = intent.getStringExtra("date");
        clock = intent.getStringExtra("clock");


        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        tvTitle = findViewById(R.id.title);
        tvContent = findViewById(R.id.content);
        btEdittext = findViewById(R.id.edittext);

        tvTitle.setText(title);
        tvContent.setText(content);

        btEdittext.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailActivity.this,UpdateActivity.class);
            intent1.putExtra("id",id);
            intent1.putExtra("date", date);
            intent1.putExtra("content", content);
            intent1.putExtra("title", title);
            intent1.putExtra("clock", clock);
            startActivity(intent1);
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void back(View view){
        onBackPressed();
    }
}