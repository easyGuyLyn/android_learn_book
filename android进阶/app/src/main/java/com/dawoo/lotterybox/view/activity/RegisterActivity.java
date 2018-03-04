package com.dawoo.lotterybox.view.activity;


import android.app.Activity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.activity.KeyboardUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.mvp.presenter.UserPresenter;
import com.dawoo.lotterybox.mvp.view.IRegisterView;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 * Created by benson on 17-12-27.
 */

public class RegisterActivity extends BaseActivity implements IRegisterView {
    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.name_et)
    EditText mNameEt;
    @BindView(R.id.pwd_lable_tv)
    TextView mPwdLableTv;
    @BindView(R.id.pwd_et)
    EditText mPwdEt;
    @BindView(R.id.input_type_iv)
    ImageView mInputTypeIv;
    @BindView(R.id.confirm_pwd_lable_tv)
    TextView mConfirmPwdLableTv;
    @BindView(R.id.confirm_pwd_et)
    EditText mConfirmPwdEt;
    @BindView(R.id.confirm_input_type_iv)
    ImageView mConfirmInputTypeIv;
    @BindView(R.id.up_ll)
    LinearLayout mUpLl;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.protocal_tv)
    TextView mProtocalTv;
    @BindView(R.id.protocal_ll)
    LinearLayout mProtocalLl;
    private UserPresenter mPresenter;
    public static final String MODE = "TYPE_MODE";
    public static final String MODE_STANDAR = "1";
    public static final String MODE_DEMO = "2";

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acitivity_register);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestory();
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.title_name_activity_register), true);
        mPresenter = new UserPresenter<>(this, this);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.input_type_iv, R.id.confirm_input_type_iv, R.id.login_btn, R.id.protocal_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.input_type_iv:
                doPwdToggle();
                break;
            case R.id.confirm_input_type_iv:
                doConPwdToggle();
                break;
            case R.id.login_btn:
                doRigster();
                break;
            case R.id.protocal_ll:
                ToastUtil.showToastShort(this, "功能待续");
                break;
        }
    }

    @Override
    public void onRigsterResult(boolean isSuccess) {
        if (isSuccess) {
            ToastUtil.showResShort(this, R.string.register_success);
            finish();
        }
    }

    @Override
    public void doRigster() {
        String name = mNameEt.getText().toString().trim();
        String pwd = mPwdEt.getText().toString().trim();
        String confirmPwd = mConfirmPwdEt.getText().toString().trim();
        String reateChannel = "4";
        String playerType = "member";

        String mode = getIntent().getStringExtra(MODE);

        if (TextUtils.isEmpty(mode)) {
            mode = "1";//账号模式：1-正式，2-演示
        }
        mPresenter.register(name, pwd, confirmPwd, reateChannel, playerType, mode);
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

    @Override
    public void doConPwdToggle() {
        if (mConfirmInputTypeIv.isSelected()) {
            mConfirmInputTypeIv.setSelected(false);
            mConfirmPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mConfirmInputTypeIv.setSelected(true);
            mConfirmPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        }
        KeyboardUtil.hideInputKeyboard(this);
    }
}
