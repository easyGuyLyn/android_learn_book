package com.dawoo.lotterybox.util;

import android.widget.Toast;

import com.dawoo.lotterybox.BoxApplication;

/**
 * Created by archar on 18-2-8.
 */

public class SingleToast {

    private static Toast mToast;

    public static void showMsg(String msg) {

        if (mToast != null) {
            mToast.setText(msg);
        } else {
            mToast = Toast.makeText(BoxApplication.getContext(), msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
