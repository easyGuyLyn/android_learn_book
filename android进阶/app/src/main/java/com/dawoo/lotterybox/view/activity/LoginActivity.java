package com.dawoo.lotterybox.view.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.KeyboardUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.mvp.presenter.UserPresenter;
import com.dawoo.lotterybox.mvp.view.ILoginView;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by benson on 17-12-21.
 */

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.name_et)
    EditText mNameEt;
    @BindView(R.id.user_pwd_iv)
    ImageView mUserPwdIv;
    @BindView(R.id.pwd_et)
    EditText mPwdEt;
    @BindView(R.id.input_type_iv)
    ImageView mInputTypeIv;
    @BindView(R.id.forget_pwd_tv)
    TextView mForgetPwdTv;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;
    private UserPresenter mPresenter;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        mHeadView.setHeader(getString(R.string.title_name_activity_login), true);
        mPresenter = new UserPresenter<>(this, this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestory();
        super.onDestroy();
    }


    @Override
    public void onLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            User user = new User();
            user.setLogin(true);
            user.setToken(loginBean.getToken());
            user.setRefreshToken(loginBean.getRefreshToken());
            user.setExpire(loginBean.getExpire());
            DataCenter.getInstance().setUser(user);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void doLogin() {
        KeyboardUtil.hideInputKeyboard(this);

        String name = mNameEt.getText().toString().trim();
        String pwd = mPwdEt.getText().toString().trim();
        String appKey = getResources().getString(R.string.app_key);
        String appSecret = getResources().getString(R.string.app_secret);
        String serialNo = DataCenter.getInstance().getSysInfo().getMac();
        mPresenter.login(name, pwd, appKey, appSecret, serialNo);
    }

    @Override
    public void doPwdToggle() {
        if (mInputTypeIv.isSelected()) {
            mInputTypeIv.setSelected(false);
            mPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mInputTypeIv.setSelected(true);
            mPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        }

        KeyboardUtil.hideInputKeyboard(this);
    }


    @OnClick({R.id.input_type_iv, R.id.forget_pwd_tv, R.id.login_btn, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.input_type_iv:
                doPwdToggle();
                break;
            case R.id.forget_pwd_tv:
                ToastUtil.showToastShort(this, "功能待续");
                break;
            case R.id.login_btn:
                doLogin();
                break;
            case R.id.register_btn:
                startRegisterActivity();
                break;
        }
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.MODE, RegisterActivity.MODE_STANDAR);
        startActivity(intent);
    }
}
