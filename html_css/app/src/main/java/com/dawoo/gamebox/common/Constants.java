package com.dawoo.gamebox.common;

import com.dawoo.gamebox.enums.ThemeEnum;

/**
 * APP 常量
 * Created by fei on 16-12-8.
 */
public final class Constants {
    // 站点一： 1       0000        d29a1a9691ee59b43f4460e8ec35406e
    // 飞站：  11      FHAN        a8bdc0c6e6c3c9570d051aa65ef10d3b
    // 测试1： 21      rf80        632fbc122abfffdec078a665a76a1c16
    // 超二：  71      8l6r        6bf3b743e414045e5123fc317fb9b535

    /**
     * 版本更新秘钥，当前版本号（version_code）通过后台加密得到
     */
    public static final String KEY_CODE = "key.update";

    public static final String THEME_KEY = "theme";
    public static final String THEME_DEFAULT = ThemeEnum.DEFAULT.getCode();

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public static final int RESULT_2_HOME = 100;
    public static final int RESULT_2_DEPOSIT = 101;
    public static final int RESULT_2_TRANSFER = 102;
    public static final int RESULT_2_MINE = 104;
    public static final int RESULT_LOGIN_FAIL = 99;
    public static final int REGISTER_SUCCESS = 201;
    public static final int REGISTER_REFRESH = 202;

    public static final int RESULT_2_LOTTERY = 302;
    public static final int RESULT_2_HALL = 303;
    public static final int RESULT_2_BET = 304;

    public static final int REQUEST_2_BETACTIVITY = 90;
    public static final int BETACTIVITY_2_DEPOSIT = 91;
    public static final int BETACTIVITY_2_BET = 92;
    public static final int BETACTIVITY_2_MINE = 93;
    public static final int BETACTIVITY_2_HOME = 400;
    public static final int BETACTIVITY_2_HALL = 401;
    public static final int BETACTIVITY_2_MESSAGE = 402;

    public static final int LOGOUT_RESULT = 80;

    public static final String RESULT_FLAG = "_result_flag";
    public static final String KEY_NEED_CAPTCHA = "_need_captcha";
    /** 取消验证码时间 */
    public static final String KEY_CAPTCHA_TIME = "_captcha_time";

    public static final String KEY_LOTTERY_SITE = "_is_lottery_site";
    public static final String KEY_THEME_PATH = "_theme_path";

    public static final String APP_TYPE = "android";

    public static final String JS_OBJECT = "gamebox";

    public static final String KEY_CUSTOMER_SERVICE = "_customer_service";
    /** 是否第一次启动应用 */
    public static final String KEY_IS_FIRST = "_is_first";

    public static final int FILE_CHOOSER_RESULT_CODE = 1;
}