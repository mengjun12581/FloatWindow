package com.example.floatingwindow;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FloatingWindowService extends Service {

    private final String TAG = getClass().getSimpleName();
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private FloatingView mFloatingView;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "FloatingWindowService onCreate");
        super.onCreate();
        // 获取WindowManager服务
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 设置LayoutParam
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        if (windowManager != null && mFloatingView != null) {
            windowManager.removeView(mFloatingView);
        }
        showFloatingWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "FloatingWindowService onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "FloatingWindowService onDestroy");
        if (windowManager != null && mFloatingView != null) {
            windowManager.removeView(mFloatingView);
        }
    }


    //定义浮动窗口布局  
    private LinearLayout mFloatLayout;
    private ImageButton mFloatView;
    private int width;
    private int height;


    @TargetApi(Build.VERSION_CODES.M)
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            /*// 新建悬浮窗控件
             *//*mFloatingView = new Button(getApplicationContext());
//            view.setText("悬浮窗");
            mFloatingView.setText("+");
            mFloatingView.setTextSize(18);
//            view.setBackgroundColor(Color.GRAY);
            mFloatingView.setOnTouchListener(new FloatingOnTouchListener());
            mFloatingView.setBackground(getDrawable(R.drawable.floating_bg));*//*

            // 新建悬浮窗控件
            mFloatingView = new FloatingView(getApplicationContext());
//            mFloatingView.setOnTouchListener(new FloatingOnTouchListener());

            layoutParams.format = PixelFormat.RGBA_8888;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //调整悬浮窗显示的停靠位置为左侧置顶
            //layoutParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
            *//*layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;*//*
            //调整悬浮窗显示的停靠位置为左侧置顶
//            layoutParams.gravity = Gravity.END;
            layoutParams.width = DensityUtil.dp2Px(this, 50);
            layoutParams.height = DensityUtil.dp2Px(this, 50);
            layoutParams.x = 0;
            layoutParams.y = 100;

            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(mFloatingView, layoutParams);*/


            width = windowManager.getDefaultDisplay().getWidth();
            height = windowManager.getDefaultDisplay().getHeight();
            //设置图片格式，效果为背景透明  
            layoutParams.format = PixelFormat.RGBA_8888;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）  
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //调整悬浮窗显示的停靠位置为左侧置顶  
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity  
            layoutParams.x = 0;
            layoutParams.y = 0;


            //设置悬浮窗口长宽数据    
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;


            LayoutInflater inflater = LayoutInflater.from(getApplication());
            //获取浮动窗口视图所在布局  
            mFloatLayout = (LinearLayout) inflater.inflate(R.layout.layout, null);
            //添加mFloatLayout  
            windowManager.addView(mFloatLayout, layoutParams);
            //浮动窗口按钮  
            mFloatView = (ImageButton) mFloatLayout.findViewById(R.id.imageButton);
            mFloatView.setOnTouchListener(new FloatingOnTouchListener());
            mFloatView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "mFloatView click");
                }
            });
            mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


        }
    }


    /*private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;

                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    }*/

    private class FloatingOnTouchListener implements View.OnTouchListener {
        int lastx = 0;
        int lasty = 0;
        int movex = 0;
        int movey = 0;
        boolean isMove;

        @Override


        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastx = (int) event.getRawX();
                    lasty = (int) event.getRawY();
                    isMove = false;
                    return false;
                case MotionEvent.ACTION_MOVE:
                    int curx = (int) event.getRawX();
                    int cury = (int) event.getRawY();


                    int x;
                    int y;
                    x = Math.abs(curx - lastx);
                    y = Math.abs(cury - lasty);
                    if (x < 5 || y < 5) {
                        isMove = false;
                        return false;
                    } else {
                        isMove = true;
                    }


                    // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                    layoutParams.x = curx - mFloatView.getMeasuredWidth() / 2;
                    // 减25为状态栏的高度
                    layoutParams.y = cury - mFloatView.getMeasuredHeight() / 2;
                    // 刷新
                    windowManager.updateViewLayout(mFloatLayout, layoutParams);
                    return true;
                case MotionEvent.ACTION_UP:
                    int finalX = (int) event.getRawX();
                    int finalY = (int) event.getRawY();
                    boolean isok = false;


                    if (finalY < mFloatView.getMeasuredHeight()) {
                        movey = 0;
                        movex = finalX - mFloatView.getMeasuredWidth() / 2;
                    }


                    if (finalY > height - mFloatView.getMeasuredHeight()) {
                        movey = height - mFloatView.getMeasuredHeight();
                        movex = finalX - mFloatView.getMeasuredWidth() / 2;
                    }


                    if (finalY > mFloatView.getMeasuredHeight() && finalY < height - mFloatView.getMeasuredHeight()) {
                        isok = true;
                    }
                    if (isok && finalX - mFloatView.getMeasuredWidth() / 2 < width / 2) {
                        movex = 0;
                        movey = finalY - mFloatView.getMeasuredHeight() / 2;
                    } else if (isok && finalX - mFloatView.getMeasuredWidth() / 2 > width / 2) {
                        movex = width - mFloatView.getMeasuredWidth();
                        movey = finalY - mFloatView.getMeasuredHeight() / 2;
                    }


                    layoutParams.x = movex;
                    layoutParams.y = movey;
                    if (isMove) {
                        windowManager.updateViewLayout(mFloatLayout, layoutParams);
                    }
                    return isMove;//false 为点击 true 为移动
                default:
                    break;
            }
            return false;
        }


    }
}
