package com.dawoo.lotterybox.view.view.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.view.popu.MoneyUnitPopu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

/**
 * 下单提示
 * Created by benson on 18-2-11.
 */

public class OrderTipDialog implements SeekBar.OnSeekBarChangeListener {

    private BaseDialog mBaseDialog;
    private Context mContext;
    private Unbinder mUnbinder;
    @BindView(R.id.title_name_tv)
    TextView mTitleNameTv;
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
    @BindView(R.id.lable_times_tv)
    TextView mLableTimesTv;
    @BindView(R.id.value_times_tv)
    TextView mValueTimesTv;
    @BindView(R.id.arrw_down_iv)
    ImageView mArrwDownIv;
    @BindView(R.id.money_tv)
    TextView mMoneyTv;
    @BindView(R.id.money_ll)
    LinearLayout mMoneyLl;
    @BindView(R.id.num_tv)
    TextView mNumTv;
    @BindView(R.id.gold_coins_tv)
    TextView mGoldCoinsTv;
    @BindView(R.id.cancle_btn)
    Button mCancleBtn;
    @BindView(R.id.sure_btn)
    Button mSureBtn;
    @BindView(R.id.close_iv)
    ImageView mCloseIv;
    private MoneyUnitPopu mMoneyUnitPopu;
    private BottomNumDialog mBottomNumDialog;

    public OrderTipDialog(@NonNull Context context) {
        mContext = context;
        mBaseDialog = new BaseDialog(context, R.style.CustomDialogStyle);
        mBaseDialog.setContentView(R.layout.dialog_order_tip);
        mUnbinder = ButterKnife.bind(this, mBaseDialog);
        mBaseDialog.setCancelable(false);
        mBaseDialog.setCanceledOnTouchOutside(false);
        mSeekBar.setOnSeekBarChangeListener(this);
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

    public void setRightBtnClickListener(View.OnClickListener listener) {
        if (mBaseDialog != null && listener != null) {
            mSureBtn.setOnClickListener(listener);
        }
    }

    @OnClick({R.id.value_times_tv, R.id.money_ll, R.id.cancle_btn, R.id.close_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.value_times_tv:
                createBottomNum();
                break;
            case R.id.money_ll:
                createPopuWindow();
                break;
            case R.id.cancle_btn:
                dismiss();
                break;
            case R.id.close_iv:
                dismiss();
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mValueRebateTv.setText("" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    void createPopuWindow() {
        if (mMoneyUnitPopu == null) {
            mMoneyUnitPopu = new MoneyUnitPopu(mContext, mMoneyLl, mMoneyTv);
        } else {
            mMoneyUnitPopu.doTogglePopupWindow();
        }
    }


    /**
     * 消除引用
     */
    public void onDestory() {
        if (mMoneyUnitPopu != null) {
            mMoneyUnitPopu.onDestory();
        }

        if (mBottomNumDialog != null) {
            mBottomNumDialog.onDestory();
        }

        if (mBaseDialog != null) {
            mBaseDialog.dismiss();
            mBaseDialog = null;
        }

        if (mUnbinder != null) {
            mContext = null;
            mMoneyUnitPopu = null;
            mUnbinder.unbind();
        }
    }

    void createBottomNum() {
        if (mBottomNumDialog == null) {
            mBottomNumDialog = new BottomNumDialog(mContext, mValueTimesTv);
            mBottomNumDialog.show();
        } else {
            mBottomNumDialog.show();
        }
    }
}
