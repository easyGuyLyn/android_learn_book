package com.dawoo.gamebox.mvp.model.account;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-2-25.
 */

public interface IAccountModel {
    Subscription getUserAssert(Subscriber subscriber);

}
