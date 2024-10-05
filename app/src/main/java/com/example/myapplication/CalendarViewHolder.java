package com.example.myapplication;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder  extends RecyclerView.ViewHolder
{
    public TextView dayOfMonth,note1,note2,note3,note4,note5;
    public LinearLayout linearLayout;
    public LinearLayout constraintLayout;
//    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        linearLayout = itemView.findViewById(R.id.linearLayout);
        constraintLayout = itemView.findViewById(R.id.cstlayout);
        note1 = itemView.findViewById(R.id.note1);
        note2 = itemView.findViewById(R.id.note2);
        note3 = itemView.findViewById(R.id.note3);
        note4 = itemView.findViewById(R.id.note4);

    }
}
