package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

    public class AllNotesListAdapter extends RecyclerView.Adapter<AllNotesListAdapter.ViewHolder> {
        private Context context;
        private List<Map.Entry<String, List<Note>>> noteList;
        private iSelectListener.onItemClickListNote selectListener;
        private MyDbSqlite myDbSqlite;
        private boolean isRadioButtonVisible = false;


        public AllNotesListAdapter(Context context, List<Map.Entry<String, List<Note>>> noteList, iSelectListener.onItemClickListNote selectListener, boolean checkTrash) {
            this.context = context;
            this.noteList = noteList;
            this.selectListener = selectListener;
            myDbSqlite = new MyDbSqlite(context);
            this.isRadioButtonVisible = checkTrash;
        }

        ViewGroup viewGroup;

        @NonNull
        @Override
        public AllNotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
            viewGroup = parent;
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllNotesListAdapter.ViewHolder holder, int position) {
            holder.linearLayout.removeAllViews();
            Map.Entry<String, List<Note>> entry = noteList.get(position);
            List<Note> notes = entry.getValue();
            holder.create(entry.getKey());
            for (Note n : notes) {

                View noteView = LayoutInflater.from(context)
                        .inflate(R.layout.item_note, holder.linearLayout, false);

                // Tìm các TextView trong noteView
                TextView noteTitleTextView = noteView.findViewById(R.id.title);
                TextView clock = noteView.findViewById(R.id.clock);
                CheckBox checkBox = noteView.findViewById(R.id.checkb);
                CheckBox radioButton = noteView.findViewById(R.id.checkDelete);



                if (n.getStatus1() == 1) {
                    noteTitleTextView.setPaintFlags(noteTitleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkBox.setChecked(true);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // Nếu CheckBox được chọn, bật gạch ngang
                            noteTitleTextView.setPaintFlags(noteTitleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            myDbSqlite.updateNoteStatus(n.getId(), "1");

    //                        Toast.makeText(context, n.getId(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Nếu CheckBox không được chọn, tắt gạch ngang
                            noteTitleTextView.setPaintFlags(noteTitleTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                            myDbSqlite.updateNoteStatus(n.getId(), "0");

                        }
                    }
                });

                // Thiết lập dữ liệu cho các TextView
                noteTitleTextView.setText(n.getTitle());
                clock.setText(n.getClock());


                if (isRadioButtonVisible) {
                    radioButton.setVisibility(View.VISIBLE);
                } else {
                    radioButton.setVisibility(View.GONE);

                }
                noteView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

    //                    radioButton.setVisibility(View.VISIBLE);
                        isRadioButtonVisible = true;
    //                    radioButton.setChecked(true);

                        selectListener.onItemCheckClick1(isRadioButtonVisible);
                        notifyDataSetChanged();
                        return true;
                    }
                });

                noteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRadioButtonVisible){
                            checkBox.setEnabled(false);
                            radioButton.setChecked(!radioButton.isChecked());
                            selectListener.onItemCheckClick(n.getId(),radioButton.isChecked());

                        }
                        else {
                            selectListener.onItemClickNoteList(n, entry.getKey()); // Gọi phương thức với n
                            Log.d("TAG55555", String.valueOf(n.getId())); // In ID
                            checkBox.setEnabled(true);
                        }

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

            public void create(String text) {
                TextView newTextView = new TextView(context);
                newTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                newTextView.setText(text);
                newTextView.setTextSize(20); // Đặt kích thước chữ
                // Thêm TextView vào LinearLayout

                linearLayout.addView(newTextView, 0);
            }
        }
    }
