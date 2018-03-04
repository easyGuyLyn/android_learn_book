package com.dawoo.gamebox.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tencent.smtt.sdk.CookieManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.LoginBean;
import com.dawoo.gamebox.bean.VerifyRealNameBean;
import com.dawoo.gamebox.mvp.presenter.LoginPresenter;
import com.dawoo.gamebox.mvp.view.ILoginView;
import com.dawoo.gamebox.util.NetUtil;
import com.dawoo.gamebox.util.SPTool;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.activity.webview.WebViewActivity;
import com.dawoo.gamebox.view.view.HeaderView;
import com.dawoo.gamebox.view.view.InputBoxDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hwangjr.rxbus.RxBus;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by benson on 17-12-21.
 */

public class LoginActivity extends BaseActivity implements ILoginView {
    @BindView(R.id.head_view)
    HeaderView mHeaderView;
    @BindView(R.id.etUsername)
    EditText mEtUsername;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.iv3)
    ImageView mIv3;
    @BindView(R.id.etCaptcha)
    EditText mEtCaptcha;
    @BindView(R.id.ivCaptcha)
    ImageView mIvCaptcha;
    @BindView(R.id.tv_captcha)
    TextView mTvCaptcha;
    @BindView(R.id.rlCaptcha)
    RelativeLayout mRlCaptcha;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;
    private LoginPresenter mLoginPresenter;
    private String mDomain;
    private boolean needCaptcha;
    private int mMCode;  //登录返回状态码
    private String successUserName = "";
    private String successUserPwd = "";
    private Response mResponse;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        mDomain = DataCenter.getInstance().getDomain();
        mHeaderView.setHeader(getString(R.string.title_name_Login_activity), true);
        // 填充输入框
        fillInput();
        mLoginPresenter = new LoginPresenter(this, this);
//        subcriberOnNextListner = new SubscriberOnNextListener<Person>() {
//
//            @Override
//            public void onNext(Person person) {
//                onLoginResult(true, 110);
//            }
//        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestory();
    }


    @Override
    public void doOnLogin() {
        String name = mEtUsername.getText().toString().trim();
        String pwd = mEtPassword.getText().toString().trim();
        String captcha = mEtCaptcha.getText().toString().trim();
        successUserName = name;
        successUserPwd = pwd;
        if (validate(name, pwd, captcha)) {
            // mLoginPresenter.doLogin(name, pwd, new ProgressSubscriber(subcriberOnNextListner, this));
            showProgress(R.string.login_loading, true);
            String url = mDomain + ConstantValue.LOGIN_URL;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("username", name)
                    .add("password", pwd)
                    .add("captcha", captcha).build();
            Request request = new Request.Builder().url(url)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("User-Agent", "app_android;Android")
                    .post(body)
                    .build();
            try {
                CookieManager.getInstance().setCookie(mDomain, "");
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
                    public void onResponse(Call call, Response response) throws IOException {
                        mResponse = response;
                        mMCode = response.code();
                        if (mMCode < 200 && mMCode > 302) {
                            LogUtils.e("login Error ==> " + response.message());
                            ToastUtil.showToastShort(LoginActivity.this, "" + response.message());
                            dismissProgress();
                            return;
                        }
                        final String jsonData = response.body().string();
                        Log.e("登录中返回报文：", jsonData);

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
                ToastUtil.showToastShort(LoginActivity.this, "" + e.getMessage());
                dismissProgress();
            }
        }

    }


    @Override
    public void verifyRealName(Object o) {


    }

    @NonNull
    private Map<String, String> setParams(String username, String password, String captcha) {
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
        LoginBean loginBean = null;
        try {
            loginBean = new Gson().fromJson(jsonData, LoginBean.class);
            needCaptcha = loginBean.isIsOpenCaptcha();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            ToastUtil.showToastShort(this, e.getMessage());
        }

        if (loginBean == null) {
            return;
        }

        if (loginBean.isSuccess()) {
            if (mMCode == 302 && !TextUtils.isEmpty(loginBean.getPropMessages().getLocation())) {
                InputBoxDialog inputBoxDialog = new InputBoxDialog(this, R.style.CommonHintDialog);
                inputBoxDialog.show();
                LoginBean finalLoginBean = loginBean;
                inputBoxDialog.setOkonClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String realName = inputBoxDialog.mEtSetRealName.getText().toString().trim();
                        if (!TextUtils.isEmpty(realName)) {
//                            mLoginPresenter.verifyRealName(loginBean.getPropMessages().getGbToken(), realName, successUserName, successUserName, successUserPwd, successUserPwd);
                            inputBoxDialog.dismiss();
                            showProgress(R.string.login_loading, true);

                            String url = mDomain + ConstantValue.REAL_NAME_URL;
                            OkHttpClient client = new OkHttpClient();
                            RequestBody body = new FormBody.Builder()
                                    .add("gb.token", finalLoginBean.getPropMessages().getGbToken())
                                    .add("result.realName", realName)
                                    .add("needRealName", "yes")
                                    .add("result.playerAccount", successUserName)
                                    .add("search.playerAccount", successUserName)
                                    .add("tempPass", successUserPwd)
                                    .add("newPassword", successUserPwd)
                                    .add("passLevel", "20").build();
                            Request request = new Request.Builder().url(url)
                                    .addHeader("X-Requested-With", "XMLHttpRequest")
                                    .addHeader("User-Agent", "app_android;Android")
                                    .post(body)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
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
                                            if (!verifyRealNameBean.getData().isNameSame()) {
                                                uiToast(getString(R.string.real_name_error));
                                            } else if (verifyRealNameBean.getData().isConflict())
                                                uiToast(getString(R.string.account_existence));
                                            else {
                                                uiToast(getString(R.string.verify_name_ok));
                                                needCaptcha = false;
                                                doOnLogin();
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
            ToastUtil.showToastShort(LoginActivity.this, message);
        }
        if (loginBean.isIsOpenCaptcha()) {
            SPTool.put(this, ConstantValue.KEY_NEED_CAPTCHA, true);
            SPTool.put(this, ConstantValue.KEY_CAPTCHA_TIME, new Date().getTime());
            mRlCaptcha.setVisibility(View.VISIBLE);
            mTvCaptcha.setVisibility(View.VISIBLE);
            getCaptcha();
        }
        dismissProgress();
    }

    private void uiToast(String sring) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToastLong(LoginActivity.this, sring);
            }
        });
    }

    /**
     * 登录成功
     */
    private void loginSuccess(Response response) {
        RxBus.get().post(ConstantValue.EVENT_TYPE_LOGINED, "login");
        SPTool.put(this, ConstantValue.KEY_NEED_CAPTCHA, false);
        SPTool.remove(this, ConstantValue.KEY_CAPTCHA_TIME);

        NetUtil.setCookie(response);

        DataCenter.getInstance().setLogin(true);
        DataCenter.getInstance().setUserName(successUserName);
        DataCenter.getInstance().setPassword(successUserPwd);

        SPTool.put(this, ConstantValue.KEY_USERNAME, successUserName);
        SPTool.put(this, ConstantValue.KEY_PASSWORD, successUserPwd);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }

    /**
     * 表单验证
     */
    public boolean validate(String username, String password, String captcha) {
        if (TextUtils.isEmpty(username)) {
            getFocusable(mEtUsername);
            ToastUtil.showResShort(LoginActivity.this, R.string.username_hint);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            getFocusable(mEtPassword);
            ToastUtil.showResShort(LoginActivity.this, R.string.password_hint);
            return false;
        }

        if (needCaptcha && captcha.length() != 4) {
            getFocusable(mEtCaptcha);

            ToastUtil.showResShort(LoginActivity.this, R.string.enter_captcha);
            if (mRlCaptcha.getVisibility() == View.GONE) {
                mRlCaptcha.setVisibility(View.VISIBLE);
                mTvCaptcha.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.ivCaptcha, R.id.btnLogin, R.id.btnRegister})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.ivCaptcha:
                getCaptcha();
                break;
            case R.id.btnLogin:
                doOnLogin();
                break;
            case R.id.btnRegister:
                // 注册
                String url = DataCenter.getInstance().getDomain() + ConstantValue.REGISTER_URL;
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(ConstantValue.WEBVIEW_URL, url);
                intent.putExtra(ConstantValue.WEBVIEW_TYPE, ConstantValue.WEBVIEW_TYPE_ORDINARY);
                startActivityForResult(intent, ConstantValue.KEY_REGIST_BACK_LOGIN);
                break;
        }
    }

    private void fillInput() {
        String spUsername = (String) SPTool.get(this, ConstantValue.KEY_USERNAME, "");
        String spPassword = (String) SPTool.get(this, ConstantValue.KEY_PASSWORD, "");
        if (!TextUtils.isEmpty(spPassword)) {
            mEtUsername.setText(spUsername);
            mEtPassword.setText(spPassword);
        } else {
            if (!TextUtils.isEmpty(spUsername)) {
                mEtUsername.setText(spUsername);
                mEtPassword.requestFocus();
            }
        }

        needCaptcha = (boolean) SPTool.get(this, ConstantValue.KEY_NEED_CAPTCHA, false);
        if (needCaptcha) {
            long now = new Date().getTime();
            long date = (long) SPTool.get(this, ConstantValue.KEY_CAPTCHA_TIME, now);
            if (now - date < 30 * 60 * 1000) {
                mRlCaptcha.setVisibility(View.VISIBLE);
                mTvCaptcha.setVisibility(View.VISIBLE);
            }
        }
    }

    //获取验证码
    private void getCaptcha() {
        OkHttpUtils.get().url(mDomain + ConstantValue.CAPTCHA_URL).addParams("_t", String.valueOf(new Date().getTime()))
                .headers(NetUtil.setHeaders()).build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("captcha error ==> " + e.getMessage());
                ToastUtil.showResShort(LoginActivity.this, R.string.getCaptchaFail);
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                mIvCaptcha.setImageBitmap(response);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValue.KEY_REGIST_BACK_LOGIN) {
            if (resultCode == ConstantValue.KEY_REGIST_BACK_LOGIN) {
                fillInput();
                doOnLogin();
            }
        }
    }
}
