package com.dawoo.gamebox.view.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.FAB;
import com.dawoo.gamebox.bean.GetPacket;
import com.dawoo.gamebox.bean.HongbaoCount;
import com.dawoo.gamebox.bean.HongbaoTemp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by benson on 18-1-1.
 */

public class HongBaoDialog {

    private static final int START = 0;
    private static final int SUCCESS = 1;
    private static final int FAILE = 2;
    private static final int REGULAR = 3;
    private static int STATUS = START; // 记录当前状态
    private static int STATUS_BEFORE = START; // 记录前一个状态

    private final HongbaoCount mHongbaoCount;
    private final View.OnClickListener mListener;
    private final Context mContext;
    private final FAB.ActivityBean mActivityBean;
    @BindView(R.id.open_close_iv)
    ImageView mOpenCloseIv;
    @BindView(R.id.next_tv)
    TextView mNextTv;
    @BindView(R.id.time_tv)
    TextView mTimeTv;
    @BindView(R.id.count_tv)
    TextView mCountTv;
    @BindView(R.id.count0_tv)
    TextView mCount0Tv;
    @BindView(R.id.open_regular_fl)
    FrameLayout mOpenRegularFl;
    @BindView(R.id.open_open_fl)
    FrameLayout mOpenOpenFl;
    @BindView(R.id.open_ll)
    RelativeLayout mOpenLl;
    @BindView(R.id.regural_close_iv)
    ImageView mReguralCloseIv;
    @BindView(R.id.regular_tv)
    TextView mRegularTv;
    @BindView(R.id.regular_confirm_fl)
    FrameLayout mRegularConfirmFl;
    @BindView(R.id.regural_ll)
    RelativeLayout mReguralLl;
    @BindView(R.id.success_close_iv)
    ImageView mSuccessCloseIv;
    @BindView(R.id.success_tip_tv)
    TextView mSuccessTipTv;
    @BindView(R.id.remain_count_tv)
    TextView mRemainCountTv;
    @BindView(R.id.success_open_fl)
    FrameLayout mSuccessOpenFl;
    @BindView(R.id.success_regular_fl)
    FrameLayout mSuccessRegularFl;
    @BindView(R.id.success_ll)
    RelativeLayout mSuccessLl;
    @BindView(R.id.faile_close_iv)
    ImageView mFaileCloseIv;
    @BindView(R.id.faile_tip_tv)
    TextView mFaileTipTv;
    @BindView(R.id.faile_remain_count_tv)
    TextView mFailRemianCountTv;
    @BindView(R.id.faile_open_fl)
    FrameLayout mFaileOpenFl;
    @BindView(R.id.faile_regular_fl)
    FrameLayout mFaileRegularFl;
    @BindView(R.id.faile_ll)
    RelativeLayout mFaileLl;
    @BindView(R.id.open_hongbao_iv)
    ImageView mOpenHongbaoIv;
    @BindView(R.id.success_hongbao_iv)
    ImageView mSuccessHongbaoIv;
    @BindView(R.id.faile_hongbao_iv)
    ImageView mFaileHongBaoIv;
    private Dialog mDialog;


    public HongBaoDialog(@NonNull Context context, HongbaoCount hongbaoCount, FAB.ActivityBean activityBean, View.OnClickListener listener) {
        mContext = context;
        mHongbaoCount = hongbaoCount;
        mActivityBean = activityBean;
        mListener = listener;

        mDialog = new Dialog(context, R.style.CustomDialogStyle);
        mDialog.setContentView(R.layout.hongbao_dialog);
        mDialog.setCancelable(false);
        ButterKnife.bind(this, mDialog);
        setLayoutParams();
        initData();


        mDialog.show();
    }

    private void setLayoutParams() {
        Window win = mDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    public void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    void initData() {
        STATUS = START;
        STATUS_BEFORE = START;
        // 隐藏红包规则
        mOpenRegularFl.setVisibility(View.GONE);
        mSuccessRegularFl.setVisibility(View.GONE);
        mFaileRegularFl.setVisibility(View.GONE);


        if (mHongbaoCount == null) {
            return;
        }
        // 初始化红包数据
        mNextTv.setText(mContext.getString(R.string.next_time));
        mTimeTv.setText(mHongbaoCount.getNextLotteryTime());


        if (mHongbaoCount != null) {
            if ("true".equals(mHongbaoCount.getIsEnd())) {

                mCountTv.setText(mContext.getResources().getString(R.string.hongbao_end));
                mOpenOpenFl.setEnabled(false);
                mOpenOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);

                mNextTv.setVisibility(View.VISIBLE);
                mTimeTv.setVisibility(View.VISIBLE);
                mCountTv.setVisibility(View.GONE);

                if (TextUtils.isEmpty(mHongbaoCount.getNextLotteryTime())) {
                    mNextTv.setVisibility(View.GONE);
                    mTimeTv.setVisibility(View.GONE);
                    mCountTv.setVisibility(View.VISIBLE);
                }
            } else if (-1 == mHongbaoCount.getDrawTimes()) {

                mCountTv.setText(mContext.getResources().getString(R.string.hongbao_end));
                mOpenOpenFl.setEnabled(false);
                mOpenOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                mNextTv.setVisibility(View.VISIBLE);
                mTimeTv.setVisibility(View.VISIBLE);
                mCountTv.setVisibility(View.GONE);
                if (TextUtils.isEmpty(mHongbaoCount.getNextLotteryTime())) {
                    mNextTv.setVisibility(View.GONE);
                    mTimeTv.setVisibility(View.GONE);
                    mCountTv.setVisibility(View.VISIBLE);
                }


            } else if (-5 == mHongbaoCount.getDrawTimes()) {

                mCountTv.setText(mContext.getResources().getString(R.string.haobao_qiangguang));
                mOpenOpenFl.setEnabled(false);
                mOpenOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                mNextTv.setVisibility(View.VISIBLE);
                mTimeTv.setVisibility(View.VISIBLE);
                mCountTv.setVisibility(View.GONE);

                if (TextUtils.isEmpty(mHongbaoCount.getNextLotteryTime())) {
                    mNextTv.setVisibility(View.GONE);
                    mTimeTv.setVisibility(View.GONE);
                    mCountTv.setVisibility(View.VISIBLE);
                }
            } else if (0 == mHongbaoCount.getDrawTimes()) {

                mCount0Tv.setText(mContext.getResources().getString(R.string.haobao_times_0));
                mOpenOpenFl.setEnabled(false);
                mOpenOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                mNextTv.setVisibility(View.VISIBLE);
                mTimeTv.setVisibility(View.VISIBLE);
                mCount0Tv.setVisibility(View.VISIBLE);
                mCountTv.setVisibility(View.GONE);

                if (TextUtils.isEmpty(mHongbaoCount.getNextLotteryTime())) {
                    mNextTv.setVisibility(View.GONE);
                    mTimeTv.setVisibility(View.GONE);
                    mCountTv.setVisibility(View.VISIBLE);
                }
            } else if (mListener != null && mHongbaoCount != null && mActivityBean != null) {
                mCountTv.setText(countSpan(mContext.getResources().getString(R.string.total_times, mHongbaoCount.getDrawTimes()), mHongbaoCount.getDrawTimes()));
                if (0 == mHongbaoCount.getDrawTimes()) {
                    mOpenOpenFl.setEnabled(false);
                    mOpenOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                }

                HongbaoTemp temp = new HongbaoTemp(mActivityBean.getActivityId(), mHongbaoCount.getToken());
                mOpenOpenFl.setTag(R.id.list_item_data_id, temp);

                mOpenOpenFl.setOnClickListener(mListener);
                mFaileOpenFl.setOnClickListener(mListener);
                mSuccessOpenFl.setOnClickListener(mListener);
            }
        }

        if (mActivityBean != null && mActivityBean.getDescription() != null) {
            mRegularTv.setText(Html.fromHtml(mActivityBean.getDescription()));
        }
    }

    public void setGetPacketResult(GetPacket getPacket) {
        if (getPacket == null) {
            return;
        }

        // -5：已抢完 -4：条件不满足 -3:红包活动结束 -2：抽奖异常 -1：已抽完 0：已结束
        int gameNum = getPacket.getGameNum();
        mHongbaoCount.setToken(getPacket.getToken());
        HongbaoTemp temp = new HongbaoTemp(mActivityBean.getActivityId(), mHongbaoCount.getToken());
        mFaileOpenFl.setTag(R.id.list_item_data_id, temp);
        switch (gameNum) {
            case -5:
                openHongbao(FAILE);
                mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_snatched_out));
                mFailRemianCountTv.setText("");
                mFaileOpenFl.setEnabled(false);
                mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                break;
            case -4:
                openHongbao(FAILE);
                mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_condition_not_satisfied));
                mFailRemianCountTv.setText("");
                mFaileOpenFl.setEnabled(false);
                mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                break;
            case -3:
                openHongbao(FAILE);
                mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_activity_end));
                mFailRemianCountTv.setText("");
                mFaileOpenFl.setEnabled(false);
                mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                break;
            case -2:
                openHongbao(FAILE);
                mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_excepttion));
                mFailRemianCountTv.setText("");
                mFaileOpenFl.setEnabled(false);
                mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                break;
            case -1:
                openHongbao(FAILE);
                mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_chou_wan));
                mFailRemianCountTv.setText("");
                mFaileOpenFl.setEnabled(false);
                mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                break;
            case 0:
                openHongbao(FAILE);
                mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_have_end));
                mFailRemianCountTv.setText("");
                mFaileOpenFl.setEnabled(false);
                mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                break;
            default:
                if (!"0".equals(getPacket.getAward()) && !"0.0".equals(getPacket.getAward()) && mActivityBean != null) {
                    openHongbao(SUCCESS);
                    mSuccessTipTv.setText(mContext.getResources().getString(R.string.hongbao_get_partten, getPacket.getAward()));
                    mHongbaoCount.setToken(getPacket.getToken());
                    HongbaoTemp temp1 = new HongbaoTemp(mActivityBean.getActivityId(), mHongbaoCount.getToken());
                    mSuccessOpenFl.setTag(R.id.list_item_data_id, temp1);
                    mRemainCountTv.setText(countSpan(mContext.getResources().getString(R.string.total_times, getPacket.getGameNum()), getPacket.getGameNum()));
                    if (0 == getPacket.getGameNum()) {
                        mSuccessOpenFl.setEnabled(false);
                        mSuccessOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                    }
                } else {
                    openHongbao(FAILE);
                    mFaileTipTv.setText(mContext.getResources().getString(R.string.hongbao_faile));
                    mFailRemianCountTv.setText(countSpan(mContext.getResources().getString(R.string.total_times, getPacket.getGameNum()), getPacket.getGameNum()));
                    if (0 == getPacket.getGameNum()) {
                        mFaileOpenFl.setEnabled(false);
                        mFaileOpenFl.setBackgroundResource(R.mipmap.hongbao_finish);
                    }

                }
        }
    }


    @OnClick({R.id.open_close_iv, R.id.open_regular_fl, R.id.regural_close_iv, R.id.regular_confirm_fl, R.id.success_close_iv, R.id.success_regular_fl, R.id.faile_close_iv, R.id.faile_regular_fl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.open_close_iv:
                dismissDialog();
                break;
            case R.id.open_regular_fl:
                // 进入红包规则
                STATUS_BEFORE = STATUS;
                switchHBStatus(REGULAR);
                break;
            case R.id.regural_close_iv:
                dismissDialog();
                break;
            case R.id.regular_confirm_fl:
                switchHBStatus(STATUS_BEFORE);
                break;
            case R.id.success_close_iv:
                dismissDialog();
                break;
//            case R.id.success_open_fl:
//                switchHBStatus(START);
//                break;
            case R.id.success_regular_fl:
                // 进入红包规则
                STATUS_BEFORE = STATUS;
                switchHBStatus(REGULAR);
                break;
            case R.id.faile_close_iv:
                dismissDialog();
                break;
            //  case R.id.faile_open_fl:
            //      switchHBStatus(FAILE);
            //      break;
            case R.id.faile_regular_fl:
                // 进入红包规则
                STATUS_BEFORE = STATUS;
                switchHBStatus(REGULAR);
                break;
        }
    }

    public void openHongbao(int status) {
        STATUS = status;
        switchHBStatus(status);
    }


    /**
     * 切换红包状态
     */
    void switchHBStatus(int i) {
        switch (i) {
            case START:
                mOpenLl.setVisibility(View.VISIBLE);
                mSuccessLl.setVisibility(View.GONE);
                mFaileLl.setVisibility(View.GONE);
                mReguralLl.setVisibility(View.GONE);
                break;
            case SUCCESS:
                mOpenLl.setVisibility(View.GONE);
                mSuccessLl.setVisibility(View.VISIBLE);
                mFaileLl.setVisibility(View.GONE);
                mReguralLl.setVisibility(View.GONE);
                break;
            case FAILE:
                mOpenLl.setVisibility(View.GONE);
                mSuccessLl.setVisibility(View.GONE);
                mFaileLl.setVisibility(View.VISIBLE);
                mReguralLl.setVisibility(View.GONE);
                break;
            case REGULAR:
                mOpenLl.setVisibility(View.GONE);
                mSuccessLl.setVisibility(View.GONE);
                mFaileLl.setVisibility(View.GONE);
                mReguralLl.setVisibility(View.VISIBLE);
                break;
        }
    }

    SpannableString countSpan(String countString, int count) {

        SpannableString sp = new SpannableString(countString);
        if (count < 10) {
            sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_text_hongbao)), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (count < 100) {
            sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_text_hongbao)), 4, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (count < 1000) {
            sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_text_hongbao)), 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_text_hongbao)), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }
}
