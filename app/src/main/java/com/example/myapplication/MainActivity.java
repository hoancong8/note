package com.example.myapplication;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.extensions.CalendarGridView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthText,yearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    FloatingActionButton flbt1,flbt2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flbt1 = findViewById(R.id.floatbt1);
        flbt2 = findViewById(R.id.floatbt2);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthText = findViewById(R.id.monthTV);
        yearText = findViewById(R.id.YearTV);
    }
    private void setMonthView()
    {
        monthText.setText(monthFromDate(selectedDate));
        yearText.setText(yearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(yearFromDate(selectedDate),monthFromDate(selectedDate),daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
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
                    if (position < childCount ) { // Không phải là item cuối cùng trong cột
                        c.drawLine(child.getLeft(), child.getTop(), child.getRight(), child.getTop(), paint);
                    }
                }
            }
        });
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }
    private String yearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }
    public void click(View view){
        flbt1.setVisibility(View.VISIBLE);
        flbt2.setVisibility(View.VISIBLE);
    }
    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthFromDate(selectedDate) + yearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        }
    }
}