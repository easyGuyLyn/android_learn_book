package com.dawoo.lotterybox.view.activity.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import com.dawoo.lotterybox.R;

import java.util.List;

/**
 * Created by b on 18-2-13.
 * 折线图 RecyclerView
 */

public class LineRecyclerView extends RecyclerView {

    private List<Integer> integers;
    private Paint mPaint;
    private Context mContext;
    private float lastX = 0;
    private float lastY = 0;
    private boolean isDrawLine = true;

    public LineRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public LineRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.line_bg));
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setList(List<Integer> integers) {
        this.integers = integers;
    }

    public void isDrawLine(boolean isDraw){
        this.isDrawLine = isDraw;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (!isDrawLine || integers == null)
            return;
        int firstPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
        int lastPosition = ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
        for (int i = firstPosition; i < lastPosition+1; i++) {
            LinearLayout childView = (LinearLayout) getLayoutManager().findViewByPosition(i);
            if (childView != null) {
                float childGroupViewX = childView.getLeft();
                float childGroupViewY = childView.getTop();
                View view = childView.getChildAt(integers.get(i));
                if (view != null) {
//                    int[] position = new int[2];
//                    view.getLocationInWindow(position);
                    float x = view.getX()+childGroupViewX+view.getWidth()/2;
                    float y = view.getY()+childGroupViewY+view.getHeight()/2;
                    if (lastX != 0 && y > lastY)
                        c.drawLine(lastX, lastY, x, y, mPaint);
                    lastX = x;
                    lastY = y;
                }
            }
        }
        lastX = 0;
        lastY = 0;
    }
}
