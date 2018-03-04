package com.dawoo.gamebox.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.view.activity.LoginActivity;
import com.dawoo.gamebox.view.activity.MainActivity;
import com.dawoo.gamebox.view.activity.webview.WebViewActivity;
import com.hwangjr.rxbus.RxBus;

/**
 * 一些页面的跳转
 * Created by benson on 18-1-14.
 */

public class ActivityUtil {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }


    public static void gotoLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
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

    /**
     * 登出
     */
    public static void logout() {
        DataCenter.getInstance().setLogin(false);
        DataCenter.getInstance().setCookie("");
        DataCenter.getInstance().setUserName("");
        DataCenter.getInstance().setPassword("");
        SPTool.remove(BoxApplication.getContext(), ConstantValue.KEY_PASSWORD);
        RxBus.get().post(ConstantValue.EVENT_TYPE_LOGOUT, "logout");
        ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
    }

}
