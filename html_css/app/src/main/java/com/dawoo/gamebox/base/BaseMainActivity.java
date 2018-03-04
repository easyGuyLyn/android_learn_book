package com.dawoo.gamebox.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.activity.ApiActivity;
import com.dawoo.gamebox.activity.CommonActivity;
import com.dawoo.gamebox.activity.GameActivity;
import com.dawoo.gamebox.activity.LoginActivity;
import com.dawoo.gamebox.activity.PayActivity;
import com.dawoo.gamebox.activity.SettingActivity;
import com.dawoo.gamebox.been.LoginBean;
import com.dawoo.gamebox.common.AttrConst;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.constant.URLConst;
import com.dawoo.gamebox.enums.CodeEnum;
import com.dawoo.gamebox.enums.WebViewCodeEnum;
import com.dawoo.gamebox.tool.CommonWVClient;
import com.dawoo.gamebox.tool.ResourceTool;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.dawoo.gamebox.view.AppUpdate;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.loader.SkinManager;


public abstract class BaseMainActivity extends BaseActivity {
    private long mTime = 5 * 1000;
    private static final String TAG = BaseMainActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.wvHome)
    protected WebView wvHome;
    @BindView(R.id.wvMine)
    protected WebView wvMine;

    //首页
    @BindView(R.id.ivHome)
    protected ImageView ivHome;
    @BindView(R.id.tvHome)
    protected TextView tvHome;
    //我的
    @BindView(R.id.ivMine)
    protected ImageView ivMine;
    @BindView(R.id.tvMine)
    protected TextView tvMine;

    //顶部菜单
    @BindView(R.id.llLogout)
    protected LinearLayout llLogout;
    @BindView(R.id.tvUsername)
    protected TextView tvUsername;
    @BindView(R.id.ivLogo)
    protected ImageView ivLogo;
    @BindView(R.id.iToolbar)
    protected Toolbar toolbar;

    protected ImageView ivRefresh;

    protected String domain;

    protected int currTabIndex = 0;
    protected WebViewClient client;
    protected MyHandler handler;

    private BaseMainActivity instance;
    private AppUpdate appUpdate;
    // endregion
    private boolean isInit = true;

    @Override
    public void init() {
        if (isInit) {
            instance = this;
            // 消除toolbar左边距
            toolbar.setContentInsetsAbsolute(30, 0);
            domain = MyApplication.domain;
            wvHome.loadUrl(domain + URLConstants.MAIN_URL);
            handler = new MyHandler(this);
            appUpdate = new AppUpdate(this, this);
            client = new MyWebViewClient();

            // 设置主题
            setTheme();
            // 设置Logo
            setLogo();
            // 登录账号
            login();
            // 检查更新
            checkUpdate();
            getService();
            isInit = false;
        }


    }

    /**
     * 设置主题
     */
    private void setTheme() {
        if (!Constants.THEME_DEFAULT.equals(theme)) {
            loadSkin(theme);
            dynamicAddView(toolbar, AttrConst.BACKGROUND, R.color.colorPrimaryDark);
        }
    }

    /**
     * 载入皮肤
     */
    private void loadSkin(String skinName) {
        SkinManager.getInstance().loadSkin(skinName, new SkinLoaderListener() {
            @Override
            public void onStart() {
                Log.e(TAG, "初始化皮肤...");
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "换肤成功!");
            }

            @Override
            public void onFailed(String errMsg) {
                Log.e(TAG, "换肤失败!");
            }

            @Override
            public void onProgress(int progress) {
                if (progress % 10 == 0) Log.e(TAG, "换肤进度：" + progress);
            }
        });
    }

    /**
     * 设置Logo
     */
    private void setLogo() {
        String logoName = getResources().getString(R.string.app_logo);
        ivLogo.setImageDrawable(getResources().getDrawable(ResourceTool.getDrawResID(this, logoName)));
    }

    /**
     * 登录应用
     */
    protected void login() {
        if (getAccount()) return;
        // 登陆请求
        OkHttpUtils.post().url(domain + URLConstants.LOGIN_URL)
                .params(setParams()).headers(setHeaders()).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(final Response response, int id) throws Exception {
                final String jsonData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleLogin(jsonData, response);
                    }
                });
                return null;
            }

            private void handleLogin(String jsonData, Response response) {
                if (jsonData == null || jsonData.endsWith("")) {
                    ToastTool.showToastShort(BaseMainActivity.this, "返回数据为空");
                    return;
                }
                LoginBean loginBean = new Gson().fromJson(jsonData, LoginBean.class);
                if (loginBean.isSuccess()) {
                    setCookie(response);
                    MyApplication.isLogin = true;
                    MyApplication.isDemo = false;
                    loginAfter();
                } else {
                    startActivityForResult(new Intent(instance, LoginActivity.class), 0);
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                Log.e(TAG, "login Error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Object response, int id) {
            }
        });
    }

    @NonNull
    private Map<String, String> setParams() {
        Map<String, String> params = new HashMap<>();
        params.put("username", MyApplication.username);
        params.put("password", MyApplication.password);
        return params;
    }

    /**
     * 获取SP中账号信息
     */
    private boolean getAccount() {
        String username = (String) SPTool.get(this, Constants.KEY_USERNAME, "");
        if (StringUtils.isEmpty(username)) {
            //  wvHome.loadUrl(domain + URLConstants.MAIN_URL);
            return true;
        }

        MyApplication.username = username;
        MyApplication.password = (String) SPTool.get(this, Constants.KEY_PASSWORD, "");
        return false;
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    appUpdate.checkUpdate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        appUpdate.permissionsResult(grantResults);
    }

    /**
     * 加载Web资源
     */
    protected void loadWeb(InJavaScript javaScript) {
        this.webSetting(wvHome);
        wvHome.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvHome.setWebViewClient(client);
        wvHome.loadUrl("javascript:window.open();");

        this.webSetting(wvMine);
        wvMine.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvMine.setWebViewClient(client);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.RESULT_2_HOME:
                switchTabHome();
                break;
            case Constants.RESULT_2_MINE:
                switchTabMine();
                wvMine.reload();
                break;
            case Constants.REGISTER_SUCCESS:
                SPTool.put(instance, Constants.KEY_USERNAME, MyApplication.username);
                SPTool.put(instance, Constants.KEY_PASSWORD, MyApplication.password);
                login();
                break;
            case Constants.LOGOUT_RESULT:
                toLogout();
                break;
        }
    }

    /**
     * 登录成功后的操作
     */
    protected void loginAfter() {
        MyApplication.isLogin = true;
        //登录后改变网页状态
        wvHome.loadUrl("javascript:window.sessionStorage.setItem('is_login', true);window.page.menu.getHeadInfo();");
    }

    /**
     * 登陆后重新加载WebView
     */
    protected void reloadWebView() {
        wvHome.clearHistory();
        wvMine.clearHistory();
        //预加载首页，会员中
        String href = getIntent().getStringExtra("href");
        wvHome.loadUrl(TextUtils.isEmpty(href) ? domain + URLConstants.MAIN_URL : href);
        wvHome.setTag(false);
        wvMine.loadUrl(domain + URLConstants.MINE_URL);
        wvMine.setTag(false);
    }

    protected void changeToolbarView() {
        if (ivRefresh != null) {
            if (MyApplication.isLogin) {
                if (currTabIndex == 0 || currTabIndex == 1) {
                    tvUsername.setVisibility(View.VISIBLE);
                    ivRefresh.setVisibility(View.GONE);
                } else if (currTabIndex == 3) {
                    tvUsername.setVisibility(View.GONE);
                    ivRefresh.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // 首页
    @OnClick(R.id.llTabHome)
    public void switchTabHome() {
        showToolbar();
        if (currTabIndex == 0) {
            wvHome.clearHistory();
            wvHome.loadUrl(domain + URLConstants.MAIN_URL);
        } else {
            initTab(0);
            dynamicAddView(ivHome, AttrConst.BACKGROUND, R.drawable.tab_home_selected);
            dynamicAddView(tvHome, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
            wvHome.setVisibility(View.VISIBLE);
            wvMine.loadUrl("javascript:window.page.getOpenResult();");
//            if (wvHome.getTag() instanceof Boolean && Boolean.valueOf(wvHome.getTag().toString()) == true) {
//                wvHome.clearHistory();
////                wvHome.loadUrl(domain + URLConstants.MAIN_URL);
//                wvHome.setTag(false);
//            }
        }
        changeToolbarView();
    }

    // 我的
    @OnClick(R.id.llTabMine)
    public void switchTabMine() {
        if (!MyApplication.isLogin) {
            toLoginActivity(Constants.RESULT_2_MINE);
        } else {
            hideToolbar();
            if (currTabIndex == 4) {
                wvMine.clearHistory();
                wvMine.loadUrl(domain + URLConstants.MINE_URL);
            } else {
                initTab(4);
                dynamicAddView(ivMine, AttrConst.BACKGROUND, R.drawable.tab_mine_selected);
                dynamicAddView(tvMine, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
                wvMine.setVisibility(View.VISIBLE);
                if (wvMine.getTag() instanceof Boolean && Boolean.valueOf(wvMine.getTag().toString()) == true) {
                    wvMine.clearHistory();
                    wvMine.loadUrl(domain + URLConstants.MINE_URL);
                    wvMine.setTag(false);
                } else {
                    wvMine.loadUrl("javascript:window.page.getUserInfo();");
                }
            }
        }
    }

    /**
     * 初始化底部Tab栏
     */
    protected void initTab(int targetIndex) {
        switchTabAnim(currTabIndex, targetIndex);

        wvHome.setVisibility(View.GONE);
        dynamicAddView(ivHome, AttrConst.BACKGROUND, R.drawable.tab_home_normal);
        dynamicAddView(tvHome, AttrConst.TEXT_COLOR, R.color.tabTextNormal);

        wvMine.setVisibility(View.GONE);
        dynamicAddView(ivMine, AttrConst.BACKGROUND, R.drawable.tab_mine_normal);
        dynamicAddView(tvMine, AttrConst.TEXT_COLOR, R.color.tabTextNormal);
    }

    /**
     * 底部切换动画
     */
    protected void switchTabAnim(int curr, int target) {
        currTabIndex = target;
        if (curr == target) return;

        int ya, yn;
        if (curr < target) {
            ya = -1;
            yn = 1;
        } else {
            ya = 1;
            yn = -1;
        }
        setAnimation(getWebView(curr), 0, ya, 0, 0, 300);
        setAnimation(getWebView(target), yn, 0, 0, 0, 300);
    }

    protected void showToolbar() {
        if (toolbar.getVisibility() == View.GONE) {
            setAnimation(toolbar, -1, 0, 0, 0, 300);
            toolbar.setVisibility(View.VISIBLE);
        }
        if (!MyApplication.isLogin) {
            llLogout.setVisibility(View.VISIBLE);
            tvUsername.setVisibility(View.GONE);
        }
    }

    protected void hideToolbar() {
        if (toolbar.getVisibility() == View.VISIBLE) {
            setAnimation(toolbar, 0, -1, 0, 0, 300);
            toolbar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnLogin)
    public void toLogin() {
        toLoginActivity(Constants.RESULT_2_HOME);
    }

    @OnClick(R.id.btnRegister)
    public void toRegister() {
        toCommonActivity(domain + URLConstants.REGISTER_URL);
    }

    private final class MyWebViewClient extends CommonWVClient implements Runnable {
        private boolean mDone = false;
        private String mLoadingUrl = "";

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = super.shouldInterceptRequest(view, request);
            if (response != null) {
                int statusCode = response.getStatusCode();
                if (response.getResponseHeaders() != null && response.getResponseHeaders().containsKey("headerStatus")) {
                    statusCode = Integer.valueOf(response.getResponseHeaders().get("headerStatus"));
                }
                if (statusCode == 600 || statusCode == 606) {
                    toLoginActivity(Constants.RESULT_LOGIN_FAIL);
                }
            }
            return response;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "request url = " + url);
            // 当请求的url是登录请求时，拦截
            if (url.contains(URLConstants.LOGIN_URL)) {
                view.stopLoading();
                view.goBack();
                if (!MyApplication.isLogin && !url.equals(mLoadingUrl)) {
                    mLoadingUrl = url;
                    logout(true);
                    ToastTool.show(instance, getString(R.string.reLogin));
                    toLoginActivity(Constants.RESULT_2_HOME);
                }
                return false;
            } else if (url.endsWith(URLConst.ERROR_606)) {
                view.stopLoading();
                view.goBack();
                mLoadingUrl = url;
                logout(true);
                ToastTool.show(instance, getString(R.string.e606));
                toLoginActivity(Constants.RESULT_LOGIN_FAIL);
                return false;
            }
            WebView.HitTestResult hitTestResult = view.getHitTestResult();
            if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                view.loadUrl(url);
                Log.e(TAG, "!TextUtils.isEmpty(url) && hitTestResult == null " + url);
                return true;
            }
            //防止循环加载
            if (!url.equals(mLoadingUrl)) {
                view.loadUrl(url);
            }
            Log.e(TAG, "跳转 ==> " + url);
            if (TextUtils.isEmpty(mLoadingUrl)) {
                mLoadingUrl = url;
            }
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            WebResourceResponse response = loadResource(url);
            if (response != null)
                return response;
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mDone = false;

            Log.e(TAG, "开始请求 ==> " + url);
            if (url.equals(MyApplication.domain + "/")) {
                url = url + "mainIndex.html";
            }
            super.onPageStarted(view, url, favicon);
            if (!TextUtils.isEmpty(MyApplication.cookie)) {
                syncCookie(url, MyApplication.cookie);
            }
            if (view.getId() != R.id.wvService) {
                showProgress(R.string.loading_tip, true);
            }
            // 加载2秒后强制回馈完成
            view.postDelayed(this, 2000);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //加载图片先不展示 等页面全部加载完毕再加载图片
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
            if (view.getId() == R.id.wvHome)
                view.loadUrl("javascript:window.gamebox.getLoginState(sessionStorage.is_login);");
            else
                view.loadUrl(String.format("javascript:window.gamebox.getLoginState(%s);", String.valueOf(MyApplication.isLogin)));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgress();
                }
            });


            if (!MyApplication.isLogin && url.endsWith(URLConstants.MAIN_URL)) {
                String cookie = CookieManager.getInstance().getCookie(url);
                Log.e(TAG, "当前的Cookie ==> " + cookie);
                MyApplication.cookie = cookie;
            }
            Log.e(TAG, "请求结束 ==> " + url);
        }

        // 重写此方法，可以让WebView执行https请求
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        // 当WebView发生改变的时候调用这个方法
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        // 6.0 及以上版本调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) {
                if (error.getErrorCode() == WebViewCodeEnum.NOT_RESOLVED.getCode()) {
                    view.loadUrl(Const.PAGE_UN_NET);
                } else {
                    ToastTool.show(instance, R.string.net_busy_error);
                }
            }
        }

        @Override
        public synchronized void run() {
            if (!mDone) {
                mDone = true;
            }
        }
    }

    protected class InJavaScript {
        @JavascriptInterface
        public void toast(String msg) {
            ToastTool.showToast(instance, msg);
        }

        // 获取登录状态
        @JavascriptInterface
        public void getLoginState(String state) {
            if (!state.equals("undefined")) {
                if (MyApplication.isLogin && !Boolean.valueOf(state)) {
                    // 当登录状态改变时
                    BaseMainActivity.this.logout(false);
                    MyApplication.isLogin = false;
                }
            }
        }

        // 进入游戏
        @JavascriptInterface
        public void gotoGame(final String url) {
            Log.i(TAG, "gotoGame:" + url);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.gameUrlError);
                    } else {
                        Intent intent = new Intent(instance, GameActivity.class);
                        intent.putExtra("url", url.startsWith("http") ? url : domain + url);
                        startActivity(intent);
                    }
                }
            });
        }

        // 跳转第三方支付
        @JavascriptInterface
        public void gotoPay(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        Intent intent = new Intent(instance, PayActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        }

        // 注册后登录
        @JavascriptInterface
        public void gotoLogin(final String _href) {
            Log.e(TAG, "调gotoLogin方法");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (_href.isEmpty()) {
                            String[] params = _href.split(",");
                            SPTool.put(instance, Constants.KEY_USERNAME, params[0]);
                            SPTool.put(instance, Constants.KEY_PASSWORD, params[1]);
                            login();
                        }
                        // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                        int flag = 0;
                        switch (currTabIndex) {   //判断登录是从哪个tab发起的
                            case 0:
                                flag = Constants.RESULT_2_HOME;
                                break;
                            case 3:
                                if (ParamTool.isLotterySite(context)) {   //纯彩票
                                    flag = Constants.RESULT_2_HALL;
                                }
                                break;
                        }
                        toLoginActivity(flag);
                    } catch (Exception e) {
                        ToastTool.show(instance, R.string.net_busy_error);
                    }
                }
            });
        }

        // 登录
        @JavascriptInterface
        public void goLogin() {
            Log.e(TAG, "调goLogin方法");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                        int flag = 0;
                        switch (currTabIndex) {//判断登录是从哪个tab发起的
                            case 0:
                                flag = Constants.RESULT_2_HOME;
                                break;
                            case 3:
                                if (ParamTool.isLotterySite(context)) {   //纯彩票
                                    flag = Constants.RESULT_2_HALL;
                                }
                                break;
                        }
                        toLoginActivity(flag);
                    } catch (Exception e) {
                        ToastTool.show(instance, R.string.net_busy_error);
                    }
                }
            });
        }

        // 退出登录
        @JavascriptInterface
        public void logout() {
            toLogout();
        }

        @JavascriptInterface
        public void gotoActivity(final String url) {
            Log.e(TAG, "调gotoActivity方法");
            Log.i(TAG, "gotoActivity:" + url);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        String newUrl = url.contains("http") ? url : domain + url;
                        if (newUrl.contains("/mine/index.") || newUrl.contains(domain + "/index.")) {
                            wvHome.loadUrl(newUrl);
                        } else {
                            String csUrl = String.valueOf(SPTool.get(context, Constants.KEY_CUSTOMER_SERVICE, Const.PAGE_404));
                            String code = getString(R.string.app_code);
                            if (url.contains(csUrl) && "sn2m".equals(code)) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            } else {
                                toCommonActivity(newUrl);
                                Log.e(TAG, "---> gotoActivity(     跳转    )");
                            }
                        }
                    }
                }
            });
        }

        // API详情
        @JavascriptInterface
        public void gotoApi(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!MyApplication.isLogin) {
                        try {
                            // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                            int flag = 0;
                            switch (currTabIndex) {//判断登录是从哪个tab发起的
                                case 0:
                                    flag = Constants.RESULT_2_HOME;
                                    break;
                                case 3:
                                    if (ParamTool.isLotterySite(context)) {   //纯彩票
                                        flag = Constants.RESULT_2_HALL;
                                    }
                                    break;
                            }
                            toLoginActivity(flag);
                            return;
                        } catch (Exception e) {
                            ToastTool.show(instance, R.string.net_busy_error);
                        }
                    }


                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        Intent intent = new Intent(instance, ApiActivity.class);
                        intent.putExtra("url", url.contains("http") ? url : domain + url);
                        startActivityForResult(intent, 0);
                    }
                }
            });
        }

        // 设置
        @JavascriptInterface
        public void setting() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(instance, SettingActivity.class);
                    startActivityForResult(intent, Constants.LOGOUT_RESULT);
                }
            });
        }
    }

    protected static class MyHandler extends Handler {
        WeakReference<BaseMainActivity> mActivity;

        MyHandler(BaseMainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public void toLogout() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpUtils.get().url(domain + URLConstants.LOGOUT_URL).headers(setHeaders()).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e(TAG, "logout error ==> " + e.getLocalizedMessage());
                            if (e.toString().contains(CodeEnum.S_DUE.getCode()) || e.toString().contains(CodeEnum.S_KICK_OUT.getCode())) {
                                BaseMainActivity.this.logout(true);
                                wvHome.loadUrl("javascript:window.page.menu.getHeadInfo();");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            BaseMainActivity.this.logout(true);
//                                wvHome.loadUrl(domain + URLConstants.MAIN_URL);
                            wvHome.loadUrl("javascript:window.page.menu.getHeadInfo();");
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "logout exception ==> " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (currTabIndex) {
                case 0:
                    break;
                case 4:
                    if (wvMine.canGoBack()) {
                        wvMine.goBack();
                        return true;
                    }
                    break;
            }
            onBackPressed();
        }
        return false;
    }

    // 返回后无需经过启动页
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    protected View getWebView(int index) {
        switch (index) {
            case 0:
                return wvHome;
            case 4:
                return wvMine;
            default:
                return null;
        }
    }

    public void toCommonActivity(String url) {
        Intent intent = new Intent(instance, CommonActivity.class);
        intent.putExtra("url", url);

        //处理不同tab到投注页面再登录后的返回目标
        if (currTabIndex == 0) {
            intent.putExtra(Constants.RESULT_FLAG, Constants.BETACTIVITY_2_HOME);
        } else if (currTabIndex == 2 && ParamTool.isLotterySite(context)) {   //纯彩票有购彩大厅至投注页面入口
            intent.putExtra(Constants.RESULT_FLAG, Constants.BETACTIVITY_2_HALL);
        }
        startActivityForResult(intent, 0);
    }

    /**
     * 获取客服地址
     */
    private void getService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils.get().url(domain + URLConstants.SERVICE_URL)
                                .headers(setHeaders()).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e(TAG, "getService error ==> " + e.getLocalizedMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                SPTool.remove(context, Constants.KEY_CUSTOMER_SERVICE);
                                SPTool.put(context, Constants.KEY_CUSTOMER_SERVICE, response);
                                Log.i(TAG, "getService :" + response);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!MyApplication.isLogin) {
//            wvHome.loadUrl(domain + URLConstants.MAIN_URL);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvHome.stopLoading();
        wvHome.removeAllViews();
        wvHome.destroy();
        wvHome = null;

        wvMine.stopLoading();
        wvMine.removeAllViews();
        wvMine.destroy();
        wvMine = null;
    }

}