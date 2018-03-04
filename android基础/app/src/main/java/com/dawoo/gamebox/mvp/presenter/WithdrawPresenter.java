package com.dawoo.gamebox.mvp.presenter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.mvp.model.withdraw.WithdrawModel;
import com.dawoo.gamebox.mvp.view.IAddBankCardView;
import com.dawoo.gamebox.mvp.view.IAddBitcoinView;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.IWithdrawView;
import com.dawoo.gamebox.net.rx.ProgressSubscriber;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.List;

import rx.Subscription;

/**
 * Created by b on 18-1-15.
 * 取款
 */

public class WithdrawPresenter<T extends IBaseView> extends BasePresenter {

    private final WithdrawModel mMWithdrawModel;
    private Dialog mMDialog;

    public WithdrawPresenter(Context mContext, T view) {
        super(mContext, view);
        mMWithdrawModel = new WithdrawModel();
    }

    /**
     * 获取账户取款相关信息
     */
    public void getWithdraw() {
        Subscription subscription = mMWithdrawModel.getWithDraw(
                new ProgressSubscriber(o -> ((IWithdrawView) mView).onWithdrawInfo(o), mContext));
        subList.add(subscription);
    }

    /**
     * 提交取款申请
     */
    public void submitWithdraw(double withdrawAmount, String token, int remittanceWay, String originPwd) {
        Subscription subscription = mMWithdrawModel.submitWithdraw(
                new ProgressSubscriber(o -> ((IWithdrawView) mView).submitWithdraw(o), mContext),
                withdrawAmount,
                token,
                remittanceWay,
                originPwd);
        subList.add(subscription);
    }


    /**
     * 验证安全码
     */
    public void checkSafePassword(String originPwd) {
        Subscription subscription = mMWithdrawModel.checkSafePassword(
                new ProgressSubscriber(o -> ((IWithdrawView) mView).checkSafePassword(o), mContext),
                originPwd);
        subList.add(subscription);
    }


    /**
     * 获取银行卡种类和已绑定银行卡信息
     */
    public void getCardType() {
        Subscription subscription = mMWithdrawModel.getCardType(
                new ProgressSubscriber(o -> ((IAddBankCardView) mView).getCardType(o), mContext));
        subList.add(subscription);
    }

    /**
     * 添加银行卡
     */
    public void submitBankCard(String bankcardMasterName, String bankName, String bankcardNumber, String bankDeposit) {
        Subscription subscription = mMWithdrawModel.submitBankCard(
                new ProgressSubscriber(o -> ((IAddBankCardView) mView).submitBankCard(o), mContext),
                bankcardMasterName,
                bankName,
                bankcardNumber,
                bankDeposit);
        subList.add(subscription);
    }


    /**
     * 获取比特币信息
     */
    public void getBtcInfo() {
        Subscription subscription = mMWithdrawModel.getCardType(
                new ProgressSubscriber<>(o -> ((IAddBitcoinView) mView).getBtcInfo(o), mContext));
        subList.add(subscription);
    }

    ;

    /**
     * 添加比特币
     */

    public void submitBtc(String bankcardNumber) {
        Subscription subscription = mMWithdrawModel.submitBtc(
                new ProgressSubscriber(o -> ((IAddBitcoinView) mView).submitBtc(o), mContext),
                bankcardNumber);
        subList.add(subscription);
    }

    /**
     * 结算金额
     */

    public void withdrawFee(double withdrawAmount) {
        Subscription subscription = mMWithdrawModel.withdrawFee(
                new ProgressSubscriber(o -> ((IWithdrawView) mView).withdrawFee(o), mContext),
                withdrawAmount);
        subList.add(subscription);
    }


    /**
     * 银行选择dialog
     */
    private int mIndex = 3;

    public void initSelectBankDialog(List<String> mBanks) {
        mIndex = 3;
        if (mBanks.size() == 0) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.get_card_error));
            return;
        }
        mMDialog = new Dialog(mContext, R.style.CommonHintDialog);
        Window window = mMDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mMDialog.setContentView(R.layout.dialog_select_bank);
        LoopView loopView = mMDialog.findViewById(R.id.lp_select_bank);
        loopView.setItems(mBanks);
        loopView.setInitPosition(3);
        loopView.setTextSize(20);
        loopView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bgColor));
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mIndex = index;
            }
        });

        mMDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMDialog.dismiss();
            }
        });
        mMDialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAddBankCardView) mView).selectedBank(mBanks.get(mIndex), mIndex);
                mMDialog.dismiss();
            }
        });
        mMDialog.show();
    }

    public String getMosaicString(String str) {
        StringBuilder sb = new StringBuilder(str);
        if (str == null) {
            return "";
        } else if (1 < str.length() && str.length() < 10) {
            sb.replace(1, str.length(), "*");
            return sb.toString();
        } else if (12 < str.length()) {
            sb.replace(8, 12, "****");
            return sb.toString();
        }
        return "****";

    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (mMDialog != null) {
            mMDialog.dismiss();
        }
        mMDialog = null;
    }
}
