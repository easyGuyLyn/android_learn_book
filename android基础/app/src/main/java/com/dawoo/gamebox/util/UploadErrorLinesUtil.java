package com.dawoo.gamebox.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.dawoo.gamebox.BoxApplication;
import com.dawoo.gamebox.BuildConfig;
import com.dawoo.gamebox.ConstantValue;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by archar on 18-1-25.
 * <p>
 * 上传所有的错误线路信息
 */

public class UploadErrorLinesUtil {

    private static int count = 0;

    public static void upload(String domains, String errorMessages, String codes, String mark) {
        Log.e("LineUtil  domains", domains);
        Log.e("LineUtil  errorMessages", errorMessages);
        Log.e("LineUtil  codes", codes);
        Log.e("LineUtil  mark", mark);
        if (TextUtils.isEmpty(domains)) {
            return;
        }
        if (count > 0) return;
        String url = "https://apiplay.info:1344/boss" + ConstantValue.COLLECT_APP_DOMAINS;

        String siteId = resolvePackgeName();

        Log.e("LineUtil  siteId", siteId);
        String userName = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_USERNAME, "");
        String lastLoginTime = "";
        String ip = getIPAddress(BoxApplication.getContext());

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.TrustAllManager());

        RequestBody body = new FormBody.Builder()
                .add("siteId", siteId)
                .add("username", userName)
                .add("lastLoginTime", lastLoginTime)
                .add("domain", domains)
                .add("ip", ip)
                .add("errorMessage", errorMessages)
                .add("code", "000")
                .add("mark", mark)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        count++;
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LineUtil  ", "上传错误线路信息失败: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("LineUtil  ", "上传错误线路信息结果 response:" + response.toString());
                Log.e("LineUtil  ", "上传错误线路信息结果 code:" + response.code());
            }
        });

    }


    /**
     * 处理站点id
     *
     * @return
     */
    private static String resolvePackgeName() {
        String packageName = BoxApplication.getContext().getPackageName();

        String subStr = packageName.substring(packageName.indexOf("sid"));
        if (subStr.contains("debug")) {
            return subStr.substring(3, subStr.lastIndexOf("."));
        } else {
            return subStr.substring(3);
        }
    }


    private static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
