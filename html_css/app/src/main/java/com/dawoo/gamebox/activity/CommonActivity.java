package com.dawoo.gamebox.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.gamebox.MainActivity;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.been.BalanceBean;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.constant.URLConst;
import com.dawoo.gamebox.tool.ActivityManager;
import com.dawoo.gamebox.tool.CommonWVClient;
import com.dawoo.gamebox.tool.DateTool;
import com.dawoo.gamebox.tool.FileTool;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.google.gson.Gson;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.autolayout.utils.AutoUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class CommonActivity extends BaseActivity {
    private static final String TAG = CommonActivity.class.getSimpleName();

    // region 组件及变量定义
    private static final int TIME = 3;

    @BindView(R.id.wvCommon)
    WebView wvCommon;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    //顶部菜单
    @BindView(R.id.llLogin)
    LinearLayout llLogin;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvMoney)
    TextView tvMoney;

    @BindView(R.id.ibtnBack)
    ImageView ibtnBack;
    @BindView(R.id.ibtnBack2)
    ImageView ibtnBack2;
    @BindView(R.id.tvBack)
    TextView tvBack;

    //右边资金菜单
    @BindView(R.id.llAssets)
    LinearLayout llAssets;
    @BindView(R.id.tvAssets)
    TextView tvAssets;
    @BindView(R.id.tvWallet)
    TextView tvWallet;
    @BindView(R.id.rvAssets)
    RecyclerView rvAssets;
    // 刷新API余额
    @BindView(R.id.btnRefreshAssets)
    Button btnRefreshAssets;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;

    String url;
    String domain;
    MyHandler handler;
    BalanceBean balanceBean;
    CommonActivity instance;
    //刷新余额计时器
    private int time = TIME + 1;
    Timer timer;

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgUrl;
    private  int code = 0;
    // endregion
    private Integer resultFlag;
    @Override
    public int initView() {
        return R.layout.activity_common;
    }

    @Override
    public void init() {
        instance = this;
        domain = MyApplication.domain;
        CookieManager.getInstance().setAcceptCookie(true);
        handler = new MyHandler(this);
        url = getIntent().getStringExtra("url");
        url = (url.contains("http") || url.contains("file:///")) ? url : domain + "/" + url;
        Log.e(TAG, "common url = " + url);
        // 如果是包含/app/download.html跳转到浏览器browser
        gotoBrowser(url);

        // 是否显示API余额入口
        openAssets(url);
        // 加载页面
        loadWeb();
        Intent intent = getIntent();
        resultFlag = intent.getIntExtra(Constants.RESULT_FLAG, Constants.RESULT_2_HOME);

        rvAssets.setLayoutManager(new LinearLayoutManager(this));

        changeTitleBack(ibtnBack, ibtnBack2, tvBack);
        //AndroidBug5497Workaround.assistActivity(this);
    }

    private static class MyHandler extends Handler {
        WeakReference<CommonActivity> mActivity;

        MyHandler(CommonActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


    void gotoBrowser(String url) {
        if(url == null) {
            return;
        }

        if(url.contains("/app/download.html")) {
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }
    }


    /**
     * 开放余额查询入口
     */
    private void openAssets(String url) {
        // 资金纪录
        final String fund = "fund/record/index.";
        // 存款
        final String deposit = "wallet/deposit/index.";
        boolean doesShow = (url.contains(fund) || url.contains(deposit)) && !ParamTool.isLotterySite(this);

        if (doesShow) {
            tvUsername.setText((String) SPTool.get(this, Constants.KEY_USERNAME, ""));
            fetchAssets();
        } else {
            llLogin.setVisibility(View.GONE);
        }
    }

    /**
     * 加载Web资源
     */
    private void loadWeb() {
        this.webSetting(wvCommon);
        wvCommon.addJavascriptInterface(new InJavaScriptLocalObj(), Constants.JS_OBJECT);
        wvCommon.setWebViewClient(new MyWebViewClient());
        wvCommon.setWebChromeClient(new MyWebChromeClient());
        if (url.contains("/e404.")) {
            url = Const.PAGE_404;
        }
        if (url.contains(URLConstants.DEPOSIT_URL) && MyApplication.isDemo) {
            url = Const.PAGE_403;
        }
        wvCommon.loadUrl(url);
    }

    @OnClick(R.id.llLogin)
    public void openAssets() {
        if (ParamTool.isLotterySite(this) || llAssets.getVisibility() == View.VISIBLE) {
            setAnimation(llAssets, 0, 1, 0, 0, 300);
            llAssets.setVisibility(View.GONE);
        } else {
            setAnimation(llAssets, 1, 0, 0, 0, 300);
            llAssets.setVisibility(View.VISIBLE);
            tvAssets.setText(balanceBean.getPlayerAssets());
            tvWallet.setText(balanceBean.getPlayerWallet());

            rvAssets.setAdapter(new CommonAdapter<BalanceBean.ApisBean>(instance, R.layout.api_balance_item, balanceBean.getApis()) {
                @Override
                protected void convert(ViewHolder holder, BalanceBean.ApisBean apisBean, int position) {
                    holder.setText(R.id.main_moneyinfo_name, apisBean.getApiName());
                    holder.setText(R.id.main_moneyinfo_money, apisBean.getBalance() + "");
                }

                @Override
                public void onViewHolderCreated(ViewHolder holder, View itemView) {
                    super.onViewHolderCreated(holder, itemView);
                    AutoUtils.autoSize(itemView);
                }
            });
        }
    }

    @OnClick(R.id.btnRefreshAssets)
    public void refreshAssets() {
        if (time == TIME + 1) {
            time--;
            timer = new Timer();
            timer.schedule(new AssetsTimerTask(), 1000, 1000);//计时
            btnRefreshAssets.setBackgroundColor(getResources().getColor(R.color.disBtnAssets));
            showProgress(getResources().getString(R.string.refreshing), true);
            //刷新余额
            OkHttpUtils.post().url(domain + URLConstants.REFRESH_BALACE_URL).headers(setHeaders())
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e(TAG, "refreshAssets error ==> " + e.getLocalizedMessage());
                    dismissProgress();
                    ToastTool.show(CommonActivity.this, R.string.refreshFailed);
                }

                @Override
                public void onResponse(String response, int id) {
                    dismissProgress();
                    fillMoney(response);
                }
            });
        }
    }

    class AssetsTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    time--;
                    btnRefreshAssets.setText(String.format(getResources().getString(R.string.refreshBalance), time));
                    if (time < 0) {
                        btnRefreshAssets.setBackgroundColor(getResources().getColor(R.color.btnAssets));
                        timer.cancel();
                        btnRefreshAssets.setText(getResources().getString(R.string.assetRefresh));
                        time = TIME + 1;
                    }
                }
            });
        }
    }

    private void fillMoney(String response) {
        balanceBean = new Gson().fromJson(response, BalanceBean.class);
        tvMoney.setText(String.format("%s%s", balanceBean.getCurrSign(), balanceBean.getPlayerAssets()));
    }

    @OnClick(R.id.btnDeposit)
    public void toDeposit() {
        setResult(Constants.RESULT_2_DEPOSIT);
        finish();
    }

    /**
     * 查询API余额
     */
    private void fetchAssets() {
        boolean isLotterySite = (boolean) SPTool.get(context, Constants.KEY_LOTTERY_SITE, false);
        llLogin.setVisibility(isLotterySite ? View.GONE : View.VISIBLE);
        domain = domain == null ? MyApplication.domain : domain;
        // 获取api余额
        OkHttpUtils.post().url(domain + URLConstants.GET_API_BALACE_URL).headers(setHeaders()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "fetchAssets error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                fillMoney(response);
            }
        });
    }

    @OnClick(R.id.rlBank)
    void goBack() {
        setResult(code);
        finish();
    }

    private final class MyWebViewClient extends CommonWVClient {
        private boolean mDone = false;
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
            showProgress(R.string.loading_tip, true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            tvTitle.setText(wvCommon.getTitle());
            dismissProgress();
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
            if (tvTitle.getText().length() == 0)
                tvTitle.setText(title);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && wvCommon.canGoBack()) {
            wvCommon.goBack();
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

        // 进入游戏
        @JavascriptInterface
        public void gotoGame(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        Intent intent = new Intent(instance, GameActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        }

        // 进入第三方支付
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

        @JavascriptInterface
        public void gotoActivity(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "---> gotoActivity(" + url + ")");

                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        wvCommon.loadUrl(url.contains(domain) ? url : domain + url);
                        openAssets(url);
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
                    } else if("1".equals(target)) {
                        setResult(Constants.RESULT_2_DEPOSIT);
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

        // 再次存款
        @JavascriptInterface
        public void depositAgain() {
            List<Activity> activities = ActivityManager.GetActivityList();
            for (Activity activity : activities) {
                if (!(activity instanceof MainActivity)) {
                    activity.finish();
                }
            }
        }

        // 保存图片（二维码）
        @JavascriptInterface
        public void saveImage(final String href) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!StringUtils.isEmpty(href)) {
                            imgUrl = href.contains(domain) ? href : domain + "/" + href;
                            Log.i(TAG, "image url ==> " + imgUrl);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // 检查该权限是否已经获取
                                int hasAuth = ContextCompat.checkSelfPermission(CommonActivity.this, permissions[0]);
                                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                                if (hasAuth != PackageManager.PERMISSION_GRANTED) {
                                    // 提交请求权限
                                    ActivityCompat.requestPermissions(CommonActivity.this, permissions, 321);
                                } else {
                                    handleImage();
                                }
                            } else {
                                handleImage();
                            }
                        } else {
                            ToastTool.show(CommonActivity.this, R.string.imageUrlNil);
                        }
                    } catch (Exception e) {
                        ToastTool.show(CommonActivity.this, R.string.saveImgFailed);
                        Log.e(TAG, "saveImage ==> " + e.getMessage());
                    }
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
                            //ToastTool.show(instance, R.string.url_is_null);
                            if (_href.isEmpty()) {
                                String[] params = _href.split(",");
                                SPTool.put(instance, Constants.KEY_USERNAME, params[0]);
                                SPTool.put(instance, Constants.KEY_PASSWORD, params[1]);
                                if(ParamTool.isLotterySite(context)) {
                                    toLoginActivity(3);
                                } else {
                                    toLoginActivity(0);
                                }

                            }
                        } else {
                            String[] params = _href.split(",");
                            MyApplication.username = params[0];
                            MyApplication.password = params[1];
                            setResult(Constants.REGISTER_SUCCESS);
                            finish();
                        }
                        // 如果登录操作是因为点击其他url触发，则登陆后跳转到该url

                    } catch (Exception e) {
                        if(ParamTool.isLotterySite(context)) {
                            toLoginActivity(3);
                        } else {
                            toLoginActivity(0);
                        }
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
            CommonActivity.this.finish();
        }
    }

    private void handleImage() {
        final String[] files = setImage();
        OkHttpUtils.get().url(imgUrl).build().execute(new FileCallBack(files[0], files[1]) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "saveImage error ==> " + e.getMessage());
                ToastTool.show(CommonActivity.this, R.string.saveImgFailed);
            }

            @Override
            public void onResponse(File response, int id) {
                ToastTool.show(CommonActivity.this, R.string.saveImgSuccess);
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
        instance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "resultCode ==> " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_2_BETACTIVITY) {   //跳转至登录的返回
            code = resultCode;
        } else if(0 == requestCode || 1 == requestCode) {

            if (MyApplication.isLogin == true) {
                if (data != null && "ok".equals(data.getStringExtra("loginSuccess")) && resultCode <= Constants.RESULT_LOGIN_FAIL) {
                    Intent intent = new Intent();
                    intent.putExtra("loginSuccess","ok");
                    setResult(Constants.RESULT_2_HOME, intent);
                    finish();
                }
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvCommon.stopLoading();
        wvCommon.removeAllViews();
        wvCommon.destroy();
        wvCommon = null;
    }


}
