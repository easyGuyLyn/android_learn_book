package com.dawoo.gamebox.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.Utils;
import com.dawoo.gamebox.activity.LoginActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.tool.ActivityManager;
import com.dawoo.gamebox.tool.NetTool;
import com.dawoo.gamebox.tool.ProperTool;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.util.CustomProgressDialog;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Response;
import solid.ren.skinlibrary.base.SkinBaseActivity;

/**
 * Created by user on 2017/2/27.
 */
public abstract class BaseActivity extends SkinBaseActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected CustomProgressDialog progressDialog;

    protected Context context;
    protected boolean isDebug;
    protected String theme;

    private final List<String> resources = new ArrayList<>();
    private AssetManager am = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        theme = (String) SPTool.get(this, Constants.THEME_KEY, Constants.THEME_DEFAULT);

        isDebug = Boolean.valueOf(ProperTool.getProperty(this, "isDebug"));
        if (isDebug) {
            MyApplication.domain = ProperTool.getProperty(this, "debug.domain");
        }

        ActivityManager.OnCreateActivity(this);
        setContentView(initView());
        ButterKnife.bind(this);
        Utils.init(this);
        fetchResources();
        init();

        flymeSetStatusBarLightMode(getWindow(), false);
        miuiSetStatusBarLightMode(this, true);
//        CustomActivityOnCrash.install(this);
    }

    public abstract void init();

    public abstract int initView();

    public void setAnimation(View view, int xs, int xe, int ys, int ye, int time) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, xs,
                Animation.RELATIVE_TO_SELF, xe,
                Animation.RELATIVE_TO_SELF, ys,
                Animation.RELATIVE_TO_SELF, ye);
        translateAnimation.setDuration(time);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);
    }

    protected void changeTitleBack(ImageView ib1, ImageView ib2, TextView tv) {
        if (ParamTool.isLotterySite(context)) {   //纯彩票修改返回展示内容
            ib1.setVisibility(View.GONE);
            ib2.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        }
    }

    protected void showProgress(int resId, boolean canBack) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = new CustomProgressDialog(this, getResources().getString(resId));
        progressDialog.setCancelable(canBack);
        progressDialog.show();
    }

    protected void showProgress(String resId, boolean canBack) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = new CustomProgressDialog(this, resId);
        progressDialog.setCancelable(canBack);
        progressDialog.show();
    }

    protected void dismissProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /**
     * 设置WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void webSetting(final WebView webView) {
        WebSettings settings = webView.getSettings();
        String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCacheDir);

        settings.setEnableSmoothTransition(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setBuiltInZoomControls(false);
        settings.setDatabaseEnabled(true);          // 设置支持本地存储
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);        //设置支持DomStorage
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setJavaScriptEnabled(true);        // 支持JS
        settings.setLightTouchEnabled(false);
        settings.setLoadWithOverviewMode(true);    // 是否使用预览模式加载界面
        //图片先不加载最后再加载
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        //settings.setLoadsImagesAutomatically(true); // 自动加载图片
        settings.setMediaPlaybackRequiresUserGesture(true);
        settings.setUseWideViewPort(true);          // 图片适配WebView
        settings.setAppCacheEnabled(true);          // 启用缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //修改硬件加速导致页面渲染闪烁问题
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                webView.requestFocus();
                return false;
            }
        });
        String ua = settings.getUserAgentString().replace("Android", "app_android");
        settings.setUserAgentString(ua);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.setWebViewClient(new WebViewClient() {

        });
    }


    @NonNull
    protected Map<String, String> setHeaders() {
        Map<String, String> headers = NetTool.setHeaders();
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = MyApplication.cookie;
        Log.e(TAG, String.format("请求的Cookie --> %s", cookie));
        if (cookie != null) {
            headers.put("Cookie", cookie);
            cookieManager.setCookie(MyApplication.domain, cookie);
        } else {
            cookie = "";
        }
        return headers;
    }

    @Override
    protected void onDestroy() {
        ActivityManager.OnDestroyActivity(this);
        super.onDestroy();
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean miuiSetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 登录后设置cookie
     */
    protected void setCookie(Response response) {
        List<String> cookies = response.headers().values("Set-Cookie");
        for (String cookie : cookies) {
            if (cookie.contains("SID=") && cookie.length() > 80) {
                Log.e(TAG, "登录后Cookie ==> " + cookie);
                MyApplication.cookie = cookie;
                CookieManager.getInstance().setCookie(MyApplication.domain, cookie);
            }
        }
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     */
    protected boolean syncCookie(String url, String cookie) {
        Log.e(TAG, "同步的Cookie ==> " + cookie);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(MyApplication.domain, cookie);
        String newCookie = cookieManager.getCookie(MyApplication.domain);
        return !TextUtils.isEmpty(newCookie);
    }

    /**
     * 获取本地资源文件
     */
    private void fetchResources() {
        am = getAssets();
        try {
            String[] css = am.list("res/css");
            if (css != null)
                Collections.addAll(resources, css);

            String[] img = am.list("res/img");
            if (img != null)
                Collections.addAll(resources, img);

            String[] js = am.list("res/js");
            if (js != null)
                Collections.addAll(resources, js);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入本地资源文件
     *
     * @param url 文件路径
     * @return WebResourceResponse
     */
    protected WebResourceResponse loadResource(String url) {
        WebResourceResponse response = null;
        int lastSlash = url.lastIndexOf("/");
        if (lastSlash != -1) {
            String suffix = url.substring(lastSlash + 1);
            if (resources.contains(suffix)) {
                try {
                    String mimeType = null;
                    InputStream is = null;
                    if (suffix.endsWith(".css")) {
                        mimeType = "text/css";
                        is = am.open("res/css/" + suffix);
                    } else if (suffix.endsWith(".js")) {
                        mimeType = "application/x-javascript";
                        is = am.open("res/js/" + suffix);
                    } else if (suffix.endsWith(".jpg")) {
                        mimeType = "image/jpeg";
                        is = am.open("res/img/" + suffix);
                    }

                    if (mimeType != null && is != null)
                        response = new WebResourceResponse(mimeType, "UTF-8", is);

                } catch (IOException e) {
                    Log.e(TAG, "处理流出现异常==> " + e.getMessage());
                }
            }
        }
        return response;
    }

    protected void toLoginActivity(int resultFlag) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.RESULT_FLAG, resultFlag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApplication.isLogin = false;
        startActivityForResult(intent, 0);
    }

    protected void toLoginActivity(int requestFlag, int resultFlag) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.RESULT_FLAG, resultFlag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApplication.isLogin = false;
        startActivityForResult(intent, requestFlag);
    }

    /**
     * 退出登录，处理app数据
     */
    protected void logout(boolean isClear) {
        MyApplication.isLogin = false;
        MyApplication.isDemo = false;
        MyApplication.cookie = null;
        MyApplication.username = null;
        MyApplication.password = null;

        if (isClear) {//是否清除用户信息
            SPTool.remove(this, Constants.KEY_PASSWORD);
            SPTool.remove(this, Constants.KEY_NEED_CAPTCHA);
            SPTool.remove(this, Constants.KEY_CAPTCHA_TIME);
        }
    }
}
