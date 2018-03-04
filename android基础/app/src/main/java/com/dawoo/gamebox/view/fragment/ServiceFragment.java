
package com.dawoo.gamebox.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.CookieManager;

import android.webkit.JavascriptInterface;

import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;

import android.webkit.WebResourceRequest;

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
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.SchemeEnum;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.FileTool;
import com.dawoo.gamebox.util.NetUtil;
import com.dawoo.gamebox.util.SPTool;
import com.dawoo.gamebox.view.activity.LoginActivity;
import com.dawoo.gamebox.view.activity.MainActivity;
import com.dawoo.gamebox.view.activity.webview.WebViewActivity;
import com.dawoo.gamebox.view.view.CustomProgressDialog;
import com.dawoo.gamebox.view.view.WebHeaderView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class ServiceFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.head_view)
    WebHeaderView mHeadView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.webview_fl)
    FrameLayout mWebviewFL;
    Unbinder unbinder;
    private WebView mWebview;
    private CustomProgressDialog mProgressDialog;
    private boolean isInitData = false;
    private Handler mHandler;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgUrl;

    public ServiceFragment() {
    }

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service, container, false);
        unbinder = ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        getService();
    }

    public void initViews() {
        createWebView();
        mRefreshLayout.setEnabled(false);
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
        mWebview = new WebView(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebviewFL.addView(mWebview, layoutParams);
    }

  /*  *//**
     * 登录成功后，回调加载账户
     *//*
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGINED)})
    public void loginedReload(String s) {
        if (isInitData) {
            loadData();
        }
    }*/

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
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
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
        settings.setUserAgentString(ua);
        CookieManager.getInstance().setAcceptCookie(true);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "gamebox");

    }

    @Override
    public void onRefresh() {
        if (mWebview != null) {
            mWebview.reload();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgress(R.string.loading_tip, true);
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
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.i("thirdPay url ==> " + url);
            if (url == null) return false;
            if (url.contains("login/commonLogin.html")) {
                ActivityUtil.gotoLogin();
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
                } else if (url.startsWith("https://payh5.bbnpay.com/") || isZXPay(url) || isGaotongPay(url) || isAimiSenPay(url) || isXingPay(url)) {
                    startBrowsers(url);
                } else {
                    mWebview.loadUrl(url);
                }
            } catch (Exception e) {
                LogUtils.e("跳转支付页面异常 ==> " + e.getMessage());
            }

            return true;
        }


        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();
        }

//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
//        }

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
            setTitle(title);
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
        if (title != null) {
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

    boolean isZXPay(String url) {
        if (url.startsWith("https://zhongxin.junka.com/")) {
            return true;
        }
        return false;
    }

    boolean isGaotongPay(String url) {
        if (url.startsWith("http://wgtj.gaotongpay.com/")) {
            return true;
        }
        return false;
    }

    boolean isAimiSenPay(String url) {
        if (url.startsWith("https://www.joinpay.com")) {
            return true;
        }
        return false;
    }

    boolean isXingPay(String url) {
        if (url.startsWith("https://gate.lfbpay.com")) {
            return true;
        }
        return false;
    }

    boolean isSite271(String url) {
        if (url.startsWith("http://pay.88vipbet.com/onlinePay")) {
            return true;
        }
        return false;
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
                    intent.putExtra("url", url);
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
        public void saveImage(final String href) {
            mHandler.post(() -> {
                try {
                    if (!TextUtils.isEmpty(href)) {
                        String domain = DataCenter.getInstance().getDomain();
                        imgUrl = href.contains(domain) ? href : domain + "/" + href;

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
                    } else {
                        ToastUtil.showToastShort(mContext, getString(R.string.imageUrlNil));
                    }
                } catch (Exception e) {
                    ToastUtil.showToastShort(mContext, getString(R.string.saveImgFailed));
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

    /**
     * 获取客服地址
     */
    private void getService() {
        OkHttpUtils.get().url(DataCenter.getInstance().getDomain() + ConstantValue.SERVICE_URL)
                .headers(NetUtil.setHeaders()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("getService error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                SPTool.remove(mContext, ConstantValue.KEY_CUSTOMER_SERVICE);
                SPTool.put(mContext, ConstantValue.KEY_CUSTOMER_SERVICE, response);
                if (mHeadView != null) {
                    mWebview.loadUrl(response);
                }
                isInitData = true;
            }
        });
    }

}