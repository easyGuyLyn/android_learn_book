package com.dawoo.lotterybox.view.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.lotterybox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by b on 18-2-11.
 * 时时彩布局Head
 */

public class HeaderSSCBaseView extends RelativeLayout {

    @BindView(R.id.iv_go_back)
    ImageView mIvGoBack;
    @BindView(R.id.tv_play_method)
    TextView mTvPlayMethod;
    @BindView(R.id.tv_right_menu)
    TextView mTvRightMenu;

    private Context mContext;
    private MenuPopupWindow mPopupWindow;
    private HeadPopupItemClick mPopupItemClick;

    public HeaderSSCBaseView(Context context) {
        this(context, null);
    }

    public HeaderSSCBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderSSCBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ssc_base_title, this);
        initPoppupWindow();
        ButterKnife.bind(this, view);
    }

    private void initPoppupWindow() {
        mPopupWindow = new MenuPopupWindow();
        mPopupWindow.setWidth(300);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setPopupItemClick(HeadPopupItemClick headPopupItemClick){
        this.mPopupItemClick = headPopupItemClick;
    }


    @OnClick({R.id.iv_go_back, R.id.tv_right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
                break;

            case R.id.tv_right_menu:
                if (mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                else
                    showAtDropDownRight();
                break;

        }

    }

    public interface HeadPopupItemClick{
        void onPopupBalance();
        void onRecentAwards();
        void onMyBet();
        void onMyChaseNumber();
        void onTrendChart();
        void onLztPop();
        void onWfPop();
    }

    public void showAtDropDownRight() {
        if (mPopupWindow != null) {
            int[] location = new int[2];
            //显示在headview的右下方
            this.getLocationOnScreen(location);
            mPopupWindow.showAtLocation(this, 0, location[0] + this.getWidth() - mPopupWindow.getWidth(), location[1] + this.getHeight());
        } else {
            initPoppupWindow();
            showAtDropDownRight();
        }


    }

    public void dismissPop() {
        if (mPopupWindow != null)
            mPopupWindow.dismiss();
    }

    //设置玩法左图标
    public void setPlayMethodLeftDrawable(int id) {
        mTvPlayMethod.setCompoundDrawables(ContextCompat.getDrawable(mContext, id), null, null, null);
    }

    //设置玩法名称
    public void setPlayMethodLeftText(String title) {
        mTvPlayMethod.setText(title);
    }

    //获取玩法控件id
    public int getPlayMethodLeftID() {
        return mTvPlayMethod.getId();
    }

    //获取右侧id
    public int getHeadRightID() {
        return mTvRightMenu.getId();
    }

    class MenuPopupWindow extends PopupWindow implements OnClickListener{
        @BindView(R.id.tv_user_name)
        TextView mTvUserName;
        @BindView(R.id.tv_balance)
        TextView mTvBalance;
        @BindView(R.id.tv_recent_awards)
        TextView mTvRecentAwards;
        @BindView(R.id.tv_my_bet)
        TextView mTvMyBet;
        @BindView(R.id.tv_my_chase_number)
        TextView mTvMyChaseNumber;
        @BindView(R.id.tv_trend_chart)
        TextView mTvTrendChart;
        @BindView(R.id.tv_lzt_pop)
        TextView mTvLztPop;
        @BindView(R.id.tv_wf_pop)
        TextView mTvWfPop;
        public MenuPopupWindow(){
            View popView = LayoutInflater.from(mContext).inflate(R.layout.layout_ssc_head_assistant_popupwindow,null);
            setContentView(popView);
            ButterKnife.bind(this, popView);
            mTvUserName.setOnClickListener(this);
            mTvBalance.setOnClickListener(this);
            mTvRecentAwards.setOnClickListener(this);
            mTvMyBet.setOnClickListener(this);
            mTvMyChaseNumber.setOnClickListener(this);
            mTvTrendChart.setOnClickListener(this);
            mTvLztPop.setOnClickListener(this);
            mTvWfPop.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.tv_user_name){
                dismissPop();
                return;
            }

            if (mPopupItemClick == null)
                return;
            switch (v.getId()){
                case R.id.tv_balance:
                    mPopupItemClick.onPopupBalance();
                    break;
                case R.id.tv_recent_awards:
                    mPopupItemClick.onRecentAwards();
                    break;
                case R.id.tv_my_bet:
                    mPopupItemClick.onMyBet();
                    break;
                case R.id.tv_my_chase_number:
                    mPopupItemClick.onMyChaseNumber();
                    break;
                case R.id.tv_trend_chart:
                    mPopupItemClick.onTrendChart();
                    break;
                case R.id.tv_lzt_pop:
                    mPopupItemClick.onLztPop();
                    break;
                case R.id.tv_wf_pop:
                    mPopupItemClick.onWfPop();
                    break;
            }
        }
    }

}
