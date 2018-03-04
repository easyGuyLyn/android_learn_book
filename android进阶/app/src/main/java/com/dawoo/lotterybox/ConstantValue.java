package com.dawoo.lotterybox;

/**
 * 常量
 */

public interface ConstantValue {
//    /**
//     * 获取域名请求地址
//     */
//    String BASE_URL = "https://apiplay.info:1344/boss/";
//    //String BASE_URL = "http://192.168.0.92/boss/"; //测试
    /**
     * 线路域名URL
     */
    String LINE_URL = "/app/line.html";
    /**
     * 登录URL
     */
    String LOGIN_URL = "/passport/login.html";
    /**
     * 注册URL
     */
    String REGISTER_URL = "/signUp/index.html";
    /**
     * 验证码URL
     */
    String CAPTCHA_URL = "/captcha/code.html";

    /**
     * 发送消息的验证码
     */
    String CAPTCHA_URL_MY_MSG = "/captcha/feedback.html";
    /**
     * 错误线路收集接口
     */
    String COLLECT_APP_DOMAINS = "/facade/collectAppDomainError.html";
    /**
     * 客服
     */
    String SERVICE_URL = "/index/getCustomerService.html";

    String WEBVIEW_URL = "WEBVIEW_URL";
    String WEBVIEW_TYPE = "WEBVIEW_TYPE";
    String WEBVIEW_TYPE_GAME = "WEBVIEW_TYPE_GAME";
    String WEBVIEW_TYPE_NO_VIDEO_GAME = "WEBVIEW_TYPE_NO_VIDEO_GAME";
    String WEBVIEW_TYPE_ORDINARY = "WEBVIEW_TYPE_ORDINARY";
    String WEBVIEW_TYPE_PAY = "WEBVIEW_TYPE_PAY";
    String KEY_USERNAME = "KEY_USERNAME";
    String KEY_PASSWORD = "KEY_PASSWORD";
    String KEY_NEED_CAPTCHA = "KEY_NEED_CAPTCHA";
    String KEY_CAPTCHA_TIME = "KEY_CAPTCHA_TIME";
    String KEY_CUSTOMER_SERVICE = "KEY_CUSTOMER_SERVICE";
    String KEY_TIME_ZONE = "KEY_TIME_ZONE";
    int KEY_REGIST_BACK_LOGIN = 23333;

    /**
     * 登录后
     */
    String EVENT_TYPE_LOGINED = "EVENT_TYPE_LOGINED";
    /**
     * 跳转到存款
     */
    String EVENT_TYPE_GOTOTAB_DEPOSIT = "EVENT_TYPE_GOTOTAB_DEPOSIT";
    /**
     * 一键回收
     */
    String EVENT_TYPE_ONE_KEY_BACK = "EVENT_TYPE_ONE_KEY_BACK";

    /**
     * 我的链接和用户数据
     */
    String EVENT_TYPE_MINE_LINK = "EVENT_TYPE_MINE_LINK";
    /**
     * 申请优惠  选中申请优惠类型
     */
    String EVENT_TYPE_APP_PREF = "EVENT_TYPE_APP_PREF";
    /**
     * 登出帐户
     */
    String EVENT_TYPE_LOGOUT = "EVENT_TYPE_LOGOUT";
    /**
     * 賬戶更新
     */
    String EVENT_TYPE_ACCOUNT = "EVENT_TYPE_ACCOUNT";
    /**
     * 接口失败，抛出异常
     * 页面有刷新或者加载更多，或者页面要在抛出异常处理一些事物
     */
    String EVENT_TYPE_NETWORK_EXCEPTION = "EVENT_TYPE_NETWORK_EXCEPTION";
    /**
     * 刷新我的界面用户数据
     */
    String EVENT_TYPE_REFRESH_MINE_DATA = "EVENT_TYPE_REFRESH_MINE_DATA";

    /**
     * 接口单页请求数据条数
     */
    int RECORD_LIST_PAGE_SIZE = 12;


    int RECORD_LIST_page_Number = 1;


    String THEME_COLOR_BLUE = "white";
    String THEME_COLOR_RED = "red";
    String THEME_COLOR_GREEN = "green";
    String THEME_COLOR_BALCK = "black";

    String APP_KEY = "Ap4P4G1QF7lHgg==";
    String APP_SECRET = "e5a6dbf1b5205f1ac1f5e668f8d4c9a7";

    String VOICE_ON_CLICK = "VOICE_ON_CLICK";
}
