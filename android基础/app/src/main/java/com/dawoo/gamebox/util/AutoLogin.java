package com.dawoo.gamebox.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.tencent.smtt.sdk.CookieManager;
import android.widget.TextView;

import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.LoginBean;
import com.dawoo.gamebox.view.activity.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by archar on 18-2-1.
 * <p>
 * 自动登录
 */

public class AutoLogin {

    public static boolean isSuccessLogin = false;

    public static void loginOrGoMain(Activity activity, TextView loading) {
        if (checkHasUserRecord()) {
            loading.setText(activity.getString(R.string.logining));
            login(activity);
        } else {
            goMain(activity);
        }
    }

    /**
     * 登录
     */

    private static void login(Activity activity) {
        String userName = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_USERNAME, "");
        String password = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_PASSWORD, "");
        String mDomain = DataCenter.getInstance().getDomain();
        String url = mDomain + ConstantValue.LOGIN_URL;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .add("captcha", "").build();
        Request request = new Request.Builder().url(url)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("User-Agent", "app_android;Android")
                .post(body)
                .build();
        CookieManager.getInstance().setCookie(mDomain, "");
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("自动登录 Error ==> " + e.getLocalizedMessage());
                showErrorLoginMsg(activity);
                goMain(activity);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                LoginBean loginBean = null;
                if (jsonData != null) {
                    try {
                        loginBean = new Gson().fromJson(jsonData, LoginBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorLoginMsg(activity);
                    }

                    if (loginBean != null) {
                        if (loginBean.isSuccess()) {
                            if (response.code() == 200) {
                                LoginSuccess(response, userName, password);
                            } else {
                                showErrorLoginMsg(activity);
                            }
                        } else {
                            showErrorLoginMsg(activity);
                        }
                    } else {
                        showErrorLoginMsg(activity);
                    }
                } else {
                    showErrorLoginMsg(activity);
                }
                goMain(activity);
            }
        });
    }


    private static void showErrorLoginMsg(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showResLong(activity, R.string.needRealName);
            }
        });
    }

    /**
     * 登录成功后  做的一些初始化
     *
     * @param response
     * @param successUserName
     * @param successUserPwd
     */
    private static void LoginSuccess(Response response, String successUserName, String successUserPwd) {
        isSuccessLogin = true;
        NetUtil.setCookie(response);
        DataCenter.getInstance().setLogin(true);
        DataCenter.getInstance().setUserName(successUserName);
        DataCenter.getInstance().setPassword(successUserPwd);
        LogUtils.e("自动登录  ==> success " + "初始化成功");
    }


    /**
     * 跳主界面
     *
     * @param activity
     */

    private static void goMain(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 检测  sp 里的有无老账户
     *
     * @return
     */
    private static boolean checkHasUserRecord() {
        String userName = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_USERNAME, "");
        String password = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_PASSWORD, "");

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }
}
