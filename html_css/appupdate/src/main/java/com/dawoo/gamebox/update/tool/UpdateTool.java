package com.dawoo.gamebox.update.tool;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.dawoo.gamebox.constant.URLConst;
import com.dawoo.gamebox.tool.IJson;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.update.common.Constants;
import com.dawoo.gamebox.update.model.UpdateInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 应用更新工具
 * Created by fei on 16-11-21.
 */
public class UpdateTool {
    @SuppressWarnings("unused")
    private static final String TAG = UpdateTool.class.getSimpleName();

    /**
     * 检查更新（新）
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appType, String keyCode, UpdateCallback updateCallback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("type", appType);
        params.put("code", keyCode);

        OkHttpUtils.get().params(params).headers(params).url(URLConst.UPDATE_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UpdateTool.onError(e, updateCallback);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "update info ==> " + response);
                onNext(IJson.fromJson(response, UpdateInfo.class), updateCallback);
            }
        });
    }

    // 显示信息
    private static void onNext(UpdateInfo updateInfo, UpdateCallback updateCallback) {
        if (updateInfo != null && updateInfo.versionCode != null) {
            Log.i(TAG, "返回数据: " + updateInfo.toString());
            updateCallback.onSuccess(updateInfo);
        } else {
            updateCallback.onError(); // 失败
        }
    }

    // 错误信息
    private static void onError(Throwable throwable, UpdateCallback updateCallback) {
        updateCallback.onError();
        Log.e(TAG, throwable.getMessage());
    }

    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context    上下文
     * @param updateInfo 更新信息
     * @param infoName   通知名称
     * @param storeApk   存储的Apk
     */
    @SuppressWarnings("unused")
    public static void downloadApk(Context context, UpdateInfo updateInfo, String infoName, String storeApk) {
        try {
            Context appContext = context.getApplicationContext();
            // 请求 DOWNLOAD_SERVICE 服务
            DownloadManager manager = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri uri = Uri.parse(updateInfo.appUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(infoName);
            request.setDescription(storeApk);
            request.setMimeType("application/vnd.android.package-archive");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);
            long downloadId = manager.enqueue(request);

            // 存储下载Key
            SPTool.put(appContext, Constants.DOWNLOAD_APK_ID, downloadId);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    // 错误回调
    public interface UpdateCallback {
        void onSuccess(UpdateInfo updateInfo);

        void onError();
    }

}