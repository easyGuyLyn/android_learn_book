package com.dawoo.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.LogUtils;
import com.dawoo.coretool.ToastUtil;
import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.gamebox.BuildConfig;
import com.dawoo.gamebox.ConstantValue;
import com.dawoo.gamebox.R;
import com.dawoo.gamebox.bean.CheckLog;
import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.util.AutoLogin;
import com.dawoo.gamebox.util.IJson;
import com.dawoo.gamebox.util.SSLUtil;
import com.dawoo.gamebox.util.SharePreferenceUtil;
import com.dawoo.gamebox.util.UploadErrorLinesUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dawoo.gamebox.ConstantValue.fecthUrl;

/**
 * 过渡页
 * 检查线路
 * Created by benson on 17-12-27.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.avi)
    AVLoadingIndicatorView mAvi;
    @BindView(R.id.tvLoading)
    TextView mTvLoading;
    @BindView(R.id.ivLogo)
    ImageView mIvLogo;
    @BindView(R.id.tvCopyright)
    TextView mTvCopyright;
    @BindView(R.id.log_recyclerview)
    RecyclerView mLogRecyclerView;
    private SplashActivity mContext;
    private boolean isHttps;
    private String invalidUrl;  // 是否有效域名
    private long beginTime = System.currentTimeMillis();
    private final static long WAIT_TIME = 3000;
    private boolean isGoon = true;  // 是否继续Task
    private List<FetchLineTask> mFetchLineTaskList = new ArrayList<>();
    private LogQuickAdapter mLogQuickAdapter;

    private int CHECKING = 0;
    private int CHECK_FAIL = 1;
    private int CHECK_SUCCESS = 2;
    private int GET_FAIL = 3;
    private int GET_SUCCESS = 4;
    private int STOP_GET = 5;

    private StringBuilder mDomains = new StringBuilder();//错误的线路集合
    private StringBuilder mErrorMessages = new StringBuilder();//错误的msg集合
    private StringBuilder mCodes = new StringBuilder();//错误的code集合
    private String mark; //上传错误信息时的辨认值

    private volatile boolean mIsJumpActivity;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acitivity_splash);
    }

    @Override
    protected void initViews() {
        mContext = this;
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置applogo
        Glide.with(this).load(R.mipmap.app_logo).into(mIvLogo);
        //  设置Copyright
        mTvCopyright.setText(String.format("Copyright © %s Reserved. v%s", getResources().getString(R.string.app_name), PackageInfoUtil.getVersionName(this)));

        mLogQuickAdapter = new LogQuickAdapter(R.layout.log_check_splash_acitivity_list_item_view);
        mLogRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true));
        mLogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLogRecyclerView.setAdapter(mLogQuickAdapter);
    }

    @Override
    protected void initData() {
        // 检测域名
        // checkSpLine();
        String currentTime = System.currentTimeMillis() + "";
        mark = currentTime.substring(currentTime.length() - 6);
        fetchLines();
    }

    /**
     * 检测本地保存的线路是否可用
     */
    void checkSpLine() {
        // 取出本地域名线路
        String spDomain = SharePreferenceUtil.getDomain(this);
        LogUtils.e("checkSpLine() --> SharePreferenceUtil.getDomain = " + spDomain);

        if (TextUtils.isEmpty(spDomain)) {
            fetchLines();
        } else {
            String url = spDomain + "/__check";
            // 检查域名是否可用
            mLogQuickAdapter.addData(new CheckLog(CHECKING, getString(R.string.checking_line, spDomain), mark));
            okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
            okHttpClient.newBuilder().sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.TrustAllManager());
            //创建一个Request
            final okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .build();
            //new call
            okhttp3.Call call = okHttpClient.newCall(request);
            //请求加入调度
            call.enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(okhttp3.Call call, final IOException e) {

                    String msg = "SP中域名（" + spDomain + "）不可用，将重新检测域名";
                    LogUtils.e(msg);
                    appendErrorLine(spDomain, e.getMessage(), "000");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToastShort(mContext, msg);
                            mLogQuickAdapter.addData(new CheckLog(CHECK_FAIL, spDomain, mark));
                        }
                    });

                    fetchLines();
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    handleLine(response, spDomain);
                }
            });
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
            LogUtils.i(msg);
            ToastUtil.showToastShort(mContext, msg);

            invalidUrl = line;
            SharePreferenceUtil.saveDomain(mContext, "");
            //SPTool.remove(mContext, Const.KEY_DOMAIN);
            appendErrorLine(line, "域名过期", code);

            fetchLines();
        } else if (CodeEnum.OK.getCode().equals(code)) {
            LogUtils.i("SP中域名（" + line + "）可用，正在启动应用");
            isGoon = false;
            gotoMain(line);
        } else {
            appendErrorLine(line, "域名发生未知问题,不可用", code);
            LogUtils.e("checkLine --> response = " + response);
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
            String spLine = SharePreferenceUtil.getDomain(mContext);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogQuickAdapter.addData(new CheckLog(CHECK_SUCCESS, getString(R.string.checking_line_success, spLine), ""));
                    mTvLoading.setText(getString(R.string.checkOk));
                }
            });

            // String spLine = (String) SPTool.get(this, Const.KEY_DOMAIN, "");
            if (TextUtils.isEmpty(spLine)) {
                line = getSslUrl(line);     // -- del
                //   SPTool.put(context, Const.KEY_DOMAIN, line);
                SharePreferenceUtil.saveDomain(mContext, line);
                LogUtils.e("checkSpLine() --> SharePreferenceUtil.saveDomain= " + line);
            }

            DataCenter.getInstance().setDomain(line);
            if (BuildConfig.DEBUG) {
                DataCenter.getInstance().setDomain(BuildConfig.HOST_URL);
            }
            jumpActivity();
        }
    }

    /**
     * 获取SSL链接
     */
    private String getSslUrl(String line) {
        if (isHttps) {
            if (line.startsWith("http:")) {
                line = line.replace("http", "https");
            } else if (!line.startsWith("https:")) {
                if (BuildConfig.DEBUG) { // isDebug
                    line = String.format("http://%s", line);
                } else {
                    line = String.format("https://%s", line);
                }
            }
        }
        return line;
    }


    private synchronized void jumpActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mIsJumpActivity) return;
                mIsJumpActivity = true;
                if (mFetchLineTaskList != null) {
                    for (FetchLineTask task : mFetchLineTaskList) {
                        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                            task.cancel(true);
                        }
                    }
                }

                // UploadErrorLinesUtil.upload(mDomains.toString(), mErrorMessages.toString(), mCodes.toString());
                //自动登录且跳主界面 或者 跳主界面
                AutoLogin.loginOrGoMain(SplashActivity.this, mTvLoading);
            }
        });
    }

    /**
     * 查询线路域名
     */
    private void fetchLines() {
//        Map<String, String> params = new HashMap<>(2);
//        params.put("code", getResources().getString(R.string.app_code));
//        params.put("s", getResources().getString(R.string.app_sid));

        for (int i = 0; i < 3; i++) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newBuilder().sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.TrustAllManager());
            RequestBody body = new FormBody.Builder()
                    .add("code", getResources().getString(R.string.app_code))
                    .add("s", getResources().getString(R.string.app_sid))
                    .build();
            Request request = new Request.Builder().url(fecthUrl[i]).post(body).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLogQuickAdapter.addData(new CheckLog(GET_FAIL, getString(R.string.get_checking_line_fail, e.getMessage()), mark));
                            errorPrompt(e.getMessage());
                        }
                    });

                    LogUtils.e(e.getMessage());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    fillterLines(response);
                }
            });
        }

    }

    void fillterLines(Response response) {
        String data = "";
        try {
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> lines = IJson.fromJson(data, List.class);

        if (BuildConfig.DEBUG) {
            DataCenter.getInstance().setDomain(BuildConfig.HOST_URL);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvLoading.setText(getString(R.string.checkOk));
                    jumpActivity();
                }
            });
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

        for (String line : lines) {
            if (!isGoon) break;
            if (invalidUrl != null && invalidUrl.endsWith(line)) continue;

            if (isGoon && line != null) {
                LogUtils.e("检测域名: " + line);
                String finalLine = line;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLogQuickAdapter.addData(new CheckLog(GET_SUCCESS, getString(R.string.get_checking_line_success, finalLine)));
//                    }
//                });

                FetchLineTask task;
                if (BuildConfig.DEBUG) {
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


    @SuppressLint("StaticFieldLeak")
    private class FetchLineTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
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
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mLogQuickAdapter.addData(new CheckLog(CHECKING, getString(R.string.checking_line, line)));
//                }
//            });

            String url = line + "/__check";
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newBuilder().sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.TrustAllManager());

            Request request = new Request.Builder().url(url).get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLogQuickAdapter.addData(new CheckLog(CHECK_FAIL, line, mark));
                            String msg = "1.域名（" + line + "）不可用";
                            LogUtils.e(msg);
                            appendErrorLine(line, e.getMessage(), "000");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.request().isHttps())
                        isHttps = true;
                    String code = getStatusCode(response);
                    if (CodeEnum.OK.getCode().equals(code)) {
                        isGoon = false;
                        for (FetchLineTask task : mFetchLineTaskList) {
                            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                                task.cancel(true);
                            }
                        }
                        LogUtils.i("1.域名（" + line + "）可用");
                        gotoMain(line);
                    } else {
                        appendErrorLine(line, "域名不可用", code);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mLogQuickAdapter.addData(new CheckLog(CHECK_SUCCESS, response.toString(), mark));
//                            }
//                        });
                        LogUtils.i("checkLine --> response = " + response);
                    }
                }
            });

        } else {
            fetchLines();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAvi.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (FetchLineTask fetchLineTask : mFetchLineTaskList) {
            if (fetchLineTask != null) {
                fetchLineTask.cancel(true);
            }
        }
        if (isGoon) {
            UploadErrorLinesUtil.upload(mDomains.toString(), mErrorMessages.toString(), mCodes.toString(), mark);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAvi.hide();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 网络请求异常提示
     *
     * @param msg
     */
    private void errorPrompt(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        if (msg == null) return;
        if (msg.contains("associated")) {
            ToastUtil.showToastShort(this, getString(R.string.unNet));
        } else {
            if (!msg.contains(BuildConfig.BASE_URL)) {
                ToastUtil.showToastShort(this, msg);
            }
        }
    }


    /**
     * 错误代码
     * Created by fei on 17-7-29.
     */
    public enum CodeEnum {
        OK("OK", "请求正确"),
        SUCCESS("200", "请求成功"),
        S_DUE("600", "Session过期"),
        S_KICK_OUT("606", "Session过期"),
        DUE("604", "域名过期");

        private String code;
        private String name;

        CodeEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

     /*   @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder((BaseViewHolder) holder,position);
        }*/
    }
}
