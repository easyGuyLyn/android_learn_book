package com.dawoo.lotterybox.view.activity;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.bean.Pay;
import com.dawoo.lotterybox.util.MSPropties;
import com.dawoo.lotterybox.view.view.HeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jack on 18-2-8.
 */

public class payActivity extends BaseActivity {
    //    @BindView(R.id.recyclerview_lsit)
//    RecyclerView recyclerviewLsit;
    @BindView(R.id.head_view)
    HeaderView headView;
    private PayAdadpter payAdapter;

    private List<Pay> payList = new ArrayList<>();


    private int[] payimage = new int[]{R.mipmap.pay_weixin, R.mipmap.pay_zfb, R.mipmap.pay_qq};
    private String[] paymane = new String[]{"微信", "支付宝", "qq"};
    private String[] paydescription = new String[]{"跳转到微信支付", "跳转到支付宝支付", "跳转到qq支付"};

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void initViews() {
        headView.setHeader(getResources().getString(R.string.pay_title), true);
        payAdapter = new PayAdadpter(R.layout.payactivity_list_item);
//        payAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));
//        recyclerviewLsit.setLayoutManager(new LinearLayoutManager(this));
//        recyclerviewLsit.setAdapter(payAdapter);

    }

    @Override
    protected void initData() {
//        for (int i = 0; i < payimage.length; i++) {
//            Pay pay1 = new Pay();
//            pay1.setPayimage(payimage[i]);
//            pay1.setPayname(paymane[i]);
//            pay1.setPaydescription(paydescription[i]);
//            payList.add(pay1);
//        }
    }

    class PayAdadpter extends BaseQuickAdapter {

        public PayAdadpter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
        }

    }


}
