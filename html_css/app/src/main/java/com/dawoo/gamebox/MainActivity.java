package com.dawoo.gamebox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.gamebox.activity.BetActivity;
import com.dawoo.gamebox.base.BaseMainActivity;
import com.dawoo.gamebox.been.BalanceBean;
import com.dawoo.gamebox.common.AttrConst;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.google.gson.Gson;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.autolayout.utils.AutoUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends BaseMainActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.wvDeposit)
    WebView wvDeposit;
    @BindView(R.id.wvTransfer)
    WebView wvTransfer;
    @BindView(R.id.wvService)
    WebView wvService;
    //存款
    @BindView(R.id.ivDeposit)
    protected ImageView ivDeposit;
    @BindView(R.id.tvDeposit)
    protected TextView tvDeposit;
    //转账
    @BindView(R.id.ivTransfer)
    ImageView ivTransfer;
    @BindView(R.id.tvTransfer)
    TextView tvTransfer;
    //客服
    @BindView(R.id.ivService)
    protected ImageView ivService;
    @BindView(R.id.tvService)
    protected TextView tvService;

    @BindView(R.id.llLogin)
    LinearLayout llLogin;

    //右边资金菜单
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.llAssets)
    LinearLayout llAssets;
    @BindView(R.id.tvAssets)
    TextView tvAssets;
    @BindView(R.id.tvWallet)
    TextView tvWallet;
    @BindView(R.id.rvAssets)
    RecyclerView rvAssets;
    @BindView(R.id.btnRefreshAssets)
    Button btnRefreshAssets;

    private MainActivity instance;
    //刷新余额计时器
    private static final int TIME = 3;
    private int time = TIME + 1;
    Timer timer;
    private BalanceBean balanceBean;
    // endregion

    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        super.init();
        instance = this;
        // 加载web资源
        loadWeb(new InJavaScriptLocal());
        rvAssets.setLayoutManager(new LinearLayoutManager(this));
        wvHome.setTag(true);
        wvDeposit.setTag(true);
        wvTransfer.setTag(true);
        wvMine.setTag(true);
        wvHome.setTag(false);
    }

    /**
     * 加载Web资源
     */
    protected void loadWeb(InJavaScriptLocal javaScript) {
        super.loadWeb(javaScript);

        this.webSetting(wvDeposit);
        wvDeposit.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvDeposit.setWebViewClient(client);


        this.webSetting(wvTransfer);
        wvTransfer.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvTransfer.setWebViewClient(client);

        this.webSetting(wvService);
        wvService.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvService.setWebViewClient(client);
        wvService.setWebChromeClient(new MyWebChromeClient());
        wvService.getSettings().setUseWideViewPort(false);
        wvService.getSettings().setUserAgentString(wvService.getSettings().getUserAgentString().replace("app_android", "Android"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "resultCode ==> " + resultCode);

        if (MyApplication.isLogin == true) {
            if (data != null && "ok".equals(data.getStringExtra("loginSuccess")) && resultCode <= Constants.RESULT_LOGIN_FAIL) {
                loginAfter();
            }
        } else {
            // 刷新toolbar
            logout(true);
        }


        if (resultCode > Constants.RESULT_LOGIN_FAIL) {
            loginAfter();
        } else {
            if (wvHome.getTag() instanceof Boolean && Boolean.valueOf(wvHome.getTag().toString()) == false)
                wvMine.loadUrl("javascript:window.page.getOpenResult();");//如果wvHome已经加载过数据了则不再刷新，只局部刷新
            else
                switchTab("0");
        }

        switch (resultCode) {
            case Constants.RESULT_2_DEPOSIT:
                wvdepositLoadURL();
                switchTabDeposit();
                break;
            case Constants.RESULT_2_TRANSFER:
                wvTransfer.loadUrl(domain + URLConstants.TRANSFER_URL);
                switchTabTransfer();
                break;
            case Constants.REGISTER_REFRESH:
                refreshAssets();
                break;
            case Constants.BETACTIVITY_2_HOME:
                if (wvHome.getTag() instanceof Boolean && Boolean.valueOf(wvHome.getTag().toString()) == false)
                    wvMine.loadUrl("javascript:window.page.getOpenResult();");//如果wvHome已经加载过数据了则不再刷新，只局部刷新
                else
                    switchTab("0");
                break;
            case Constants.BETACTIVITY_2_MINE:
                switchTab("4");
                break;
            case Constants.BETACTIVITY_2_DEPOSIT:
                switchTab("1");
                break;
            case Constants.BETACTIVITY_2_BET:
                switchTab("3");
                break;
            default:
//                switchTab(String.valueOf(currTabIndex));
                break;
        }

        if (requestCode == Constants.FILE_CHOOSER_RESULT_CODE) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            mUploadMsg.onReceiveValue(result);
            mUploadMsg = null;
        }
    }

    private void switchTab(String target) {
        switch (target) {
            case "0":
                switchTabHome();
                break;
            case "1":
                if (MyApplication.isLogin) {
                    switchTabDeposit();
                } else {
                    switchTabHome();
                }
                break;
            case "2":
                if (MyApplication.isLogin) {
                    switchTabTransfer();
                } else {
                    switchTabHome();
                }
                break;
            case "3":
                switchTabService();
                break;
            case "4":
                if (MyApplication.isLogin) {
                    switchTabMine();
                } else {
                    switchTabHome();
                }
                break;
        }
    }

    /**
     * 登录成功后的操作
     */
    @Override
    protected void loginAfter() {
        super.loginAfter();
//        reloadWebView();

        OkHttpUtils.post().url(domain + URLConstants.GET_API_BALACE_URL).headers(setHeaders())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "loginAfter error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                llLogin.setVisibility(View.VISIBLE);
                llLogout.setVisibility(View.GONE);
                balanceBean = new Gson().fromJson(response, BalanceBean.class);
                tvUsername.setText(balanceBean.getUsername());
                tvUsername.setVisibility(View.VISIBLE);
                tvMoney.setText(String.format("%s%s", balanceBean.getCurrSign(), balanceBean.getPlayerAssets()));
            }
        });
    }

    /**
     * 登陆后重新加载WebView
     */
    @Override
    protected void reloadWebView() {
        super.reloadWebView();
        wvTransfer.clearHistory();
        wvDeposit.clearHistory();

        wvTransfer.setTag(true);
        //预加载存款页面
        wvdepositLoadURL();
        wvDeposit.setTag(false);
    }

    // 存款
    @OnClick(R.id.llTabDeposit)
    public void switchTabDeposit() {
        if (!MyApplication.isLogin) {
            toLoginActivity(Constants.RESULT_2_DEPOSIT);
        } else {
            showToolbar();
            if (currTabIndex == 1) {
                wvDeposit.clearHistory();
                wvdepositLoadURL();
            } else {
                initTab(1);
                dynamicAddView(ivDeposit, AttrConst.BACKGROUND, R.drawable.tab_deposit_selected);
                dynamicAddView(tvDeposit, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
                wvDeposit.setVisibility(View.VISIBLE);
                if (wvDeposit.getTag() instanceof Boolean && Boolean.valueOf(wvDeposit.getTag().toString()) == true) {
                    wvDeposit.clearHistory();
                    wvdepositLoadURL();
                    wvDeposit.setTag(false);
                }
            }
        }
    }

    /**
     * 设置wvdepostlod头部参数
     */
    void wvdepositLoadURL() {

        wvDeposit.loadUrl(domain + URLConstants.DEPOSIT_URL);
    }

    // 转账
    @OnClick(R.id.llTabTransfer)
    public void switchTabTransfer() {
        if (!MyApplication.isLogin) {
            toLoginActivity(Constants.RESULT_2_TRANSFER);
        } else {
            showToolbar();
            if (currTabIndex == 2) {
                wvTransfer.clearHistory();
                wvTransfer.loadUrl(domain + URLConstants.TRANSFER_URL);
            } else {
                initTab(2);
                dynamicAddView(ivTransfer, AttrConst.BACKGROUND, R.drawable.tab_transfer_selected);
                dynamicAddView(tvTransfer, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
                wvTransfer.setVisibility(View.VISIBLE);
                if (wvTransfer.getTag() instanceof Boolean && Boolean.valueOf(wvTransfer.getTag().toString()) == true) {
                    wvTransfer.clearHistory();
                    wvTransfer.loadUrl(domain + URLConstants.TRANSFER_URL);
                    wvTransfer.setTag(false);
                }
            }
        }
    }

    // 客服
    @OnClick(R.id.llTabService)
    public void switchTabService() {
        String csUrl = String.valueOf(SPTool.get(context, Constants.KEY_CUSTOMER_SERVICE, Const.PAGE_404));
        // TODO 鉴于195客服链接致使WebView崩溃问题，暂时处理为用浏览器打开195客服
        String code = getString(R.string.app_code);
        if ("xjvs".equals(code) || "ucuy".equals(code) || "nu9r".equals(code) || "5rdu".equals(code) || "3qj8".equals(code) || "57h0".equals(code) || "n6pg".equals(code)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(csUrl));
            startActivity(intent);
        } else {
            showToolbar();
            wvService.loadUrl(csUrl);
            if (currTabIndex == 3) {
                wvService.reload();
            } else {
                initTab(3);
                dynamicAddView(ivService, AttrConst.BACKGROUND, R.drawable.tab_service_selected);
                dynamicAddView(tvService, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
                wvService.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 初始化底部Tab栏
     */
    @Override
    public void initTab(int targetIndex) {
        super.initTab(targetIndex);

        wvDeposit.setVisibility(View.GONE);
        dynamicAddView(ivDeposit, AttrConst.BACKGROUND, R.drawable.tab_deposit_normal);
        dynamicAddView(tvDeposit, AttrConst.TEXT_COLOR, R.color.tabTextNormal);

        wvTransfer.setVisibility(View.GONE);
        dynamicAddView(ivTransfer, AttrConst.BACKGROUND, R.drawable.tab_transfer_normal);
        dynamicAddView(tvTransfer, AttrConst.TEXT_COLOR, R.color.tabTextNormal);

        wvService.setVisibility(View.GONE);
        dynamicAddView(ivService, AttrConst.BACKGROUND, R.drawable.tab_service_normal);
        dynamicAddView(tvService, AttrConst.TEXT_COLOR, R.color.tabTextNormal);

        if (llAssets.getVisibility() == View.VISIBLE) {
            llAssets.setVisibility(View.GONE);
        }
    }

    private final class InJavaScriptLocal extends InJavaScript {
        @JavascriptInterface
        public void gotoFragment(final String targets) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(targets)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        String target;
                        if (targets.split(",").length != 2) {
                            target = targets;
                        } else {
                            target = targets.split(",")[0];
                            wvHome.loadUrl(domain + targets.split(",")[1]);
                        }
                        switchTab(target);
                    }
                }
            });
        }

        // 刷新API余额
        @JavascriptInterface
        public void refreshApiBalance(final String apiId) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (apiId != null)
                        refreshAssets();
                }
            });
        }

        @JavascriptInterface
        public void gotoBet(final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(url)) {
                        ToastTool.show(instance, R.string.url_is_null);
                    } else {
                        Intent intent = new Intent(instance, BetActivity.class);
                        if (currTabIndex == 0) {
                            intent.putExtra(Constants.RESULT_FLAG, Constants.BETACTIVITY_2_HOME);
                        }
                        intent.putExtra("url", url);
                        startActivityForResult(intent, 0);
                    }
                }
            });
        }
    }

    private ValueCallback<Uri> mUploadMsg;

    private final class MyWebChromeClient extends WebChromeClient {
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMsg = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), Constants.FILE_CHOOSER_RESULT_CODE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (currTabIndex) {
                case 1:
                    if (wvDeposit.canGoBack()) {
                        wvDeposit.goBack();
                        return true;
                    }
                    break;
                case 2:
                    if (wvTransfer.canGoBack()) {
                        wvTransfer.goBack();
                        return true;
                    }
                    break;
                case 3:
                    if (wvService.canGoBack()) {
                        wvService.goBack();
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    protected View getWebView(int index) {
        View view = super.getWebView(index);
        if (view == null) {
            switch (index) {
                case 1:
                    return wvDeposit;
                case 2:
                    return wvTransfer;
                case 3:
                    return wvService;
            }
        }
        return view;
    }

    // 展开api余额
    @OnClick(R.id.llLogin)
    public void openAssets() {
        if (llAssets.getVisibility() == View.VISIBLE) {
            setAnimation(llAssets, 0, 1, 0, 0, 300);
            llAssets.setVisibility(View.GONE);
        } else {
            setAnimation(llAssets, 1, 0, 0, 0, 300);
            llAssets.setVisibility(View.VISIBLE);
            fillAssets();
        }
    }

    private void fillAssets() {
        tvMoney.setText(String.format("%s%s", balanceBean.getCurrSign(), balanceBean.getPlayerAssets()));
        if (balanceBean.getUsername() != null) {
            tvUsername.setText(balanceBean.getUsername());
        }
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

    // 刷新API余额
    @OnClick(R.id.btnRefreshAssets)
    public void refreshAssets() {
        if (time == TIME + 1) {
            time--;
            timer = new Timer();
            timer.schedule(new AssetsTimerTask(), 1000, 1000);//计时
            btnRefreshAssets.setBackgroundColor(getResources().getColor(R.color.disBtnAssets));
            showProgress(getResources().getString(R.string.refreshing), true);
            try {
                OkHttpUtils.post().url(domain + URLConstants.REFRESH_BALACE_URL).headers(setHeaders())
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "---> refreshAssets error --> " + e.getLocalizedMessage());
                        ToastTool.show(instance, R.string.net_busy_error);
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dismissProgress();
                        balanceBean = new Gson().fromJson(response, BalanceBean.class);
                        fillAssets();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "---> refreshAssets --> " + e.getMessage());
            }
        }
    }

    private class AssetsTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    btnRefreshAssets.setText(String.format(getResources().getString(R.string.refreshBalance), time));
                    time--;
                    if (time < 0) {
                        timer.cancel();
                        btnRefreshAssets.setText(R.string.assetRefresh);
                        btnRefreshAssets.setBackgroundColor(getResources().getColor(R.color.btnAssets));
                        time = TIME + 1;
                    }
                }
            });
        }
    }

    /**
     * 退出登录，处理app数据
     */
    @Override
    protected void logout(boolean isClear) {
        super.logout(isClear);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wvTransfer.clearHistory();
                wvDeposit.clearHistory();
                wvMine.clearHistory();

                llLogout.setVisibility(View.VISIBLE);
                llLogin.setVisibility(View.GONE);
                llAssets.setVisibility(View.GONE);

                wvDeposit.setTag(false);
                wvTransfer.setTag(false);

                switchTabHome();
            }
        });
    }


    @Override
    protected void onDestroy() {
        wvDeposit.stopLoading();
        wvDeposit.removeAllViews();
        wvDeposit.destroy();
        wvDeposit = null;

        super.onDestroy();
        wvTransfer.stopLoading();
        wvTransfer.removeAllViews();
        wvTransfer.destroy();
        wvTransfer = null;

        wvService.stopLoading();
        wvService.removeAllViews();
        wvService.destroy();
        wvService = null;
    }

}