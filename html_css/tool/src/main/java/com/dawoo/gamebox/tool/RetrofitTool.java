package com.dawoo.gamebox.tool;

import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.constant.URLConst;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 网络工具
 * Create by Fei on 16-10-16.
 */
public class RetrofitTool {
    public static <T> T createApi(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URLConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(setHttpClient())
                .build();
        return retrofit.create(clazz);
    }
    public static <T> T login(Class<T> clazz ,String domain) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(domain)
                .addConverterFactory(GsonConverterFactory.create())
                .client(setHttpClient())
                .build();
        return retrofit.create(clazz);
    }

    private static OkHttpClient setHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Const.TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(Const.TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Const.TIME_OUT, TimeUnit.SECONDS).build();
    }
}