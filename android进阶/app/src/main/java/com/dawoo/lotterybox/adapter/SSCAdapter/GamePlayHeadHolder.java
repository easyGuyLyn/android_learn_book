package com.dawoo.lotterybox.adapter.SSCAdapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.BaseViewHolder;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;
import com.dawoo.lotterybox.view.activity.shishicai.CQSSCAActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 18-2-11.
 */

public class GamePlayHeadHolder extends BaseViewHolder{
    private Context mContext;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.hot_and_cold)
    CheckBox mHotAndCold;
    @BindView(R.id.cb_omit)
    CheckBox mCbOmit;
    @BindView(R.id.iv_random)
    ImageView mIvRandom;
    @BindView(R.id.tv_how_play)
    TextView mTvHowPlay;

    public GamePlayHeadHolder(Context context,View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.mContext = context;
    }


    public void onBindView(PlayTypeBean.PlayBean mPlayTypeBean) {
        mTvHowPlay.setText(mPlayTypeBean.getSingleExplain());
        mTvHowPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mContext instanceof CQSSCAActivity){
                   ((CQSSCAActivity)mContext).showHowPlayDialog();
               }
            }
        });
    }
}
