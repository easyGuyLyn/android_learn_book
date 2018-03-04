package com.dawoo.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dawoo.coretool.CleanLeakUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.bean.Withdraw;
import com.dawoo.gamebox.bean.WithdrawFee;
import com.dawoo.gamebox.bean.WithdrawResult;
import com.dawoo.gamebox.bean.WithdrawSubmitResult;
import com.dawoo.gamebox.mvp.presenter.WithdrawPresenter;
import com.dawoo.gamebox.mvp.view.IWithdrawView;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.activity.webview.WebViewActivity;
import com.dawoo.gamebox.view.view.CommonHintDialog;
import com.dawoo.gamebox.view.view.CommonInputBoxDialog;
import com.dawoo.gamebox.view.view.HeaderView;
import com.hwangjr.rxbus.RxBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 18-1-14.
 * 取款
 */

public class WithdrawMoneyActivity extends BaseActivity implements IWithdrawView, View.OnClickListener {
    @BindView(R.id.head_view)
    HeaderView mHeadView;
    @BindView(R.id.il_no_sufficient_funds)
    LinearLayout mIlNoSufficientFunds;
    @BindView(R.id.tv_withdraw_state)
    TextView mTvWithdrawState;
    @BindView(R.id.bt_deposit)
    Button mBtDeposit;
    @BindView(R.id.bt_bank_card_account)
    Button mBtBankCardAccount;
    @BindView(R.id.bt_bitcoin_account)
    Button mBtBitcoinAccount;
    @BindView(R.id.tv_bind_bank_card)
    TextView mTvBindBankCard;
    @BindView(R.id.ll_bank_card)
    LinearLayout LlBankCard;
    @BindView(R.id.iv_bank_icon)
    ImageView mIvBankIcon;
    @BindView(R.id.tv_bank_card)
    TextView mTvBankCard;
    @BindView(R.id.et_withdrawals_amount)
    EditText mEtWithdrawalsAmount;
    @BindView(R.id.tv_service_charge)
    TextView mTvServiceCharge;
    @BindView(R.id.tv_administrative_fee)
    TextView mTvAdministrativeFee;
    @BindView(R.id.tv_discount)
    TextView mTvDiscount;
    @BindView(R.id.tv_end_withdrawals_amount)
    TextView mTvEndWithdrawalsAmount;
    @BindView(R.id.tv_look_record)
    TextView mTvLookRecord;
    @BindView(R.id.bt_submit)
    Button mBtSubmit;
    @BindView(R.id.ll_withdraw)
    LinearLayout mLlWithdraw;

    private WithdrawPresenter mMWithdrwaPresenter;
    private Withdraw mMWithdraw;
    private Withdraw.AuditMapBean mAuditMapBean;
    private int mType = 1;
    private final int BANK_CARD = 1;
    private final int BITCOIN_CARD = 2;
    private CommonInputBoxDialog mMCommonInputDialog;
    private String mMOriginPwd;
    private double mActualWithdraw;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_withdraw_money);
    }

    @Override
    protected void initViews() {
        mHeadView.setHeader(getString(R.string.withdraw_money_activity), true);
        mBtBankCardAccount.setSelected(true);
        mBtBankCardAccount.setOnClickListener(this);
        mBtBitcoinAccount.setOnClickListener(this);
        mTvBindBankCard.setOnClickListener(this);
        mBtDeposit.setOnClickListener(this);
        mTvLookRecord.setOnClickListener(this);
        mBtSubmit.setOnClickListener(this);
        mEtWithdrawalsAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if ("0".equals(s.toString())) {
                        mEtWithdrawalsAmount.setText(null);
                        return;
                    }
                    mCountDownTimer.cancel();
                    mCountDownTimer.start();
                }
            }
        });
    }

    @Override
    protected void initData() {
        mMWithdrwaPresenter = new WithdrawPresenter(this, this);
        mMWithdrwaPresenter.getWithdraw();
    }


    @Override
    public void onClick(View v) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (v.getId()) {
            case R.id.bt_bank_card_account:
                mBtBankCardAccount.setSelected(true);
                mBtBitcoinAccount.setSelected(false);
                mBtBankCardAccount.setTextColor(ContextCompat.getColor(this, R.color.white));
                mBtBitcoinAccount.setTextColor(ContextCompat.getColor(this, R.color.tab_button_blue));
                setDataToView(BANK_CARD);
                break;
            case R.id.bt_bitcoin_account:
                mBtBitcoinAccount.setSelected(true);
                mBtBankCardAccount.setSelected(false);
                mBtBankCardAccount.setTextColor(ContextCompat.getColor(this, R.color.tab_button_blue));
                mBtBitcoinAccount.setTextColor(ContextCompat.getColor(this, R.color.white));
                setDataToView(BITCOIN_CARD);
                break;
            case R.id.tv_bind_bank_card:
                if (mType == BANK_CARD)
                    startActivity(new Intent(WithdrawMoneyActivity.this, AddBankCardActivity.class));
                else
                    startActivity(new Intent(WithdrawMoneyActivity.this, AddBitcoinActivity.class));
                finish();
                break;
            case R.id.bt_submit:
                if (mMWithdraw == null) {
                    mMWithdrwaPresenter.getWithdraw();
                    return;
                }
                if (!mMWithdraw.isSafePassword()) {
                    ToastUtil.showResLong(this, R.string.set_origin_pwd);
                    startActivity(new Intent(this, ModifySecurityPwdActivity.class));
                    finish();
                    return;
                }
                if (mTvBindBankCard.getVisibility() == View.VISIBLE) {
                    showNoBankCardDialog();
                    return;
                }
                testInputAndSubmit();
                break;
            case R.id.bt_deposit:
                RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_DEPOSIT, "gotodeposit");
                finish();
                break;
            case R.id.tv_look_record:
                if (mMWithdraw != null) {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra(ConstantValue.WEBVIEW_URL, DataCenter.getInstance().getDomain() + mMWithdraw.getAuditLogUrl());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

    }

    private void testInputAndSubmit() {
        if (TextUtils.isEmpty(mEtWithdrawalsAmount.getText().toString().trim())) {
            return;
        }
        double num = Double.valueOf(mEtWithdrawalsAmount.getText().toString().trim());
        if (num == 0) {
            ToastUtil.showResShort(this, R.string.input_withdrawals_amount);
            return;
        } else if (num > mMWithdraw.getTotalBalance()) {
            DecimalFormat df = new DecimalFormat("######0.00");
            String f1 = df.format(mMWithdraw.getTotalBalance());

            ToastUtil.showToastLong(this, String.format(getString(R.string.withdraw_max), f1));
            return;
        } else if (num < Double.parseDouble(mMWithdraw.getRank().getWithdrawMinNum()) || num > Double.parseDouble(mMWithdraw.getRank().getWithdrawMaxNum())) {
            ToastUtil.showResLong(this, R.string.input_withdraw_error);
            return;
        }
        if (mActualWithdraw > 0) {
            mMCommonInputDialog = new CommonInputBoxDialog(this, R.style.CommonHintDialog);
            mMCommonInputDialog.show();
            mMCommonInputDialog.setOkClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMOriginPwd = mMCommonInputDialog.getOriginPwd();
                    if (TextUtils.isEmpty(mMOriginPwd))
                        ToastUtil.showResLong(WithdrawMoneyActivity.this, R.string.input_cant_null);
                    else
                        mMWithdrwaPresenter.submitWithdraw(num, mMWithdraw.getToken(), mType, mMOriginPwd);
                }
            });

        } else ToastUtil.showResLong(this, R.string.withdraw_min);

    }

    @Override
    public void onWithdrawInfo(Object o) {
        WithdrawResult withdrawResult = (WithdrawResult) o;
        if (withdrawResult.getCode() == 1100) {
            mTvWithdrawState.setText(R.string.have_withdraw_order);
            mIlNoSufficientFunds.setVisibility(View.VISIBLE);
            mBtDeposit.setVisibility(View.GONE);
            return;
        }
        if (withdrawResult.getCode() == 1001) {
            ActivityUtil.gotoLogin();
            finish();
            return;
        }
        if (withdrawResult.getCode() == 1103) {
            showNoBankCardDialog();
            return;
        }
        if (withdrawResult.getCode() == 1102) {
            mTvWithdrawState.setText(String.format(getString(R.string.not_sufficient_funds_hint), withdrawResult.getMsg()));
            mIlNoSufficientFunds.setVisibility(View.VISIBLE);
            return;
        }
        mLlWithdraw.setVisibility(View.VISIBLE);
        mMWithdraw = withdrawResult.getData();
        if (mMWithdraw == null) {
            return;
        }
        if (mMWithdraw.isHasBank() == false) {
            showNoBankCardDialog();
            return;
        }

        if (mMWithdraw.getBankcardMap().getBankCardBean1() != null)
            setDataToView(BANK_CARD);
//        else if (mMWithdraw.getBankcardMap().getBankCardBean2()!=null){
//            mBtBitcoinAccount.performClick();
//        }
    }


    private void setDataToView(int i) {
        if (mMWithdraw == null) {
            mMWithdrwaPresenter.getWithdraw();
            return;
        }
        Withdraw.BankcardMapBean.BankCardBean bankBean;
        if (i == BANK_CARD) {
            bankBean = mMWithdraw.getBankcardMap().getBankCardBean1();
            if (bankBean == null) {
                mType = BANK_CARD;
                mTvBindBankCard.setVisibility(View.VISIBLE);
                mTvBindBankCard.setText(R.string.bind_bank_card_hind);
                LlBankCard.setVisibility(View.GONE);
            } else {
                mTvBindBankCard.setVisibility(View.GONE);
                LlBankCard.setVisibility(View.VISIBLE);
                Glide.with(this).
                        load(DataCenter.getInstance().getDomain() + bankBean.getBankUrl())
                        .into(mIvBankIcon);
                mTvBankCard.setText(bankBean.getBankcardMasterName() + "    " + bankBean.getBankcardNumber());
            }
        } else {
            bankBean = mMWithdraw.getBankcardMap().getBankCardBean2();
            if (bankBean == null) {
                mType = BITCOIN_CARD;
                mTvBindBankCard.setVisibility(View.VISIBLE);
                mTvBindBankCard.setText(R.string.add_bitcoin_activity);
                LlBankCard.setVisibility(View.GONE);
            } else {
                mTvBindBankCard.setVisibility(View.GONE);
                LlBankCard.setVisibility(View.VISIBLE);
                mIvBankIcon.setImageResource(R.mipmap.bitcoin);
                mTvBankCard.setText("    " + bankBean.getBankcardNumber());
            }
        }
        mEtWithdrawalsAmount.setHint(mMWithdraw.getCurrencySign() + mMWithdraw.getRank().getWithdrawMinNum() + "-" + mMWithdraw.getCurrencySign() + mMWithdraw.getRank().getWithdrawMaxNum());
        mAuditMapBean = mMWithdraw.getAuditMap();
        if (mAuditMapBean == null) return;
        double conunterFee = 0;
        try {
            conunterFee = Double.parseDouble(mAuditMapBean.getCounterFee());
        } catch (Exception e) {
            Log.i("conunterFee", "conunterFee:" + conunterFee);
        }
        mTvServiceCharge.setText("0.0".equals(Math.abs(conunterFee) + Math.abs(mAuditMapBean.getWithdrawFeeMoney())) ? "免手续费" : mMWithdraw.getCurrencySign() + (Math.abs(conunterFee) + Math.abs(mAuditMapBean.getWithdrawFeeMoney())));
        mTvAdministrativeFee.setText(mMWithdraw.getCurrencySign() + Math.abs(mAuditMapBean.getAdministrativeFee()));
        mTvDiscount.setText(mMWithdraw.getCurrencySign() + Math.abs(mAuditMapBean.getDeductFavorable()));
    }

    private void showNoBankCardDialog() {
        CommonHintDialog mCommonHintDialog = new CommonHintDialog(this, R.style.CommonHintDialog);
        mCommonHintDialog.show();
        mTvBindBankCard.setVisibility(View.VISIBLE);
        LlBankCard.setVisibility(View.GONE);
    }

    @Override
    public void submitWithdraw(Object o) {
        WithdrawSubmitResult withdrawSubmitResult = (WithdrawSubmitResult) o;
        if (withdrawSubmitResult != null) {
            if (withdrawSubmitResult.getCode() == 1001) {
                ActivityUtil.gotoLogin();
                finish();
                return;
            }
            if (withdrawSubmitResult.getCode() == 1305) {
                ToastUtil.showResLong(this, R.string.origin_pwd_error);
                return;
            }

            if (withdrawSubmitResult.getData() != null && !TextUtils.isEmpty(withdrawSubmitResult.getData().getMsg()))
                ToastUtil.showToastLong(this, withdrawSubmitResult.getData().getMsg());
            else
                ToastUtil.showToastLong(this, withdrawSubmitResult.getMessage());

            if (withdrawSubmitResult.getCode() == 1404) {
                startActivity(new Intent(this, ModifySecurityPwdActivity.class));
                finish();
                return;
            }
            mMWithdraw.setToken(withdrawSubmitResult.getData().getToken());
            if (withdrawSubmitResult.getCode() == 0) {
                finish();
            }
        }
    }

    @Override
    public void checkSafePassword(Object o) {
        WithdrawSubmitResult withdrawSubmitResult = (WithdrawSubmitResult) o;
        if (withdrawSubmitResult != null) {
            if (withdrawSubmitResult.getCode() == 1303) {
                ToastUtil.showResLong(this, R.string.origin_pwd_error);
                return;
            }
            if (withdrawSubmitResult.getCode() == 1404) {
                ToastUtil.showToastLong(this, withdrawSubmitResult.getMessage());
                startActivity(new Intent(this, ModifySecurityPwdActivity.class));
                finish();
                return;
            }
            if (withdrawSubmitResult.getCode() == 1001) {
                ActivityUtil.gotoLogin();
                finish();
                return;
            }
            if (withdrawSubmitResult.getCode() == 0) {
                mMCommonInputDialog.dismiss();
            }
        }
    }

    @Override
    public void withdrawFee(Object o) {
        WithdrawFee withdrawFee = (WithdrawFee) o;
        if (withdrawFee != null) {
            if (withdrawFee.getCode() == 1001) {
                ActivityUtil.gotoLogin();
                finish();
                return;
            }
            if (withdrawFee.getData() != null) {
                mActualWithdraw = withdrawFee.getData().getActualWithdraw();
                BigDecimal bg = new BigDecimal(withdrawFee.getData().getCounterFee());
                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                if (f1 == 0) {
                    mTvServiceCharge.setText("免手续费");
                } else
                    mTvServiceCharge.setText("¥" + Math.abs(f1));
                mTvAdministrativeFee.setText("¥" + Math.abs(withdrawFee.getData().getAdministrativeFee()));
                mTvDiscount.setText("¥" + Math.abs(withdrawFee.getData().getDeductFavorable()));
                mTvEndWithdrawalsAmount.setText("¥" + withdrawFee.getData().getActualWithdraw());
            }
        }

    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            String num = mEtWithdrawalsAmount.getText().toString().trim();

            if (!TextUtils.isEmpty(num)) {
                mActualWithdraw = 0;
                mMWithdrwaPresenter.withdrawFee(Double.parseDouble(num));
            }
        }
    };

    @Override
    protected void onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
        mMWithdrwaPresenter.onDestory();
    }
}
