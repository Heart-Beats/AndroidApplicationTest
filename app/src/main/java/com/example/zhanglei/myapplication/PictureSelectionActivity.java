package com.example.zhanglei.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhanglei.myapplication.adapter.BaseNormalAdapterWrapper;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.ui.widget.MediaGridInset;

import java.util.List;
import java.util.Set;

public class PictureSelectionActivity extends AppCompatActivity {

    private static final String TAG = "PictureSelectionActivit";

    private static final int REQUEST_CODE_CHOOSE = 1;

    private boolean isSelectedImage;

    private RecyclerView imageRecyclerView;
    private ImageAdapter imageAdapter;
    private MyImageEngine myImageEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_seletion);
        imageRecyclerView = findViewById(R.id.image_recycler_view);
        myImageEngine = new MyImageEngine();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        if (!isSelectedImage) {
            // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            // staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            // imageRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            imageRecyclerView.setLayoutManager(linearLayoutManager);

            int spacing = getResources().getDimensionPixelSize(R.dimen.media_grid_spacing);
            imageRecyclerView.addItemDecoration(new MediaGridInset(3, spacing, false));//边距和分割线，需要自己定义
            imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    // staggeredGridLayoutManager.invalidateSpanAssignments();//这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
            startSelectPicture();
        }
    }

    private void startSelectPicture() {
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(1024)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                // .gridExpectedSize(1024)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(myImageEngine)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private class GifSizeFilter extends Filter {
        public GifSizeFilter(int width, int height, int picttureSize) {
        }

        @Override
        protected Set<MimeType> constraintTypes() {
            return null;
        }

        @Override
        public IncapableCause filter(Context context, Item item) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        isSelectedImage = true;
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelectedUri = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelectedUri: " + mSelectedUri);
            if (mSelectedUri.size() != 0) {
                imageAdapter = new ImageAdapter(mSelectedUri, this, imageRecyclerView);

                BaseNormalAdapterWrapper adapterWrapper = new BaseNormalAdapterWrapper(imageAdapter) {

                    int widthPixels = getResources().getDisplayMetrics().widthPixels;
                    LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(widthPixels, 200);

                    @Override
                    protected View getHeaderView() {
                        TextView headView = new TextView(PictureSelectionActivity.this);
                        headView.setText("我是头部");
                        headView.setGravity(Gravity.CENTER);
                        headView.setTextSize(Dimension.SP, 24);
                        headView.setLayoutParams(lp);
                        return headView;
                    }

                    @Override
                    protected View getFooterView() {
                        TextView footerView = new TextView(PictureSelectionActivity.this);
                        footerView.setText("我是尾部");
                        footerView.setGravity(Gravity.CENTER);
                        footerView.setTextSize(Dimension.SP, 24);
                        footerView.setLayoutParams(lp);
                        return footerView;
                    }
                };
                imageRecyclerView.setAdapter(adapterWrapper);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }
}
