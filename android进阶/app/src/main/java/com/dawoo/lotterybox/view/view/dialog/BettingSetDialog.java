package com.dawoo.lotterybox.view.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.dawoo.lotterybox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 投注设置
 * Created by benson on 18-2-11.
 */

public class BettingSetDialog {

    @BindView(R.id.title_name_tv)
    TextView mTitleNameTv;
    @BindView(R.id.unite_tv)
    TextView mUniteTv;
    @BindView(R.id.unite_yuan_tv)
    RadioButton mUniteYuanTv;
    @BindView(R.id.unite_jiao_tv)
    RadioButton mUniteJiaoTv;
    @BindView(R.id.unite_fen_tv)
    RadioButton mUniteFenTv;
    @BindView(R.id.mode_tv)
    TextView mModeTv;
    @BindView(R.id.mode_one_yuan_tv)
    RadioButton mModeOneYuanTv;
    @BindView(R.id.mode_two_yuan_tv)
    RadioButton mModeTwoYuanTv;
    @BindView(R.id.lable_times_tv)
    TextView mLableTimesTv;
    @BindView(R.id.value_times_tv)
    TextView mValueTimesTv;
    @BindView(R.id.lable_rate_tv)
    TextView mLableRateTv;
    @BindView(R.id.value_rate_tv)
    TextView mValueRateTv;
    @BindView(R.id.lable_rebate_tv)
    TextView mLableRebateTv;
    @BindView(R.id.value_rebate_tv)
    TextView mValueRebateTv;
    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.switch_order_tip)
    SwitchCompat mSwitchOrderTip;
    @BindView(R.id.cancle_btn)
    Button mCancleBtn;
    @BindView(R.id.sure_btn)
    Button mSureBtn;
    @BindView(R.id.close_iv)
    ImageView mCloseIv;
    private BaseDialog mBaseDialog;
    private Unbinder mUnBinder;

    public BettingSetDialog(@NonNull Context context) {
        mBaseDialog = new BaseDialog(context, R.style.CustomDialogStyle);
        mBaseDialog.setContentView(R.layout.dialog_betting_set);
        mUnBinder = ButterKnife.bind(this, mBaseDialog);
        mBaseDialog.setCancelable(true);
        mBaseDialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        if (mBaseDialog != null && !mBaseDialog.isShowing()) {
            mBaseDialog.show();
        }
    }

    public void dismiss() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }


    public void onDestory() {
        if (mBaseDialog != null) {
            mBaseDialog.dismiss();
            mBaseDialog = null;
        }

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @OnClick({R.id.cancle_btn, R.id.sure_btn, R.id.close_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancle_btn:
                break;
            case R.id.sure_btn:
                dismiss();
                break;
            case R.id.close_iv:
                dismiss();
                break;
        }
    }
}
