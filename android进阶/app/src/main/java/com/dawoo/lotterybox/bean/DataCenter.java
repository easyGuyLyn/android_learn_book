package com.dawoo.lotterybox.bean;

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
        mUser.setImgDomain(user.getImgDomain());
        mUser.setLogin(user.isLogin());
        mUser.setUserId(user.getUserId());
        mUser.setUsername(user.getUsername());
        mUser.setPassword(user.getPassword());
        mUser.setNickname(user.getNickname());
        mUser.setBalance(user.getBalance());
        mUser.setAvatarUrl(user.getAvatarUrl());
        mUser.setToken(user.getToken());
        mUser.setRefreshToken(user.getRefreshToken());
        mUser.setExpire(user.getExpire());
    }

    public User getUser() {
        return mUser;
    }

    public User setDomain(String domain) {
        mUser.setDomain(domain);
        return mUser;
    }

    public User setImgDomain(String domain) {
        mUser.setImgDomain(domain);
        return mUser;
    }


    public String getDomain() {
        return mUser.getDomain();
    }

    public SysInfo getSysInfo() {
        return mSysInfo;
    }


}
