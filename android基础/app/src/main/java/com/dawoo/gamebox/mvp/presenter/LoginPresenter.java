package com.dawoo.gamebox.mvp.presenter;

import android.content.Context;

import com.dawoo.gamebox.mvp.model.login.ILoginModel;
import com.dawoo.gamebox.mvp.model.login.LoginModel;
import com.dawoo.gamebox.mvp.model.user.UserModel;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.ICasinoGameListView;
import com.dawoo.gamebox.mvp.view.ILoginView;
import com.dawoo.gamebox.net.rx.ProgressSubscriber;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;


/**
 * Created by benson on 17-12-21.
 */

public class LoginPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private IBaseView mView;
    private final LoginModel mModel;
    private List<Subscription> subList = new ArrayList<>();

    public LoginPresenter(Context context, T loginView) {
        super(context, loginView);
        mContext = context;
        this.mView = loginView;
        mModel = new LoginModel();
    }

    /**
     * 验证真实姓名
     */
    public void verifyRealName(String token, String realName, String playerAccount, String playeAccount, String tempPass, String newPassword) {
        Subscription subscription = ((LoginModel)mModel).verifyRealName(new ProgressSubscriber(o ->
                        ((ILoginView) mView).verifyRealName(o), mContext),
                token,
                realName,
                playerAccount,
                playeAccount,
                tempPass,
                newPassword);
        subList.add(subscription);
    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
