package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView tvTitle,tvContent;
    private ImageButton btEdittext,btnDelete;
    private String title,content,date,clock;
    private int id;
    private MyDbSqlite myDbSqlite;
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
        myDbSqlite  = new MyDbSqlite(this);



//        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        tvTitle = findViewById(R.id.title);
        tvContent = findViewById(R.id.content);
        btEdittext = findViewById(R.id.edittext);
        btnDelete = findViewById(R.id.delete);



        tvTitle.setText(title);
        tvContent.setText(content);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        btEdittext.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailActivity.this,UpdateActivity.class);
            intent1.putExtra("id",id);
            intent1.putExtra("date", date);
            intent1.putExtra("content", content);
            intent1.putExtra("title", title);
            intent1.putExtra("clock", clock);
            activityResultLauncher.launch(intent1);
        });

    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa bản ghi")
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi phương thức deleteRecord ở đây
                        if (myDbSqlite.deleteById(id)){
                            Toast.makeText(DetailActivity.this, "ok", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(DetailActivity.this, "not ok", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Đóng dialog nếu người dùng chọn "Không"
                    }
                });

        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> {
                if (o.getResultCode() == Activity.RESULT_OK){
                    Intent data = o.getData();
                    if (data != null) {
                        String title = data.getStringExtra("title");
                        String detail = data.getStringExtra("detail");

                        tvTitle.setText(title);
                        tvContent.setText(detail);

                    }
                }
            });

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void back(View view){
        onBackPressed();
    }
}