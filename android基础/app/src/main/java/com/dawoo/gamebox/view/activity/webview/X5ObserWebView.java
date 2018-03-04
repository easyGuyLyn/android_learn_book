package com.dawoo.gamebox.view.activity.webview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

import com.tencent.smtt.sdk.WebView;

/**
 * Created by benson on 18-2-23.
 */

//重定义webview 这里继承的是X5WebView
public class X5ObserWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public X5ObserWebView(final Context context) {
        super(context);
    }

    public X5ObserWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }



//    @Override
//    protected void tbs_onScrollChanged(int l, int t, int oldl, int oldt, View view) {
//        this.super_onScrollChanged(l, t, oldl, oldt);
//        //X5WebView 父类屏蔽了 onScrollChanged 方法 要用该方法
//        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
//    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //普通webview用这个
        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
        void onScroll(int l, int t);
    }
}