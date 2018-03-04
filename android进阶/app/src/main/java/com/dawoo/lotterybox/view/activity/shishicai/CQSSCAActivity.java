package com.dawoo.lotterybox.view.activity.shishicai;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.SSCAdapter.GamePlayAdapter;
import com.dawoo.lotterybox.bean.AwardResultBean;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.ExpectDataBean;
import com.dawoo.lotterybox.bean.lottery.LotteryEnum;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;
import com.dawoo.lotterybox.mvp.presenter.CQSSCPresenter;
import com.dawoo.lotterybox.mvp.view.ICQSSCView;
import com.dawoo.lotterybox.util.lottery.initdata.CQSSCGFWanFaUtil;
import com.dawoo.lotterybox.view.activity.BaseActivity;
import com.dawoo.lotterybox.view.view.CountDownTimerUtils;
import com.dawoo.lotterybox.view.view.HeaderSSCBaseView;
import com.dawoo.lotterybox.view.view.PlayTypePopupWindow;
import com.dawoo.lotterybox.view.view.TimeTextView;
import com.dawoo.lotterybox.view.view.dialog.BettingSetDialog;
import com.dawoo.lotterybox.view.view.dialog.HowToPlayDialog;
import com.dawoo.lotterybox.view.view.dialog.OrderTipDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 重庆时时彩A盘
 * Created by b on 18-2-9.
 */

public class CQSSCAActivity extends BaseActivity implements ICQSSCView, HeaderSSCBaseView.HeadPopupItemClick {

    @BindView(R.id.lLayout_ssc_bottom_second)
    LinearLayout mLLayoutSscBottomSecond;
    @BindView(R.id.head_view)
    HeaderSSCBaseView mHeadView;
    @BindView(R.id.rlv_lottery_record)
    RecyclerView mRlvLotteryRecord;
    @BindView(R.id.rlv_game_play)
    RecyclerView mRlvGamePlay;
    @BindView(R.id.tv_now_stage)
    TextView mTvNowStage;
    @BindView(R.id.tv_now_stage_state)
    TextView mTvNowStageState;
    @BindView(R.id.ttv_timer)
    TimeTextView mTimeTextView;
    @BindView(R.id.tv_periods)
    TextView mTvPeriods;
    @BindView(R.id.iv_red_result_1)
    ImageView mIvRedResult1;
    @BindView(R.id.iv_red_result_2)
    ImageView mIvRedResult2;
    @BindView(R.id.iv_red_result_3)
    ImageView mIvRedResult3;
    @BindView(R.id.iv_red_result_4)
    ImageView mIvRedResult4;
    @BindView(R.id.iv_red_result_5)
    ImageView mIvRedResult5;
    @BindView(R.id.tv_computer_select)
    TextView mTvComputerSelect;
    @BindView(R.id.tv_setting_special)
    TextView mTvSettingSpecial;
    @BindView(R.id.et_multiple)
    EditText mEtMultiple;
    @BindView(R.id.tv_place_order)
    TextView mTvPlaceOrder;


    private BottomSheetBehavior mBottomSheetBehavior;
    private AwardResultsQuickAdapter mQuickAdapter;
    private PlayTypePopupWindow mPlayTypePopupWindow;
    public PlayTypeBean.PlayBean mPlayTypeBean;//当前的玩法对象

    private CountDownTimerUtils mCountDownTimerUtils;
    private OrderTipDialog mOrderTipDialog;
    private GamePlayAdapter mGamePlayAdapter;
    private CQSSCPresenter mCQSSCPrecenter;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_base_ssc);
    }

    @Override
    protected void initViews() {
        mHeadView.setPopupItemClick(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(mLLayoutSscBottomSecond);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mQuickAdapter = new AwardResultsQuickAdapter(R.layout.item_ssc_lottery_result);
        mQuickAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));
        View headView = getLayoutInflater().inflate(R.layout.layout_ssc_lottery_record_head, (ViewGroup) mRlvLotteryRecord.getParent(), false);
        View footerView = getLayoutInflater().inflate(R.layout.layout_ssc_lottery_record_footer, (ViewGroup) mRlvLotteryRecord.getParent(), false);
        mQuickAdapter.addHeaderView(headView);
        mQuickAdapter.addFooterView(footerView);
        mRlvLotteryRecord.setLayoutManager(new LinearLayoutManager(this));
        mRlvLotteryRecord.setAdapter(mQuickAdapter);
        mRlvGamePlay.setLayoutManager(new LinearLayoutManager(this));
        mPlayTypeBean = CQSSCGFWanFaUtil.initFirstTypeData();
        mHeadView.setPlayMethodLeftText(mPlayTypeBean.getPlayTypeName());
        mGamePlayAdapter = new GamePlayAdapter(this, mPlayTypeBean);
        mRlvGamePlay.setAdapter(mGamePlayAdapter);
        initListeners();
        initPlayTypePopWindow();

    }


    @Override
    protected void initData() {
        mCQSSCPrecenter = new CQSSCPresenter(this, this);
        mCQSSCPrecenter.getAwardResults();
        mCQSSCPrecenter.getExpectData();
        mCQSSCPrecenter.getLotteryOdd(mPlayTypeBean.getPlayTypeCode());
        mCountDownTimerUtils = CountDownTimerUtils.getCountDownTimer();

        initLayoutChildBean();
    }

    private void initLayoutChildBean() {
        for (int i = 0; i < mPlayTypeBean.getLayoutBeans().size(); i++) {
            if (mPlayTypeBean.getLayoutBeans().get(i).getChildLayoutBeans() == null || mPlayTypeBean.getLayoutBeans().get(i).getChildLayoutBeans().size() == 0) {  //初始化子布局子item数据
                List<PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean> mChildLayoutBeans = new ArrayList<>();
                for (int j = 0; j < mPlayTypeBean.getLayoutBeans().get(i).getChildItemCount(); j++) {
                    PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean childLayoutBean = new PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean();
                    childLayoutBean.setNumber(String.valueOf(j + mPlayTypeBean.getLayoutBeans().get(i).getStartNumber()));
                    mChildLayoutBeans.add(childLayoutBean);
                }
                mPlayTypeBean.getLayoutBeans().get(i).setChildLayoutBeans(mChildLayoutBeans);
            }
        }
    }

    private void initPlayTypePopWindow() {
        mPlayTypePopupWindow = new PlayTypePopupWindow(this, LotteryEnum.CQSSC.getType());
        mPlayTypePopupWindow.setOnClickPlayType(new PlayTypePopupWindow.OnClickPlayType() {
            @Override
            public void callBackTypeName(PlayTypeBean.PlayBean playTypeBean) {
                mPlayTypeBean = playTypeBean;
                initLayoutChildBean();
                mGamePlayAdapter.setNewData(mPlayTypeBean);
                mHeadView.setPlayMethodLeftText(playTypeBean.getPlayTypeName());
            }
        });
    }

    private void initListeners() {
        // Capturing the callbacks for bottom sheet
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
    }

    @OnClick({R.id.tv_play_method, R.id.tv_place_order, R.id.tv_setting_special, R.id.tv_computer_select})
    public void onBaseClick(View v) {
        switch (v.getId()) {
            case R.id.tv_play_method:
                mPlayTypePopupWindow.doTogglePopupWindow(v, mPlayTypeBean);
                break;

            case R.id.tv_place_order:
                mOrderTipDialog = new OrderTipDialog(this);
                mOrderTipDialog.setRightBtnClickListener(mOrderRightBtn);
                mOrderTipDialog.show();
                break;

            case R.id.tv_setting_special:
                BettingSetDialog mBettingSetDialog = new BettingSetDialog(this);
                mBettingSetDialog.show();
                break;

            case R.id.tv_computer_select:
                if (mPlayTypeBean.getLayoutBeans().get(0).getLayoutType() == 0) {
                    Random random = new Random();
                    for (int i = 0; i < mPlayTypeBean.getLayoutBeans().size(); i++) {
                        int position = random.nextInt(mPlayTypeBean.getLayoutBeans().get(i).getChildItemCount());
                        for (int j = 0; j < mPlayTypeBean.getLayoutBeans().get(i).getChildItemCount(); j++) {
                            if (j == position)
                                mPlayTypeBean.getLayoutBeans().get(i).getChildLayoutBeans().get(j).setSelected(true);
                            else
                                mPlayTypeBean.getLayoutBeans().get(i).getChildLayoutBeans().get(j).setSelected(false);
                        }
                        mGamePlayAdapter.notifyItemChanged(i + 1);
                    }
                } else {
                    ToastUtil.showToastLong(this, "不支持机选");
                }

                break;
        }
    }

    View.OnClickListener mOrderRightBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(CQSSCAActivity.this, CQSSCAShoppingCartActivity.class));
            mOrderTipDialog.dismiss();
        }
    };

    //倒计时期间回调
    CountDownTimerUtils.TickDelegate mTickDelegate = new CountDownTimerUtils.TickDelegate() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("mm:ss");

        @Override
        public void onTick(long pMillisUntilFinished) {
            String str1 = sdf1.format(pMillisUntilFinished);
            mTimeTextView.setText(str1);
        }
    };

    //距离开盘时间 倒计时结束回调
    CountDownTimerUtils.FinishDelegate mLeftOpenTimeFinishDelegate = new CountDownTimerUtils.FinishDelegate() {
        @Override
        public void onFinish() {
            mTimeTextView.setText("00:00");
            mIvRedResult1.setImageResource(R.drawable.anim_blue_ball_result);
            mIvRedResult2.setImageResource(R.drawable.anim_blue_ball_result);
            mIvRedResult3.setImageResource(R.drawable.anim_blue_ball_result);
            mIvRedResult4.setImageResource(R.drawable.anim_blue_ball_result);
            mIvRedResult5.setImageResource(R.drawable.anim_blue_ball_result);
            ((AnimationDrawable) mIvRedResult1.getDrawable()).start();
            ((AnimationDrawable) mIvRedResult2.getDrawable()).start();
            ((AnimationDrawable) mIvRedResult3.getDrawable()).start();
            ((AnimationDrawable) mIvRedResult4.getDrawable()).start();
            ((AnimationDrawable) mIvRedResult5.getDrawable()).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCQSSCPrecenter.getAwardResults();
                            }
                        });
                    }
                }
            }).start();
            mCQSSCPrecenter.getExpectData();
        }
    };

    //距离封盘时间 倒计时结束回调
    CountDownTimerUtils.FinishDelegate mLeftTimeFinishDelegate = new CountDownTimerUtils.FinishDelegate() {
        @Override
        public void onFinish() {
            mTimeTextView.setText("00:00");
            mIvRedResult1.setImageResource(R.mipmap.red_ball_result_);
            mIvRedResult2.setImageResource(R.mipmap.red_ball_result_);
            mIvRedResult3.setImageResource(R.mipmap.red_ball_result_);
            mIvRedResult4.setImageResource(R.mipmap.red_ball_result_);
            mIvRedResult5.setImageResource(R.mipmap.red_ball_result_);
            mCQSSCPrecenter.getExpectData();
        }
    };

    @Override
    public void onPopupBalance() {

    }

    @Override
    public void onRecentAwards() {

    }

    @Override
    public void onMyBet() {

    }

    @Override
    public void onMyChaseNumber() {

    }

    @Override
    public void onTrendChart() {

    }

    @Override
    public void onLztPop() {
        startActivity(new Intent(this, CQSSCWayPaperActivity.class));
    }

    //show游戏说明dialog
    public void showHowPlayDialog() {
        HowToPlayDialog howToPlayDialog = new HowToPlayDialog(this);
        howToPlayDialog.setPlayWay(mPlayTypeBean.getPlayTypeExplain());
        howToPlayDialog.setLotteryNOteNumber(mPlayTypeBean.getSample());
        howToPlayDialog.show();
    }

    @Override
    public void onWfPop() {
        startActivity(new Intent(this, PlayExplainActivity.class));
    }

    @Override
    public void onAwardResults(List<CQSSCAwardResultBean> awardResultBeans) {
        if (awardResultBeans != null) {
            mQuickAdapter.setNewData(awardResultBeans);
            setResultToBall(awardResultBeans.get(0).getOpenCode());
            mTvPeriods.setText(getString(R.string.cq_a_which_periods, awardResultBeans.get(0).getExpect()));
        }
    }

    @Override
    public void onAwardResultsAndNoOpen(List<AwardResultBean> awardResultBeans) {
        if (awardResultBeans != null) {
            mQuickAdapter.setNewData(awardResultBeans);
        }
    }

    @Override
    public void onExpectData(ExpectDataBean expectDataBean) {
        if (expectDataBean != null) {
            mTvNowStage.setText(getString(R.string.which_periods, expectDataBean.getExpect()));
            if (expectDataBean.getLeftOpenTime() > 0) {
                mCountDownTimerUtils.cancel();
                mCountDownTimerUtils.setMillisInFuture(expectDataBean.getLeftOpenTime() * 1000)
                        .setCountDownInterval(1000)
                        .setFinishDelegate(mLeftOpenTimeFinishDelegate)
                        .setTickDelegate(mTickDelegate)
                        .start();
                mTvNowStageState.setText(R.string.not_bet);
            } else {
                mCountDownTimerUtils.cancel();
                mCountDownTimerUtils.setMillisInFuture(expectDataBean.getLeftTime() * 1000)
                        .setCountDownInterval(1000)
                        .setFinishDelegate(mLeftTimeFinishDelegate)
                        .setTickDelegate(mTickDelegate)
                        .start();
                mTvNowStageState.setText(R.string.stop);
            }

        }
    }

    @Override
    public void onLotteryOdd(Object o) {

    }

    public void setResultToBall(String string) {
        String[] numbers = string.split(",");
        mIvRedResult1.setImageResource(getImageResourceFormNumber(numbers[0]));
        mIvRedResult2.setImageResource(getImageResourceFormNumber(numbers[1]));
        mIvRedResult3.setImageResource(getImageResourceFormNumber(numbers[2]));
        mIvRedResult4.setImageResource(getImageResourceFormNumber(numbers[3]));
        mIvRedResult5.setImageResource(getImageResourceFormNumber(numbers[4]));

    }

    public int getImageResourceFormNumber(String number) {
        int imageResource = 0;
        if ("0".equals(number))
            imageResource = R.mipmap.red_ball_result_0;
        else if ("1".equals(number))
            imageResource = R.mipmap.red_ball_result_1;
        else if ("2".equals(number))
            imageResource = R.mipmap.red_ball_result_2;
        else if ("3".equals(number))
            imageResource = R.mipmap.red_ball_result_3;
        else if ("4".equals(number))
            imageResource = R.mipmap.red_ball_result_4;
        else if ("5".equals(number))
            imageResource = R.mipmap.red_ball_result_5;
        else if ("6".equals(number))
            imageResource = R.mipmap.red_ball_result_6;
        else if ("7".equals(number))
            imageResource = R.mipmap.red_ball_result_7;
        else if ("8".equals(number))
            imageResource = R.mipmap.red_ball_result_8;
        else if ("9".equals(number))
            imageResource = R.mipmap.red_ball_result_9;

        return imageResource;
    }

    class AwardResultsQuickAdapter extends BaseQuickAdapter {
        public AwardResultsQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            CQSSCAwardResultBean cqsscAwardResultBean = (CQSSCAwardResultBean) item;
            String openCode = cqsscAwardResultBean.getOpenCode().replace(",", "");
            helper.setText(R.id.tv_no, cqsscAwardResultBean.getExpect());
            helper.setText(R.id.tv_award_results, openCode);
        }
    }

    @Override
    protected void onDestroy() {
        mPlayTypePopupWindow.dissMissPopWindow();
        mCountDownTimerUtils.cancel();
        mHeadView.dismissPop();
        super.onDestroy();
    }
}
