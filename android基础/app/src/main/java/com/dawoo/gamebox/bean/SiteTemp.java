package com.dawoo.gamebox.bean;

/**
 * 临时传值
 * Created by benson on 18-1-11.
 */

public class SiteTemp {
    private boolean level;
    private SiteApiRelation.SiteApisBean.GameListBean gameListBean;
    private SiteApiRelation.SiteApisBean siteApisBean;
    private int position;

    public SiteTemp() {
    }

    public SiteTemp(boolean level, SiteApiRelation.SiteApisBean.GameListBean gameListBean, SiteApiRelation.SiteApisBean siteApisBean, int position) {
        this.level = level;
        this.gameListBean = gameListBean;
        this.siteApisBean = siteApisBean;
        this.position = position;
    }

    public boolean isLevel() {
        return level;
    }

    public void setLevel(boolean level) {
        this.level = level;
    }

    public SiteApiRelation.SiteApisBean.GameListBean getGameListBean() {
        return gameListBean;
    }

    public void setGameListBean(SiteApiRelation.SiteApisBean.GameListBean gameListBean) {
        this.gameListBean = gameListBean;
    }

    public SiteApiRelation.SiteApisBean getSiteApisBean() {
        return siteApisBean;
    }

    public void setSiteApisBean(SiteApiRelation.SiteApisBean siteApisBean) {
        this.siteApisBean = siteApisBean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
