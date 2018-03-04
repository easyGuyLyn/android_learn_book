package com.dawoo.gamebox.bean;

/**
 * 数据中心
 * Created by benson on 18-1-26.
 */

public class DataCenter {
    private static DataCenter instance;
    private static User mUser = new User();
    private static SysInfo mSysInfo = new SysInfo();

    public static DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }


    public void setUser(User user) {
        mUser.setDomain(user.getDomain());
        mUser.setCookie(user.getCookie());
        mUser.setLogin(user.isLogin);
        mUser.setUsername(user.getUsername());
        mUser.setPassword(user.getPassword());
    }
    public User getUser() {
        return mUser;
    }

    public String getDomain() {
        return mUser.getDomain();
    }

    public void setDomain(String domain) {
        mUser.setDomain(domain);
    }

    public String getCookie() {
        return mUser.getCookie();
    }

    public void setCookie(String cookie) {
        mUser.setCookie(cookie);
    }

    public boolean isLogin() {
        return mUser.isLogin();
    }

    public void setLogin(boolean isLogin) {
        mUser.setLogin(isLogin);
    }


    public String getUserName() {
        return mUser.getUsername();
    }

    public void setUserName(String userName) {
        mUser.setUsername(userName);
    }

    public String getPassword() {
        return mUser.getPassword();
    }

    public void setPassword(String password) {
        mUser.setPassword(password);
    }





    public SysInfo getSysInfo() {
        return mSysInfo;
    }


}
