package com.dawoo.gamebox.tool;

/**
 * Log Utils for debug
 *
 * @author fengjun
 */
public class Log {

    private static final boolean DEBUG = true;
    private static final String TAG = "SkinLoader";
    private static final String LINE = "——————————————————————————————————————————————————————";

    private Log() {
        throw new AssertionError();
    }

    public static void i(String msg) {
        if (DEBUG) {
            android.util.Log.i(TAG, LINE);
            android.util.Log.i(TAG, msg);
            android.util.Log.i(TAG, LINE);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            android.util.Log.d(TAG, LINE);
            android.util.Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            android.util.Log.w(TAG, LINE);
            android.util.Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            android.util.Log.e(TAG, LINE);
            android.util.Log.e(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.i(tag, LINE);
            android.util.Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(tag, LINE);
            android.util.Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.w(tag, LINE);
            android.util.Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.e(tag, LINE);
            android.util.Log.e(tag, msg);
        }
    }
}
