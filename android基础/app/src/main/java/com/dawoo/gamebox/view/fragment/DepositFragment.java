package com.dawoo.gamebox.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import com.dawoo.gamebox.view.activity.webview.X5ObserWebView;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;

import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.CapitalRecord;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.SchemeEnum;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.FileTool;
import com.dawoo.gamebox.util.SPTool;
import com.dawoo.gamebox.util.SharePreferenceUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.activity.LoginActivity;
import com.dawoo.gamebox.view.activity.MainActivity;
import com.dawoo.gamebox.view.activity.PromoRecordActivity;
import com.dawoo.gamebox.view.activity.webview.WebViewActivity;
import com.dawoo.gamebox.view.view.CustomProgressDialog;
import com.dawoo.gamebox.view.view.WebHeaderView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.tencent.smtt.sdk.CookieManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class DepositFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.head_view)
    WebHeaderView mHeadView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.webview_fl)
    FrameLayout mWebviewFL;
    Unbinder unbinder;
    private X5ObserWebView mWebview;
    private CustomProgressDialog mProgressDialog;
    private boolean isInitData = false;
    private Handler mHandler;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgUrl;
    private String mUrl;

    public DepositFragment() {
    }

    public static DepositFragment newInstance() {
        DepositFragment fragment = new DepositFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deposit, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected void loadData() {
//        mUrl = SharePreferenceUtil.getDepositUrl(mContext);
//        if (!mUrl.contains("http") && mUrl.startsWith("/")) {
//
//        }
        mUrl = DataCenter.getInstance().getDomain() + ConstantValue.DEPOSIT_URL;

        String postDate = DataCenter.getInstance().getSysInfo().getPostString();
        mWebview.postUrl(mUrl, EncodingUtils.getBytes(postDate, "BASE64"));

        isInitData = true;
    }

    public void initViews() {
        createWebView();
       // mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(this);
        //改变加载显示的颜色
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        mHeadView.setLeftBackListener(new OnLeftBackClickListener());
        webSetting(mWebview);
    }


    private class OnLeftBackClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mWebview.canGoBack()) {
                mWebview.goBack();
            } else {
                ToastUtil.showResShort(mContext, R.string.is_end_page);
            }
        }
    }


    private void initData() {
        mHandler = new Handler();
    }

    private void createWebView() {
        mWebview = new X5ObserWebView(mContext);
        mWebview.setOnScrollChangedCallback(new X5ObserWebView.OnScrollChangedCallback(){

            @Override
            public void onScroll(int l, int t) {
                if (t == 0) {//webView在顶部
                    mRefreshLayout.setEnabled(true);
                } else {//webView不是顶部
                    mRefreshLayout.setEnabled(false);
                }
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebviewFL.addView(mWebview, layoutParams);
    }

    /**
     * 登录成功后，回调加载账户
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGINED)})
    public void loginedReload(String s) {
        if (isInitData) {
            loadData();
        }
    }

    /**
     * 设置WebView
     */
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    protected void webSetting(final WebView webView) {
        WebSettings settings = webView.getSettings();
        String appCacheDir = BoxApplication.getContext().getDir("cache", Context.MODE_PRIVATE).getPath();
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
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //修改硬件加速导致页面渲染闪烁问题
        // webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setOnTouchListener((v, event) -> {
            webView.requestFocus();
            return false;
        });
        String ua = settings.getUserAgentString().replace("Android", "app_android");
        settings.setUserAgentString(ua + "; is_native=true");
        CookieManager.getInstance().setAcceptCookie(true);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "gamebox");

    }

    @Override
    public void onRefresh() {
        if (mWebview != null && mUrl != null) {
            String postDate = DataCenter.getInstance().getSysInfo().getPostString();
            mWebview.postUrl(mUrl, EncodingUtils.getBytes(postDate, "BASE64"));
        }
    }


    /**
     * 提交一次后不能重复发起提交
     */
//    public void reFreshDepositUrl() {
//        if (mWebview != null && mUrl != null && mUrl.equals(mWebview.getUrl())) {
//            mWebview.reload();
//        }
//    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgress(R.string.loading_tip, true);
            if (mUrl != null && mUrl.equals(view.getUrl())) {
                mRefreshLayout.setEnabled(true);
            } else {
                mRefreshLayout.setEnabled(false);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // webView.goBack() 重设标题
            setTitle(String.valueOf(view.getTitle()));
            dismissProgress();
            // 加载完数据设置为不刷新状态，将下拉进度收起来
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }

            if (url.equals(mUrl)) {
                mWebview.clearHistory();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.i("thirdPay url ==> " + url);
            if (url == null) return false;
            if (url.contains("login/commonLogin.html")) {
                logout();
                return false;
            } else if (url.contains("/mainIndex.html")) {
                logout();
                return false;
            }


            try {
                if (url.startsWith(SchemeEnum.INTENT.getCode()) || url.startsWith(SchemeEnum.QQ.getCode())
                        || url.startsWith(SchemeEnum.ALIPAY.getCode()) || url.startsWith(SchemeEnum.WECHAT.getCode())) {

                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    intent.setSelector(null);
                    List<ResolveInfo> resolves = BoxApplication.getContext().getPackageManager().queryIntentActivities(intent, 0);
                    if (resolves.size() > 0) {
                        mContext.startActivity(intent);
                        // mContext.startActivityIfNeeded(intent, -1);
                    }
                } else {
                    mWebview.loadUrl(url);
                }
            } catch (Exception e) {
                LogUtils.e("跳转支付页面异常 ==> " + e.getMessage());
            }

            return true;
        }


//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
//        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();
        }

        //        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            return super.shouldInterceptRequest(view, request);
//        }


        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            switch (errorCode) {
                case ERROR_CONNECT:
                    mWebview.loadUrl("file:///android_asset/html/unNet.html");
                    break;
            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!title.startsWith("http")) {
                setTitle(title);
            }

        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            setProgressBar(progress);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    private void setTitle(String title) {
        if (title == null) {
            return;
        }

        if (mWebview.getUrl() != null && mWebview.getUrl().contains(SharePreferenceUtil.getDepositUrl(mContext))) {
            mHeadView.setHeader(title, false, false);
        } else {
            mHeadView.setHeader(title, true, false);
        }
    }


    /**
     * 调用浏览
     *
     * @param url
     */
    private void startBrowsers(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * 设置进度条
     *
     * @param progress
     */
    private void setProgressBar(int progress) {
        if (progress == 100) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == mProgressBar.getVisibility()) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(progress);
        }
    }

    private void logout() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            DataCenter.getInstance().setLogin(false);
            DataCenter.getInstance().setCookie("");
            DataCenter.getInstance().setUserName("");
            DataCenter.getInstance().setPassword("");
            ActivityUtil.gotoLogin();
            ((MainActivity) mContext).switchTab(MainActivity.TAB_INDEX_HOME);
            RxBus.get().post(ConstantValue.EVENT_TYPE_LOGOUT, "logout");
        }
    }


    @Override
    public void onDestroy() {
        mContext = null;
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }

    protected void showProgress(int resId, boolean canBack) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
        mProgressDialog = new CustomProgressDialog(mContext, getResources().getString(resId));
        mProgressDialog.setCancelable(canBack);
        mProgressDialog.show();
    }


    protected void dismissProgress() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }


    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void log(final String url) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastUtil.showToastShort(mContext, getString(R.string.url_is_null));
                    } else {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        }

        @JavascriptInterface
        public void toast(String msg) {
            ToastUtil.showToastShort(mContext, msg);
        }

        // 进入游戏
        @JavascriptInterface
        public void gotoGame(final String url) {
            mHandler.post(() -> {
                if (TextUtils.isEmpty(url)) {
                    ToastUtil.showToastShort(mContext, getString(R.string.url_is_null));
                } else {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(ConstantValue.WEBVIEW_URL, url);
                    intent.putExtra(ConstantValue.WEBVIEW_TYPE, ConstantValue.WEBVIEW_TYPE_GAME);
                    startActivity(intent);
                }
            });
        }

        // 进入第三方支付
        @JavascriptInterface
        public void gotoPay(final String url) {
            mHandler.post(() -> {
                if (TextUtils.isEmpty(url)) {
                    ToastUtil.showToastShort(mContext, getString(R.string.url_is_null));
                } else {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(ConstantValue.WEBVIEW_URL, url);
                    intent.putExtra(ConstantValue.WEBVIEW_TYPE, ConstantValue.WEBVIEW_TYPE_PAY);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void gotoActivity(final String url) {
            mHandler.post(() -> {
                if (TextUtils.isEmpty(url)) {
                    ToastUtil.showToastShort(mContext, getString(R.string.url_is_null));
                } else {
                    String domain = DataCenter.getInstance().getDomain();
                    mWebview.loadUrl(url.contains(domain) ? url : domain + url);
                    // openAssets(url);
                }
            });
        }


        // 回到主页
        @JavascriptInterface
        public void gotoHome(final String url) {
            mHandler.post(() -> {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }

        // 注册后登录
        @JavascriptInterface
        public void gotoLogin(final String _href) {
            mHandler.post(() -> {
                try {
                    if (TextUtils.isEmpty(_href)) {
                        ToastUtil.showToastShort(mContext, getString(R.string.url_is_null));
                    } else {
                        String[] params = _href.split(",");
                        DataCenter.getInstance().getUser().username = params[0];
                        DataCenter.getInstance().getUser().password = params[1];

                        SPTool.put(mContext, ConstantValue.KEY_USERNAME, DataCenter.getInstance().getUser().username);
                        SPTool.put(mContext, ConstantValue.KEY_PASSWORD, DataCenter.getInstance().getUser().password);
                    }
                    // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url

                } catch (Exception e) {
                    ToastUtil.showToastShort(mContext, getString(R.string.net_busy_error));
                }
            });
        }

        // 注册后登录
        @JavascriptInterface
        public void gotoLoginNew(String name, String pwd) {
            mHandler.post(() -> {
                try {
                    DataCenter.getInstance().getUser().setUsername(name);
                    DataCenter.getInstance().getUser().setPassword(pwd);

                    SPTool.put(mContext, ConstantValue.KEY_USERNAME, DataCenter.getInstance().getUser().username);
                    SPTool.put(mContext, ConstantValue.KEY_PASSWORD, DataCenter.getInstance().getUser().password);

                } catch (Exception e) {
                    // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                    ToastUtil.showToastShort(mContext, getString(R.string.net_busy_error));
                }
            });
        }

        @JavascriptInterface
        public void goLogin() {
            mHandler.post(() -> {
                DataCenter.getInstance().setLogin(false);
                Intent intent = new Intent(mContext, LoginActivity.class);
                //  intent.putExtra(Constants.RESULT_FLAG, resultFlag);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });

        }


        @JavascriptInterface
        public void showPro() {
            showProgress(R.string.loading_tip, true);
        }

        @JavascriptInterface
        public void disPro() {
            dismissProgress();
        }


        // 保存图片（二维码）
        @JavascriptInterface
        public void saveImage(final String url) {
            mHandler.post(() -> {
                if (TextUtils.isEmpty(url)) {
                    ToastUtil.showToastShort(mContext, getString(R.string.imageUrlNil));
                    return;
                }
                try {
                        String domain = DataCenter.getInstance().getDomain();
                        if(url.contains("http")) {
                            imgUrl = url;
                        } else {
                            imgUrl = url.contains(domain) ? url : domain + "/" + url;
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // 检查该权限是否已经获取
                            int hasAuth = ContextCompat.checkSelfPermission(mContext, permissions[0]);
                            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                            if (hasAuth != PackageManager.PERMISSION_GRANTED) {
                                // 提交请求权限
                                requestPermissions(permissions, 321);

                            } else {
                                handleImage();
                            }
                        } else {
                            handleImage();
                        }
                } catch (Exception e) {
                    ToastUtil.showToastShort(mContext, getString(R.string.saveImgFailed));
                }
            });
        }

        /**
         * 通知账户已经变动
         */
        @JavascriptInterface
        public void notifyAccountChange() {
            RxBus.get().post(ConstantValue.EVENT_TYPE_ACCOUNT, "updateAccount");
        }

        /**
         * 刷新当前页面
         */
        @JavascriptInterface
        public void refreshPage() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mWebview != null) {
                        mWebview.reload();
                    }
                }
            });

        }


        /**
         * 如果有上一级页面，就返回上一级
         * 如果没有上一级页面，就不返回，而是提示用户
         */
        @JavascriptInterface
        public void goBackPage() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mWebview == null) {
                        return;
                    }
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        ToastUtil.showResShort(mContext, R.string.is_end_page);
                    }
                }
            });
        }

        /**
         * 跳入存款页面
         */
        @JavascriptInterface
        public void gotoDepositPage() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mWebview == null) {
                        return;
                    }
                    loadData();
                }
            });
        }

        /**
         * 跳到资金记录页面
         */
        @JavascriptInterface
        public void gotoCapitalRecordPage() {
            mContext.startActivity(new Intent(mContext, CapitalRecord.class));
        }

        /**
         * 跳到优惠记录页面
         */
        @JavascriptInterface
        public void gotoPromoRecordPage() {
            startActivity(new Intent(mContext, PromoRecordActivity.class));
        }

        /**
         * 去登录
         */
        @JavascriptInterface
        public void gotoLoginPage() {
            startActivity(new Intent(mContext, LoginActivity.class));
        }

        /**
         * 去首页
         */
        @JavascriptInterface
        public void gotoHomePage() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) mContext).switchTab(MainActivity.TAB_INDEX_HOME);
                    loadData();
                }
            });
        }

        /**
         * 点击声音
         */
        @JavascriptInterface
        public void onClickVoice() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    SoundUtil.getInstance().playVoiceOnclick();
                }
            });
        }


        /**
         * 打开新的webview（打开一个新的弹窗，并且根据传入的url进行加载）
         *
         * @param url
         */
        @JavascriptInterface
        public void startNewWebView(String url, String type) {
            ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_ORDINARY);
        }

        /**
         * 注册后登录
         *
         * @param name
         * @param pwd
         */
        @JavascriptInterface
        public void nativeAutoLogin(String name, String pwd) {
            mHandler.post(() -> {
                try {
                    DataCenter.getInstance().getUser().setUsername(name);
                    DataCenter.getInstance().getUser().setPassword(pwd);

                    SPTool.put(mContext, ConstantValue.KEY_USERNAME, DataCenter.getInstance().getUser().username);
                    SPTool.put(mContext, ConstantValue.KEY_PASSWORD, DataCenter.getInstance().getUser().password);

                } catch (Exception e) {
                    // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                    ToastUtil.showToastShort(mContext, getString(R.string.net_busy_error));
                }
            });
        }
    }

    private void handleImage() {
        final String[] files = setImage();
        OkHttpUtils.get().url(imgUrl).build().execute(new FileCallBack(files[0], files[1]) {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("saveImage error ==> " + e.getMessage());
                ToastUtil.showResShort(mContext, R.string.saveImgFailed);
            }

            @Override
            public void onResponse(File response, int id) {
                ToastUtil.showResShort(mContext, R.string.saveImgSuccess);
                fileScan(files[2]);
            }
        });
    }

    private String[] setImage() {
        String[] files = new String[3];

        String saveDir = FileTool.getSDCardDir() + "/Pictures";
        if (!FileTool.dirIsExists(saveDir)) {
            FileTool.makeDir(saveDir, false);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("pay_code_").append(DateTool.getCurrentDate(DateTool.FMT_DATE_TIME_3)).append(".png");
        files[0] = saveDir;
        files[1] = sb.toString();
        files[2] = saveDir + "/" + sb.toString();

        return files;
    }

    // 保存后扫描文件
    private void fileScan(String filePath) {
        Uri data = Uri.parse("file://" + filePath);
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleImage();
            }
        }
    }

//    /**
//     * 跳入存款页面
//     */
//    @JavascriptInterface
//    public void gotoDepositPage() {
//        ((MainActivity) mContext).switchTab(MainActivity.TAB_INDEX_DEPOSIT);
//    }

}
