package com.dawoo.gamebox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.been.BalanceBean;
import com.dawoo.gamebox.been.LoginBean;
import com.dawoo.gamebox.been.VerifyRealNameBean;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.dawoo.gamebox.view.CustomDialog;
import com.dawoo.gamebox.view.InputBoxDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dawoo.gamebox.common.URLConstants.VERIFY_NAME_URL;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etCaptcha)
    EditText etCaptcha;
    @BindView(R.id.ivCaptcha)
    ImageView ivCaptcha;
    @BindView(R.id.rlCaptcha)
    RelativeLayout rlCaptcha;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnTry)
    Button btnTry;
    private CustomDialog dialog;

    private String username;
    private String password;
    private String captcha;
    private String domain;
    // 登录后回哪去标识
    private boolean needCaptcha = false;
    private Integer resultFlag;
    // endregion
    @BindView(R.id.ibtnBack)
    ImageView ibtnBack;
    @BindView(R.id.ibtnBack2)
    ImageView ibtnBack2;
    @BindView(R.id.tvBack)
    TextView tvBack;
    private Response mResponse;
    private int mMCode;

    @Override
    public int initView() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        domain = MyApplication.domain;
        tvTitle.setText(R.string.btnLogin);
        // 填充输入框
        fillInput();
        Intent intent = getIntent();
        resultFlag = intent.getIntExtra(Constants.RESULT_FLAG, Constants.RESULT_2_HOME);

        dialog = new CustomDialog(this, true, R.string.demoPrompt, true, new CustomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    demo();
                }
                dialog.dismiss();
            }
        });
        if (ParamTool.isLotterySite(context)) {//混合站不显示试玩按钮
            btnTry.setVisibility(View.VISIBLE);
        } else {
            btnTry.setVisibility(View.GONE);
        }
        changeTitleBack(ibtnBack, ibtnBack2, tvBack);
    }

    private void fillInput() {
        username = (String) SPTool.get(this, Constants.KEY_USERNAME, "");
        password = (String) SPTool.get(this, Constants.KEY_PASSWORD, "");
        if (!StringUtils.isEmpty(password)) {
            etUsername.setText(username);
            etPassword.setText(password);
        } else {
            if (!StringUtils.isEmpty(username)) {
                etUsername.setText(username);
                etPassword.requestFocus();
            }
        }

        needCaptcha = (boolean) SPTool.get(this, Constants.KEY_NEED_CAPTCHA, false);
        if (needCaptcha) {
            long now = new Date().getTime();
            long date = (long) SPTool.get(this, Constants.KEY_CAPTCHA_TIME, now);
            if (now - date < 30 * 60 * 1000) {
                rlCaptcha.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        if (validate()) {
            showProgress(R.string.login_loading, true);
            CookieManager.getInstance().setCookie(MyApplication.domain, "");


            CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
            //设置https
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                  //  .cookieJar(cookieJar)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
           // OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("captcha", captcha).build();


            String cookie = setHeaders().get("Cookie");
            if(cookie == null) {
                cookie = "";
            }
            Request request = new Request.Builder().url(domain + URLConstants.LOGIN_URL)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("User-Agent",  "app_android;Android")
                    .addHeader("Cookie", cookie)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                    .post(body)
                    .build();
//               .addHeader("X-Requested-With", "XMLHttpRequest")
//                    .addHeader("User-Agent", "app_android;Android")
            try {
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        uiToast("" + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissProgress();
                            }
                        });
                        LogUtils.e("login Error ==> " + e.getLocalizedMessage());

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        mResponse = response;
                        setCookie(response);
                        mMCode = response.code();
                        if (mMCode < 200 && mMCode > 302) {
                            LogUtils.e("login Error ==> " + response.message());
                            ToastTool.showToastShort(LoginActivity.this, "" + response.message());
                            dismissProgress();
                            return;
                        }
                        final String jsonData = response.body().string();
                        Log.e("登录中返回报文：", jsonData);
                        needCaptcha = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleLogin(jsonData, response);

                            }
                        });
                    }
                });
            } catch (Exception e) {
                LogUtils.e("login Error ==> " + e.getLocalizedMessage());
                ToastTool.showToastShort(LoginActivity.this, "" + e.getMessage());
                dismissProgress();
            }
        }
    }

    @NonNull
    private Map<String, String> setParams() {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("captcha", captcha);
        return params;
    }

    /**
     * 处理登录
     */
    private void handleLogin(String jsonData, Response response) {
        final LoginBean loginBean = new Gson().fromJson(jsonData, LoginBean.class);
        if (loginBean.isSuccess()) {
            if (mMCode == 302 && !TextUtils.isEmpty(loginBean.getPropMessages().getLocation())) {
                final InputBoxDialog inputBoxDialog = new InputBoxDialog(this, R.style.CommonHintDialog);
                inputBoxDialog.show();
                inputBoxDialog.setOkonClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String realName = inputBoxDialog.mEtSetRealName.getText().toString().trim();
                        if (!TextUtils.isEmpty(realName)) {
//                            mLoginPresenter.verifyRealName(loginBean.getPropMessages().getGbToken(), realName, successUserName, successUserName, successUserPwd, successUserPwd);
                            inputBoxDialog.dismiss();
                            showProgress(R.string.login_loading, true);

                            String url = domain + VERIFY_NAME_URL;
                            OkHttpClient client = new OkHttpClient();
                            RequestBody body = new FormBody.Builder()
                                    .add("gb.token", loginBean.getPropMessages().getGbToken())
                                    .add("result.realName", realName)
                                    .add("needRealName", "yes")
                                    .add("result.playerAccount", username)
                                    .add("search.playerAccount", username)
                                    .add("tempPass", password)
                                    .add("newPassword", password)
                                    .add("passLevel", "20").build();
                            Request request = new Request.Builder().url(url)
                                    .addHeader("X-Requested-With", "XMLHttpRequest")
                                    .addHeader("User-Agent", "app_android;Android")
                                    .post(body)
                                    .build();

                            client.newCall(request).enqueue(new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    uiToast("" + e.getMessage());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissProgress();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    dismissProgress();
                                    mResponse = response;
                                    mMCode = response.code();
                                    if (mMCode != 200) {
                                        uiToast(getString(R.string.verify_name_error));
                                        return;
                                    }
                                    final String jsonData = response.body().string();
                                    Log.e("验证中返回报文：", jsonData);
                                    if (!TextUtils.isEmpty(jsonData)) {
                                        VerifyRealNameBean verifyRealNameBean = new Gson().fromJson(jsonData, VerifyRealNameBean.class);
                                        if (verifyRealNameBean != null) {
                                            if (!verifyRealNameBean.isNameSame()) {
                                                uiToast(getString(R.string.real_name_error));
                                            } else if (verifyRealNameBean.isConflict())
                                                uiToast(getString(R.string.account_existence));
                                            else{
                                                uiToast(getString(R.string.verify_name_ok));
                                                needCaptcha = false;
                                                login();
                                            }
                                        }
                                    } else
                                        uiToast(getString(R.string.verify_name_error));

                                }
                            });
                        }
                    }
                });

            } else
                loginSuccess(response);
        } else {
            String message = loginBean.getMessage() + "";
            if (loginBean.getPropMessages().getCaptcha() != null) {
                message = loginBean.getPropMessages().getCaptcha();
            }
            ToastTool.show(LoginActivity.this, message);
        }
        if (loginBean.isIsOpenCaptcha()) {
            SPTool.put(this, Constants.KEY_NEED_CAPTCHA, true);
            SPTool.put(this, Constants.KEY_CAPTCHA_TIME, new Date().getTime());
            rlCaptcha.setVisibility(View.VISIBLE);
            getCaptcha();
        }
        dismissProgress();
    }

    /**
     * 登录成功
     */
    private void loginSuccess(Response response) {
        SPTool.put(this, Constants.KEY_NEED_CAPTCHA, false);
        SPTool.remove(this, Constants.KEY_CAPTCHA_TIME);

        setCookie(response);

        MyApplication.isLogin = true;
        MyApplication.username = username;
        MyApplication.password = password;
        MyApplication.isDemo = false;

        SPTool.put(this, Constants.KEY_USERNAME, username);
        SPTool.put(this, Constants.KEY_PASSWORD, password);
        Intent intent = new Intent();
        intent.putExtra("loginSuccess","ok");
        setResult(resultFlag, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.REGISTER_SUCCESS:
                setResult(Constants.REGISTER_SUCCESS);
                finish();
                break;
        }
    }

    @OnClick(R.id.rlBank)
    void goBack() {
        finish();
    }

    //TODO
    @OnClick(R.id.ivCaptcha)
    public void refreshCaptcha() {
        getCaptcha();
    }

    @OnClick(R.id.btnRegister)
    public void toRegister() {
        toCommonActivity(domain + URLConstants.REGISTER_URL);
    }

    @OnClick(R.id.btnTry)
    public void toTry() {
        dialog.show();
    }

    private void demo() {
        OkHttpUtils.get().url(domain + URLConstants.TRY_URL).headers(setHeaders()).build().execute(new Callback() {
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

    private void uiToast(final String sring) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongToast(sring);
            }
        });
    }

    /**
     * 登录成功后的操作
     */
    protected void loginAfter() {

        OkHttpUtils.post().url(domain + URLConstants.GET_API_BALACE_URL).headers(setHeaders())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "loginAfter error ==> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String response, int id) {
//                llLogout.setVisibility(View.GONE);
                BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                if (bean != null) {
                    MyApplication.isLogin = true;
                    MyApplication.username = username;
                    MyApplication.password = password;
                    MyApplication.isDemo = false;
                    setResult(resultFlag);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 表单验证
     */
    public boolean validate() {
        username = etUsername.getText().toString();
        if (StringUtils.isEmpty(username)) {
            getFocusable(etUsername);
            ToastTool.show(LoginActivity.this, R.string.username_hint);
            return false;
        }

        password = etPassword.getText().toString();
        if (StringUtils.isEmpty(password)) {
            getFocusable(etPassword);
            ToastTool.show(LoginActivity.this, R.string.password_hint);
            return false;
        }

        captcha = etCaptcha.getText().toString();
        if (needCaptcha && captcha.length() != 4) {
            getFocusable(etCaptcha);

            ToastTool.show(LoginActivity.this, R.string.enter_captcha);
            if (rlCaptcha.getVisibility() == View.GONE) {
                rlCaptcha.setVisibility(View.VISIBLE);
            }
            getCaptcha();
            return false;
        }

        return true;
    }

    private void getFocusable(EditText view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public void toCommonActivity(String url) {
        Intent intent = new Intent(LoginActivity.this, CommonActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent, 0);
    }

    //获取验证码
    private void getCaptcha() {
        OkHttpUtils.get().url(domain + URLConstants.CAPTCHA_URL).addParams("_t", String.valueOf(new Date().getTime()))
                .headers(setHeaders()).build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "captcha error ==> " + e.getMessage());
                ToastTool.show(LoginActivity.this, R.string.getCaptchaFail);
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                ivCaptcha.setImageBitmap(response);
            }
        });
    }

}
