package com.dawoo.gamebox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseMainActivity;
import com.dawoo.gamebox.been.BalanceBean;
import com.dawoo.gamebox.common.AttrConst;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.dawoo.gamebox.view.CustomDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class LotteryActivity extends BaseMainActivity {

    private static final String TAG = LotteryActivity.class.getSimpleName();

    // region ???????
    @BindView(R.id.wvLottery)
    WebView wvDeposit;
    @BindView(R.id.wvHall)
    protected WebView wvHall;
    @BindView(R.id.wvBet)
    protected WebView wvBet;
    // ????
    @BindView(R.id.ivLottery)
    ImageView ivLottery;
    @BindView(R.id.tvLottery)
    TextView tvLottery;
    // ????
    @BindView(R.id.ivHall)
    protected ImageView ivHall;
    @BindView(R.id.tvHall)
    protected TextView tvHall;
    // ????
    @BindView(R.id.ivBet)
    protected ImageView ivBet;
    @BindView(R.id.tvBet)
    protected TextView tvBet;
    @BindView(R.id.llGuide)
    LinearLayout llGuide;

    private LotteryActivity instance;
    private CustomDialog dialog;

    // endregion

    @Override
    public int initView() {
        return R.layout.activity_lottery;
    }

    @Override
    public void init() {
        super.init();

        instance = this;
        // 加载web资源
        loadWeb(new InJavaScriptLocal());
        dialog = new CustomDialog(this, true, R.string.demoPrompt, true, new CustomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    demo();
                }
                dialog.dismiss();
            }
        });
        showGuide();
        //是否需要加载url    home首次已经加载了所以设置为不需要
        wvHall.setTag(true);
        wvBet.setTag(true);
        wvDeposit.setTag(true);
        wvMine.setTag(true);
        wvHome.setTag(false);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
    }

    /**
     * 加载Web资源
     */
    protected void loadWeb(InJavaScriptLocal javaScript) {
        super.loadWeb(javaScript);

        this.webSetting(wvDeposit);
        wvDeposit.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvDeposit.setWebViewClient(client);

        this.webSetting(wvHall);
        wvHall.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvHall.setWebViewClient(client);
        wvHall.setTag(true);

        this.webSetting(wvBet);
        wvBet.addJavascriptInterface(javaScript, Constants.JS_OBJECT);
        wvBet.setWebViewClient(client);
        wvBet.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "resultCode ==> " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > Constants.RESULT_LOGIN_FAIL) {
            loginAfter();
        }
        switch (resultCode) {
            case Constants.RESULT_2_LOTTERY:
                wvDeposit.loadUrl(domain + URLConstants.DEPOSIT_URL);
                switchTabDeposit();
                break;
            case Constants.RESULT_2_HALL:
                wvHall.loadUrl(domain + URLConstants.HALL_URL);
                switchTabHall();
                break;
            case Constants.RESULT_2_BET:
                wvBet.loadUrl(domain + URLConstants.BET_URL);
                switchTabBet();
                changeToolbarView();
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
            case Constants.BETACTIVITY_2_HOME:
                if (wvHome.getTag() instanceof Boolean && Boolean.valueOf(wvHome.getTag().toString()) == false)
                    wvMine.loadUrl("javascript:window.page.getOpenResult();");//如果wvHome已经加载过数据了则不再刷新，只局部刷新
                else
                    switchTab("0");
                break;
            case Constants.BETACTIVITY_2_HALL:
                if (wvHall.getTag() instanceof Boolean && Boolean.valueOf(wvHall.getTag().toString()) == false)
                    wvHall.loadUrl("javascript:window.page.menu.getHeadInfo();");//如果wvHall已经加载过数据了则不再刷新，只局部刷新
                else
                    switchTab("3");
                break;
            default:
//                switchTab(String.valueOf(currTabIndex));
                break;
        }
    }

    /**
     * 登录成功后的操作
     */
    @Override
    protected void loginAfter() {
        super.loginAfter();
        OkHttpUtils.post().url(domain + URLConstants.GET_API_BALACE_URL).headers(setHeaders())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "loginAfter error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                llLogout.setVisibility(View.GONE);
                BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                if (bean != null) {
                    String username = bean.getUsername();
                    if (username != null && !username.contains("*")) {
                        username = username.substring(0, 2) + "****" + username.substring(username.length() - 2, username.length());
                    }
                    tvUsername.setText(String.format(getString(R.string.welcome), username));
                    tvUsername.setVisibility(View.VISIBLE);
                    changeToolbarView();
                }
            }
        });
//        reloadWebView();
    }

    /**
     * 登陆后重新加载WebView
     */
    @Override
    protected void reloadWebView() {
        super.reloadWebView();
        wvHall.clearHistory();
        wvBet.clearHistory();
        wvDeposit.clearHistory();

        wvBet.setTag(true);
        //预加载存款页面
        wvDeposit.loadUrl(domain + URLConstants.DEPOSIT_URL);
        wvDeposit.setTag(false);
    }

    // 充值提款
    @OnClick(R.id.llTabLottery)
    public void switchTabDeposit() {
        if (!MyApplication.isLogin) {
            toLoginActivity(Constants.RESULT_2_LOTTERY);
        } else if (MyApplication.isDemo) { //
            CustomDialog dialog = new CustomDialog(this, R.string.demoNoPermit, true, new CustomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            showToolbar();
            if (currTabIndex == 1) {
                wvDeposit.clearHistory();
                wvDeposit.loadUrl(domain + URLConstants.DEPOSIT_URL);
            } else {
                initTab(1);
                dynamicAddView(ivLottery, AttrConst.BACKGROUND, R.drawable.tab_deposit_selected);
                dynamicAddView(tvLottery, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
                wvDeposit.setVisibility(View.VISIBLE);
                if (wvDeposit.getTag() instanceof Boolean && Boolean.valueOf(wvDeposit.getTag().toString()) == true) {
                    wvDeposit.clearHistory();
                    wvDeposit.loadUrl(domain + URLConstants.DEPOSIT_URL);
                    wvDeposit.setTag(false);
                }
            }
            changeToolbarView();
        }
    }

    // 购彩大厅
    @OnClick(R.id.llTabHall)
    public void switchTabHall() {
        /*if (!isLogin && !MyApplication.isLogin) {
            toLoginActivity(Constants.RESULT_2_HALL);
        } else {*/
        if(MyApplication.isLogin) {
            wvHall.loadUrl("javascript:window.top.page.autoLoginPlByApp();");
        }

        hideToolbar();
        if (currTabIndex == 2) {
            wvHall.clearHistory();
            wvHall.loadUrl(domain + URLConstants.HALL_URL);
        } else {
            initTab(2);
            dynamicAddView(ivHall, AttrConst.BACKGROUND, R.drawable.tab_hall_selected);
            dynamicAddView(tvHall, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
            wvHall.setVisibility(View.VISIBLE);
            if (wvHall.getTag() instanceof Boolean && Boolean.valueOf(wvHall.getTag().toString()) == true) {
                wvHall.clearHistory();
                wvHall.loadUrl(domain + URLConstants.HALL_URL);
                wvHall.setTag(false);
            }else{
                wvHall.loadUrl("javascript:window.page.menu.getHeadInfo();");
            }
        }
        /*}*/
    }

    // 投注记录
    @OnClick(R.id.llTabBet)
    public void switchTabBet() {
        if (!MyApplication.isLogin) {
            toLoginActivity(Constants.RESULT_2_BET);
        } else {
            showToolbar();
            if (currTabIndex == 3) {
                wvBet.clearHistory();
                wvBet.loadUrl(domain + URLConstants.BET_URL);
            } else {
                initTab(3);
                dynamicAddView(ivBet, AttrConst.BACKGROUND, R.drawable.tab_bet_selected);
                dynamicAddView(tvBet, AttrConst.TEXT_COLOR, R.color.tabTextSelected);
                wvBet.setVisibility(View.VISIBLE);
                if (wvBet.getTag() instanceof Boolean && Boolean.valueOf(wvBet.getTag().toString()) == true) {
                    wvBet.clearHistory();
                    wvBet.loadUrl(domain + URLConstants.BET_URL);//需刷新购彩记录
                    wvBet.setTag(false);
                }else{
                    wvBet.loadUrl("javascript:window.page.refreshBetOrder();");
                }
            }
            changeToolbarView();
        }
    }

    /**
     * 初始化底部Tab栏
     */
    @Override
    public void initTab(int targetIndex) {
        super.initTab(targetIndex);

        wvDeposit.setVisibility(View.GONE);
        dynamicAddView(ivLottery, AttrConst.BACKGROUND, R.drawable.tab_deposit_normal);
        dynamicAddView(tvLottery, AttrConst.TEXT_COLOR, R.color.tabTextNormal);

        wvHall.setVisibility(View.GONE);
        dynamicAddView(ivHall, AttrConst.BACKGROUND, R.drawable.tab_hall_normal);
        dynamicAddView(tvHall, AttrConst.TEXT_COLOR, R.color.tabTextNormal);

        wvBet.setVisibility(View.GONE);
        dynamicAddView(ivBet, AttrConst.BACKGROUND, R.drawable.tab_bet_normal);
        dynamicAddView(tvBet, AttrConst.TEXT_COLOR, R.color.tabTextNormal);
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
                        } else if (currTabIndex == 2) {
                            intent.putExtra(Constants.RESULT_FLAG, Constants.BETACTIVITY_2_HALL);
                        }
                        intent.putExtra("url", url);
                        startActivityForResult(intent, 0);
                    }
                }
            });
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
                    toLoginActivity(Constants.RESULT_2_LOTTERY);
                }
                break;
            case "2":
                switchTabHall();
                break;
            case "3":
                if (MyApplication.isLogin) {
                    switchTabBet();
                } else {
                    toLoginActivity(Constants.RESULT_2_BET);
                }
                break;
            case "4":
                if (MyApplication.isLogin) {
                    switchTabMine();
                } else {
                    toLoginActivity(Constants.RESULT_2_MINE);
                }
                break;
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
                    if (wvHall.canGoBack()) {
                        wvHall.goBack();
                        return true;
                    }
                    break;
                case 3:
                    if (wvBet.canGoBack()) {
                        wvBet.goBack();
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
                    return wvHall;
                case 3:
                    return wvBet;
            }
        }
        return view;
    }

    @OnClick(R.id.btnDemo)
    public void toDemo() {
        dialog.show();
    }

    @OnClick(R.id.ivRefresh)
    public void toRefresh() {
       if(currTabIndex == 3){
           ToastTool.showToast(LotteryActivity.this,getResources().getString(R.string.refreshed));
           wvBet.loadUrl("javascript:window.page.refreshBetOrder();");
       }
    }

    private void demo() {
        OkHttpUtils.get().url(domain + URLConstants.DEMO_URL).headers(setHeaders()).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(final Response response, int id) throws Exception {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCookie(response);
                        MyApplication.isLogin = true;
                        MyApplication.isDemo = true;
                        loginAfter();
                    }
                });
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "demo Error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Object response, int id) {
            }
        });
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
                llLogout.setVisibility(View.VISIBLE);
                tvUsername.setVisibility(View.GONE);
                switchTabHome();
            }
        });
        wvHall.clearHistory();
        wvBet.clearHistory();
        wvDeposit.clearHistory();
        wvMine.clearHistory();

        wvBet.setTag(true);
        wvDeposit.setTag(true);
        wvMine.setTag(true);
    }

    private void showGuide() {
        boolean isFirst = Boolean.valueOf(String.valueOf(SPTool.get(this, Constants.KEY_IS_FIRST, true)));
        if (isFirst) {
            if (ParamTool.isLotterySite(context)) { //纯彩票隐藏左侧切换提示
                llGuide.setVisibility(View.GONE);
            } else {
                llGuide.setVisibility(View.VISIBLE);
            }
        } else {
            llGuide.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnIGot)
    public void iGot() {
        llGuide.setVisibility(View.GONE);
        SPTool.put(this, Constants.KEY_IS_FIRST, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvDeposit.stopLoading();
        wvDeposit.removeAllViews();
        wvDeposit.destroy();
        wvDeposit = null;

        wvHall.stopLoading();
        wvHall.removeAllViews();
        wvHall.destroy();
        wvHall = null;

        wvBet.stopLoading();
        wvBet.removeAllViews();
        wvBet.destroy();
        wvBet = null;
    }
}