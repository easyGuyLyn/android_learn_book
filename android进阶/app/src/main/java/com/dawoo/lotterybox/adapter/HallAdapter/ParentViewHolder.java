package com.dawoo.lotterybox.adapter.HallAdapter;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;

/**
 * Created by hbh on 2017/4/20.
 * 父布局ViewHolder
 */

public class ParentViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;

    private TextView tv_lottery_type_name;

    private TextView tv_lottery_frequency;

    private RelativeLayout mRl_lottery_type;


    public ParentViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final TypeAndLotteryBean dataBean, final int pos, final ItemClickListener listener){
        mRl_lottery_type = (RelativeLayout) view.findViewById(R.id.rl_lottery_type);
        tv_lottery_type_name = (TextView) view.findViewById(R.id.tv_lottery_type_name);
        tv_lottery_frequency = (TextView) view.findViewById(R.id.tv_lottery_frequency);

        tv_lottery_type_name.setText(dataBean.getTypeName());

        //父布局OnClick监听
        mRl_lottery_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBean.getLotteries()!=null && dataBean.getLotteries().size()!=0){
                    if (listener != null) {
                        if (dataBean.isExpand()) {
                            listener.onHideChildren();
                            dataBean.setExpand(false);
                        } else {
                            listener.onExpandChildren(dataBean,pos);
                            dataBean.setExpand(true);
                        }
                    }
                }else {
                    Toast.makeText(mContext,"研发中", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                }
            });
            valueAnimator.start();
        }
    }
}
