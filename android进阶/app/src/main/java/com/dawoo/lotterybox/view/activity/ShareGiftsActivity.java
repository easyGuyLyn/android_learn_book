package com.dawoo.lotterybox.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.lotterybox.R;
import com.dawoo.lotterybox.view.view.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 18-2-9.
 */

public class ShareGiftsActivity extends BaseActivity {
    @BindView(R.id.head_view)
    HeaderView headView;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView7)
    TextView textView7;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_sharegifts);
    }

    @Override
    protected void initViews() {
        headView.setHeader(getResources().getString(R.string.sharegifs_title), true);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.head_view, R.id.textView3, R.id.textView4, R.id.imageView, R.id.textView5, R.id.textView7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_view:
                break;
            case R.id.textView3:
                break;
            case R.id.textView4:
                break;
            case R.id.imageView:
                break;
            case R.id.textView5:
                break;
            case R.id.textView7:
                break;
            default:
        }
    }
}
