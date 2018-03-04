package com.dawoo.gamebox.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.dawoo.gamebox.MainActivity;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.constant.URLConst;
import com.dawoo.gamebox.tool.ActivityManager;
import com.dawoo.gamebox.tool.CommonWVClient;
import com.dawoo.gamebox.tool.ToastTool;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApiActivity extends BaseActivity {
    private static final String TAG = ApiActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.wvApi)
    WebView wvApi;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String url;
    String domain;
    MyHandler handler;
    ApiActivity instance = ApiActivity.this;

    @Override
    public int initView() {
        return R.layout.activity_api;
    }

    @Override
    public void init() {
        domain = MyApplication.domain;
        handler = new MyHandler(this);
        url = getIntent().getStringExtra("url");
        // 载入Web资源
        loadWeb();
    }

    private static class MyHandler extends Handler {
        WeakReference<ApiActivity> mActivity;

        MyHandler(ApiActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 载入Web资源
     */
    private void loadWeb() {
        this.webSetting(wvApi);
        wvApi.addJavascriptInterface(new InJavaScriptLocalObj(), Constants.JS_OBJECT);
        wvApi.setWebViewClient(new MyWebViewClient());
        wvApi.setWebChromeClient(new MyWebChromeClient());
        wvApi.loadUrl(url);
    }

    @Override
    public void changeStatusColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }
    }

    @OnClick(R.id.ibtnBack)
    void goBack(View view) {
        finish();
    }

    private final class MyWebViewClient extends CommonWVClient {
        private String mLoadingUrl = "";

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
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    private final class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == progressBar.getVisibility()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(progress);
            }
            super.onProgressChanged(view, progress);
        }
    }

    /**
     * 返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return false;
    }

    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void toast(String msg) {
            ToastTool.showToast(ApiActivity.this, msg);
        }

        @JavascriptInterface
        public void gotoGame(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(ApiActivity.this, R.string.gameUrlError);
                    } else {
                        Intent intent = new Intent(ApiActivity.this, GameActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        }

        @JavascriptInterface
        public void gotoActivity(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "---> gotoActivity(" + url + ")");

                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(ApiActivity.this, R.string.url_is_null);
                    } else {
                        String newUrl = url.contains("http") ? url : domain + url;
                        Intent intent = new Intent(ApiActivity.this, CommonActivity.class);
                        intent.putExtra("url", newUrl);
                        startActivity(intent);
                    }
                }
            });
        }

        @JavascriptInterface
        public void gotoApi(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "---> gotoApi(" + url + ")");

                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(ApiActivity.this, R.string.url_is_null);
                    } else {
                        Log.e(TAG, "---> gotoApi(" + url + ")");
                        String newUrl = url.contains("http") ? url : domain + url;
                        wvApi.clearHistory();
                        wvApi.loadUrl(newUrl);
                    }
                }
            });
        }

        // 回到主页
        @JavascriptInterface
        public void gotoHome(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    List<Activity> list = ActivityManager.GetActivityList();
                    for (Activity activity : list) {
                        activity.finish();
                    }
                    String href = url.contains("http") ? url : MyApplication.domain + url;
                    Intent intent = new Intent(ApiActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("href", href);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void backRefresh() {
            Log.e(TAG, "-->backRefresh");
            setResult(Constants.REGISTER_REFRESH);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvApi.stopLoading();
        wvApi.removeAllViews();
        wvApi.destroy();
        wvApi = null;
    }
}
