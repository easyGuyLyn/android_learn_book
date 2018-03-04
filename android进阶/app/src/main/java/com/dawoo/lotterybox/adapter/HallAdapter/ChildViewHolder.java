package com.dawoo.lotterybox.adapter.HallAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;
import com.dawoo.lotterybox.view.activity.shishicai.CQSSCBActivity;
import com.dawoo.lotterybox.view.activity.shishicai.CQSSCAActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子布局ViewHolder
 */

public class ChildViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;
    private GridView mGv_lottery;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final TypeAndLotteryBean dataBean, final int pos) {
        List<TypeAndLotteryBean.LotteriesBean> lotteries = dataBean.getLotteries();
        mGv_lottery = (GridView) view.findViewById(R.id.gv_lottery);

        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (TypeAndLotteryBean.LotteriesBean lotteriesBean : lotteries) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("lottery", lotteriesBean.getName());
            listMap.add(map);
        }
        String[] from = {"lottery"};
        int[] to = {R.id.tv_lottery_name};
        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, listMap, R.layout.item_grad_lottery_type, from, to);
        mGv_lottery.setAdapter(simpleAdapter);
        mGv_lottery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mContext.startActivity(new Intent(mContext, CQSSCAActivity.class));
                } else if (position == 1) {
                    mContext.startActivity(new Intent(mContext, CQSSCBActivity.class));
                }
            }
        });
    }
}
