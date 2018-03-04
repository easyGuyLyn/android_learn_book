package com.dawoo.gamebox.mvp.model.withdraw;

import com.dawoo.gamebox.bean.BankCards;
import com.dawoo.gamebox.bean.Bitcoin;
import com.dawoo.gamebox.bean.HongbaoCount;
import com.dawoo.gamebox.bean.WithdrawFee;
import com.dawoo.gamebox.bean.WithdrawResult;
import com.dawoo.gamebox.bean.WithdrawSubmitResult;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.mvp.service.IWithdrawService;
import com.dawoo.gamebox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-1-15.
 * 取款
 */

public class WithdrawModel extends BaseModel implements IWithdrawModel{

    @Override
    public Subscription getWithDraw(Subscriber subscriber) {
        Observable<WithdrawResult> observable = RetrofitHelper.getService(IWithdrawService.class).getWithDraw();
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription submitWithdraw(Subscriber subscriber,double withdrawAmount,  String token,int remittanceWay,String originPwd) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getService(IWithdrawService.class).submitWithdraw(withdrawAmount,token,remittanceWay,originPwd);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCardType(Subscriber subscriber) {
        Observable<BankCards> observable = RetrofitHelper.getService(IWithdrawService.class).getCardAndBanksInfo().map(new HttpResultFunc<BankCards>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription submitBankCard(Subscriber subscriber, String bankcardMasterName, String bankName, String bankcardNumber, String bankDeposit) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getService(IWithdrawService.class).submitBankCard(bankcardMasterName,bankName,bankcardNumber,bankDeposit);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription submitBtc(Subscriber subscriber,String bankcardNumber) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getService(IWithdrawService.class).submitBtc(bankcardNumber);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getBtcInfo(Subscriber subscriber) {
        Observable<Bitcoin> observable = RetrofitHelper.getService(IWithdrawService.class).getBtcInfo();
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription checkSafePassword(Subscriber subscriber, String originPwd) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getService(IWithdrawService.class).checkSafePassword(originPwd);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription withdrawFee(Subscriber subscriber, double withdrawFee) {
        Observable<WithdrawFee> observable = RetrofitHelper.getService(IWithdrawService.class).withdrawFee(withdrawFee);
        return toSubscribe(observable, subscriber);
    }
}
