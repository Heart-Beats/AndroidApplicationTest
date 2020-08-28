package com.example.zhanglei.myapplication;

import android.view.View;

public abstract class ClickHelperListener implements View.OnClickListener {

    private static final long DOUBLE_TIME = 500;
    private static long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
            onDoubleClick(v);
        } else {
            onSingleClick(v);
        }
        lastClickTime = currentTimeMillis;
    }

    public abstract void onDoubleClick(View v);

    public abstract void onSingleClick(View v);

}
