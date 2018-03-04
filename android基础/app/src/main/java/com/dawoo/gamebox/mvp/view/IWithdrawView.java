package com.dawoo.gamebox.mvp.view;

/**
 * Created by b on 18-1-15.
 * 取款
 */

public interface IWithdrawView extends IBaseView {

    void onWithdrawInfo(Object o);

    void submitWithdraw(Object o);

    void checkSafePassword(Object o);

    void withdrawFee(Object o);

}
