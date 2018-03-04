package com.dawoo.gamebox;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.dawoo.gamebox.bean.DataCenter;
import com.dawoo.gamebox.util.ActivityUtil;
import com.dawoo.gamebox.util.BackGroundUtil;
import com.dawoo.gamebox.util.CrashHandler;
import com.dawoo.gamebox.util.SSLUtil;
import com.dawoo.gamebox.util.SoundUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by benson on 17-12-27.
 */

public class BoxApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        context = getApplicationContext();

        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        //设置https
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //    .cookieJar(cookieJar)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        OkHttpUtils.initClient(client);
        BackGroundUtil.registerActivityLifecycleCallbacks(this);
        CrashHandler.getInstance().init(this);

        DataCenter.getInstance().getSysInfo().initSysInfo(context);
        ActivityUtil.setContext(context);
        // 加载音频
        SoundUtil.getInstance().load(R.raw.anjian);

        loadX5();
    }


    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 加载x5
     */
    void loadX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("loadX5", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
