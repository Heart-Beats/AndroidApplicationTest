package com.example.zhanglei.myapplication.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.FragmentKt;

import com.example.zhanglei.myapplication.ClickHelperListener;
import com.example.zhanglei.myapplication.MyAnimatorSet;
import com.example.zhanglei.myapplication.MyView;
import com.example.zhanglei.myapplication.PictureSelectionActivity;
import com.example.zhanglei.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张磊
 */
public class MainFragment extends BaseFragment {

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
    private Button playVideo;

    private MyOnclickListener myOnclickListener;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        if (toolbar != null) {
            toolbar.setTitle("主页");
        }
        myView = view.findViewById(R.id.my_view);
        startAnimation = view.findViewById(R.id.start_animation);
        pictureSelect = view.findViewById(R.id.picture_select);
        playVideo = view.findViewById(R.id.play_video);
        myAnimatorSet = new MyAnimatorSet();
        myOnclickListener = new MyOnclickListener();
    }

    @Override
    public void onStart() {
        super.onStart();

        myView.setOnClickListener(new ClickHelperListener() {
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

        startAnimation.setOnClickListener(myOnclickListener);
        pictureSelect.setOnClickListener(myOnclickListener);
        playVideo.setOnClickListener(myOnclickListener);
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
        animator1.addUpdateListener(animation -> {
            Point point = (Point) animation.getAnimatedValue();
            Log.d(TAG, "onAnimationUpdate: " + point.getX() + "," + point.getY());
            myView.setX(point.getX());
            myView.setY(point.getY());
            myView.invalidate();
        });
        animator1.start();

        for (int t = 0; t <= 2 * Math.PI; t += 1) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyOnclickListener implements View.OnClickListener {

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
                case R.id.play_video:
                    FragmentKt.findNavController(MainFragment.this).
                            navigate(R.id.action_mainFragment_to_videoFragment);
                default:
                    break;
            }
        }
    }

    private void requestPermission() {
        List<String> requestPermissionsList = new ArrayList<>();
        for (String permission : permissionsList) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsList.add(permission);
            }
        }

        if (!requestPermissionsList.isEmpty()) {
            String[] requestPermissions = requestPermissionsList.toArray(new String[0]);
            ActivityCompat.requestPermissions(requireActivity(), requestPermissions, REQUEST_CODE);
        } else {
            startPictureSelectionActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), "必须同意所有权限才能使用本功能",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startPictureSelectionActivity();
            }
        }
    }

    private void startPictureSelectionActivity() {
        Intent intent = new Intent();
        intent.setClass(requireContext(), PictureSelectionActivity.class);
        startActivity(intent);
    }

    public static class Point {

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

    public static class PointEvaluator implements TypeEvaluator<Point> {

        private double t;

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            double x = 16 * Math.pow(Math.sin(t / Math.PI), 3);
            double y = 13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);
            Point point = new Point((float) x, (float) y);
            t += 0.1;
            return point;
        }
    }
}
