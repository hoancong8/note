package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class MyDbSqlite extends SQLiteOpenHelper {
    private Context context;
    private static final String COLUMN_ID = "iId";
    private static final String COLUMN_TITLE = "sTitle";
    private static final String COLUMN_DETAIL = "sDetail";
    private static final String COLUMN_STATUS = "iStatus";
    private static final String COLUMN_DATE = "dDate";
    private static final String COLUMN_REPEAT ="iRepeat";
    private static final String TABLE_NAME = "Note";
    private static final String Query_cr_table="CREATE TABLE " + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DETAIL + " TEXT, "
            + COLUMN_STATUS + " INT DEFAULT 0, "
            + COLUMN_REPEAT + " INT DEFAULT 0, "
            + COLUMN_DATE + " DATETIME);";
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
    public void convertTime(){

    }
    public void addNote(String title, String detail, int status,int repeat, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DETAIL,detail);
        cv.put(COLUMN_STATUS,status);
        cv.put(COLUMN_REPEAT,repeat);
        cv.put(COLUMN_DATE, date);
        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1){
            Toast.makeText(context,"Thêm thất bại",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }



//
//    public Cursor sreach_el(String el){
//        String query="SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_EN + "= " + "'" +el+"'";
//        SQLiteDatabase db= this.getWritableDatabase();
//        Cursor cursor=null;
//        if(db != null){
//            cursor= db.rawQuery(query,null);
//        }
//        return cursor;
//    }
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
