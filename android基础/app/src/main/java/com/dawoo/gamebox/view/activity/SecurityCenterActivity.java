package com.dawoo.gamebox.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.BankCards;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.mvp.presenter.WithdrawPresenter;
import com.dawoo.gamebox.mvp.view.IAddBankCardView;
import com.dawoo.gamebox.util.SoundUtil;
import com.dawoo.gamebox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心
 */
public class SecurityCenterActivity extends BaseActivity implements IAddBankCardView {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.tip_tv)
    TextView mTipTv;
    @BindView(R.id.modify_login_pwd_rl)
    RelativeLayout mModifyLoginPwdRl;
    @BindView(R.id.modify_security_pwd_rl)
    RelativeLayout mModifySecurityPwdRl;
    @BindView(R.id.lock_screen_gisture_rl)
    RelativeLayout mLockScreenGistureRl;
    @BindView(R.id.bank_card_rl)
    RelativeLayout mBankCardRl;
    @BindView(R.id.btb_account_rl)
    RelativeLayout mBtbAccountRl;
    @BindView(R.id.iv_bank_name)
    ImageView mIvBankName;
    @BindView(R.id.tv_bank_card_number)
    TextView mTvBankCardNumber;

    public static final String TO_BANK_CARD_ACT = "BankcardBean";
    private BankCards.UserBean.BankcardBean mBankcardBean;
    private WithdrawPresenter mWithdrawPresenter;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_security_center);
    }

    @Override
    protected void initViews() {
        String name = getIntent().getStringExtra("name");
        mNameTv.setText(getString(R.string.name_security, name));
    }

    @Override
    protected void initData() {
        mWithdrawPresenter = new WithdrawPresenter(this,this);
        mWithdrawPresenter.getCardType();
    }

    @OnClick({R.id.iv_back,R.id.modify_login_pwd_rl, R.id.modify_security_pwd_rl, R.id.lock_screen_gisture_rl, R.id.bank_card_rl, R.id.btb_account_rl})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().playVoiceOnclick();
        switch (view.getId()) {
            case R.id.modify_login_pwd_rl:
                startActivity(new Intent(this, ModifyLoginPwdActivity.class));
                break;
            case R.id.modify_security_pwd_rl:
                startActivity(new Intent(this, ModifySecurityPwdActivity.class));
                break;
            case R.id.lock_screen_gisture_rl:
                startActivity(new Intent(this, GestureSettingActivity.class));
                break;
            case R.id.bank_card_rl:
                Intent intent = new Intent(this,AddBankCardActivity.class);
                intent.putExtra(TO_BANK_CARD_ACT,mBankcardBean);
                startActivityForResult(intent,AddBankCardActivity.ADD_BANK_CARD);
                break;
            case R.id.btb_account_rl:
                startActivity(new Intent(this, AddBitcoinActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void getCardType(Object o) {
        BankCards bankCards = (BankCards) o;
        if (bankCards != null && bankCards.getUser() != null && bankCards.getUser().getBankcard() != null){
            mBankcardBean = bankCards.getUser().getBankcard();
            Glide.with(this).
                    load(DataCenter.getInstance().getDomain()  + mBankcardBean.getBankUrl())
                    .into(mIvBankName);
            mTvBankCardNumber.setText(mBankcardBean.getBankcardNumber());
        }else {
            mTvBankCardNumber.setText(getString(R.string.not_card));
        }
    }

    @Override
    public void submitBankCard(Object o) {

    }

    @Override
    public void selectedBank(String bankName,int index) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AddBankCardActivity.ADD_BANK_CARD){
            mWithdrawPresenter.getCardType();
        }
    }

    @Override
    protected void onDestroy() {
        mWithdrawPresenter.onDestory();
        super.onDestroy();
    }
}
