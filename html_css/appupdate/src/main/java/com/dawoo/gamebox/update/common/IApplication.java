package com.dawoo.gamebox.update.common;

import android.app.Application;

/**
 *
 * Created by fei on 2017/8/9.
 */
public class IApplication extends Application {

    private static Application app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static void init(Application application){

        app = application;
    }

    public static Application getInstance(){

        return app;
    }
}
