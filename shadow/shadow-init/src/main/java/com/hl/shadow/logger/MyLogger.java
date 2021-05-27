package com.hl.shadow.logger;

import android.util.Log;

import com.tencent.shadow.core.common.Logger;

/**
 * @author 张磊  on  2021/05/22 at 16:22
 * Email: 913305160@qq.com
 */
class MyLogger implements Logger {

    private static final int LOG_LEVEL_TRACE = 5;
    private static final int LOG_LEVEL_DEBUG = 4;
    private static final int LOG_LEVEL_INFO = 3;
    private static final int LOG_LEVEL_WARN = 2;
    private static final int LOG_LEVEL_ERROR = 1;

    private String name;

    MyLogger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    private void log(int level, String message, Throwable t) {
        final String tag = String.valueOf(name);

        switch (level) {
            case LOG_LEVEL_TRACE:
            case LOG_LEVEL_DEBUG:
                if (t == null) {
                    Log.d(tag, message);
                } else {
                    Log.d(tag, message, t);
                }
                break;
            case LOG_LEVEL_INFO:
                if (t == null) {
                    Log.i(tag, message);
                } else {
                    Log.i(tag, message, t);
                }
                break;
            case LOG_LEVEL_WARN:
                if (t == null) {
                    Log.w(tag, message);
                } else {
                    Log.w(tag, message, t);
                }
                break;
            case LOG_LEVEL_ERROR:
                if (t == null) {
                    Log.e(tag, message);
                } else {
                    Log.e(tag, message, t);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void trace(String msg) {
        log(LOG_LEVEL_TRACE, msg, null);
    }

    @Override
    public void trace(String format, Object o) {
        FormattingTuple tuple = MessageFormatter.format(format, o);
        log(LOG_LEVEL_TRACE, tuple.getMessage(), null);
    }

    @Override
    public void trace(String format, Object o, Object o1) {
        FormattingTuple tuple = MessageFormatter.format(format, o, o1);
        log(LOG_LEVEL_TRACE, tuple.getMessage(), null);
    }

    @Override
    public void trace(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        log(LOG_LEVEL_TRACE, tuple.getMessage(), null);
    }

    @Override
    public void trace(String msg, Throwable throwable) {
        log(LOG_LEVEL_TRACE, msg, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String msg) {
        log(LOG_LEVEL_DEBUG, msg, null);
    }

    @Override
    public void debug(String format, Object o) {
        FormattingTuple tuple = MessageFormatter.format(format, o);
        log(LOG_LEVEL_DEBUG, tuple.getMessage(), null);
    }

    @Override
    public void debug(String format, Object o, Object o1) {
        FormattingTuple tuple = MessageFormatter.format(format, o, o1);
        log(LOG_LEVEL_DEBUG, tuple.getMessage(), null);
    }

    @Override
    public void debug(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        log(LOG_LEVEL_DEBUG, tuple.getMessage(), null);
    }

    @Override
    public void debug(String msg, Throwable throwable) {
        log(LOG_LEVEL_DEBUG, msg, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String msg) {
        log(LOG_LEVEL_INFO, msg, null);
    }

    @Override
    public void info(String format, Object o) {
        FormattingTuple tuple = MessageFormatter.format(format, o);
        log(LOG_LEVEL_INFO, tuple.getMessage(), null);
    }

    @Override
    public void info(String format, Object o, Object o1) {
        FormattingTuple tuple = MessageFormatter.format(format, o, o1);
        log(LOG_LEVEL_INFO, tuple.getMessage(), null);
    }

    @Override
    public void info(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        log(LOG_LEVEL_INFO, tuple.getMessage(), null);
    }

    @Override
    public void info(String msg, Throwable throwable) {
        log(LOG_LEVEL_INFO, msg, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String msg) {
        log(LOG_LEVEL_WARN, msg, null);
    }

    @Override
    public void warn(String format, Object o) {
        FormattingTuple tuple = MessageFormatter.format(format, o);
        log(LOG_LEVEL_WARN, tuple.getMessage(), null);
    }

    @Override
    public void warn(String format, Object o, Object o1) {
        FormattingTuple tuple = MessageFormatter.format(format, o, o1);
        log(LOG_LEVEL_WARN, tuple.getMessage(), null);
    }

    @Override
    public void warn(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        log(LOG_LEVEL_WARN, tuple.getMessage(), null);
    }

    @Override
    public void warn(String msg, Throwable throwable) {
        log(LOG_LEVEL_WARN, msg, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String msg) {
        log(LOG_LEVEL_ERROR, msg, null);
    }

    @Override
    public void error(String format, Object o) {
        FormattingTuple tuple = MessageFormatter.format(format, o);
        log(LOG_LEVEL_ERROR, tuple.getMessage(), null);
    }

    @Override
    public void error(String format, Object o, Object o1) {
        FormattingTuple tuple = MessageFormatter.format(format, o, o1);
        log(LOG_LEVEL_ERROR, tuple.getMessage(), null);
    }

    @Override
    public void error(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        log(LOG_LEVEL_ERROR, tuple.getMessage(), null);
    }

    @Override
    public void error(String msg, Throwable throwable) {
        log(LOG_LEVEL_ERROR, msg, throwable);
    }
}