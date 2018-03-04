package com.dawoo.gamebox.mvp.view;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by benson on 17-12-21.
 */

public interface ILoginView extends IBaseView {

    void doOnLogin();

    void verifyRealName(Object o);

}

