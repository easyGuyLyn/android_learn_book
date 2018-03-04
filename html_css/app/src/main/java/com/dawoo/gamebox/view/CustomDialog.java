package com.dawoo.gamebox.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dawoo.gamebox.R;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.widget.base.BaseDialog;

public class CustomDialog extends BaseDialog<CustomDialog> implements View.OnClickListener {

    private Context context;
    private TextView tvCancel;
    private OnCloseListener listener;
    private int titleResId;
    private int contentResId;
    private String updateInfo;
    // 是否隐藏取消按钮
    private boolean hideCancel = false;
    boolean isContentCenter = false;   //contentn内容是否居中

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialog(Context context, OnCloseListener listener) {
        this(context);
        this.listener = listener;
    }

    public CustomDialog(Context context, int contentResId, OnCloseListener listener) {
        this(context, listener);
        this.contentResId = contentResId;
    }

    public CustomDialog(Context context, int titleResId, int contentResId, OnCloseListener listener) {
        this(context, contentResId, listener);
        this.titleResId = titleResId;
    }
    public CustomDialog(Context context,int contentResId, boolean hideCancel, OnCloseListener listener) {
        this(context, contentResId, listener);
        this.hideCancel = hideCancel;
    }
    public CustomDialog(Context context,boolean contentIsCenter ,int contentResId, boolean hideCancel, OnCloseListener listener) {
        this(context, contentResId, listener);
        this.hideCancel = hideCancel;
        isContentCenter = contentIsCenter;
    }
    public CustomDialog(Context context,int titleResId,  int contentResId,String updateInfo, OnCloseListener listener) {
        this(context, titleResId, contentResId, listener);
        this.updateInfo = updateInfo;
    }

    public CustomDialog(Context context, int titleResId, int contentResId, boolean hideCancel, OnCloseListener listener) {
        this(context, contentResId,hideCancel, listener);
        this.titleResId = titleResId;
    }

    public CustomDialog(Context context, int contentResId, boolean hideCancel, boolean mCancel, OnCloseListener listener) {
        this(context, contentResId, hideCancel, listener);
        this.mCancel = mCancel;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new ZoomInEnter());
        dismissAnim(new ZoomOutExit());

        return initView();
    }

    @NonNull
    private View initView() {
        View inflate = View.inflate(context, R.layout.dialog_custom, null);
        TextView tvTitle = inflate.findViewById(R.id.tvTitle);
        tvTitle.setText(titleResId > 0 ? titleResId : R.string.prompt);
        TextView tvContent = inflate.findViewById(R.id.tvContent);
        tvContent.setText(contentResId > 0 ? contentResId : R.string.defaultContent);
        if(isContentCenter){
            tvContent.setGravity(Gravity.CENTER);
        }
        TextView tvUpdateInfo = inflate.findViewById(R.id.tvUpdaeInfo);
        tvUpdateInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvUpdateInfo.setText(updateInfo);
        TextView tvConfirm = inflate.findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(this);
        tvCancel = inflate.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        tvCancel.setVisibility(hideCancel ? View.GONE : View.VISIBLE);
        this.setCanceledOnTouchOutside(mCancel);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
            case R.id.tvCancel:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }

}