package com.hl.shadow.pluginmanager;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.hl.pluginmanager.Constant;
import com.tencent.shadow.core.manager.installplugin.InstalledPlugin;
import com.tencent.shadow.core.manager.installplugin.InstalledType;
import com.tencent.shadow.core.manager.installplugin.PluginConfig;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.FailedException;
import com.tencent.shadow.dynamic.loader.PluginServiceConnection;
import com.tencent.shadow.dynamic.manager.PluginManagerThatUseDynamicLoader;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author 张磊  on  2021/04/08 at 17:22
 * Email: 913305160@qq.com
 */
public class MyPluginManager extends PluginManagerThatUseDynamicLoader {

    private static final String TAG = "MyPluginManager";

    private final ExecutorService installPluginExecutorService = new ThreadPoolExecutor(1, 1, 2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(Short.MAX_VALUE), r -> new Thread(r, "安装插件线程"), (r, executor) -> {
        Log.e(TAG, String.format("%s 已满载，拒绝执行任务 %s", executor, r));
    });

    private final ExecutorService mFixedPool = new ThreadPoolExecutor(4, 4, 2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(Short.MAX_VALUE), r -> new Thread(r, "解压插件线程"), (r, executor) -> {
        Log.e(TAG, String.format("%s 已满载，拒绝执行任务 %s", executor, r));
    });

    public MyPluginManager(Context context) {
        super(context);
    }

    /**
     * @return PluginManager实现的别名，用于区分不同PluginManager实现的数据存储路径
     */
    @Override
    protected String getName() {
        return Constant.PLUGIN_MANAGER_NAME;
    }

    /**
     * @return demo插件so的abi
     */
    @Override
    public String getAbi() {
        return Constant.ABI;
    }

    /**
     * @return 宿主中注册的PluginProcessService实现的类名
     */
    protected String getPluginProcessServiceName() {
        return Constant.PLUGIN_PROCESS_SERVICE_NAME;
    }

    /**
     * @param context  context
     * @param fromId   标识本次请求的来源位置，用于区分入口
     * @param bundle   参数列表
     * @param callback 用于从PluginManager实现中返回View
     */
    @Override
    public void enter(final Context context, long fromId, Bundle bundle, final EnterCallback callback) {
        Log.d("MyPluginManager", "enter: 开始进入插件 -------------- \n" +
                " formId == " + fromId + "，传入bundle == " + bundle);

        // 插件 zip 包地址，可以直接写在这里，也用Bundle可以传进来
        final String pluginZipPath = bundle.getString(Constant.KEY_PLUGIN_ZIP_PATH);
        final String partKey = bundle.getString(Constant.KEY_PLUGIN_PART_KEY);
        final String className = bundle.getString(Constant.KEY_ACTIVITY_CLASSNAME);
        if (className == null) {
            throw new NullPointerException("className == null");
        }

        // 打开 Activity 示例
        if (fromId == Constant.FROM_ID_START_ACTIVITY) {
            final Bundle extras = bundle.getBundle(Constant.KEY_EXTRAS);
            if (callback != null) {
                // 开始加载插件了，实现加载布局
                callback.onShowLoadingView(null);
            }
            installPluginExecutorService.execute(() -> {
                try {
                    InstalledPlugin installedPlugin
                            = installPlugin(pluginZipPath, null, true);//这个调用是阻塞的
                    Intent pluginIntent = new Intent();
                    pluginIntent.setClassName(context.getPackageName(), className);
                    if (extras != null) {
                        pluginIntent.replaceExtras(extras);
                    }

                    startPluginActivity(context, installedPlugin, partKey, pluginIntent);
                } catch (Exception e) {
                    Log.e(TAG, "enter: startPluginActivity 失败", e);
                }
                if (callback != null) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(() -> {
                        // 这里插件就启动完成了
                        callback.onCloseLoadingView();
                        callback.onEnterComplete();
                    });
                }
            });

        } else if (fromId == Constant.FROM_ID_CALL_SERVICE) {
            // 打开Server示例
            Intent pluginIntent = new Intent();
            pluginIntent.setClassName(context.getPackageName(), className);

            installPluginExecutorService.execute(() -> {
                try {
                    InstalledPlugin installedPlugin
                            = installPlugin(pluginZipPath, null, true);//这个调用是阻塞的

                    loadPlugin(installedPlugin.UUID, partKey);

                    Intent pluginIntent1 = new Intent();
                    pluginIntent1.setClassName(context.getPackageName(), className);

                    boolean callSuccess = mPluginLoader.bindPluginService(pluginIntent1, new PluginServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                            // 在这里实现AIDL进行通信操作
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName componentName) {
                            throw new RuntimeException("onServiceDisconnected");
                        }
                    }, Service.BIND_AUTO_CREATE);

                    if (!callSuccess) {
                        throw new RuntimeException("bind service失败 className==" + className);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "enter: bindPluginService 失败", e);
                }
            });
        } else {
            throw new IllegalArgumentException("不认识的fromId==" + fromId);
        }
    }

    /**
     * 从压缩包中解压安装插件
     *
     * @param zip  压缩包路径
     * @param hash 压缩包hash
     * @param odex 插件是否需要 odex 优化
     */
    public InstalledPlugin installPlugin(String zip, String hash, boolean odex) throws IOException, JSONException, InterruptedException, ExecutionException {
        final PluginConfig pluginConfig = installPluginFromZip(new File(zip), hash);
        final String uuid = pluginConfig.UUID;
        List<Future<Object>> futures = new LinkedList<>();
        if (pluginConfig.runTime != null && pluginConfig.pluginLoader != null) {
            Future<Object> odexRuntime = mFixedPool.submit(() -> {
                oDexPluginLoaderOrRunTime(uuid, InstalledType.TYPE_PLUGIN_RUNTIME,
                        pluginConfig.runTime.file);
                return null;
            });
            futures.add(odexRuntime);
            Future<Object> odexLoader = mFixedPool.submit(() -> {
                oDexPluginLoaderOrRunTime(uuid, InstalledType.TYPE_PLUGIN_LOADER,
                        pluginConfig.pluginLoader.file);
                return null;
            });
            futures.add(odexLoader);
        }
        for (Map.Entry<String, PluginConfig.PluginFileInfo> plugin : pluginConfig.plugins.entrySet()) {
            final String partKey = plugin.getKey();
            final File apkFile = plugin.getValue().file;
            Future<Object> extractSo = mFixedPool.submit(() -> {
                extractSo(uuid, partKey, apkFile);
                return null;
            });
            futures.add(extractSo);
            if (odex) {
                Future<Object> odexPlugin = mFixedPool.submit(() -> {
                    oDexPlugin(uuid, partKey, apkFile);
                    return null;
                });
                futures.add(odexPlugin);
            }
        }

        for (Future<Object> future : futures) {
            future.get();
        }
        onInstallCompleted(pluginConfig);

        return getInstalledPlugins(1).get(0);
    }


    /**
     * 打开安装的插件中对应的 Activity
     *
     * @param context         传入的上下文
     * @param installedPlugin 安装的插件
     * @param partKey         安装的插件对应的 partKey
     * @param pluginIntent    需要打开的 activity 对应的 intent
     */
    public void startPluginActivity(Context context, InstalledPlugin installedPlugin, String partKey, Intent pluginIntent) throws RemoteException, TimeoutException, FailedException {
        Intent intent = convertActivityIntent(installedPlugin, partKey, pluginIntent);
        // if (!(context instanceof Activity)) {
        //     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mPluginLoader.startActivityInPluginProcess(intent);
    }

    public Intent convertActivityIntent(InstalledPlugin installedPlugin, String partKey, Intent pluginIntent) throws RemoteException, TimeoutException, FailedException {
        loadPlugin(installedPlugin.UUID, partKey);
        return mPluginLoader.convertActivityIntent(pluginIntent);
    }


    /**
     * 载入插件, 会根据插件 apk 生成对应的
     *      pluginPackageManager
     *      pluginClassLoader
     *      resources
     *      pluginInfo
     *      shadowApplication
     *      appComponentFactory
     */
    protected void loadPlugin(String uuid, String partKey) throws RemoteException, TimeoutException, FailedException {
        loadPluginLoaderAndRuntime(uuid);
        Map map = mPluginLoader.getLoadedPlugin();

        if (!map.containsKey(partKey)) {
            mPluginLoader.loadPlugin(partKey);
        }
        Boolean isCall = (Boolean) map.get(partKey);
        if (isCall == null || !isCall) {
            mPluginLoader.callApplicationOnCreate(partKey);
        }
    }

    private void loadPluginLoaderAndRuntime(String uuid) throws RemoteException, TimeoutException, FailedException {
        if (mPpsController == null) {
            System.out.println("开始绑定 Service-------------------------");
            bindPluginProcessService(getPluginProcessServiceName());
            System.out.println("等待绑定 Service------------------------");
            waitServiceConnected(10, TimeUnit.SECONDS);
        }

        System.out.println("载入 RunTime ---------------");
        loadRunTime(uuid);

        System.out.println("载入 Loader -------------------");
        loadPluginLoader(uuid);
    }

}

