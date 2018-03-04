package com.dawoo.gamebox.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dawoo.gamebox.R;
import com.dawoo.gamebox.base.BaseActivity;
import com.dawoo.gamebox.common.Constants;
import com.dawoo.gamebox.common.MyApplication;
import com.dawoo.gamebox.enums.SchemeEnum;
import com.dawoo.gamebox.tool.CommonWVClient;
import com.dawoo.gamebox.tool.DateTool;
import com.dawoo.gamebox.tool.FileTool;
import com.dawoo.gamebox.tool.ToastTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.dawoo.gamebox.common.MyApplication.domain;

public class PayActivity extends BaseActivity {

    private static final String TAG = PayActivity.class.getSimpleName();

    // region 组件及变量定义
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.wvPay)
    WebView wvPay;
    // endregion
    @BindView(R.id.ibtnBack)
    ImageView ibtnBack;
    @BindView(R.id.ibtnBack2)
    ImageView ibtnBack2;
    @BindView(R.id.tvBack)
    TextView tvBack;
    private MyHandler handler;
    private String imgUrl;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PayActivity instance;
    private int code = 0;

    @Override
    public int initView() {
        return R.layout.activity_pay;
    }

    @Override
    public void init() {
        handler = new MyHandler(this);
        instance = PayActivity.this;
        // 载入Web资源
        loadWeb();
        changeTitleBack(ibtnBack, ibtnBack2, tvBack);
    }

    /**
     * 载入Web资源
     */
    private void loadWeb() {
        String url = getIntent().getStringExtra("url");

        Log.i(TAG, "loading start url ==> " + url);

        this.webSetting(wvPay);

        wvPay.addJavascriptInterface(new InJavaScriptLocalObj(), Constants.JS_OBJECT);
        wvPay.setWebViewClient(new MyWebViewClient());
        wvPay.setWebChromeClient(new MyWebChromeClient());
        if (!url.contains("http"))
            wvPay.loadUrl(domain + url);
        else
            wvPay.loadUrl(url);

    }

    @Override
    protected void webSetting(WebView webView) {
        super.webSetting(webView);
        //  webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString().replace("app_android", "Android"));
    }

    private static class MyHandler extends Handler {
        WeakReference<PayActivity> mActivity;

        MyHandler(PayActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


    private final class MyWebViewClient extends CommonWVClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "thirdPay url ==> " + url);
            if (url == null) return false;
            try {
                if (url.startsWith(SchemeEnum.INTENT.getCode()) || url.startsWith(SchemeEnum.QQ.getCode())
                        || url.startsWith(SchemeEnum.ALIPAY.getCode()) || url.startsWith(SchemeEnum.WECHAT.getCode())) {

                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    intent.setSelector(null);
                    List<ResolveInfo> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
                    if (resolves.size() > 0) {
                        startActivityIfNeeded(intent, -1);
                    }
                } else if (url.startsWith("https://payh5.bbnpay.com/") || isZXPay(url) || isGaotongPay(url) || isAimiSenPay(url) || isXingPay(url) || isSite322(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    wvPay.loadUrl(url);
                }
            } catch (Exception e) {
                Log.e(TAG, "跳转支付页面异常 ==> " + e.getMessage());
            }

            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }


    }

    private final class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tvTitle.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == progressBar.getVisibility()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(progress);
            }
            super.onProgressChanged(view, progress);
        }


    }

    @OnClick(R.id.rlBank)
    public void goBack(View view) {
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvPay.canGoBack()) {
            wvPay.goBack();
            return true;
        } else {
            this.finish();
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvPay.stopLoading();
        wvPay.removeAllViews();
        wvPay.destroy();
        wvPay = null;
    }

    boolean isZXPay(String url) {
        // String code = getString(R.string.app_code);
        if (url.startsWith("https://zhongxin.junka.com/")) {
            return true;
        }
        return false;
    }

    boolean isGaotongPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("http://wgtj.gaotongpay.com/")) {
            return true;
        }
        return false;
    }

    boolean isAimiSenPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("https://www.joinpay.com")) {
            return true;
        }
        return false;
    }

    boolean isXingPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("https://gate.lfbpay.com")) {
            return true;
        }
        return false;
    }

    boolean isSite271(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("http://pay.88vipbet.com/onlinePay")) {
            return true;
        }
        return false;
    }

    boolean isSite322(String url) {
        if (url.startsWith("http://pay.tsv02.com/onlinePay")) {
            return true;
        }
        return false;
    }

    private final class InJavaScriptLocalObj {
        // 保存图片（二维码）
        @JavascriptInterface
        public void saveImage(final String href) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!StringUtils.isEmpty(href)) {
                            imgUrl = href.contains(domain) ? href : domain + "/" + href;
                            Log.i(TAG, "image url ==> " + imgUrl);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // 检查该权限是否已经获取
                                int hasAuth = ContextCompat.checkSelfPermission(PayActivity.this, permissions[0]);
                                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                                if (hasAuth != PackageManager.PERMISSION_GRANTED) {
                                    // 提交请求权限
                                    ActivityCompat.requestPermissions(PayActivity.this, permissions, 321);
                                } else {
                                    handleImage();
                                }
                            } else {
                                handleImage();
                            }
                        } else {
                            ToastTool.show(instance, R.string.imageUrlNil);
                        }
                    } catch (Exception e) {
                        ToastTool.show(instance, R.string.saveImgFailed);
                        Log.e(TAG, "saveImage ==> " + e.getMessage());
                    }
                }
            });
        }
    }

    private void handleImage() {
        final String[] files = setImage();
        OkHttpUtils.get().url(imgUrl).build().execute(new FileCallBack(files[0], files[1]) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "saveImage error ==> " + e.getMessage());
                ToastTool.show(instance, R.string.saveImgFailed);
            }

            @Override
            public void onResponse(File response, int id) {
                ToastTool.show(instance, R.string.saveImgSuccess);
                fileScan(files[2]);
            }
        });
    }

    private String[] setImage() {
        String[] files = new String[3];

        String saveDir = FileTool.getSDCardDir() + "/Pictures";
        if (!FileTool.dirIsExists(saveDir)) {
            FileTool.makeDir(saveDir, false);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("pay_code_").append(DateTool.getCurrentDate(DateTool.FMT_DATE_TIME_3)).append(".png");
        files[0] = saveDir;
        files[1] = sb.toString();
        files[2] = saveDir + "/" + sb.toString();

        return files;
    }

    // 保存后扫描文件
    private void fileScan(String filePath) {
        Uri data = Uri.parse("file://" + filePath);
        instance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "resultCode ==> " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_2_BETACTIVITY) {   //跳转至登录的返回
            code = resultCode;
        } else if (0 == requestCode || 1 == requestCode) {

            if (MyApplication.isLogin == true) {
                if (data != null && "ok".equals(data.getStringExtra("loginSuccess")) && resultCode <= Constants.RESULT_LOGIN_FAIL) {
                    Intent intent = new Intent();
                    intent.putExtra("loginSuccess", "ok");
                    setResult(Constants.RESULT_2_HOME, intent);
                    finish();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleImage();
            }
        }
    }

}
