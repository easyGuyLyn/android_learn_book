package com.dawoo.lotterybox.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.PlayStyleAdapter.PlayStyeParentAdapter;
import com.dawoo.lotterybox.bean.lottery.LotteryEnum;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;
import com.dawoo.lotterybox.util.AssetsReader;
import com.dawoo.lotterybox.util.GsonUtil;
import com.dawoo.lotterybox.util.JsonToPlayBean;
import com.dawoo.lotterybox.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archar on 18-
 */

public class PlayTypePopupWindow {

    private List<PlayTypeBean> mList = new ArrayList<>();
    private View mContentView;
    private PopupWindow mPopWindow = null;
    private RecyclerView mRecyclerView = null;
    private Context mContext;
    private PlayStyeParentAdapter mPlayStyeParentAdapter;
    private PlayTypePopupWindow mPlayTypePopupWindow;
    private OnClickPlayType mOnClickPlayType;
    private PlayTypeBean.PlayBean mDefaultPlayBean;

    public PlayTypePopupWindow(Context context, String code) {
        mContext = context;
        mPlayTypePopupWindow = this;
        if (code.equals(LotteryEnum.CQSSC.getType())) {
            mList = GsonUtil.jsonToList(AssetsReader.getJson(mContext, AssetsReader.SSC_JSON_FILE), PlayTypeBean.class);
        } else {
            mList = GsonUtil.jsonToList(AssetsReader.getJson(mContext, AssetsReader.SSC_JSON_FILE), PlayTypeBean.class);
        }

        mDefaultPlayBean = mList.get(0).getPlayBeans().get(0);
        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_play_style_popwindow, null);
            mRecyclerView = mContentView.findViewById(R.id.rcv_play);
            mPlayStyeParentAdapter = new PlayStyeParentAdapter(mContext, mList, mPlayTypePopupWindow, mRecyclerView);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(mPlayStyeParentAdapter);
            refeshSelectData(mDefaultPlayBean);
        }
        if (mPopWindow == null) {
            mPopWindow = new CurrentShowAsDropPopWindow(mContentView, RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT, true);
            mPopWindow.setTouchable(true);
            mPopWindow.setOutsideTouchable(true);
        }
    }


    public void refeshSelectData(PlayTypeBean.PlayBean currentPlayType) {
        mDefaultPlayBean = currentPlayType;
        for (PlayTypeBean playBean : mList) {
            for (PlayTypeBean.PlayBean playTypeBean : playBean.getPlayBeans()) {
                if (playTypeBean.getPlayTypeName().equals(currentPlayType.getPlayTypeName())
                        && playTypeBean.getMainType().equals(currentPlayType.getMainType())
                        && playTypeBean.getScheme().equals(currentPlayType.getScheme())) {
                    playTypeBean.setSelected(true);
                } else {
                    playTypeBean.setSelected(false);
                }
            }
        }
        if (mPlayStyeParentAdapter != null) {
            mPlayStyeParentAdapter.notifyDataSetChanged();
        }
    }

    public void doTogglePopupWindow(View view, PlayTypeBean.PlayBean currentPlayType) {
        if (mPopWindow == null) {
            return;
        }
        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        } else {
            refeshSelectData(currentPlayType);
            mPopWindow.showAsDropDown(view);
        }
    }

    public void dissMissPopWindow() {
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }

    public void dissMissPopAndCallBack(PlayTypeBean.PlayBean name) {
        if (mOnClickPlayType != null) {
            mOnClickPlayType.callBackTypeName(name);
        }
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }

    public PlayTypeBean.PlayBean getDefaultPlayBean() {
        return mDefaultPlayBean;
    }

    public void setOnClickPlayType(OnClickPlayType onClickPlayType) {
        mOnClickPlayType = onClickPlayType;
    }

    public interface OnClickPlayType {
        void callBackTypeName(PlayTypeBean.PlayBean name);
    }

}
