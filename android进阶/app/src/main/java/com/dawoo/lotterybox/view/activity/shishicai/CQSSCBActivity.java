package com.dawoo.lotterybox.view.activity.shishicai;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.AwardResultBean;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.ExpectDataBean;
import com.dawoo.lotterybox.bean.playType.PlayDetailBean;
import com.dawoo.lotterybox.bean.playType.SSCBPlayChhooseBean;
import com.dawoo.lotterybox.mvp.presenter.CQSSCPresenter;
import com.dawoo.lotterybox.mvp.view.ICQSSCView;
import com.dawoo.lotterybox.util.lottery.initdata.CQSSCBDataUtils;
import com.dawoo.lotterybox.view.activity.BaseActivity;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_KD_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_LH_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_RZDW_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_SM_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_SZDW_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_SZP_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_YZDW_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_YZZH_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_ZX3_Fragment;
import com.dawoo.lotterybox.view.fragment.cq_ssc_b.SSC_ZX6_Fragment;
import com.dawoo.lotterybox.view.view.CountDownTimerUtils;
import com.dawoo.lotterybox.view.view.SscBFragmentManager;
import com.dawoo.lotterybox.view.view.TimeTextView;
import com.dawoo.lotterybox.view.view.bottomSheet.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by archar on 18-2-26.
 */

public class CQSSCBActivity extends BaseActivity implements ICQSSCView {
    @BindView(R.id.left_btn)
    FrameLayout mLeftBtn;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.tv_small_helper)
    TextView mTvSmallHelper;
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
    @BindView(R.id.tv_xdh)
    TextView mTvXdh;
    @BindView(R.id.tv_now_stage)
    TextView mTvNowStage;
    @BindView(R.id.tv_now_stage_state)
    TextView mTvNowStageState;
    @BindView(R.id.ttv_timer)
    TimeTextView mTimeTextView;
    @BindView(R.id.rlv_lottery_record)
    RecyclerView mRlvLotteryRecord;
    @BindView(R.id.rlv_game_parentTitle)
    RecyclerView rlv_game_parentTitle;
    @BindView(R.id.lLayout_ssc_bottom_second)
    RelativeLayout mLLayoutSscBottomSecond;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.hot_and_cold)
    CheckBox mHotAndCold;
    @BindView(R.id.cb_omit)
    CheckBox mCbOmit;
    @BindView(R.id.fragment_content)
    FrameLayout mFragmentContent;
    @BindView(R.id.rl_head)
    RelativeLayout mRlHead;
    @BindView(R.id.iv_random)
    ImageView mIvRandom;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingLayout;
    @BindView(R.id.rl_touzhu)
    RelativeLayout mRlTouzhu;

    private CQSSCPresenter mCQSSCPrecenter;
    private FragmentManager mFragmentManager;
    private AwardResultsQuickAdapter mQuickAdapter;//中间开奖记录适配器
    private CountDownTimerUtils mCountDownTimerUtils;//倒计时

    private PlayTypeChooseQuickAdapter mPlayTypeChooseQuickAdapter;//导航选择适配器
    private List<SSCBPlayChhooseBean> mSSCBPlayChhooseBeans = new ArrayList<>();//导航数据源

    private List<PlayDetailBean> mPlayDetailBeans = new ArrayList<>();//任何玩法所选好的对象

    private Handler mHandler = new Handler();

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_ssc_b);
    }

    @Override
    protected void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mQuickAdapter = new AwardResultsQuickAdapter(R.layout.item_ssc_b_lottery_result);
        mQuickAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));
        mRlvLotteryRecord.setLayoutManager(new LinearLayoutManager(this));
        mRlvLotteryRecord.setAdapter(mQuickAdapter);
        mPlayTypeChooseQuickAdapter = new PlayTypeChooseQuickAdapter(R.layout.choose_play_type_item_layout);
        rlv_game_parentTitle.setLayoutManager(new LinearLayoutManager(this));
        rlv_game_parentTitle.setAdapter(mPlayTypeChooseQuickAdapter);
        rlv_game_parentTitle.getParent().requestDisallowInterceptTouchEvent(true);
        initListeners();
    }

    private void initListeners() {
        mSlidingLayout.setTouchEnabled(false);
        mRlHead.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeSlidingLayOutWithMove();
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        initPlayTypeData();
        mCQSSCPrecenter = new CQSSCPresenter(this, this);
        mCQSSCPrecenter.getAwardResults();
        mCQSSCPrecenter.getExpectData();
        //   mCQSSCPrecenter.getLotteryOdd(mPlayTypeBean.getPlayTypeCode());
        mCountDownTimerUtils = CountDownTimerUtils.getCountDownTimer();
    }


    private void initPlayTypeData() {
        mSSCBPlayChhooseBeans = CQSSCBDataUtils.initChooseData();
        mPlayTypeChooseQuickAdapter.setNewData(mSSCBPlayChhooseBeans);
        SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_SZP_Fragment.class);
    }

    @Override
    public void onAwardResults(List<CQSSCAwardResultBean> awardResultBeans) {
        if (awardResultBeans != null) {
            mQuickAdapter.setNewData(awardResultBeans);
            setResultToBall(awardResultBeans.get(0).getOpenCode());
            mTvPeriods.setText(getString(R.string.cq_b_which_periods, awardResultBeans.get(0).getExpect()));
        }
    }

    @Override
    public void onAwardResultsAndNoOpen(List<AwardResultBean> awardResultBeans) {

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
            mCQSSCPrecenter.getAwardResults();
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
            helper.setText(R.id.tv_status, "小 大 龙 虎");
        }
    }

    class PlayTypeChooseQuickAdapter extends BaseQuickAdapter {
        public PlayTypeChooseQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            SSCBPlayChhooseBean sscbPlayChhooseBean = (SSCBPlayChhooseBean) item;
            helper.setText(R.id.tv_sscb_palyType, sscbPlayChhooseBean.getBetName());
            if (sscbPlayChhooseBean.isSelected()) {
                helper.getView(R.id.tv_sscb_palyType).setSelected(true);
            } else {
                helper.getView(R.id.tv_sscb_palyType).setSelected(false);
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CQSSCBDataUtils.refreshChooseData(mSSCBPlayChhooseBeans, helper.getAdapterPosition());
                    notifyDataSetChanged();
                    offTouZhuView();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showFragment(sscbPlayChhooseBean);
                        }
                    });
                }
            });
        }
    }

    //子fragment 按了某个选项
    public void onTouZhuView(PlayDetailBean bean) {
        if (mPlayDetailBeans.contains(bean)) {
            mPlayDetailBeans.remove(bean);
        } else {
            mPlayDetailBeans.add(bean);
        }
        if (mPlayDetailBeans.size() == 0) {
            mRlTouzhu.setVisibility(View.GONE);
        } else {
            mRlTouzhu.setVisibility(View.VISIBLE);
        }
    }

    //关闭投注窗口，且清空数据
    public void offTouZhuView() {
        mPlayDetailBeans.clear();
        mRlTouzhu.setVisibility(View.GONE);
    }

    private void showFragment(SSCBPlayChhooseBean sscbPlayChhooseBean) {
        String name = sscbPlayChhooseBean.getBetName();
        if (name.equals(mSSCBPlayChhooseBeans.get(0).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_SZP_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(1).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_SM_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(2).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_YZDW_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(3).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_RZDW_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(4).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_SZDW_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(5).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_YZZH_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(6).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_ZX3_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(7).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_ZX6_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(8).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_KD_Fragment.class);
        } else if (name.equals(mSSCBPlayChhooseBeans.get(9).getBetName())) {
            SscBFragmentManager.getInstance().switchFragment(mFragmentManager, SSC_LH_Fragment.class);
        }
    }

    @OnClick({R.id.left_btn, R.id.tv_small_helper})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.tv_small_helper:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mCountDownTimerUtils.cancel();
        mCQSSCPrecenter.onDestory();
        SscBFragmentManager.getInstance().clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mSlidingLayout != null &&
                (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void changeSlidingLayOutWithMove() {
        if (mSlidingLayout != null &&
                (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

        if (mSlidingLayout != null &&
                (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }

    }

}
