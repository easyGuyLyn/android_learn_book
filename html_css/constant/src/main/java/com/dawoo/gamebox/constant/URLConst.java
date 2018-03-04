package com.dawoo.gamebox.constant;

/**
 * URL 地址
 * Created by fei on 16-12-8.
 */
public final class URLConst {
    /**
     * 获取域名请求地址
     */
    public static final String BASE_URL = "https://apiplay.info:1344/boss/";
    //    public static final String BASE_URL = "http://192.168.0.150/boss/";
//    public static final String BASE_URL = "http://192.168.0.92/boss/";
//    public static final String BASE_URL = "http://192.168.0.159/boss/";

    public static final String BASE_URL_1 = "https://apiplay.info:1344/boss/";
    public static final String BASE_URL_2 = "https://agpicdance.info:1344/boss/";
    public static final String BASE_URL_3 = "https://hpdbtopgolddesign.com:1344/boss/";

    public static final String[] fecthUrl = {BASE_URL_1 + "app/line.html", BASE_URL_2 + "app/line.html", BASE_URL_3 + "app/line.html"};

    /**
     * 线路域名URL
     */
    public static final String LINE_URL = BASE_URL + "app/line.html";
    /**
     * 检查更新URL
     */
    public static final String UPDATE_URL = BASE_URL + "app/update.html";
    /**
     * 强制踢出
     */
    public static final String ERROR_606 = "/errors/606.html";
    /**
     * 访问受限
     */
    public static final String ERROR_605 = "/errors/605.html";

    /**
     * 错误线路收集接口
     */
    public static final String COLLECT_APP_DOMAINS = "facade/collectAppDomainError.html";
}