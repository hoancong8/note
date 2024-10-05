package com.example.myapplication;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class CalendarDiffCallback extends DiffUtil.Callback {
    private final ArrayList<String> oldList;
    private final ArrayList<String> newList;

    public CalendarDiffCallback(ArrayList<String> oldList, ArrayList<String> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // So sánh ID hoặc một thuộc tính duy nhất của Note
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition)) ;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // So sánh nội dung của Note
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
