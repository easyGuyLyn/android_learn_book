package com.dawoo.lotterybox;

import android.app.Application;
import android.content.Context;

import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.lotterybox.bean.DataCenter;
import com.dawoo.lotterybox.util.SoundUtil;
import com.dawoo.lotterybox.util.lottery.initdata.CQSSCGFWanFaUtil;
import com.squareup.leakcanary.LeakCanary;

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

        DataCenter.getInstance().setDomain(BuildConfig.HOST_URL).setImgDomain(BuildConfig.HOST_IMG_URL);
        DataCenter.getInstance().getSysInfo().initSysInfo(
                PackageInfoUtil.getVersionName(context),
                PackageInfoUtil.getVersionCode(context),
                PackageInfoUtil.getUniqueId(context));

//        AssetsReader.readCsv(this);//初始化 assets文件    改为json文件
//        CQSSCGFWanFaUtil gfWanFaUtil = new CQSSCGFWanFaUtil();
//        gfWanFaUtil.initData();  //初始化时时彩玩法源数据

        SoundUtil.getInstance().load(ConstantValue.VOICE_ON_CLICK, R.raw.anjian);
    }


    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return context;
    }

}
