package com.dawoo.gamebox.mvp.model.withdraw;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-1-15.
 * 取款、添加银行卡，比特币
 */

public interface IWithdrawModel {

    Subscription getWithDraw(Subscriber subscriber);

    Subscription submitWithdraw(Subscriber subscriber,double withdrawAmount,String token,int remittanceWay, String originPwd);

    Subscription getCardType(Subscriber subscriber);

    Subscription submitBankCard(Subscriber subscriber , String bankcardMasterName,String bankName,String bankcardNumber,String bankDeposit);

    Subscription submitBtc(Subscriber subscriber ,String bankcardNumber);

    Subscription getBtcInfo(Subscriber subscriber);

    Subscription checkSafePassword(Subscriber subscriber , String originPwd);

    Subscription withdrawFee(Subscriber subscriber , double withdrawFee);
}
