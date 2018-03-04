package com.dawoo.gamebox.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.gamebox.MainActivity;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.been.CheckLog;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.common.ParamTool;
import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.constant.URLConst;
import com.dawoo.gamebox.enums.CodeEnum;
import com.dawoo.gamebox.enums.SiteTypeEnum;
import com.dawoo.gamebox.enums.ThemeEnum;
import com.dawoo.gamebox.tool.FileTool;
import com.dawoo.gamebox.tool.IJson;
import com.dawoo.gamebox.tool.ResourceTool;
import com.dawoo.gamebox.tool.SPTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.dawoo.gamebox.util.UploadErrorLinesUtil;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

import static com.dawoo.gamebox.constant.URLConst.fecthUrl;


public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.tvLoading)
    TextView tvLoading;
    @BindView(R.id.rlSplash)
    RelativeLayout rlSplash;
    @BindView(R.id.log_recyclerview)
    RecyclerView mLogRecyclerView;

    private String invalidUrl;  // 是否有效域名
    private long beginTime = System.currentTimeMillis();
    private boolean isGoon = true;  // 是否继续Task

    private List<FetchLineTask> mFetchLineTaskList = new ArrayList<>();
    // endregion
    private LogQuickAdapter mLogQuickAdapter;


    private StringBuilder mDomains = new StringBuilder();//错误的线路集合
    private StringBuilder mErrorMessages = new StringBuilder();//错误的msg集合
    private StringBuilder mCodes = new StringBuilder();//错误的code集合
    private String mark; //上传错误信息时的辨认值
    private int CHECKING = 0;
    private int CHECK_FAIL = 1;
    private int CHECK_SUCCESS = 2;
    private int GET_FAIL = 3;
    private int GET_SUCCESS = 4;
    private int STOP_GET = 5;

    private volatile int mCurrentCheckLineCount;

    private volatile boolean mIsJumpActivity;

    @Override
    public int initView() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mLogQuickAdapter = new LogQuickAdapter(R.layout.log_check_splash_acitivity_list_item_view);
        mLogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mLogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLogRecyclerView.setAdapter(mLogQuickAdapter);
        String currentTime = System.currentTimeMillis() + "";
        mark = currentTime.substring(currentTime.length() - 6);
        String domain = (String) SPTool.get(this, Const.KEY_DOMAIN, "");
        // 检测域名
        //  checkSPLine(domain);
        fetchLines();
        // 设置主题
        setTheme();
        // Logo展示
        showLogo();
    }

    private void setTheme() {
        if (TextUtils.isEmpty((String) SPTool.get(this, Constants.THEME_KEY, ""))) {
            String theme = getResources().getString(R.string.theme);
            SPTool.put(this, Constants.THEME_KEY, theme);

            if (ThemeEnum.DEFAULT.getCode().equals(theme)) return;

            theme = "skin/" + theme;
            String SKIN_DIR = FileTool.getSkinDirPath(this);
            String skinFullName = SKIN_DIR + File.separator + theme;
            FileTool.moveRawToDir(context, theme, skinFullName);
            SPTool.put(this, Constants.KEY_THEME_PATH, skinFullName);
        }
    }

    private void showLogo() {
        String siteType = getResources().getString(R.string.site_type);
        if (SiteTypeEnum.LOTTERY.getCode().equals(siteType)) {
            rlSplash.setBackgroundResource(R.drawable.splash_cp);
        }

        String logoName = getResources().getString(R.string.app_logo);
        ivLogo.setImageDrawable(getResources().getDrawable(ResourceTool.getDrawResID(this, logoName)));
    }

    /**
     * 检测SharedPreferences保存的域名是否可用
     */
    private void checkSPLine(final String line) {
        Log.e(TAG, "check sp line...");
        if (!TextUtils.isEmpty(line)) {
            OkHttpUtils.get().url(line + "/__check").build().execute(new Callback() {
                @Override
                public Object parseNetworkResponse(Response response, int id) throws Exception {
                    if (response.request().isHttps())
                        MyApplication.isHttps = true;
                    handleLine(response, line);
                    return null;
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    String msg = "SP中域名（" + line + "）不可用，将重新检测域名";
                    Log.e(TAG, msg);
                    //      ToastTool.showToastShort(SplashActivity.this, msg);
                    fetchLines();
                }

                @Override
                public void onResponse(Object response, int id) {
                }
            });
        } else {
            fetchLines();
        }
    }

    /**
     * 处理域名
     */
    private void handleLine(Response response, String line) {
        String code = getStatusCode(response);
        // 域名过期
        if (CodeEnum.DUE.getCode().equals(code)) {
            String msg = "SP中域名（" + line + "）已过期，将重新检测域名";
            Log.e(TAG, msg);
            //   ToastTool.showToastShort(SplashActivity.this, msg);

            invalidUrl = line;
            SPTool.remove(context, Const.KEY_DOMAIN);
            fetchLines();
            appendErrorLine(line, "域名过期", code);
        } else if (CodeEnum.OK.getCode().equals(code)) {
            Log.e(TAG, "SP中域名（" + line + "）可用，正在启动应用");
            isGoon = false;
            gotoMain(line);
        } else {
            appendErrorLine(line, "域名发生未知问题,不可用", code);
            Log.e(TAG, "checkLine --> response = " + response);
        }
    }

    /**
     * 根据头部信息获取请求状态
     */
    private String getStatusCode(Response response) {
        if (response.priorResponse() != null) {
            String headerStatus = response.priorResponse().header("headerStatus");
            if (CodeEnum.DUE.getCode().equals(headerStatus)) {
                return CodeEnum.DUE.getCode();
            }
        }
        return response.message();
    }

    /**
     * 进入MainActivity
     */
    private void gotoMain(String line) {
        if (!isGoon) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLoading.setText(getString(R.string.checkOk));
                }
            });

//            String spLine = (String) SPTool.get(this, Const.KEY_DOMAIN, "");
//            if (StringUtils.isEmpty(spLine)) {
            line = getSslUrl(line);     // -- del
            SPTool.put(context, Const.KEY_DOMAIN, line);
            //  }
            MyApplication.domain = line;

            jumpActivity();

        }
    }

    /**
     * 获取SSL链接
     */
    private String getSslUrl(String line) {
        if (MyApplication.isHttps) {
            if (line.startsWith("http:")) {
                line = line.replace("http", "https");
            } else if (!line.startsWith("https:")) {
                if (isDebug) {
                    line = String.format("http://%s", line);
                } else {
                    line = String.format("https://%s", line);
                }
            }
        }
        return line;
    }


    private synchronized void jumpActivity() {
        Log.e(TAG, "mIsJumpActivity: " + mIsJumpActivity);
        if (mIsJumpActivity) return;
        mIsJumpActivity = true;

        if (mFetchLineTaskList != null) {
            for (FetchLineTask task : mFetchLineTaskList) {
                if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                    task.cancel(true);
                }
            }
        }

        Class clazz;
        if (ParamTool.isLotterySite(context)) {
            clazz = LotteryActivity.class;
        } else {
            clazz = MainActivity.class;
        }
        // Log.e(TAG, "LineUtil  start upload");
        //   UploadErrorLinesUtil.upload(mDomains.toString(), mErrorMessages.toString(), mCodes.toString());
        Intent intent = new Intent(SplashActivity.this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * 查询线路域名
     */
    private void fetchLines() {
        Map<String, String> params = new HashMap<>(2);
        params.put("code", getResources().getString(R.string.app_code));
        params.put("s", getResources().getString(R.string.app_sid));

        for (int i = 0; i < 3; i++) {
            OkHttpUtils.get().params(params).url(fecthUrl[i]).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, final Exception e, int id) {
                    Log.e(TAG, e.getMessage());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorPrompt(e.getMessage());
                            mLogQuickAdapter.addData(new CheckLog(GET_FAIL, getString(R.string.get_checking_line_fail, e.getMessage()), mark));
                        }
                    });


                }

                @Override
                public void onResponse(String response, int id) {
                    List<String> lines = IJson.fromJson(response, List.class);

                    if (isDebug) {
                        jumpActivity();
                    }
                    if (lines == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLogQuickAdapter.addData(new CheckLog(GET_SUCCESS, getString(R.string.get_checking_line_null), mark));
                                mLogQuickAdapter.addData(new CheckLog(STOP_GET, getString(R.string.stop_get_check_line), mark));
                            }
                        });
                        return;
                    }
                    Log.e("zzzzz", "检测域名: " + lines.toString());
                    FetchLineTask task;
                    for (String line : lines) {
                        if (!isGoon) break;
                        if (invalidUrl != null && invalidUrl.endsWith(line)) continue;

                        if (isGoon && line != null) {
                            Log.e(TAG, "检测域名: " + line);
                            if (isDebug) {
                                line = String.format("http://%s", line);
                            } else {
                                line = String.format("https://%s", line);   // 暂时这样处理，后续需要查询库表是否有启用ssl
                            }
                            task = new FetchLineTask();
                            mFetchLineTaskList.add(task);
                            task.execute(line);
                        }
                    }
                }
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchLineTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (isCancelled()) {
                return "执行完毕";
            }
            checkLine(params[0]);
            return "执行完毕";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    /**
     * 检测域名是否可用
     */
    private void checkLine(final String line) {
        if (isGoon && !TextUtils.isEmpty(line)) {
            OkHttpUtils.get().url(line + "/__check").build().execute(new Callback() {
                @Override
                public Object parseNetworkResponse(Response response, int id) throws Exception {
                    if (response.request().isHttps())
                        MyApplication.isHttps = true;
                    final String code = getStatusCode(response);
                    if (CodeEnum.OK.getCode().equals(code)) {
                        isGoon = false;
                        for (FetchLineTask task : mFetchLineTaskList) {
                            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                                task.cancel(true);
                            }
                        }
                        Log.e(TAG, "1.域名（" + line + "）可用");
                        gotoMain(line);
                    } else {
                        Log.e(TAG, "checkLine --> response = " + response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appendErrorLine(line, "域名不可用", code);
                                mLogQuickAdapter.addData(new CheckLog(CHECK_FAIL, line, mark));
                            }
                        });
                    }
                    return null;
                }

                @Override
                public void onError(Call call, final Exception e, int id) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = "1.域名（" + line + "）不可用";
                            Log.e(TAG, msg);
                            //  ToastTool.showToastShort(SplashActivity.this, msg);
                            appendErrorLine(line, e.getMessage(), "000");
                            mLogQuickAdapter.addData(new CheckLog(CHECK_FAIL, line, mark));
                        }
                    });

                }

                @Override
                public void onResponse(Object response, int id) {
                }
            });
        } else {
            fetchLines();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avi.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (FetchLineTask fetchLineTask : mFetchLineTaskList) {
            if (fetchLineTask != null && fetchLineTask.getStatus() == AsyncTask.Status.RUNNING) {
                fetchLineTask.cancel(true);
            }
        }
        if (isGoon) {
            UploadErrorLinesUtil.upload(mDomains.toString(), mErrorMessages.toString(), mCodes.toString(), mark);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avi.hide();
            }
        });
    }

    /**
     * 网络请求异常提示
     *
     * @param msg
     */
    private void errorPrompt(String msg) {
        if (msg.contains("associated")) {
            ToastTool.showToastShort(this, getString(R.string.unNet));
        } else {
            if (!msg.contains(URLConst.BASE_URL)) {
                // ToastTool.showToastShort(this, msg);
            }
        }
    }

    /**
     * 收集错误域名的信息
     *
     * @param line
     * @param errMsg
     * @param errCode
     */
    private void appendErrorLine(String line, String errMsg, String errCode) {
        if (mDomains.length() == 0) {
            mDomains.append(line);
        } else {
            mDomains.append(";" + line);
        }
        if (mErrorMessages.length() == 0) {
            mErrorMessages.append(errMsg);
        } else {
            mErrorMessages.append(";" + errMsg);
        }
        if (mCodes.length() == 0) {
            mCodes.append(errCode);
        } else {
            mCodes.append(";" + errCode);
        }
    }

    class LogQuickAdapter extends BaseQuickAdapter {

        public LogQuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            CheckLog checkLog = (CheckLog) item;
            if (1 == checkLog.getStatus() || 3 == checkLog.getStatus()) {
                helper.itemView.setBackgroundColor(getResources().getColor(R.color.red));
            }
            helper.setText(R.id.log_tv, checkLog.getLogStr());
            helper.setText(R.id.log_tv_code, checkLog.getCode());
        }

    }

}