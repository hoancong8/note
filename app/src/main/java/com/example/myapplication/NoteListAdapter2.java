package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteListAdapter2 extends RecyclerView.Adapter<NoteListAdapter2.ViewHolder> {
    private Context context;
    private List<Note> noteList;
    private iSelectListener.onItemClickListNote2 itemClickListNote2;
    private MyDbSqlite myDbSqlite;
    private boolean isRadioButtonVisible = false;
    public NoteListAdapter2(Context context, List<Note> noteList, iSelectListener.onItemClickListNote2 itemClickListNote2) {
        this.context = context;
        this.noteList = noteList;
        this.itemClickListNote2 = itemClickListNote2;
        myDbSqlite = new MyDbSqlite(context);
    }

    public NoteListAdapter2(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteListAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter2.ViewHolder holder, int position) {

        Note note = noteList.get(position);
        holder.title.setPaintFlags(holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        if (note.getStatus1()==1){
            holder.title.setPaintFlags( holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setChecked(true);
        }
        holder.title.setText(note.getTitle());
        holder.clock.setText(note.getClock());
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isRadioButtonVisible=true;
                itemClickListNote2.onItemCheckClick1(isRadioButtonVisible);
                notifyDataSetChanged();
                return true;
            }
        });


        if (isRadioButtonVisible) {
            holder.radioButton.setVisibility(View.VISIBLE);
        } else {
            holder.radioButton.setVisibility(View.GONE);

        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRadioButtonVisible){
                    holder.checkBox.setEnabled(false);
                    holder.radioButton.setChecked(!holder.radioButton.isChecked());

                    itemClickListNote2.onItemCheckClick(note.getId(),holder.radioButton.isChecked());
                }
                else {
                    holder.checkBox.setEnabled(true);
                    itemClickListNote2.onItemClickNoteList2(note);
                }
            }
        });

        //check status note
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Nếu CheckBox được chọn, bật gạch ngang
                    holder.title.setPaintFlags( holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    myDbSqlite.updateNoteStatus(note.getId(),"1");
                } else {
                    // Nếu CheckBox không được chọn, tắt gạch ngang
                    holder.title.setPaintFlags( holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    myDbSqlite.updateNoteStatus(note.getId(),"0");
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,clock;
        private LinearLayout linearLayout;
        private CheckBox checkBox;
        private CheckBox radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkb);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            title = itemView.findViewById(R.id.title);
            clock = itemView.findViewById(R.id.clock);
             radioButton = itemView.findViewById(R.id.checkDelete);
        }
    }
}
