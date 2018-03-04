package com.dawoo.lotterybox.util;

import android.content.Context;

import com.dawoo.coretool.ToastUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by jack on 18-2-8.
 */

public class MSPropties {

    public static void showMeg(Context context, String Msg) {
        ToastUtil.showToastLong(context, Msg);
    }


    /**
     * 造些微信的假数据
     */

    public static List getPayInfo(Context context) {


        return Collections.emptyList();
    }


}
