package com.example.floatingwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class FloatingView extends FloatingLayout {

    public FloatingView(Context context) {
        super(context);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout;
    }

    @Override
    public void renderView(View view) {

    }
}
