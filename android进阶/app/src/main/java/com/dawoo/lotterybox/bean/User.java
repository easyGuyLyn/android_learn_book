package com.dawoo.lotterybox.bean;

/**
 * Created by benson on 17-12-21.
 */

public class User extends LoginBean {

    /**
     * 域名线路
     */
    private String domain;

    /**
     * 图片的服务器域名
     */
    private String imgDomain;

    /**
     * 是否登录
     */
    private boolean isLogin;

    /**
     * 玩家id
     */
    private long userId;
    /**
     * 玩家账号
     */
    private String username;
    /**
     * 用户的登录密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 玩家钱包余额
     */
    private String balance;

    /**
     * 头像相对路径
     */
    private String avatarUrl;


    public User() {
    }

    public User(String domain, String imgDomain, boolean isLogin, long userId, String username, String password, String nickname, String balance, String avatarUrl) {
        this.domain = domain;
        this.imgDomain = imgDomain;
        this.isLogin = isLogin;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.balance = balance;
        this.avatarUrl = avatarUrl;
    }

    public String getImgDomain() {
        return imgDomain;
    }

    public void setImgDomain(String imgDomain) {
        this.imgDomain = imgDomain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;

    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

