package com.dawoo.gamebox.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jack on 18-1-28.  宽度全部填充 高度自适应
 */

public class AutoScaleWidthImageView extends android.support.v7.widget.AppCompatImageView {

    public AutoScaleWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            float scale = (float) height / width;

            int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
            int heightMeasure = (int) (widthMeasure * scale);

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasure, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}