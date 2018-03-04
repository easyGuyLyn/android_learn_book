package com.dawoo.gamebox.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.dawoo.gamebox.R;


public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context, String strMessage) {
        this(context, R.style.CustomProgressDialog, strMessage);
    }

    public CustomProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(R.layout.custom_progress_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        TextView tvMsg = this.findViewById(R.id.tvLoading);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }

}  