package com.example.myapplication;


import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements iSelectListener.onItemClickListDay {
    private TextView monthText, yearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private FloatingActionButton flbt1, flbt2;
    private LinearLayout linearLayout,linearLayout1;
    private TextView txtfl1, txtfl2;
    private CalendarAdapter calendarAdapter;
    private ArrayList<String> daysInMonth;
    private boolean check = false;
    private MyDbSqlite myDbSqlite;
    private List<Note> list;
    private ImageButton imgbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout1 = findViewById(R.id.standbyScreen);
        linearLayout1.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            // Ẩn màn hình chờ sau 3 giây
            linearLayout1.setVisibility(View.GONE);
            // Chạy hàm setMonthView() để hiển thị nội dung chính
            setMonthView();
        }, 1500);
        selectedDate = LocalDate.now();
        initWidgets();
//        setMonthView();
        // Tải dữ liệu và khởi tạo giao diện bất đồng bộ

        monthText.setOnClickListener(v -> showMonthYearPickerDialog());
        yearText.setOnClickListener(v -> showMonthYearPickerDialog());
        calendarRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, calendarRecyclerView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {
            }
            @Override
            public void onSwipeRight() {
                previousMonthAction();
            }

            @Override
            public void onSwipeLeft() {
                nextMonthAction();
            }
        }));
        imgbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = LocalDate.now();
                setMonthView();
            }
        });
    }


    public List<Note> getData() {
        list = new ArrayList<>();
        Cursor cursor = myDbSqlite.sreachByMonthYear(monthFromDate(selectedDate),yearFromDate(selectedDate)); // Gọi phương thức readData()
        if(cursor.getCount()==0){
//            Toast.makeText(this, "not data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext()){
                list.add(new Note(cursor.getString(5),cursor.getString(1)));
                Log.d("TAG77777777777",list.get(0).getTitle());
            }
        }
        return list;
//         Đóng Cursor sau khi sử dụng
//        cursor.close();
    }



    private void showMonthYearPickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_calendar);

        final NumberPicker monthPicker = dialog.findViewById(R.id.month_picker);
        final NumberPicker yearPicker = dialog.findViewById(R.id.year_picker);
        Button btnOk = dialog.findViewById(R.id.btn_ok);

        // Thiết lập cho month picker
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setDisplayedValues(new String[]{"Thang 1", "Thang 2", "Thang 3", "Thang 4", "Thang 5", "Thang 6", "Thang 7", "Thang 8", "Thang 9", "Thang 10", "Thang 11", "Thang 12"});

        // Thiết lập cho year picker
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int month = java.util.Calendar.getInstance().get(Calendar.MONTH);
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(9999);
        monthPicker.setValue(month+1);
        yearPicker.setValue(year);
        // Xử lý sự kiện khi nhấn OK
        btnOk.setOnClickListener(v -> {
            int selectedMonth = monthPicker.getValue();
            int selectedYear = yearPicker.getValue();
            selectedDate = LocalDate.of(selectedYear, selectedMonth, 1);
            daysInMonthArray(selectedDate);
            setMonthView();
            Toast.makeText(MainActivity.this, "Selected: " + selectedMonth + "/" + selectedYear, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();

    }



    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthText = findViewById(R.id.monthTV);
        yearText = findViewById(R.id.YearTV);
        flbt1 = findViewById(R.id.floatbt1);
        flbt2 = findViewById(R.id.floatbt2);
        txtfl1 = findViewById(R.id.txtflbt1);
        txtfl2 = findViewById(R.id.txtflbt2);
        linearLayout = findViewById(R.id.lnlo);
        daysInMonth = new ArrayList<>();
        myDbSqlite  = new MyDbSqlite(this);
        imgbt = findViewById(R.id.reset);
        createNotificationChannel();
    }

    private void setMonthView() {
        daysInMonth = daysInMonthArray(selectedDate);
        monthText.setText("Tháng"+monthFromDate(selectedDate));
        yearText.setText(yearFromDate(selectedDate));
        calendarAdapter = new CalendarAdapter(getData(),yearFromDate(selectedDate), monthFromDate(selectedDate), daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarRecyclerView.setHasFixedSize(true);
        calendarRecyclerView.setItemViewCacheSize(20);
        calendarRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                int childCount = parent.getChildCount();
                int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#FFBFBFBF")); // Màu của đường kẻ
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);

                    // Vẽ đường kẻ dọc
                    if ((position + 1) % spanCount != 0) { // Không phải là item cuối cùng trong hàng
                        c.drawLine(child.getRight(), child.getTop(), child.getRight(), child.getBottom(), paint);
                    }

                    // Vẽ đường kẻ ngang
                    if (position < childCount) { // Không phải là item cuối cùng trong cột
                        c.drawLine(child.getLeft(), child.getTop(), child.getRight(), child.getTop(), paint);
                    }
                }
            }
        });
    }

    //danh sach ngay trong thang
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }

    private String yearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1);
        monthText.setText(monthFromDate(selectedDate));
        yearText.setText(yearFromDate(selectedDate));
        ArrayList<String> newlist = daysInMonthArray(selectedDate);
        calendarAdapter.updateMonthList1(yearFromDate(selectedDate),monthFromDate(selectedDate),newlist);
//        calendarAdapter.updateMonthList(newlist);
        setMonthView();
    }

    public void nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1);
        monthText.setText(monthFromDate(selectedDate));
        yearText.setText(yearFromDate(selectedDate));
        ArrayList<String> newlist = daysInMonthArray(selectedDate);
        calendarAdapter.updateMonthList1(yearFromDate(selectedDate),monthFromDate(selectedDate),newlist);
        setMonthView();
//        calendarAdapter.updateMonthList(newlist);
    }

    public void click(View view) {

        if (!check) {
            flbt1.setVisibility(View.VISIBLE);
            flbt2.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            txtfl1.setVisibility(View.VISIBLE);
            txtfl2.setVisibility(View.VISIBLE);
            check = true;
        } else {
            flbt1.setVisibility(View.GONE);
            flbt2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            txtfl1.setVisibility(View.GONE);
            txtfl2.setVisibility(View.GONE);
            check = false;
        }
    }

    public void allNote(View view) {
        Intent intent = new Intent(MainActivity.this, NotesLIst.class);
        startActivity(intent);
    }

    public void clickAddNote(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setMonthView();
    }


    @Override
    public void onItemClicked(int position, String dayText) {
//        Toast.makeText(this, "hihihihihih" +dayText, Toast.LENGTH_SHORT).show();
        if (!dayText.equals("")) {
            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
            intent.putExtra("key1", dayText);
            intent.putExtra("key2", selectedDate.getMonthValue());
            intent.putExtra("key3", Integer.parseInt(yearFromDate(selectedDate)));
            intent.putExtra("key4", position);
            startActivity(intent);
        }
    }


    private void createNotificationChannel() {
        // Kiểm tra nếu phiên bản Android là Oreo hoặc mới hơn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tên kênh và mô tả
            String channelId = "alarm_channel";
            String channelName = "Alarm Notifications";
            String channelDescription = "Channel for alarm notifications";
            Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sena_alarm);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            channel.setSound(soundUri, audioAttributes);  // Đặt âm thanh cho kênh
            // Tạo kênh thông báo
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}