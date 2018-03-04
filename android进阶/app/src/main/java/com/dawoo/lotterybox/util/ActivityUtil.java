package com.dawoo.lotterybox.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.BoxApplication;
import com.dawoo.lotterybox.ConstantValue;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.view.activity.webview.WebViewActivity;

/**
 * 一些页面的跳转
 * Created by benson on 18-1-14.
 */

public class ActivityUtil {
    private static Context mContext = BoxApplication.getContext();

    public static void setContext(Context context) {
        mContext = context;
    }

    /**
     * 进入webview
     */
    public static void startWebView(String url, String msg, String type) {
        if (TextUtils.isEmpty(url) && TextUtils.isEmpty(msg)) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.game_maintenance));
            return;
        }

        if (TextUtils.isEmpty(url)) {
            ToastUtil.showToastShort(mContext, msg + "");
            return;
        }

        if (!url.contains("http")) {
            url = DataCenter.getInstance().getDomain() + "/" + url;
        }

        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(ConstantValue.WEBVIEW_URL, url);
        intent.putExtra(ConstantValue.WEBVIEW_TYPE, type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


}
