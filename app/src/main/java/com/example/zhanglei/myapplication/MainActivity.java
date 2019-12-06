package com.example.zhanglei.myapplication;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static String[] permissionsList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    private static final int REQUEST_CODE = 1;

    private MyView myView;
    private Button startAnimation;
    private Button pictureSelect;
    private MyAnimatorSet myAnimatorSet;

    private MyOnclickLisenter myOnclickLisenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = findViewById(R.id.my_view);
        startAnimation = findViewById(R.id.start_animation);
        pictureSelect = findViewById(R.id.picture_select);

        myAnimatorSet = new MyAnimatorSet();
        myOnclickLisenter = new MyOnclickLisenter();
    }

    @Override
    protected void onStart() {
        super.onStart();

        myView.setOnClickListener(new DoubleClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDoubleClick(View v) {
                myView.fixedColor();
                myAnimatorSet.pause();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSingleClick(View v) {
                myView.changeColor();
                myAnimatorSet.resume();
            }
        });

        startAnimation.setOnClickListener(myOnclickLisenter);
        pictureSelect.setOnClickListener(myOnclickLisenter);
    }

    private void setScaleAnimation() {
        Log.d(TAG, "setScaleAnimation: ");
        ObjectAnimator animatorX = new ObjectAnimator();
        animatorX.setPropertyName("scaleX");
        animatorX.setFloatValues(0.1f);
        animatorX.setDuration(5000);
        animatorX.setTarget(myView);
        ObjectAnimator animatorY = new ObjectAnimator();
        animatorY.setPropertyName("scaleY");
        animatorY.setFloatValues(0.1f);
        animatorY.setDuration(5000);
        animatorY.setTarget(myView);

        myAnimatorSet.setPlayWithAnimator(animatorX, animatorY);

    }

    private void setRotateAnimation() {
        Log.d(TAG, "setRotateAnimation: ");
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName("rotation");
        animator.setFloatValues(360);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.setTarget(myView);

        myAnimatorSet.setAfterAnimator(animator);


        ObjectAnimator animator2 = new ObjectAnimator();
        animator2.setPropertyName("translationX");
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setDuration(500);
        animator2.setTarget(myView);

        ObjectAnimator animator3 = new ObjectAnimator();
        animator3.setPropertyName("translationX");
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setDuration(500);
        animator3.setTarget(myView);

        Point startPoint = new Point(0, 0);
        Point endPoint = new Point(200, 200);
        ValueAnimator animator1 = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        animator1.setDuration(5000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + point.getX() + "," + point.getY());
                myView.setX(point.getX());
                myView.setY(point.getY());
                myView.invalidate();
            }
        });
        animator1.start();

        for (int t = 0; t <= 2 * Math.PI; t += 1) {
            double X = 16 * Math.pow(Math.sin(t / Math.PI), 3);
            double Y = 13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyOnclickLisenter implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_animation:
                    setScaleAnimation();
                    setRotateAnimation();
                    myAnimatorSet.start();
                    break;
                case R.id.picture_select:
                    requestPermission();
                    break;
                default:
                    break;
            }
        }
    }

    private void requestPermission() {
        List<String> requestPermissionsList = new ArrayList<>();
        for (String permission : permissionsList) {
            if (ContextCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsList.add(permission);
            }
        }

        if (!requestPermissionsList.isEmpty()) {
            String[] requestPermissions = requestPermissionsList.toArray(new String[requestPermissionsList.size()]);
            ActivityCompat.requestPermissions(this, requestPermissions, REQUEST_CODE);
        } else {
            startPictureSelectionActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本功能",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    startPictureSelectionActivity();
                }
                break;
            default:
                break;


        }
    }

    private void startPictureSelectionActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PictureSelectionActivity.class);
        startActivity(intent);
    }

    public class Point {

        // 设置两个变量用于记录坐标的位置
        private float x;
        private float y;

        // 构造方法用于设置坐标
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        // get方法用于获取坐标
        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public class PointEvaluator implements TypeEvaluator<Point> {

        private double t;

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            double X = 16 * Math.pow(Math.sin(t / Math.PI), 3);
            double Y = 13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);
            Point point = new Point((float) X, (float) Y);
            t += 0.1;
            return point;
        }
    }
}
