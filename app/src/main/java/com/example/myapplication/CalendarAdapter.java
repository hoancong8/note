package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private LocalDate selectedDate;
    private String month,year;
    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        selectedDate = LocalDate.now();
    }
    public CalendarAdapter(String year, String month, ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.year = year;
        this.month = month;
        this.selectedDate = LocalDate.now();
        this.onItemListener = onItemListener;
        this.daysOfMonth = daysOfMonth;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        if(month.equals(monthFromDate(selectedDate))
                && year.equals(yearFromDate(selectedDate))
                && daysOfMonth.get(position).equals(String.valueOf(selectedDate.getDayOfMonth()))){
            holder.linearLayout.setBackgroundResource(R.drawable.circle);
        }
    }
    private String monthFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }
    private String yearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }
    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}