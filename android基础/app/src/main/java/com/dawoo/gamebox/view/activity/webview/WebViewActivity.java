package com.dawoo.gamebox.view.activity.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dawoo.coretool.CleanLeakUtils;
import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.ApiEnum;
import com.dawoo.gamebox.bean.CapitalRecord;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.SchemeEnum;
import com.dawoo.gamebox.mvp.view.IWebView;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.FileTool;
import com.dawoo.gamebox.util.SPTool;
import com.dawoo.gamebox.view.activity.BaseActivity;
import com.dawoo.gamebox.view.activity.LoginActivity;
import com.dawoo.gamebox.view.activity.MainActivity;
import com.dawoo.gamebox.view.activity.MoreActivity;
import com.dawoo.gamebox.view.activity.PromoRecordActivity;
import com.dawoo.gamebox.view.view.DragViewLayout;
import com.dawoo.gamebox.view.view.WebHeaderView;
import com.hwangjr.rxbus.RxBus;
import com.squareup.haha.perflib.Main;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class WebViewActivity extends BaseActivity implements IWebView, View.OnClickListener {

    @BindView(R.id.head_view)
    WebHeaderView mHeadView;
    @BindView(R.id.webview_fl)
    DragViewLayout mWebviewFL;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Android 5.0以下版本的文件选择回调
     */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /**
     * Android 5.0及以上版本的文件选择回调
     */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;
    protected static final int REQUEST_CODE_FILE_PICKER = 51426;
    protected String mUploadableFileTypes = "image/*";
    private WebView mWebview;
    private String apiId;
    private Handler mHandler;
    private WebViewActivity instance;
    private ImageView mHomeIv;
    private ImageView mBackIv;
    private String mType;
    private LinearLayout mLl;
    private String mImgUrl;

    @Override
    protected void createLayoutView() {
        mType = getIntent().getStringExtra(ConstantValue.WEBVIEW_TYPE);

        if (ConstantValue.WEBVIEW_TYPE_GAME.equals(mType)) {
            // 全屏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initViews() {
        createWebView();
        initWebSetting();

        mHeadView.setLeftBackListener(new OnLeftBackClickListener());
    }

    @Override
    protected void initData() {
        mHandler = new Handler();
        instance = this;
        RxBus.get().register(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra(ConstantValue.WEBVIEW_URL);
        if (!url.contains("http") && url.startsWith("/")) {
            url = DataCenter.getInstance().getDomain() + url;
        }

        if (url != null && url.contains("ad=")) {
            apiId = url.substring(url.lastIndexOf("ad=") + 3, url.length());
        }
        if (apiId != null && apiId.equals(ApiEnum.MG.getCode())) {
            //ua.ios
            String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Version/10.0 Mobile/14D27 Safari/602.1";
            mWebview.getSettings().setUserAgentString(ua);
        }
        String postDate = DataCenter.getInstance().getSysInfo().getPostString();
        mWebview.postUrl(url, EncodingUtils.getBytes(postDate, "BASE64"));
        //mWebview.loadUrl(url);
    }

    private void createWebView() {
        mWebview = new WebView(BoxApplication.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebview.setLayoutParams(layoutParams);
        mWebviewFL.addView(mWebview);
        if (ConstantValue.WEBVIEW_TYPE_GAME.equals(mType)) {
            createDragViewButton();
        }
    }

    private void createDragViewButton() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DensityUtil.dp2px(this, 28),
                DensityUtil.dp2px(this, 56),
                Gravity.BOTTOM | Gravity.RIGHT);
        mLl = new LinearLayout(this);
        mLl.setGravity(Gravity.CENTER);
        mLl.setLayoutParams(params);
        mLl.setBackgroundResource(R.mipmap.drag_view_bg);
        mLl.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(this, 28), DensityUtil.dp2px(this, 28));
        mHomeIv = new ImageView(this);
        mHomeIv.setLayoutParams(childParams);
        mHomeIv.setImageResource(R.mipmap.drag_view_home);
        mHomeIv.setOnClickListener(this);

        mBackIv = new ImageView(this);
        mBackIv.setLayoutParams(childParams);
        mBackIv.setImageResource(R.mipmap.drag_view_back);
        mBackIv.setOnClickListener(this);
        mLl.addView(mHomeIv);
        mLl.addView(mBackIv);
        mWebviewFL.addView(mLl);
    }

    void setDragViewLandScapePos() {
        if (mLl == null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DensityUtil.dp2px(this, 28),
                DensityUtil.dp2px(this, 56),
                Gravity.END);
        mLl.setLayoutParams(params);
    }

    void setDragViewPortrait() {
        if (mLl == null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DensityUtil.dp2px(this, 28),
                DensityUtil.dp2px(this, 56),
                Gravity.BOTTOM | Gravity.RIGHT);
        mLl.setLayoutParams(params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            setDragViewLandScapePos();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            setDragViewPortrait();
        }
    }

    @Override
    public void initWebSetting() {
        WebSettings settings = mWebview.getSettings();
        //   String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        //   settings.setAppCachePath(appCacheDir);

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
        //  settings.setLoadWithOverviewMode(true);    // 是否使用预览模式加载界面
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
        // mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebview.requestFocus();
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        /**
         * MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
         * MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
         * MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
         **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebview.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String type = getIntent().getStringExtra(ConstantValue.WEBVIEW_TYPE);

        String ua = settings.getUserAgentString().replace("Android", "app_android");
        settings.setUserAgentString(ua + "; is_native=true");
        mWebview.addJavascriptInterface(new InJavaScriptCommon(), "gamebox");


        CookieManager.getInstance().setAcceptCookie(true);


        mWebview.setDownloadListener(new FileDownLoadListener());
        mWebview.setOnTouchListener(new MyWebviewOnTouchListener());
        mWebview.setWebViewClient(new CommonWebViewClient());
        mWebview.setWebChromeClient(new CommonWebChromeClient());

        if (ConstantValue.WEBVIEW_TYPE_GAME.equals(type)) {
            // 隐藏title
            mHeadView.setVisibility(View.GONE);
        } else if (ConstantValue.WEBVIEW_TYPE_GAME_WITH_HEAD_VIEW.equals(type)) {
            mHeadView.setVisibility(View.VISIBLE);

        } else if (ConstantValue.WEBVIEW_TYPE_ORDINARY.equals(type)) {
            mHeadView.setHeader("", true, false);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mHomeIv.getId()) {
            ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
            RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_HOME, "gotoHome");
        } else if (v.getId() == mBackIv.getId()) {
            ActivityStackManager.getInstance().finishActivity(this);
        }
    }

    private class CommonWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            setProgressBar(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }

        //  Android 2.2 (API level 8)到Android 2.3 (API level 10)版本选择文件时会触发该隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // Android 3.0 (API level 11)到 Android 4.0 (API level 15))版本选择文件时会触发，该方法为隐藏方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // Android 4.1 (API level 16) -- Android 4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileInput(uploadMsg, null, false);
        }

        // Android 5.0 (API level 21)以上版本会触发该方法，该方法为公开方法
        @SuppressWarnings("all")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选
                openFileInput(null, filePathCallback, allowMultiple);
                return true;
            } else {
                return false;
            }
        }

    }

    @SuppressLint("NewApi")
    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
        //Android 5.0以下版本
        if (mFileUploadCallbackFirst != null) {
            mFileUploadCallbackFirst.onReceiveValue(null);
        }
        mFileUploadCallbackFirst = fileUploadCallbackFirst;

        //Android 5.0及以上版本
        if (mFileUploadCallbackSecond != null) {
            mFileUploadCallbackSecond.onReceiveValue(null);
        }
        mFileUploadCallbackSecond = fileUploadCallbackSecond;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= 18) {
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }

        i.setType(mUploadableFileTypes);

        startActivityForResult(Intent.createChooser(i, "选择文件"), REQUEST_CODE_FILE_PICKER);
    }

    private class CommonWebViewClient extends WebViewClient {

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
        }

//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }

//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
//            LogUtils.e(error.getUrl() + "");
//        }


        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null) return false;
            if (url.contains("login/commonLogin.html")) {
                ActivityUtil.gotoLogin();
                return false;
            }

            return shouldfileterUrl(url);
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


    private class MyWebviewOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mWebview.requestFocus();
            return false;
        }
    }


    private class FileDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            startBrowsers(url);
        }
    }

    private class OnLeftBackClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mWebview.canGoBack()) {
                mWebview.goBack();
            } else {
                finish();
            }
        }
    }


    /**
     * 返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            // 返回键退回
            mWebview.goBack();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    private void setTitle(String title) {
        if (title != null) {
            mHeadView.setHeader(title);
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


    /**
     * 加载header
     */
    public void loadURLWithHTTPHeaders(Context context, String url) {
        WebView webView = new WebView(context);
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Referer", "http://www.google.com");
        webView.loadUrl(url, extraHeaders);
    }


    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    //Android 5.0以下版本
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
                        mFileUploadCallbackFirst = null;
                    } else if (mFileUploadCallbackSecond != null) {//Android 5.0及以上版本
                        Uri[] dataUris = null;

                        try {
                            if (intent.getDataString() != null) {
                                dataUris = new Uri[]{Uri.parse(intent.getDataString())};
                            } else {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    if (intent.getClipData() != null) {
                                        final int numSelectedFiles = intent.getClipData().getItemCount();

                                        dataUris = new Uri[numSelectedFiles];

                                        for (int i = 0; i < numSelectedFiles; i++) {
                                            dataUris[i] = intent.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            } else {
                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst.onReceiveValue(null);
                    mFileUploadCallbackFirst = null;
                } else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }
        }
    }

    private final class InJavaScriptCommon {
        // 回到主页
        @JavascriptInterface
        public void gotoHome(final String url) {
            mHandler.post(() -> {
                Intent intent = new Intent(instance, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }

        // 保存图片（二维码）
        @JavascriptInterface
        public void saveImage(final String url) {
            mHandler.post(() -> {

                if (TextUtils.isEmpty(url)) {
                    ToastUtil.showToastShort(WebViewActivity.this, getString(R.string.imageUrlNil));
                    return;
                }
                try {
                    String domain = DataCenter.getInstance().getDomain();
                    if (url.contains("http")) {
                        mImgUrl = url;
                    } else {
                        mImgUrl = url.contains(domain) ? url : domain + "/" + url;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 检查该权限是否已经获取
                        int hasAuth = ContextCompat.checkSelfPermission(WebViewActivity.this, permissions[0]);
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                        if (hasAuth != PackageManager.PERMISSION_GRANTED) {
                            // 提交请求权限
                            ActivityCompat.requestPermissions(WebViewActivity.this, permissions, 321);
                        } else {
                            handleImage();
                        }
                    } else {
                        handleImage();
                    }
                } catch (Exception e) {
                    ToastUtil.showToastShort(WebViewActivity.this, getString(R.string.saveImgFailed));
                }
            });
        }

        // 注册后登录
        @JavascriptInterface
        public void gotoLogin(final String _href) {
            mHandler.post(() -> {
                try {
                    if (TextUtils.isEmpty(_href)) {
                        ToastUtil.showToastShort(instance, getString(R.string.url_is_null));
                    } else {
                        String[] params = _href.split(",");
                        DataCenter.getInstance().getUser().username = params[0];
                        DataCenter.getInstance().getUser().password = params[1];

                        SPTool.put(WebViewActivity.this, ConstantValue.KEY_USERNAME, DataCenter.getInstance().getUser().username);
                        SPTool.put(WebViewActivity.this, ConstantValue.KEY_PASSWORD, DataCenter.getInstance().getUser().password);
                        setResult(ConstantValue.KEY_REGIST_BACK_LOGIN);
                        WebViewActivity.this.finish();
                    }
                    // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url

                } catch (Exception e) {
                    ToastUtil.showToastShort(instance, getString(R.string.net_busy_error));
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

                    SPTool.put(WebViewActivity.this, ConstantValue.KEY_USERNAME, DataCenter.getInstance().getUser().username);
                    SPTool.put(WebViewActivity.this, ConstantValue.KEY_PASSWORD, DataCenter.getInstance().getUser().password);
                    setResult(ConstantValue.KEY_REGIST_BACK_LOGIN);
                    WebViewActivity.this.finish();

                } catch (Exception e) {
                    // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                    ToastUtil.showToastShort(instance, getString(R.string.net_busy_error));
                }
            });
        }


        @JavascriptInterface
        public void goLogin() {
            mHandler.post(() -> {
                DataCenter.getInstance().setLogin(false);
                Intent intent = new Intent(instance, LoginActivity.class);
                //  intent.putExtra(Constants.RESULT_FLAG, resultFlag);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });

        }


//        @JavascriptInterface
//        public void finish() {
//            WebViewActivity.this.finish();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    WebViewActivity.this.finish();
//                }
//            });
//        }

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
            if (mWebview != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebview.reload();
                    }
                });

            }
        }


        /**
         * 如果有上一级页面，就返回上一级
         * 如果没有上一级页面，就不返回，而是提示用户
         */
        @JavascriptInterface
        public void goBackPage() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mWebview == null) {
                        return;
                    }
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        WebViewActivity.this.finish();
                        //  ToastUtil.showResShort(instance, R.string.is_end_page);
                    }
                }
            });
        }

        /**
         * 跳入存款页面
         */
        @JavascriptInterface
        public void gotoDepositPage() {
            if (mWebview == null) {
                return;
            }
            ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
            RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_DEPOSIT, "gotodeposit");
        }

        /**
         * 跳到资金记录页面
         */
        @JavascriptInterface
        public void gotoCapitalRecordPage() {
            startActivity(new Intent(instance, CapitalRecord.class));
        }

        /**
         * 跳到优惠记录页面
         */
        @JavascriptInterface
        public void gotoPromoRecordPage() {
            startActivity(new Intent(instance, PromoRecordActivity.class));
        }

        /**
         * 去登录
         */
        @JavascriptInterface
        public void gotoLoginPage() {
            startActivity(new Intent(instance, LoginActivity.class));
        }

        /**
         * 去首页
         */
        @JavascriptInterface
        public void gotoHomePage() {
            Intent intent = new Intent(instance, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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

                    SPTool.put(WebViewActivity.this, ConstantValue.KEY_USERNAME, DataCenter.getInstance().getUser().username);
                    SPTool.put(WebViewActivity.this, ConstantValue.KEY_PASSWORD, DataCenter.getInstance().getUser().password);
                    setResult(ConstantValue.KEY_REGIST_BACK_LOGIN);
                    WebViewActivity.this.finish();

                } catch (Exception e) {
                    // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url
                    ToastUtil.showToastShort(instance, getString(R.string.net_busy_error));
                }
            });
        }

    }

    private void handleImage() {
        final String[] files = setImage();
        OkHttpUtils.get().url(mImgUrl).build().execute(new FileCallBack(files[0], files[1]) {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("saveImage error ==> " + e.getMessage());
                ToastUtil.showToastShort(WebViewActivity.this, getString(R.string.saveImgFailed));
            }

            @Override
            public void onResponse(File response, int id) {
                ToastUtil.showToastShort(WebViewActivity.this, getString(R.string.saveImgSuccess));
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
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "resultCode ==> " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_2_BETACTIVITY) {   //跳转至登录的返回
            code = resultCode;
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleImage();
            }
        }
    }


    boolean shouldfileterUrl(String url) {
        if (url == null) return false;
        try {
            if (url.startsWith(SchemeEnum.INTENT.getCode()) || url.startsWith(SchemeEnum.QQ.getCode())
                    || url.startsWith(SchemeEnum.ALIPAY.getCode()) || url.startsWith(SchemeEnum.WECHAT.getCode())) {

                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                intent.setSelector(null);
                List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);
                if (resolves.size() > 0) {
                    startActivityIfNeeded(intent, -1);
                }
            } else if (url.startsWith("https://payh5.bbnpay.com/") || isZXPay(url) || isGaotongPay(url) || isAimiSenPay(url)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                mWebview.loadUrl(url);
            }
        } catch (Exception e) {
            LogUtils.e("跳转支付页面异常 ==> " + e.getMessage());
        }

        return true;
    }

    boolean isZXPay(String url) {
        // String code = getString(R.string.app_code);
        if (url.startsWith("https://zhongxin.junka.com/")) {
            return true;
        }
        return false;
    }

    boolean isGaotongPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("http://wgtj.gaotongpay.com/")) {
            return true;
        }
        return false;
    }

    boolean isAimiSenPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("https://www.joinpay.com")) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        if (mWebview != null) {
            RxBus.get().unregister(this);
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
//
//        Process.killProcess(android.os.Process.myPid());
    }
}
