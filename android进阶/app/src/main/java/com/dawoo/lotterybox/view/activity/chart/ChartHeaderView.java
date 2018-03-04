package com.dawoo.lotterybox.view.activity.chart;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.lottery.LotteryEnum;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;
import com.dawoo.lotterybox.util.SingleToast;
import com.dawoo.lotterybox.view.view.PlayTypePopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图表头部的控件
 * Created by benson on 18-2-19.
 */

public class ChartHeaderView extends RelativeLayout {
    @BindView(R.id.iv_go_back)
    ImageView mIvGoBack;
    @BindView(R.id.tv_play_method)
    TextView mTvPlayMethod;
    @BindView(R.id.iv_right_menu)
    ImageView mIvRightMenu;
    @BindView(R.id.root_view)
    View mRootView;
    private Context mContext;
    private PlayTypePopupWindow mPlayTypePopupWindow;
    private PlayTypeBean.PlayBean mPlayTypeBean;

    public ChartHeaderView(Context context) {
        super(context);
        initUI(context);
    }

    public ChartHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    public ChartHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(context);
    }

    private void initUI(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.chart_header_view, this);
        ButterKnife.bind(this, view);

        initPlayTypePopWindow();
    }


    @OnClick({R.id.iv_go_back, R.id.tv_play_method, R.id.iv_right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                ((Activity) mContext).finish();
                break;
            case R.id.tv_play_method:
                mPlayTypePopupWindow.doTogglePopupWindow(mRootView, mPlayTypeBean);
                break;
            case R.id.iv_right_menu:

                break;
        }
    }

    public void setTitleName(String name) {
        mTvPlayMethod.setText(name);
    }

    private void initPlayTypePopWindow() {
        mPlayTypeBean = new PlayTypeBean.PlayBean();
        mPlayTypeBean.setMainType("二星");
        mPlayTypeBean.setScheme("前二组选");
        mPlayTypeBean.setPlayTypeName("和值A面");

        mPlayTypePopupWindow = new PlayTypePopupWindow(mContext, LotteryEnum.CQSSC.getType());
        mPlayTypePopupWindow.setOnClickPlayType(new PlayTypePopupWindow.OnClickPlayType() {
            @Override
            public void callBackTypeName(PlayTypeBean.PlayBean playTypeBean) {
                SingleToast.showMsg(playTypeBean.getMainType() + "," + playTypeBean.getScheme() + "," + playTypeBean.getPlayTypeName());
                mPlayTypeBean = playTypeBean;
                setTitleName(playTypeBean.getScheme() + playTypeBean.getPlayTypeName());
            }
        });
    }


}
