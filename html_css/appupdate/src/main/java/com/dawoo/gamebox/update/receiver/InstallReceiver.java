package com.dawoo.gamebox.update.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.update.common.Constants;

/**
 * 安装下载接收器
 * Created by fei on 16-11-21.
 */
public class InstallReceiver extends BroadcastReceiver {

    private static final String TAG = InstallReceiver.class.getSimpleName();

    // 安装下载接收器
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadId);
        }
    }

    // 安装Apk
    private void installApk(Context context, long downloadApkId) {
        // 获取存储ID
        long id = (long) SPTool.get(context, Constants.DOWNLOAD_APK_ID, -1L);

        if (downloadApkId == id) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Log.e(TAG, "下载失败");
            }
        }
    }
}
