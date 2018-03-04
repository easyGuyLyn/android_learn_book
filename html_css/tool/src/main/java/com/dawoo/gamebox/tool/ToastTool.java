package com.dawoo.gamebox.tool;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Toast 工具
 * Created by fei on 16-10-18.
 */
public class ToastTool {
    private static String oldMsg;
    private static long time;
    public static void show(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, @StringRes int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
    }

    public static void show(View view, CharSequence msg) {
        show(view.getContext(), msg);
    }

    public static void show(View view, @StringRes int stringId) {
        show(view.getContext(), stringId);
    }
    public static void showToast(Context context, String msg) {
        Log.i("Toast",msg);
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }

    public static void showToastShort(Context context, String msg) {
        Log.i("Toast",msg);
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }
}
