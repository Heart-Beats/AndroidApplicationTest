package com.example.zhanglei.myapplication;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private static final String TAG = "ImageAdapter";

    private List<Uri> imageUris;
    private Context context;
    private RecyclerView recyclerView;

    private final static List<String> imageUrls = Arrays.asList(
            "https://img-my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037194_2965.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037193_1687.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037193_1286.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037178_9374.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037177_1254.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037177_6203.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037152_6352.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037151_9565.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037151_7904.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037148_7104.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037129_8825.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037128_5291.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037128_3531.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037127_1085.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037095_7515.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037094_8001.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037093_7168.jpg",
            "https://img-my.csdn.net/uploads/201309/01/1378037091_4950.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949643_6410.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949642_6939.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949630_4505.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949630_4593.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949629_7309.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949629_8247.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949615_1986.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949614_8482.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949614_3743.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949614_4199.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949599_3416.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949599_5269.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949598_7858.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949598_9982.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949578_2770.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949578_8744.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949577_5210.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949577_1998.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949482_8813.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949481_6577.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949480_4490.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949455_6792.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949455_6345.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949442_4553.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949441_8987.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949441_5454.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949454_6367.jpg",
            "https://img-my.csdn.net/uploads/201308/31/1377949442_4562.jpg");


    public ImageAdapter(List<Uri> imageUris, Context context, RecyclerView recyclerView) {
        this.imageUris = imageUris;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MyImageView imageView = holder.myImageView;
        String url = imageUrls.get(position);
        imageView.setTag(url);
/*        BitmapImageViewTarget bitmapImageViewTarget = new BitmapImageViewTarget(holder.myImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                //这里用来进行图片转换，转成圆形的图。
                RoundedBitmapDrawable circleBitmap = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circleBitmap.setCircular(true);
                myImageView.setImageDrawable(circleBitmap);
            }
        };*/
        // MyImageView.ImageBindInfo<Uri> imageBindInfo = new MyImageView.ImageBindInfo<>(imageUris.get(position), getImageThumbnailSize(context), holder);
        if (imageView.getTag().equals(url)) {
            MyImageView.ImageBindInfo<String> imageBindInfo = new MyImageView.ImageBindInfo<>(imageUrls.get(position), getImageThumbnailSize(context), holder);
            imageView.bindImageInfo(imageBindInfo);
        }

        //这里就是让我们最终的效果跟Gridview不同的原因了，我们把第一列和第三列的首张图片设置距离顶部一个距离，这样布局错落有致，就是我们要的效果了
/*        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(imageView.getLayoutParams());
        if (position == 0 || position == 2) {
            lp.setMargins(5, 100, 5, 5);
        } else {
            lp.setMargins(5, 5, 5, 5);
        }
        imageView.setLayoutParams(lp);*/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ItemCount==" + imageUris.size());
        // return imageUris.size();
        return imageUrls.size();
    }


    private int getImageThumbnailSize(Context context) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        // int spanCount = ((StaggeredGridLayoutManager) lm).getSpanCount();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        // int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(
        //         R.dimen.media_grid_spacing) * (spanCount - 1);
        // int imageResize = availableWidth / spanCount;
        int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(
                R.dimen.media_grid_spacing);
        int imageResize = availableWidth;
        imageResize = (int) (imageResize * 0.9);
        return imageResize;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private MyImageView myImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.image);
        }
    }

}
