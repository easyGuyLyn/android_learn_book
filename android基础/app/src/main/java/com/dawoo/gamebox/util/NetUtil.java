package com.dawoo.gamebox.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;

import com.dawoo.coretool.LogUtils;
import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.bean.DataCenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by benson on 18-1-3.
 */

public class NetUtil {
    public static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        if(userAgent == null || "".equals(userAgent)) {
            userAgent = android.webkit.WebSettings.getDefaultUserAgent(context);
        }


        String ua = userAgent.replace("Android", "app_android");
        userAgent = ua + "; app_version=v3.0";

        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @NonNull
    public static Map<String, String> setHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("User-Agent", "app_android;Android");
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = DataCenter.getInstance().getCookie();
        LogUtils.e(String.format("请求的Cookie --> %s", cookie));
        if (cookie != null && cookieManager != null) {
            headers.put("Cookie", cookie);
            cookieManager.setCookie(DataCenter.getInstance().getDomain(), cookie);
        }
        return headers;
    }

    /**
     * 登录后设置cookie
     */
    public static void setCookie(Response response) {
        List<String> cookies = response.headers().values("Set-Cookie");
        for (String cookie : cookies) {
            if (cookie.contains("SID=") && cookie.length() > 80) {
                LogUtils.e("登录后Cookie ==> " + cookie);
                DataCenter.getInstance().setCookie(cookie);
                CookieManager.getInstance().setCookie(DataCenter.getInstance().getDomain(), cookie);
            }
        }
    }

    /**
     * 这个两个在 API level 21 被抛弃
     * CookieManager.getInstance().removeSessionCookie();
     * CookieManager.getInstance().removeAllCookie();
     * <p>
     * 推荐使用这两个， level 21 新加的
     * CookieManager.getInstance().removeSessionCookies();
     * CookieManager.getInstance().removeAllCookies();
     **/
    public static void removeCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(BoxApplication.getContext());
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     */
    public static boolean syncCookie(String url, String cookie) {
        String domain = DataCenter.getInstance().getDomain();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(BoxApplication.getContext());
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(domain, cookie);
        String newCookie = cookieManager.getCookie(domain);
        return !TextUtils.isEmpty(newCookie);
    }

    public static String handleUrl(String domain, String url) {
        // 是不是http开头
        // 第一个字符是不是包含反斜杠
        if (url == null) {
            return "";
        }
        if (!url.contains("http")) {
            if (url.indexOf("/") == 0) {
                return (domain + url);
            } else {
                return (domain + "/" + url);
            }
        }

        return url;
    }
}
