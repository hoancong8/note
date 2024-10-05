package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MyDbSqlite extends SQLiteOpenHelper {
    private final Context context;
    private static final String COLUMN_ID = "iId";
    private static final String COLUMN_TITLE = "sTitle";
    private static final String COLUMN_DETAIL = "sDetail";
    private static final String COLUMN_STATUS = "iStatus";
    private static final String COLUMN_DATE = "dDate";
    private static final String COLUMN_CLOCK = "sClock";
    private static final String COLUMN_REPEAT ="iRepeat";
    private static final String TABLE_NAME = "Note";
    private static final String Query_cr_table="CREATE TABLE " + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DETAIL + " TEXT, "
            + COLUMN_STATUS + " INT DEFAULT 0, "
            + COLUMN_REPEAT + " INT DEFAULT 0, "
            + COLUMN_DATE + " DATETIME, "
            + COLUMN_CLOCK + " TEXT);";
    private static final String DATABASE_NAME = "mynote.db";
    private static final int DATABASE_VERSION = 1;
    public MyDbSqlite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Query_cr_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addNote(String title, String detail, int status,int repeat,String clock, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DETAIL,detail);
        cv.put(COLUMN_STATUS,status);
        cv.put(COLUMN_REPEAT,repeat);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_CLOCK,clock);

        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1){
            Toast.makeText(context,"Thêm thất bại",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateNote(int id, String title, String content, String date, String clock ,String status,String repeat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DETAIL,content);
        cv.put(COLUMN_STATUS,status);
        cv.put(COLUMN_REPEAT,repeat);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_CLOCK,clock);
        // Cập nhật Note với id tương ứng

        long result = db.update(TABLE_NAME, cv, COLUMN_ID+"=?", new String[]{String.valueOf(id)});

        if (result > 0) {
            Toast.makeText(context, "cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
//
    public Cursor sreachByDay(String date){
        String query="SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_DATE + "= " + "'" +date+"'";
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor=null;
        if(db != null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    public Cursor sreachByMonthYear(String month,String year){
//        String query = "SELECT * FROM "+TABLE_NAME+" WHERE strftime('%m', " + COLUMN_DATE + ") = " + "'" + month + "'" + " AND strftime('%Y', " + COLUMN_DATE + ") = "+"'" +year+"'";
        String query1 = "SELECT * FROM Note WHERE substr(dDate, 4, 2) = '" + month + "' AND substr(dDate, 7, 4) = '" + year + "';";

        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor=null;
        if(db != null){
            cursor= db.rawQuery(query1,null);
        }
        return cursor;
    }
//    public Cursor deleteByEl(String el){
//        String query="DELETE FROM " + TABLE_NAME +" WHERE " + COLUMN_EN + "= " + "'" +el+"'";
//        SQLiteDatabase db= this.getWritableDatabase();
//        Cursor cursor=null;
//        if(db != null){
//            cursor= db.rawQuery(query,null);
//        }
//        return cursor;
//    }


    public Cursor readData(){
        String query="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor=null;
        if(db != null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }


//    public void delete(){
//        String query = "DELETE FROM " + TABLE_NAME ;
//        SQLiteDatabase db= this.getWritableDatabase();
//        db.execSQL(query);
//    }
}
