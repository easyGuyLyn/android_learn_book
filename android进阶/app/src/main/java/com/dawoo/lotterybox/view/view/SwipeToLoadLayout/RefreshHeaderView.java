package com.dawoo.lotterybox.view.view.SwipeToLoadLayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.dawoo.lotterybox.R;

/**
 * Created by benson on 17-12-24.
 */

public class RefreshHeaderView extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private TextView mTvStatus;
    private ImageView mIv_refresh_headPic;
    private TextView mTv_final_time;

    public RefreshHeaderView(Context context) {
        this(context, null, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.refresh_head_layout, null);
        mTvStatus = view.findViewById(R.id.tv_refresh_status);
        mIv_refresh_headPic = view.findViewById(R.id.iv_refresh_headPic);
        mTv_final_time = view.findViewById(R.id.tv_final_time);
        addView(view, lp);
    }

    @Override
    public void onRefresh() {
        Log.e("HeadAnim", "onRefresh");
        mTvStatus.setText(getResources().getString(R.string.REFRESHING));
    }

    @Override
    public void onPrepare() {
        Log.e("HeadAnim", "onPrepare");
        startHeadAnim();
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                mTvStatus.setText(getResources().getString(R.string.RELEASE_TO_REFRESH));
            } else {
                mTvStatus.setText(getResources().getString(R.string.SWIPE_TO_REFRESH));
            }
        } else {
            mTvStatus.setText(getResources().getString(R.string.REFRESH_RETURNING));
        }
    }

    @Override
    public void onRelease() {
        Log.e("HeadAnim", "onRelease");
    }

    @Override
    public void onComplete() {
        Log.e("HeadAnim", "onComplete");
        stopHeadAnim();
        mTvStatus.setText(getResources().getString(R.string.REFRESH_COMPLETE));
    }

    @Override
    public void onReset() {
        Log.e("HeadAnim", "onReset");
    }


    private void startHeadAnim() {
        Log.e("HeadAnim", "startHeadAnim");
        mIv_refresh_headPic.setImageResource(R.drawable.anim_refresh_head);
        AnimationDrawable ad = (AnimationDrawable) mIv_refresh_headPic.getDrawable();
        ad.start();
    }

    private void stopHeadAnim() {
        Log.e("HeadAnim", "stopHeadAnim");
        Drawable drawable = mIv_refresh_headPic.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
        }
        mIv_refresh_headPic.setImageResource(R.drawable.chickenrun1);
    }

}
