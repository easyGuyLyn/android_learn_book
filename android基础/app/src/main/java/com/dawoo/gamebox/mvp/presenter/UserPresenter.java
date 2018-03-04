package com.dawoo.gamebox.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.mvp.model.user.IUserModel;
import com.dawoo.gamebox.mvp.model.user.UserModel;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.ILoginOutView;
import com.dawoo.gamebox.mvp.view.IModifyLoginPwdView;
import com.dawoo.gamebox.mvp.view.IModifySecurityPwdView;
import com.dawoo.gamebox.net.rx.ProgressSubscriber;
import com.dawoo.gamebox.view.view.CustomDialog;

import rx.Subscription;

/**
 * 用户相关的presenter
 * Created by benson on 18-1-7.
 */

public class UserPresenter<T extends IBaseView> extends BasePresenter {
    private final Context mContext;
    private T mView;
    private final IUserModel mModel;
    private CustomDialog mCustomDialog;

    public UserPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new UserModel();
        mCustomDialog = new CustomDialog(mContext, R.style.customIosDialog, R.layout.dialog_input_real_name);
    }


    /**
     * 修改登录密码
     */
    public void modifyLoginPwd(String oldPwd, String newPwd) {
        Subscription subscription = mModel.modifyLoginPwd(new ProgressSubscriber(o ->
                ((IModifyLoginPwdView) mView).onModifyResult(o), mContext), oldPwd, newPwd);
        subList.add(subscription);
    }

    /**
     * 修改登录密码,带验证码
     */
    public void modifyLoginPwdWithCode(String oldPwd, String newPwd, String code) {
        Subscription subscription = mModel.modifyLoginPwd(new ProgressSubscriber(o ->
                ((IModifyLoginPwdView) mView).onModifyResult(o), mContext), oldPwd, newPwd, code);
        subList.add(subscription);
    }


    /**
     * init 安全密码
     */
    public void initSecurityPwd() {
        Subscription subscription = mModel.initSecurityPwd(new ProgressSubscriber(o ->
                ((IModifySecurityPwdView) mView).onInitResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * set realName
     */
    public void setRealName(String realName) {
        Subscription subscription = mModel.setRealSafeName(new ProgressSubscriber(o ->
                ((IModifySecurityPwdView) mView).onSetRealNameResult(o), mContext), realName);
        subList.add(subscription);
    }


    /**
     * 修改安全密码
     */
    public void modifySecurityPwd(Boolean needCaptcha, String realName, String oldPwd, String newPwd, String confirmPwd, String code) {
        Subscription subscription = mModel.modifySecurityPwd(new ProgressSubscriber(o ->
                ((IModifySecurityPwdView) mView).onModifyResult(o), mContext), needCaptcha, realName, oldPwd, newPwd, confirmPwd, code);
        subList.add(subscription);
    }


    /**
     * 输入realName的dialog
     */
    public void setRealNameDialog() {
        showRealNameDialog();
        mCustomDialog.setCanceledOnTouchOutside(false);
        mCustomDialog.setCancelable(false);
        TextView tvOk = mCustomDialog.findViewById(R.id.ok);
        EditText et_set_realName = mCustomDialog.findViewById(R.id.et_set_realName);
        et_set_realName.setText("");
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_set_realName.getText())) {
                    setRealName(et_set_realName.getText().toString());
                    ((IModifySecurityPwdView) mView).backRealName(et_set_realName.getText().toString());
                } else {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.input_not_blank));
                }
            }
        });

    }


    public void LoginOut() {
        Subscription subscription = mModel.logOut(new ProgressSubscriber(o ->
                ((ILoginOutView) mView).onClickResult(o), mContext));
        subList.add(subscription);
    }


    public void showRealNameDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.show();
        }
    }

    public void dimissRealNameDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (mCustomDialog != null) {
            mCustomDialog.cancel();
            mCustomDialog = null;
        }

    }
}
