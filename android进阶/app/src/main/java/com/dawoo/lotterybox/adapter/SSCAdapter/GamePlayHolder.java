package com.dawoo.lotterybox.adapter.SSCAdapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.adapter.BaseViewHolder;
import com.dawoo.lotterybox.bean.playType.PlayTypeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 18-2-11.
 */

public class GamePlayHolder extends BaseViewHolder {

    private Context mContext;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rb_all)
    RadioButton mRbAll;
    @BindView(R.id.rb_big)
    RadioButton mRbBig;
    @BindView(R.id.rb_small)
    RadioButton mRbSmall;
    @BindView(R.id.rb_odd)
    RadioButton mRbOdd;
    @BindView(R.id.rb_even_numbers)
    RadioButton mRbEvenNumbers;
    @BindView(R.id.rb_clear)
    RadioButton mRbClear;
    @BindView(R.id.rg_ssc_game_play)
    RadioGroup mRgSscGamePlay;
    @BindView(R.id.gv_ball_list)
    RecyclerView mGvBallList;

    public GamePlayHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        ButterKnife.bind(this, itemView);
    }

    public void onBindView(PlayTypeBean.PlayBean.LayoutBean layoutBean) {
        mTvTitle.setText(layoutBean.getLayoutTitle());
        mRgSscGamePlay.clearCheck();
        if (layoutBean.getShowRightMenuType() == 0){
            mRbAll.setVisibility(View.VISIBLE);
            mRbBig.setVisibility(View.VISIBLE);
            mRbSmall.setVisibility(View.VISIBLE);
            mRbOdd.setVisibility(View.VISIBLE);
            mRbEvenNumbers.setVisibility(View.VISIBLE);
            mRbClear.setVisibility(View.VISIBLE);
        }else if (layoutBean.getShowRightMenuType() == 1){
            mRbAll.setVisibility(View.VISIBLE);
            mRbBig.setVisibility(View.GONE);
            mRbSmall.setVisibility(View.GONE);
            mRbOdd.setVisibility(View.GONE);
            mRbEvenNumbers.setVisibility(View.GONE);
            mRbClear.setVisibility(View.VISIBLE);
        }else {
            mRbAll.setVisibility(View.GONE);
            mRbBig.setVisibility(View.GONE);
            mRbSmall.setVisibility(View.GONE);
            mRbOdd.setVisibility(View.GONE);
            mRbEvenNumbers.setVisibility(View.GONE);
            mRbClear.setVisibility(View.GONE);
        }

        List<PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean> mChildLayoutBeans = layoutBean.getChildLayoutBeans();
//        if (layoutBean.getChildLayoutBeans()!=null && layoutBean.getChildLayoutBeans().size() != 0){  //初始化子item数据
//            mChildLayoutBeans = layoutBean.getChildLayoutBeans();
//        }else {
//            mChildLayoutBeans = new ArrayList<>();
//            for (int i = 0; i < layoutBean.getChildItemCount(); i++){
//                PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean childLayoutBean = new PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean();
//                childLayoutBean.setNumber(String.valueOf(i+layoutBean.getStartNumber()));
//                mChildLayoutBeans.add(childLayoutBean);
//            }
//        }
        GamePlayBallQuickAdapter mQuickAdapter = null;
        if (layoutBean.getItemType() == 0){  //布局类型
            mQuickAdapter = new GamePlayBallQuickAdapter(R.layout.item_grid_game_play_ball);  //球
        }else mQuickAdapter = new GamePlayBallQuickAdapter(R.layout.item_grid_game_play_block); //方块

        mGvBallList.setLayoutManager(new GridLayoutManager(mContext, 7));
        mQuickAdapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_view, null));
        mGvBallList.setAdapter(mQuickAdapter);
        mQuickAdapter.setNewData(mChildLayoutBeans);
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.isSelected())
                    view.setSelected(false);
                else view.setSelected(true);
            }
        });

        mRgSscGamePlay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = mGvBallList.getChildCount();
                switch (checkedId) {
                    case R.id.rb_all:
                        for (int i = 0; i < count; i++) {
                             mGvBallList.getChildAt(i).setSelected(true);
                        }
                        break;
                    case R.id.rb_big:
                        for (int i = 0; i < count; i++) {
                            if (i < (count / 2))
                                mGvBallList.getChildAt(i).setSelected(false);
                            else
                                mGvBallList.getChildAt(i).setSelected(true);
                        }
                        break;
                    case R.id.rb_small:
                        for (int i = 0; i < count; i++) {
                            if (i < (count / 2))
                                mGvBallList.getChildAt(i).setSelected(true);
                            else
                                mGvBallList.getChildAt(i).setSelected(false);
                        }
                        break;
                    case R.id.rb_odd:
                        for (int i = 0; i < count; i++) {
                            if ((i % 2) != 0)
                                mGvBallList.getChildAt(i).setSelected(true);
                            else
                                mGvBallList.getChildAt(i).setSelected(false);
                        }
                        break;
                    case R.id.rb_even_numbers:
                        for (int i = 0; i < count; i++) {
                            if ((i % 2) == 0)
                                mGvBallList.getChildAt(i).setSelected(true);
                            else
                                mGvBallList.getChildAt(i).setSelected(false);
                        }
                        break;
                    case R.id.rb_clear:
                        for (int i = 0; i < count; i++) {
                            mGvBallList.getChildAt(i).setSelected(false);
                        }
                        break;
                }
            }
        });

    }

    class GamePlayBallQuickAdapter extends BaseQuickAdapter {
        public GamePlayBallQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, Object item) {
            PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean childLayoutBean = (PlayTypeBean.PlayBean.LayoutBean.ChildLayoutBean) item;
            helper.setText(R.id.tv_ball, childLayoutBean.getNumber());
            if (childLayoutBean.getNumberRelevant()!=null)
            helper.setText(R.id.tv_ball_info, childLayoutBean.getNumberRelevant());
            helper.itemView.setSelected(childLayoutBean.isSelected());
        }
    }
}
