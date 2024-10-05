package com.example.myapplication;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private List<Note> notesDay;
//    private final OnItemListener onItemListener;
    private final LocalDate selectedDate;
    private String month, year;
    private iSelectListener.onItemClickListDay selectListener;
    ArrayList<String> list;
//
//    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
//        this.daysOfMonth = daysOfMonth;
//        this.onItemListener = onItemListener;
//        selectedDate = LocalDate.now();
//    }

    public CalendarAdapter(List<Note> notesDay,String year, String month, ArrayList<String> daysOfMonth,iSelectListener.onItemClickListDay selectListener) {
        this.year = year;
        this.month = month;
        this.selectedDate = LocalDate.now();
        this.selectListener = selectListener;
        this.daysOfMonth = daysOfMonth;
        list = new ArrayList<>();
        this.notesDay = notesDay;
    }

    public void updateMonthList() {
        this.year = year;
        this.month = month;

        // Tạo một bản sao của daysOfMonth trước khi sử dụng DiffUtil
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CalendarDiffCallback(daysOfMonth, daysOfMonth));

//        daysOfMonth.clear();
//        daysOfMonth.addAll(newList);
        notifyItemRangeRemoved(0, daysOfMonth.size());
        //tell the recycler view how many new items we added
        notifyItemRangeInserted(0, daysOfMonth.size());
        // Áp dụng các thay đổi từ DiffUtil
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateMonthList1(String year, String month, ArrayList<String> newList) {
        this.year = year;
        this.month = month;

        // Tạo một bản sao của daysOfMonth trước khi sử dụng DiffUtil
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CalendarDiffCallback(daysOfMonth, newList));

        daysOfMonth.clear();
        daysOfMonth.addAll(newList);
        notifyItemRangeRemoved(0, daysOfMonth.size());
        //tell the recycler view how many new items we added
        notifyItemRangeInserted(0, daysOfMonth.size());
        // Áp dụng các thay đổi từ DiffUtil
        diffResult.dispatchUpdatesTo(this);
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        if (month.equals(monthFromDate(selectedDate))) {
            if (year.equals(yearFromDate(selectedDate))) {
                if (daysOfMonth.get(position).equals(String.valueOf(selectedDate.getDayOfMonth()))) {
                    holder.linearLayout.setBackgroundResource(R.drawable.circle);
                }
            }
        }
        TextView[] t = new TextView[4];
        t[0] = holder.note1;
        t[1] = holder.note2;
        t[2] = holder.note3;
        t[3] = holder.note4;

        int i =0;
        for (Note no: notesDay) {
            String[] parts = no.getTitle().split("/");
            // Phần tử thứ hai là tháng
//            String day = parts[0];
            String day = parts[0].replaceFirst("^0","");
//            Log.d("TAGGGGGG",day1[1]);
            String month1 = parts[1];
            String year1 = parts[2];
            if (year.equals(year1)){
                if ( month1.equals(month)){
                    if (day.equals(daysOfMonth.get(position))){
                        t[i].setVisibility(View.VISIBLE);
                        t[i].setText(no.getContentTitle());
                        i=i+1;
                        if (i==4){
                            break;
                        }
                    }
                }
            }

        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    selectListener.onItemClicked(currentPosition, daysOfMonth.get(currentPosition));
                    Log.d("TAG", "Item clicked at position: " + currentPosition);
                }
                Log.d("TAG","ji");
            }
        });
    }

    private String monthFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }
    private String monthFromDate1(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }
    private String yearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }
}