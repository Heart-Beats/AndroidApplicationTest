package com.hl.shadow.logger;

import com.tencent.shadow.core.common.ILoggerFactory;
import com.tencent.shadow.core.common.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author 张磊  on  2021/04/08 at 15:35
 * Email: 913305160@qq.com
 */
public class AndroidLoggerFactory implements ILoggerFactory {

    private static final AndroidLoggerFactory sInstance = new AndroidLoggerFactory();

    public static ILoggerFactory getInstance() {
        return sInstance;
    }

    final private ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    @Override
    public Logger getLogger(String name) {
        Logger simpleLogger = loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        } else {
            Logger newInstance = new MyLogger(name);
            Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }
}
