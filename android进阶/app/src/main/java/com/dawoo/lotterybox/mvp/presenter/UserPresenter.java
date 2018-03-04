package com.dawoo.lotterybox.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.ValidateUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.mvp.model.user.IUserModel;
import com.dawoo.lotterybox.mvp.model.user.UserModel;
import com.dawoo.lotterybox.mvp.view.IBaseView;
import com.dawoo.lotterybox.mvp.view.ILoginView;
import com.dawoo.lotterybox.mvp.view.ILotteryHallView;
import com.dawoo.lotterybox.mvp.view.IRegisterView;
import com.dawoo.lotterybox.net.rx.ProgressSubscriber;

import rx.Subscription;

/**
 * 用户相关的presenter
 * Created by benson on 18-1-7.
 */

public class UserPresenter<T extends IBaseView> extends BasePresenter {
    private final Context mContext;
    private T mView;
    private final IUserModel mModel;

    public UserPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new UserModel();
    }

    /**
     * 登录
     */
    public void login(String name, String pwd, String appKey, String appSecret, String serialNo) {
        Subscription subscription = mModel.login(new ProgressSubscriber(o ->
                        ((ILoginView) mView).onLoginResult((LoginBean) o), mContext),
                name,
                pwd,
                appKey,
                appSecret,
                serialNo);
        subList.add(subscription);
    }

    /**
     * 登录
     */
    public void register(String name, String pwd, String confirmPwd, String createChannel, String playerType, String mode) {
        if (!validate(name, pwd, confirmPwd)) {
            return;
        }


        Subscription subscription = mModel.register(new ProgressSubscriber(o ->
                        ((IRegisterView) mView).onRigsterResult((Boolean) o), mContext),
                name,
                pwd,
                confirmPwd,
                createChannel,
                playerType,
                mode);
        subList.add(subscription);
    }

    /**
     * 注册的验证
     *
     * @param name
     * @param pwd
     * @param confirmPwd
     * @return
     */
    public boolean validate(String name, String pwd, String confirmPwd) {
        // 判断空
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showResShort(mContext, R.string.validate_register_user);
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showResShort(mContext, R.string.validate_register_pwd);
            return false;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtil.showResShort(mContext, R.string.validate_register_confirm_pwd);
            return false;
        }

        // 密码不相等
        if (!pwd.equals(confirmPwd)) {
            ToastUtil.showResShort(mContext, R.string.validate_register_pwd_not_same);
            return false;
        }

        // 位数不正确
        if (name.length() < 4 || name.length() > 16) {
            ToastUtil.showResShort(mContext, R.string.validate_register_user_regular_correct);
            return false;
        }
        if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtil.showResShort(mContext, R.string.validate_register_pwd_regular_correct);
            return false;
        }

        // 格式不正确
        if (!ValidateUtil.isStringFormatCorrect(name)) {
            ToastUtil.showResShort(mContext, R.string.validate_register_regular_correct);
            return false;
        }

        return true;
    }


    /**
     * 获取用户信息
     */
    public void getUerInfo() {
        Subscription subscription = mModel.getUserInfo(new ProgressSubscriber(o -> ((ILotteryHallView) mView).onGetUserInfoResult((User) o), mContext));
        subList.add(subscription);
    }

    /**
     * 设置user数据
     * @param user
     */
    public void setUser(User user) {
        User user1 = DataCenter.getInstance().getUser();
        user1.setUserId(user.getUserId());
        user1.setUsername(user.getUsername());
        user1.setNickname(user.getNickname());
        user1.setBalance(user.getBalance());
        user1.setAvatarUrl(user.getAvatarUrl());
        DataCenter.getInstance().setUser(user1);
    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
