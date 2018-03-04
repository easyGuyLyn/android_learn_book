package com.dawoo.lotterybox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity的基本类
 * Created by benson on 17-12-19.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private Unbinder mBind;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
        createLayoutView();
        mBind = ButterKnife.bind(this); // 初始化ButterKnife
        initViews();
        initData();
    }

    protected abstract void createLayoutView();


    protected abstract void initViews();

    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        ActivityStackManager.getInstance().removeActivity(this);
    }
}
