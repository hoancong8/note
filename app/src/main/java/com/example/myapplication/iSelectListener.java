package com.example.myapplication;

public interface iSelectListener {
    interface onItemClickListDay{
        void onItemClicked(int position, String dayText);
    }
    interface onItemClickListNote{
        void onItemClickNoteList(Note note,String date);    }
    interface onItemClickListNote2{
        void onItemClickNoteList2(Note note);    }

}
