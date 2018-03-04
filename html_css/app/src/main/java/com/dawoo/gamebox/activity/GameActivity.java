package com.dawoo.gamebox.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.enums.ApiEnum;
import com.dawoo.gamebox.tool.CommonWVClient;
import com.dawoo.gamebox.tool.ProperTool;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.laocaixw.layout.SuspendButtonLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class GameActivity extends BaseActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.wvGame)
    WebView wvGame;

    private String apiId;
    Handler handler;
    // endregion

    @Override
    public int initView() {
        return R.layout.activity_game;
    }

    @Override
    public void init() {

        handler = new Handler();
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptFileSchemeCookies(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(wvGame, true);
        }
        //设置全屏
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String url = getIntent().getStringExtra("url");
        if (url.contains("ad=")) {
            apiId = url.substring(url.lastIndexOf("ad=") + 3, url.length());
        }
        // 载入Web资源
        loadWeb(url);

        initIcon();

        Log.e(TAG, url);
    }

    /**
     * 载入Web资源
     */
    private void loadWeb(final String url) {
        this.webSetting(wvGame);
        wvGame.setWebViewClient(new MyWebViewClient());
        wvGame.addJavascriptInterface(new InJavaScriptLocalObj(), "gb");
        wvGame.setWebChromeClient(new MyWebChromeClient());

        wvGame.loadUrl(url);
    }

    @Override
    protected void webSetting(WebView webView) {
        super.webSetting(webView);
        if (!ParamTool.isLotterySite(context)) {
            webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString().replace("app_android", "Android"));
        }
        if (apiId != null && apiId.equals(ApiEnum.MG.getCode())) {
            webView.getSettings().setUserAgentString(ProperTool.getProperty(this, "ua.ios"));
        }
    }

    private final class MyWebViewClient extends CommonWVClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            wvGame.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgress(R.string.loading_tip, true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissProgress();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            Log.e(TAG, view.getUrl());
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(TAG, description);
        }

        // 6.0 及以上版本调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.e(TAG, error.getDescription().toString());
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.e("CONTENT", String.format("%s @ %d: %s",
                    cm.message(), cm.lineNumber(), cm.sourceId()));
            return true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void finish() {
            finish();
        }

        @JavascriptInterface
        public void log(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(GameActivity.this, R.string.url_is_null);
                    } else {
                        Intent intent = new Intent(GameActivity.this, GameActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvGame.canGoBack()) {
            //  wvGame.goBack();
            //   return true;


            //获取历史列表
            WebBackForwardList mWebBackForwardList = wvGame.copyBackForwardList();
            //判断当前历史列表是否最顶端,其实canGoBack已经判断过
            if (mWebBackForwardList.getCurrentIndex() > 0) {
                //获取历史列表
                String historyUrl = mWebBackForwardList.getItemAtIndex(
                        mWebBackForwardList.getCurrentIndex() - 1).getUrl();
                //按照自己规则检查是否为可跳转地址
                //注意:这里可以根据自己逻辑循环判断,拿到可以跳转的那一个然后webView.goBackOrForward(steps)
                if (!historyUrl.contains("com/lottery/?ad=22") && !historyUrl.contains("m/new/#/home")) {
                    //执行跳转逻辑
                    wvGame.goBack();
                    //webView.goBackOrForward(-1)
                    return true;
                }
            }
        } else {
            this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initIcon() {
        final SuspendButtonLayout suspendButtonLayout = (SuspendButtonLayout) findViewById(R.id.layout);

        if (ParamTool.isLotterySite(context)) {
            suspendButtonLayout.setChildImageResource(3, R.drawable.game_icon_service);
        }
        suspendButtonLayout.setPosition(true, 100);

        suspendButtonLayout.setOnSuspendListener(new SuspendButtonLayout.OnSuspendListener() {
            @Override
            public void onButtonStatusChanged(int status) {
                // 监听按钮状态：展开、关闭、移动等
            }

            @Override
            public void onChildButtonClick(int index) {
                switch (index) {
                    case 1:
                        GameActivity.this.finish();
                        break;
                    case 2:
                        toCommonActivity(URLConstants.DEPOSIT_URL);
                        suspendButtonLayout.closeSuspendButton();
                        break;
                    case 3:
                        if (!ParamTool.isLotterySite(context)) {
                            toCommonActivity(URLConstants.TRANSFER_URL);
                        } else {
                            toCommonActivity(String.valueOf(SPTool.get(context, Constants.KEY_CUSTOMER_SERVICE, Const.PAGE_404)));
                        }
                        suspendButtonLayout.closeSuspendButton();
                        break;
                    case 4:
                        if (wvGame.canGoBack()) wvGame.goBack();
                        else GameActivity.this.finish();
                        break;
                }
            }
        });
    }

    private void toCommonActivity(String url) {
        Intent intent = new Intent(GameActivity.this, CommonActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 回收接口
        autoRecovery();
        wvGame.stopLoading();
        wvGame.removeAllViews();
        wvGame.destroy();
        wvGame = null;
    }


    void autoRecovery() {
        if (apiId == null) {
            return;
        }
        String url = MyApplication.domain + URLConstants.AUTO_RECOVERY;
        OkHttpUtils.post().url(url).addParams("search.apiId", apiId).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                final String jsonData = response.body().string();
                Log.e("回收信息：", jsonData);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }



}
