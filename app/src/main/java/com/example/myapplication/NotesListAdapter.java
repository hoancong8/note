package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {
    private Context context;
    private List<Map.Entry<String, List<Note>>> noteList;
    private iSelectListener.onItemClickListNote selectListener;
    public NotesListAdapter(Context context, List<Map.Entry<String, List<Note>>> noteList,iSelectListener.onItemClickListNote selectListener) {
        this.context = context;
        this.noteList = noteList;
        this.selectListener = selectListener;


    }
    ViewGroup viewGroup;
    @NonNull
    @Override
    public NotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        viewGroup = parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.ViewHolder holder, int position) {
        holder.linearLayout.removeAllViews();
        Map.Entry<String,List<Note>> entry = noteList.get(position);
        List<Note> notes = entry.getValue();
        holder.create(entry.getKey());
        Log.d("TAG77777777777777","ji"+entry.getKey());
        for (Note n: notes) {

            View noteView = LayoutInflater.from(context)
                    .inflate(R.layout.item_note, holder.linearLayout, false);
            // Tìm các TextView trong noteView
            TextView noteTitleTextView = noteView.findViewById(R.id.title);
            TextView clock = noteView.findViewById(R.id.clock);
            // Thiết lập dữ liệu cho các TextView
            noteTitleTextView.setText(n.getTitle());
            clock.setText(n.getClock());
            noteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectListener.onItemClickNoteList(n,entry.getKey()); // Gọi phương thức với n
                    Log.d("TAG55555", String.valueOf(n.getId())); // In ID
                }
            });
            holder.linearLayout.addView(noteView);

        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
        public ViewHolder(@NonNull View view) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            textView = view.findViewById(R.id.title);
        }
        public void create(String text){
            TextView newTextView = new TextView(context);
            newTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            newTextView.setText(text);
            newTextView.setTextSize(20); // Đặt kích thước chữ
            // Thêm TextView vào LinearLayout

            linearLayout.addView(newTextView,0);
        }
    }
}
