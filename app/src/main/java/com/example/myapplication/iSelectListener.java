package com.example.myapplication;

import java.util.List;

public interface iSelectListener {
    interface onItemClickListDay{
        void onItemClicked(int position, String dayText);
    }
    interface onItemClickListNote{
        void onItemClickNoteList(Note note,String date);
        void onItemCheckClick(List<Integer> ids,boolean isCheck);
    }
    interface onItemClickListNote2{
        void onItemClickNoteList2(Note note);
        void onItemCheckClick(List<Integer> ids,boolean isCheck);
    }

}
