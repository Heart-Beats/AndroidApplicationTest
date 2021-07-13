package com.hl.utils;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * @author zhanglei
 */
public class HandlerThreadProcessor implements Runnable {
    private static final String TAG = "HandlerThreadProcessor";

    private Handler mainThreadHandler;
    private Handler childThreadHandler;

    private Thread thread;

    private HandlerThreadProcessor() {
        thread = new Thread(this);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        thread.start();
    }

    private static class HandlerThreadProcessHolder {
        private static final HandlerThreadProcessor INSTANCE = new HandlerThreadProcessor();
    }

    public static HandlerThreadProcessor getInstance() {
        return HandlerThreadProcessHolder.INSTANCE;
    }

    @Override
    public void run() {
        Log.d(TAG, "[HandlerThreadProcessor]-->run: ");
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        childThreadHandler = new Handler(Looper.myLooper());
        Looper.loop();
    }

    public void postToMainThread(Runnable runnable) {
        if (mainThreadHandler != null) {
            mainThreadHandler.post(runnable);
        }
    }

    public void postToChildThread(Runnable runnable) {
        if (childThreadHandler != null) {
            childThreadHandler.post(runnable);
        }
    }

}
