package com.dawoo.gamebox.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dawoo.gamebox.MainActivity;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.constant.URLConst;
import com.dawoo.gamebox.tool.ActivityManager;
import com.dawoo.gamebox.tool.CommonWVClient;
import com.dawoo.gamebox.tool.ToastTool;
import com.dawoo.gamebox.view.MenuPopup;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 彩票站-投注Activity
 *
 * @author fei
 */
public class BetActivity extends BaseActivity {
    private static final String TAG = BetActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.wvBet)
    WebView wvBet;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String url;
    String domain;
    MyHandler handler;
    BetActivity instance;
    MenuPopup popup;
    // endregion
    private int code = 0;

    private Integer resultFlag;
    private String assets = "余额：0.00";

    @Override
    public int initView() {
        return R.layout.activity_bet;
    }

    @Override
    public void init() {
        instance = this;
        domain = MyApplication.domain;
        Intent intent = getIntent();
        resultFlag = intent.getIntExtra(Constants.RESULT_FLAG, Constants.RESULT_2_HOME);
        CookieManager.getInstance().setAcceptCookie(true);
        handler = new MyHandler(this);
        url = getIntent().getStringExtra("url");
        url = (url.contains("http") || url.contains("file:///")) ? url : domain + "/" + url;
        Log.e(TAG, "common url = " + url);
        popup = new MenuPopup(this);
        popup.setPopupItemClick(new MenuPopup.PopupItemClick() {
            @Override
            public void onPopupItemClick(int index) {
                switch (index) {
                    case 0:
                        if (!MyApplication.isLogin) {
                            toLoginActivity(Constants.REQUEST_2_BETACTIVITY, resultFlag);
                        }
                        break;
                    case 1:
                        if (!MyApplication.isLogin) {
                            toLoginActivity(Constants.BETACTIVITY_2_BET, Constants.BETACTIVITY_2_BET);
                        } else {
                            setResult(Constants.BETACTIVITY_2_BET);
                            finish();
                        }
                        break;
                    case 2:
                        Intent intent = new Intent(instance, CommonActivity.class);
                        intent.putExtra("url", domain + URLConstants.LOTTERY_URL);
                        startActivityForResult(intent, 0);
                        break;
                    case 3:
                        if (!MyApplication.isLogin) {
                            toLoginActivity(Constants.BETACTIVITY_2_MINE, Constants.BETACTIVITY_2_MINE);
                        } else {
                            setResult(Constants.BETACTIVITY_2_MINE);
                            finish();
                        }
                        break;
                    case 4:
                        if (!MyApplication.isLogin) {
                            toLoginActivity(Constants.BETACTIVITY_2_MESSAGE, Constants.BETACTIVITY_2_MESSAGE);
                        } else {
                            Intent intentMessage = new Intent(instance, CommonActivity.class);
                            intentMessage.putExtra("url", domain + URLConstants.MESSAGE_CENTER);
                            startActivityForResult(intentMessage, 0);
                        }
                        break;
                    case 5:
                        if (!MyApplication.isLogin) {
                            toLoginActivity(Constants.BETACTIVITY_2_DEPOSIT, Constants.BETACTIVITY_2_DEPOSIT);
                        } else {
                            setResult(Constants.BETACTIVITY_2_DEPOSIT);
                            finish();
                        }
                        break;
                }
            }
        });
        // 加载页面
        loadWeb();

        if (MyApplication.isLogin) {
            getAsset();
        } else {
            assets = "请登录";
        }
    }

    protected void getAsset() {
        OkHttpUtils.post().url(domain + URLConstants.ASSETS_URL).headers(setHeaders())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "getAsset error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                assets = String.format("余额：%s", response);
            }
        });
    }

    private static class MyHandler extends Handler {
        WeakReference<BetActivity> mActivity;

        MyHandler(BetActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 加载Web资源
     */
    private void loadWeb() {
        this.webSetting(wvBet);
        wvBet.addJavascriptInterface(new InJavaScriptLocalObj(), Constants.JS_OBJECT);
        wvBet.setWebViewClient(new MyWebViewClient());
        wvBet.setWebChromeClient(new MyWebChromeClient());
        if (url.contains("/e404.")) {
            url = Const.PAGE_404;
        }
        if (url.contains(URLConstants.DEPOSIT_URL) && MyApplication.isDemo) {
            url = Const.PAGE_403;
        }
        wvBet.loadUrl(url);
    }

    @OnClick(R.id.rlBank)
    void goBack() {
        setResult(code);
        finish();
    }

    @OnClick(R.id.tvTitle)
    void changePlay() {
        wvBet.loadUrl("javascript:window.page.changePlay();");
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    wvBet.loadUrl("javascript:window.gamebox.setTitle(document.getElementById('toobarTitle').innerHTML);");
                }
            }, 500);
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
//            if (tvTitle.getText().length() == 0)
//                tvTitle.setText(title);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && wvBet.canGoBack()) {
            wvBet.goBack();
        } else {
            setResult(code);
            finish();
        }
        return false;
    }

    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void toast(String msg) {
            ToastTool.showToast(instance, msg);
        }

        @JavascriptInterface
        public void gotoActivity(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "---> gotoActivity(" + url + ")");

                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        wvBet.loadUrl(url.contains(domain) ? url : domain + url);
                    }
                }
            });
        }

        @JavascriptInterface
        public void gotoFragment(final String target) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(target)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else if ("4".equals(target)) {
                        setResult(Constants.RESULT_2_MINE);
                        finish();
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
                    Intent intent = new Intent(instance, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("href", href);
                    startActivity(intent);
                }
            });
        }

        // 注册后登录
        @JavascriptInterface
        public void gotoLogin(final String _href) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG, "CommonActivity gotoLogin:" + _href);
                        if (TextUtils.isEmpty(_href)) {
                            ToastTool.show(instance, R.string.url_is_null);
                        } else {
                            String[] params = _href.split(",");
                            MyApplication.username = params[0];
                            MyApplication.password = params[1];
                            setResult(Constants.REGISTER_SUCCESS);
                            finish();
                        }
                        // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url

                    } catch (Exception e) {
                        ToastTool.show(instance, R.string.net_busy_error);
                    }
                }
            });
        }

        @JavascriptInterface
        public void goLogin() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyApplication.isLogin = false;
                    toLoginActivity(Constants.REQUEST_2_BETACTIVITY, resultFlag);
                }
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

        @JavascriptInterface
        public void finish() {
            BetActivity.this.finish();
        }

        @JavascriptInterface
        public void setTitle(final String title) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!StringUtils.isEmpty(title)) {
                        tvTitle.setText(title.trim());
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "resultCode ==> " + resultCode + "    requestCode ==> " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_2_BETACTIVITY) {   //跳转至登录的返回
            code = resultCode;
            getAsset();
        } else if(resultCode == Constants.BETACTIVITY_2_MESSAGE){
            Intent intentMessage = new Intent(instance, CommonActivity.class);
            intentMessage.putExtra("url", domain + URLConstants.MESSAGE_CENTER);
            startActivityForResult(intentMessage, 0);
        }else if (resultCode != 0) {//充值提现/投注记录/会员中心   先跳转至登录页面，登录成功后返回本页面再回退之所需页面
            setResult(resultCode);
            finish();
        }
    }

    @OnClick(R.id.ivMenu)
    public void showMenu(View v) {
        popup.show(v, assets);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvBet.stopLoading();
        wvBet.removeAllViews();
        wvBet.destroy();
        wvBet = null;
    }
}
