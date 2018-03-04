package com.dawoo.gamebox.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.activity.SettingActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.tool.ProperTool;
import com.dawoo.gamebox.update.model.UpdateInfo;
import com.dawoo.gamebox.update.tool.UpdateTool;

/**
 * Created by fei on 2017/8/7.
 */
public class AppUpdate {

    private static final String TAG = AppUpdate.class.getSimpleName();
    private Activity activity;
    private Context context;
    private UpdateInfo ui;

    public AppUpdate(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    /**
     * 检查版本更新
     */
    public void checkUpdate() {
        UpdateTool.UpdateCallback updateCallback = new UpdateTool.UpdateCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(UpdateInfo updateInfo) {
                ui = updateInfo;
                showDialog();
            }

            @Override
            public void onError() {
                Log.e(TAG, "==> 暂无更新！");
            }
        };

        // 版本更新秘钥（新）
        String keyCode = ProperTool.getProperty(activity, Constants.KEY_CODE);

        // 版本更新（新）
        UpdateTool.checkUpdate(Constants.APP_TYPE, keyCode, updateCallback);
    }

    /**
     * 检查版本更新
     */
    public void checkUpdate(final SettingActivity.RequestReturn requestReturn) {
        UpdateTool.UpdateCallback updateCallback = new UpdateTool.UpdateCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(UpdateInfo updateInfo) {
                requestReturn.reBack();
                ui = updateInfo;
                showDialog();
            }

            @Override
            public void onError() {
                requestReturn.isNew();
                Log.e(TAG, "==> 暂无更新！");
            }
        };

        // 版本更新秘钥（新）
        String keyCode = ProperTool.getProperty(activity, Constants.KEY_CODE);

        // 版本更新（新）
        UpdateTool.checkUpdate(Constants.APP_TYPE, keyCode, updateCallback);
    }

    private void showDialog() {
        CustomDialog dialog = new CustomDialog(context, R.string.updateVersion, R.string.check_update, ui.memo, new CustomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    applyAuth();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void applyAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int hasAuth = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (hasAuth != PackageManager.PERMISSION_GRANTED) {
                // 提交请求权限
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                downloadApp();
            }
        } else {
            downloadApp();
        }
    }

    private void downloadApp() {
        String apkName = String.format("app_%s_%s.apk", activity.getString(R.string.app_code), ui.versionName);
        ui.appUrl = String.format("%s%s%s/%s", MyApplication.domain, ui.appUrl, ui.versionName, apkName);
        UpdateTool.downloadApk(context, ui, activity.getString(R.string.app_name), apkName);
    }

    public void permissionsResult(@NonNull int[] grantResults) {
        if (ui != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadApp();
            }
        }
    }

}
