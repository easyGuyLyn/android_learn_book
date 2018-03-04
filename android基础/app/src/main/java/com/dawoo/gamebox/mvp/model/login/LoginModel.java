package com.dawoo.gamebox.mvp.model.login;

import com.dawoo.gamebox.bean.VerifyRealNameResponse;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.mvp.service.IHomeService;
import com.dawoo.gamebox.mvp.service.ILoginService;
import com.dawoo.gamebox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-1-25.
 */

public class LoginModel extends BaseModel implements ILoginModel {

    @Override
    public Subscription verifyRealName(Subscriber subscriber, String token, String realName, String playerAccount, String playeAccount, String tempPass, String newPassword) {
        Observable<VerifyRealNameResponse> observable = RetrofitHelper.getService(ILoginService.class).verifyRealName(token,realName,
                "yes",playerAccount,playeAccount,tempPass,newPassword,"20");
        return toSubscribe(observable, subscriber);
    }

}
