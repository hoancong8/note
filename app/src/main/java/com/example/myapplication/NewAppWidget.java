package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy");
        String date = localDate.format(formatter1); // Sử dụng String cho ngày
//        String date = localDate.format(formatter1); // Sử dụng String cho ngày

        int[] textview = {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4};
        int i = 0;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.tvdate, localDate.format(formatter)); // Cập nhật nội dung với ngày hiện tại

        if (sreachByDay(date,context).size()>0){
            views.setViewVisibility(R.id.nottask, View.GONE);
//            views.setViewVisibility(R.id.nottask, View.VISIBLE);
//            views.setTextViewText(R.id.nottask,"Hôm nay không có lịch biểu");
            for (Note note: sreachByDay(date,context)) {
                views.setViewVisibility(textview[i], View.VISIBLE);
                views.setTextViewText(textview[i],note.getTitle());
//                Toast.makeText(context, note.getTitle(), Toast.LENGTH_SHORT).show();
                views.setViewVisibility(R.id.nottask, View.GONE);
                i = i + 1;
                if (i>3){
                    break;
                }
            }
        }
        else
        {
            views.setViewVisibility(R.id.nottask, View.VISIBLE);
            views.setTextViewText(R.id.nottask,"Hôm nay không có lịch biểu");
        }



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    public static List<Note> sreachByDay(String date, Context context){
        MyDbSqlite myDbSqlite = new MyDbSqlite(context);
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = myDbSqlite.sreachByDay(date);
        if(cursor.getCount()==0){

        }
        else
        {
//            Toast.makeText(context, "get date", Toast.LENGTH_SHORT).show();
            while(cursor.moveToNext()){
                noteList.add(new Note(cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(0),
                        cursor.getString(6)));
            }
        }
        return noteList;
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}