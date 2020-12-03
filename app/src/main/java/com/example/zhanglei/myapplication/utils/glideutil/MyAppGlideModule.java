package com.example.zhanglei.myapplication.utils.glideutil;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * 自定义Glide的模块
 *
 * @Author 张磊  on  2020/08/27 at 14:44
 * Email: 913305160@qq.com
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    private static final String TAG = "MyAppGlideModule";


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        Log.d(TAG, "registerComponents: 注册组件");
        registry.replace(GlideUrl.class, InputStream.class, new MyHttpGlideUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}