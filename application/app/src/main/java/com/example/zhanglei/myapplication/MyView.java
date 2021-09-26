package com.example.zhanglei.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

public class MyView extends View {
    private int mExampleColor = Color.RED;
    private float mExampleDimension = 0;
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private boolean flag;

    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyView, defStyle, 0);

        mExampleColor = a.getColor(
                R.styleable.MyView_exampleColor, mExampleColor);

        mExampleDimension = a.getDimension(
                R.styleable.MyView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.MyView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.MyView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        updateRGB();
    }

    private void updateRGB() {
        new Thread(() -> {
            flag = true;
            while (true) {
                if (flag) {

                    MyView.this.r = getRandomRgbValue();
                    MyView.this.g = getRandomRgbValue();
                    MyView.this.b = getRandomRgbValue();

                    try {
                        Thread.sleep(900);
                        invalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // }
                }
            }
        }).start();
    }

    private boolean increase = true;

    private int rgbLoopChange(int rgb,int changeValue) {
        Log.d("哈喽", "rgbLoopChange: "+rgb);
        if (rgb<=0) {
            increase = true;
        } else if (255<=rgb) {
            increase = false;
        }
        if (increase) {
            rgb += changeValue;
        } else {
            rgb -= changeValue;
        }
        return rgb;
    }

    private int getRandomRgbValue() {
        double random = Math.random()*255;
        return (int) Math.round(random);
    }

    public void changeColor() {
        this.flag = true;
    }

    public void fixedColor() {
        this.flag = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private int r;
    private int g;
    private int b;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        Paint paint = new Paint();
        paint.setARGB(255, r, g, b);
        float density = getResources().getDisplayMetrics().density;
        canvas.drawArc(0, 0, 150 * density, 150 * density, 0, 270, true, paint);
        canvas.drawARGB(50, 100, 0, 100);


        // canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_android_black_24dp),0,0,paint);
    }
}
