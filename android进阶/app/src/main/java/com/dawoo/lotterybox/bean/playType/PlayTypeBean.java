package com.dawoo.lotterybox.bean.playType;

import java.util.List;

/**
 * Created by b on 18-2-19.
 * 官方玩法
 */

public class PlayTypeBean {

    private String totalType; //总类型   如：官方
    private String parentTitle;//大类
    private String type; //小类

    private List<PlayBean> mPlayBeans;

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public String getTotalType() {
        return totalType;
    }

    public void setTotalType(String totalType) {
        this.totalType = totalType;
    }

    public List<PlayBean> getPlayBeans() {
        return mPlayBeans;
    }

    public void setPlayBeans(List<PlayBean> playBeans) {
        mPlayBeans = playBeans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




//    {
//        "backAccStatus": "0",
//            "backAccount": "0/13%",
//            "playTypeCode":"one_digital";//玩法代号
//            "id": "52",
//            "isSelected": false,
//            "mainType": "二星",
//            "odds": "0.980/0.850",
//            "playTypeAb": "后二直选跨度A面",
//            "playTypeExplain": "从0-9中选择1个号码，所选数值等于开奖号码的后2位最大与最小数字相减之差，即为中奖。",
//            "playTypeName": "跨度A面",
//            "prizeBet": "1",
//            "prizeLevel": "",
//            "sample": "投注方案：跨度9[br]开奖号码：后二位90或09，即中后二直选。",
//            "scheme": "后二直选",
//            "singleBet": "1",
//            "singleExplain": "竞猜后两位开奖号码之差",
//            "totalBet": "100"
//    }


    public static class PlayBean{
        private String mainType;//主类型
        private String betCode; //具体玩法代号
        private String playTypeName;//玩法名称
        private String playTypeCode;//玩法代号
        private String totalBet;//总注数
        private String prizeBet;//中奖注数
        private String odds;//赔率
        private String backAccount;//返水
        private String singleBet;//单注金额
        private String scheme;//方案
        private String sample;//示例
        private String playTypeExplain;//玩法说明
        private String singleExplain;//简略说明
        private String prizeLevel;//奖级
        private String playTypeAb;//玩法缩写
        private String backAccStatus;//返水状态

        private List<LayoutBean> mLayoutBeans; //布局


        private boolean isSelected;//界面上是否被选择

        public String getBetCode() {
            return betCode;
        }

        public void setBetCode(String betCode) {
            this.betCode = betCode;
        }

        public List<LayoutBean> getLayoutBeans() {return mLayoutBeans;}

        public void setLayoutBeans(List<LayoutBean> layoutBeans) { mLayoutBeans = layoutBeans;}

        public void setPlayTypeCode(String playTypeCode) { this.playTypeCode = playTypeCode;  }

        public String getPlayTypeCode() {return playTypeCode;}

        public String getMainType() {
            return mainType;
        }

        public void setMainType(String mainType) {
            this.mainType = mainType;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getTotalBet() {
            return totalBet;
        }

        public void setTotalBet(String totalBet) {
            this.totalBet = totalBet;
        }

        public String getPrizeBet() {
            return prizeBet;
        }

        public void setPrizeBet(String prizeBet) {
            this.prizeBet = prizeBet;
        }

        public String getOdds() {
            return odds;
        }

        public void setOdds(String odds) {
            this.odds = odds;
        }

        public String getBackAccount() {
            return backAccount;
        }

        public void setBackAccount(String backAccount) {
            this.backAccount = backAccount;
        }

        public String getSingleBet() {
            return singleBet;
        }

        public void setSingleBet(String singleBet) {
            this.singleBet = singleBet;
        }

        public String getSample() {
            return sample;
        }

        public void setSample(String sample) {
            this.sample = sample;
        }

        public String getPlayTypeExplain() {
            return playTypeExplain;
        }

        public void setPlayTypeExplain(String playTypeExplain) {
            this.playTypeExplain = playTypeExplain;
        }

        public String getSingleExplain() {
            return singleExplain;
        }

        public void setSingleExplain(String singleExplain) {
            this.singleExplain = singleExplain;
        }

        public String getPrizeLevel() {
            return prizeLevel;
        }

        public void setPrizeLevel(String prizeLevel) {
            this.prizeLevel = prizeLevel;
        }

        public String getPlayTypeAb() {
            return playTypeAb;
        }

        public void setPlayTypeAb(String playTypeAb) {
            this.playTypeAb = playTypeAb;
        }

        public String getBackAccStatus() {
            return backAccStatus;
        }

        public void setBackAccStatus(String backAccStatus) {
            this.backAccStatus = backAccStatus;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getPlayTypeName() {
            return playTypeName;
        }

        public void setPlayTypeName(String playTypeName) {
            this.playTypeName = playTypeName;
        }

        public static class LayoutBean{
            public int getLayoutType() {
                return layoutType;
            }

            public void setLayoutType(int layoutType) {
                this.layoutType = layoutType;
            }

            public String getLayoutTitle() {
                return layoutTitle;
            }

            public void setLayoutTitle(String layoutTitle) {
                this.layoutTitle = layoutTitle;
            }

            public int getShowRightMenuType() {
                return showRightMenuType;
            }

            public void setShowRightMenuType(int showRightMenuType) {
                this.showRightMenuType = showRightMenuType;
            }

            public int getChildItemCount() {
                return childItemCount;
            }

            public void setChildItemCount(int childItemCount) {
                this.childItemCount = childItemCount;
            }

            public List<ChildLayoutBean> getChildLayoutBeans() {
                return mChildLayoutBeans;
            }

            public void setChildLayoutBeans(List<ChildLayoutBean> childLayoutBeans) {
                mChildLayoutBeans = childLayoutBeans;
            }
            public int getStartNumber() {
                return startNumber;
            }

            public void setStartNumber(int startNumber) {
                this.startNumber = startNumber;
            }

            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }

            private int layoutType; //布局类型  0-常规   2-输入框布局  3-其他类型待添加
            private String layoutTitle;//布局标题 如：万位
            private int showRightMenuType; //控制布局右侧（全大小双单清）的显示   0-全显    1-全清   2- 全隐藏
            private int startNumber;   //一些布局里号码从1开始（如和值）；
            private int childItemCount;   //控制布局内有recyclerView时的item数量
            private int itemType; // item的类型，  0- 球   1-方框  3-其它
            private List<ChildLayoutBean> mChildLayoutBeans;

            public static class ChildLayoutBean{
                private String number; //数字
                private String numberRelevant = ""; //冷热或遗漏

                private boolean isSelected; //是否被选中

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getNumberRelevant() {
                    return numberRelevant;
                }

                public void setNumberRelevant(String numberRelevant) {
                    this.numberRelevant = numberRelevant;
                }

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }

            }
        }

    }



}
