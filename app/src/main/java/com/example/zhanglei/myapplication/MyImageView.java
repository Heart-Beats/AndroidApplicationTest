package com.example.zhanglei.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MyImageView extends FrameLayout {
    private static final String TAG = "MyImageView";


    private CardView cardView;
    private ImageView imageView;
    private ImageBindInfo imageBindInfo;

    public MyImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context) {
        cardView = new CardView(context);
        imageView = new ImageView(context);
        addView(cardView);
        cardView.addView(imageView);
        setCardViewStyle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setCardViewStyle() {
        cardView.setRadius(1024);
        cardView.setElevation(0);
    }

    private void preImageThumbnail() {
        Glide.with(getContext())
                .load(imageBindInfo.mImageSource)
                // .override(imageBindInfo.mResize)
                .centerCrop()
                .into(imageView);
    }

    public void bindImageInfo(ImageBindInfo imageBindInfo) {
        this.imageBindInfo = imageBindInfo;
        CardView.LayoutParams lp = new CardView.LayoutParams(this.imageBindInfo.mResize,this.imageBindInfo.mResize);
        imageView.setLayoutParams(lp);
        preImageThumbnail();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasureAllChildren(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public static class ImageBindInfo<T>{
        T mImageSource;
        int mResize;
        RecyclerView.ViewHolder mViewHolder;

        public ImageBindInfo(T imageSource, int resize, RecyclerView.ViewHolder viewHolder) {
            this.mImageSource = imageSource;
            this.mResize = resize;
            this.mViewHolder = viewHolder;
        }
    }
}
