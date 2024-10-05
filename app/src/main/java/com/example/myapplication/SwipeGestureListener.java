package com.example.myapplication;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    private MainActivity mainActivity;

    public SwipeGestureListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Kiểm tra xem e1 và e2 có bị null hay không
        if (e1 == null || e2 == null) {
            Log.d("SwipeGestureListener", "Một trong hai sự kiện MotionEvent bị null");
            return false; // Ngăn lỗi NullPointerException

        }
        // Tính toán khoảng cách vuốt
        float deltaX = e2.getX() - e1.getX();
        if (Math.abs(deltaX) > 0 && Math.abs(velocityX)>100) {
            // Vuốt theo chiều ngang
            if (deltaX > 0) {
                mainActivity.previousMonthAction();

                Log.d("SwipeGestureListener", "Vuốt sang phải");
            } else {
                mainActivity.nextMonthAction();
//                Animation animation = AnimationUtils.loadAnimation(mainActivity, R.anim.slide_in_right);
//
//                // Áp dụng animation lên TextView
//                mainActivity.relativeLayout.startAnimation(animation);
                Log.d("SwipeGestureListener", "Vuốt sang trái");
            }
            return true;
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Xử lý sự kiện click riêng biệt
        Log.d("SwipeGestureListener", "Sự kiện click");
        // Thực hiện xử lý sự kiện click (nếu cần)
        // Ví dụ: gọi một phương thức trong mainActivity để xử lý click
//        mainActivity.onItemClick();
        return true;
    }
}
