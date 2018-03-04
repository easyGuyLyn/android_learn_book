package com.dawoo.lotterybox.view.activity.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.lotterybox.BoxApplication;
import com.dawoo.lotterybox.ConstantValue;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.bean.SchemeEnum;
import com.dawoo.lotterybox.mvp.view.IWebView;
import com.dawoo.lotterybox.util.FileTool;
import com.dawoo.lotterybox.view.activity.BaseActivity;
import com.dawoo.lotterybox.view.activity.LoginActivity;
import com.dawoo.lotterybox.view.activity.MainActivity;
import com.dawoo.lotterybox.view.view.WebHeaderView;
import com.hwangjr.rxbus.RxBus;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;


public class WebViewActivity extends BaseActivity implements IWebView {

    @BindView(R.id.head_view)
    WebHeaderView mHeadView;
    @BindView(R.id.webview_fl)
    FrameLayout mWebviewFL;
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
    private Handler mHandler;
    private WebViewActivity instance;
    private String imgUrl;

    @Override
    protected void createLayoutView() {
        String type = getIntent().getStringExtra(ConstantValue.WEBVIEW_TYPE);

        if (ConstantValue.WEBVIEW_TYPE_GAME == type) {
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
        mWebview.loadUrl(url);
    }

    private void createWebView() {
        mWebview = new WebView(BoxApplication.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebview.setLayoutParams(layoutParams);
        mWebviewFL.addView(mWebview);
    }

    @Override
    public void initWebSetting() {
        WebSettings settings = mWebview.getSettings();
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
        mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebview.requestFocus();
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        /**
         * MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
         * MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
         * MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
         **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String type = getIntent().getStringExtra(ConstantValue.WEBVIEW_TYPE);

        String ua = settings.getUserAgentString().replace("Android", "app_android");
        settings.setUserAgentString(ua + "; is_native=true");
        mWebview.addJavascriptInterface(new InJavaScriptCommon(), "gamebox");


        settings.setUserAgentString(ua);
        CookieManager.getInstance().setAcceptCookie(true);


        mWebview.setDownloadListener(new FileDownLoadListener());
        mWebview.setOnTouchListener(new MyWebviewOnTouchListener());
        mWebview.setWebViewClient(new CommonWebViewClient());
        mWebview.setWebChromeClient(new CommonWebChromeClient());

        if (ConstantValue.WEBVIEW_TYPE_GAME.equals(type)) {
            // 隐藏title
            mHeadView.setVisibility(View.GONE);
        } else if (ConstantValue.WEBVIEW_TYPE_ORDINARY.equals(type)) {
            mHeadView.setHeader("", true, false);
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
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
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
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // webView.goBack() 重设标题
            setTitle(String.valueOf(view.getTitle()));
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            LogUtils.e(error.getUrl() + "");
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null) return false;

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
                    } else {
                        ToastUtil.showToastShort(WebViewActivity.this, getString(R.string.imageUrlNil));
                    }
                } catch (Exception e) {
                    ToastUtil.showToastShort(WebViewActivity.this, getString(R.string.saveImgFailed));
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
            } else {
                mWebview.loadUrl(url);
            }
        } catch (Exception e) {
            LogUtils.e("跳转支付页面异常 ==> " + e.getMessage());
        }

        return true;
    }



    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            RxBus.get().unregister(this);
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
}
