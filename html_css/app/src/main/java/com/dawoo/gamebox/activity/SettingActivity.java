package com.dawoo.gamebox.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.common.URLConstants;
import com.dawoo.gamebox.tool.ToastTool;
import com.dawoo.gamebox.view.AppUpdate;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();
    // endregion
    @BindView(R.id.ibtnBack)
    ImageView ibtnBack;
    @BindView(R.id.ibtnBack2)
    ImageView ibtnBack2;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.logout)
    Button logout;

    @BindView(R.id.version_name)
    TextView versionName;

    @BindView(R.id.help_layout)
    View helpLayout;

    private AppUpdate appUpdate;
    protected String domain;
    @Override
    public int initView() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        domain = MyApplication.domain;
        changeTitleBack(ibtnBack, ibtnBack2, tvBack);
        tvTitle.setText("设置");
        versionName.setText("V " + MyApplication.getVersionName(this));
        appUpdate = new AppUpdate(this, this);
        if (ParamTool.isLotterySite(context)) {   //纯彩票不展示帮助选项
            helpLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        appUpdate.permissionsResult(grantResults);
    }

    @OnClick(R.id.logout)
    public void logout() {
        setResult(Constants.LOGOUT_RESULT);
        finish();
    }

    @OnClick(R.id.about_layout)
    public void about() {
        Intent intent = new Intent(this, CommonActivity.class);
        intent.putExtra("url", domain + URLConstants.ABOUT_URL);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.help_layout)
    public void help() {
        Intent intent = new Intent(this, CommonActivity.class);
        intent.putExtra("url", domain + URLConstants.HELP_URL);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.check_version_layout)
    public void checkVersion() {
        showProgress(R.string.check_loading, true);
        appUpdate.checkUpdate(new RequestReturn() {
            @Override
            public void reBack() {
                dismissProgress();
            }

            @Override
            public void isNew() {
                dismissProgress();
                ToastTool.showToast(SettingActivity.this,getResources().getString(R.string.no_new_version));
            }
        });
    }

    @OnClick(R.id.rlBank)
    void goBack() {
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public interface RequestReturn {
        void reBack();
        void isNew();
    }
}
