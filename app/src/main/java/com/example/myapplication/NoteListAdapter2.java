package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteListAdapter2 extends RecyclerView.Adapter<NoteListAdapter2.ViewHolder> {
    private Context context;
    private List<Note> noteList;
    private iSelectListener.onItemClickListNote2 itemClickListNote2;

    public NoteListAdapter2(Context context, List<Note> noteList, iSelectListener.onItemClickListNote2 itemClickListNote2) {
        this.context = context;
        this.noteList = noteList;
        this.itemClickListNote2 = itemClickListNote2;
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
        holder.clock.setText(note.getClock());
        holder.title.setText(note.getTitle());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListNote2.onItemClickNoteList2(note);
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            title = itemView.findViewById(R.id.title);
            clock = itemView.findViewById(R.id.clock);
        }
    }
}
