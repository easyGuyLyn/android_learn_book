package com.dawoo.gamebox.been;

/**
 * Created by lenovo on 2017/3/14.
 */

public class RefreshBean {

    /**
     * apiId : 1
     * player : {"id":610,"rankId":null,"totalAssets":null,"phoneCode":null,"walletBalance":490,"synchronizationTime":null,"specialFocus":null,"balanceType":null,"balanceFreezeStartTime":null,"balanceFreezeEndTime":null,"freezeCode":null,"balanceFreezeRemark":null,"accountFreezeRemark":null,"rakebackId":null,"level":null,"ohterContactInformation":null,"rakeback":null,"backwashTotalAmount":null,"backwashBalanceAmount":null,"backwashRechargeWarn":null,"transactionSynTime":null,"rechargeCount":null,"rechargeTotal":null,"rechargeMaxAmount":null,"txCount":null,"txTotal":null,"levelLock":null,"totalProfitLoss":null,"totalTradeVolume":null,"totalEffectiveVolume":null,"createChannel":null,"mailStatus":null,"mobilePhoneStatus":null,"isFirstRecharge":null,"manualBackwashTotalAmount":null,"manualBackwashBalanceAmount":null,"nickname":null,"sex":null,"constellation":null,"birthday":null,"country":null,"region":null,"city":null,"nation":null,"createTime":null,"userAgentId":null,"defaultCurrency":"CNY","username":"qwer1234","password":null,"deptId":null,"status":null,"freezeType":null,"freezeStartTime":null,"freezeEndTime":null,"userFreezeCode":null,"registerIp":null,"agentId":null,"agentName":null,"agentRealname":null,"generalAgentName":null,"generalAgentId":null,"generalAgentRealname":null,"realName":null,"defaultLocale":null,"createUser":null,"remarkcount":null,"tagcount":null,"defaultTimezone":null,"rankName":null,"riskMarker":null,"mobilePhone":null,"mail":null,"qq":null,"msn":null,"skype":null,"mobilePhoneWayStatus":null,"mailWayStatus":null,"qqWayStatus":null,"msnWayStatus":null,"skypeWayStatus":null,"remarks":null,"rakebackName":null,"weixin":null,"weixinWayStatus":null,"lastLoginIp":null,"registerIpDictCode":null,"registerSite":null,"loginIp":null,"registCode":null,"loginTime":null,"useLine":null,"loginIpDictCode":null,"recommendUserId":null,"importUsername":null,"favorableTotal":null,"bankcardNumber":null,"memo":null,"updateUser":null,"updateTime":null,"updateUsername":null,"proxySystem":null,"countryCity":null,"playerStatus":"1","rigistLessThanAMonth":true,"onLineId":null,"sessionKey":null,"balanceFreeze":false,"accountfreeze":false,"avatar":null,"freezingBalance":500,"currencySign":"￥","permissionPwd":"49d84f160197d51d307bdc68e85dba13","oldRankId":null}
     * status : normal
     * apiMoney : 10.00
     */

    private int apiId;
    private PlayerBean player;
    private String status;
    private String apiMoney;

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public PlayerBean getPlayer() {
        return player;
    }

    public void setPlayer(PlayerBean player) {
        this.player = player;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApiMoney() {
        return apiMoney;
    }

    public void setApiMoney(String apiMoney) {
        this.apiMoney = apiMoney;
    }

    public static class PlayerBean {
        /**
         * id : 610
         * rankId : null
         * totalAssets : null
         * phoneCode : null
         * walletBalance : 490.0
         * synchronizationTime : null
         * specialFocus : null
         * balanceType : null
         * balanceFreezeStartTime : null
         * balanceFreezeEndTime : null
         * freezeCode : null
         * balanceFreezeRemark : null
         * accountFreezeRemark : null
         * rakebackId : null
         * level : null
         * ohterContactInformation : null
         * rakeback : null
         * backwashTotalAmount : null
         * backwashBalanceAmount : null
         * backwashRechargeWarn : null
         * transactionSynTime : null
         * rechargeCount : null
         * rechargeTotal : null
         * rechargeMaxAmount : null
         * txCount : null
         * txTotal : null
         * levelLock : null
         * totalProfitLoss : null
         * totalTradeVolume : null
         * totalEffectiveVolume : null
         * createChannel : null
         * mailStatus : null
         * mobilePhoneStatus : null
         * isFirstRecharge : null
         * manualBackwashTotalAmount : null
         * manualBackwashBalanceAmount : null
         * nickname : null
         * sex : null
         * constellation : null
         * birthday : null
         * country : null
         * region : null
         * city : null
         * nation : null
         * createTime : null
         * userAgentId : null
         * defaultCurrency : CNY
         * username : qwer1234
         * password : null
         * deptId : null
         * status : null
         * freezeType : null
         * freezeStartTime : null
         * freezeEndTime : null
         * userFreezeCode : null
         * registerIp : null
         * agentId : null
         * agentName : null
         * agentRealname : null
         * generalAgentName : null
         * generalAgentId : null
         * generalAgentRealname : null
         * realName : null
         * defaultLocale : null
         * createUser : null
         * remarkcount : null
         * tagcount : null
         * defaultTimezone : null
         * rankName : null
         * riskMarker : null
         * mobilePhone : null
         * mail : null
         * qq : null
         * msn : null
         * skype : null
         * mobilePhoneWayStatus : null
         * mailWayStatus : null
         * qqWayStatus : null
         * msnWayStatus : null
         * skypeWayStatus : null
         * remarks : null
         * rakebackName : null
         * weixin : null
         * weixinWayStatus : null
         * lastLoginIp : null
         * registerIpDictCode : null
         * registerSite : null
         * loginIp : null
         * registCode : null
         * loginTime : null
         * useLine : null
         * loginIpDictCode : null
         * recommendUserId : null
         * importUsername : null
         * favorableTotal : null
         * bankcardNumber : null
         * memo : null
         * updateUser : null
         * updateTime : null
         * updateUsername : null
         * proxySystem : null
         * countryCity : null
         * playerStatus : 1
         * rigistLessThanAMonth : true
         * onLineId : null
         * sessionKey : null
         * balanceFreeze : false
         * accountfreeze : false
         * avatar : null
         * freezingBalance : 500.0
         * currencySign : ￥
         * permissionPwd : 49d84f160197d51d307bdc68e85dba13
         * oldRankId : null
         */

        private int id;
        private Object rankId;
        private Object totalAssets;
        private Object phoneCode;
        private double walletBalance;
        private Object synchronizationTime;
        private Object specialFocus;
        private Object balanceType;
        private Object balanceFreezeStartTime;
        private Object balanceFreezeEndTime;
        private Object freezeCode;
        private Object balanceFreezeRemark;
        private Object accountFreezeRemark;
        private Object rakebackId;
        private Object level;
        private Object ohterContactInformation;
        private Object rakeback;
        private Object backwashTotalAmount;
        private Object backwashBalanceAmount;
        private Object backwashRechargeWarn;
        private Object transactionSynTime;
        private Object rechargeCount;
        private Object rechargeTotal;
        private Object rechargeMaxAmount;
        private Object txCount;
        private Object txTotal;
        private Object levelLock;
        private Object totalProfitLoss;
        private Object totalTradeVolume;
        private Object totalEffectiveVolume;
        private Object createChannel;
        private Object mailStatus;
        private Object mobilePhoneStatus;
        private Object isFirstRecharge;
        private Object manualBackwashTotalAmount;
        private Object manualBackwashBalanceAmount;
        private Object nickname;
        private Object sex;
        private Object constellation;
        private Object birthday;
        private Object country;
        private Object region;
        private Object city;
        private Object nation;
        private Object createTime;
        private Object userAgentId;
        private String defaultCurrency;
        private String username;
        private Object password;
        private Object deptId;
        private Object status;
        private Object freezeType;
        private Object freezeStartTime;
        private Object freezeEndTime;
        private Object userFreezeCode;
        private Object registerIp;
        private Object agentId;
        private Object agentName;
        private Object agentRealname;
        private Object generalAgentName;
        private Object generalAgentId;
        private Object generalAgentRealname;
        private Object realName;
        private Object defaultLocale;
        private Object createUser;
        private Object remarkcount;
        private Object tagcount;
        private Object defaultTimezone;
        private Object rankName;
        private Object riskMarker;
        private Object mobilePhone;
        private Object mail;
        private Object qq;
        private Object msn;
        private Object skype;
        private Object mobilePhoneWayStatus;
        private Object mailWayStatus;
        private Object qqWayStatus;
        private Object msnWayStatus;
        private Object skypeWayStatus;
        private Object remarks;
        private Object rakebackName;
        private Object weixin;
        private Object weixinWayStatus;
        private Object lastLoginIp;
        private Object registerIpDictCode;
        private Object registerSite;
        private Object loginIp;
        private Object registCode;
        private Object loginTime;
        private Object useLine;
        private Object loginIpDictCode;
        private Object recommendUserId;
        private Object importUsername;
        private Object favorableTotal;
        private Object bankcardNumber;
        private Object memo;
        private Object updateUser;
        private Object updateTime;
        private Object updateUsername;
        private Object proxySystem;
        private Object countryCity;
        private String playerStatus;
        private boolean rigistLessThanAMonth;
        private Object onLineId;
        private Object sessionKey;
        private boolean balanceFreeze;
        private boolean accountfreeze;
        private Object avatar;
        private double freezingBalance;
        private String currencySign;
        private String permissionPwd;
        private Object oldRankId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getRankId() {
            return rankId;
        }

        public void setRankId(Object rankId) {
            this.rankId = rankId;
        }

        public Object getTotalAssets() {
            return totalAssets;
        }

        public void setTotalAssets(Object totalAssets) {
            this.totalAssets = totalAssets;
        }

        public Object getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(Object phoneCode) {
            this.phoneCode = phoneCode;
        }

        public double getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(double walletBalance) {
            this.walletBalance = walletBalance;
        }

        public Object getSynchronizationTime() {
            return synchronizationTime;
        }

        public void setSynchronizationTime(Object synchronizationTime) {
            this.synchronizationTime = synchronizationTime;
        }

        public Object getSpecialFocus() {
            return specialFocus;
        }

        public void setSpecialFocus(Object specialFocus) {
            this.specialFocus = specialFocus;
        }

        public Object getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(Object balanceType) {
            this.balanceType = balanceType;
        }

        public Object getBalanceFreezeStartTime() {
            return balanceFreezeStartTime;
        }

        public void setBalanceFreezeStartTime(Object balanceFreezeStartTime) {
            this.balanceFreezeStartTime = balanceFreezeStartTime;
        }

        public Object getBalanceFreezeEndTime() {
            return balanceFreezeEndTime;
        }

        public void setBalanceFreezeEndTime(Object balanceFreezeEndTime) {
            this.balanceFreezeEndTime = balanceFreezeEndTime;
        }

        public Object getFreezeCode() {
            return freezeCode;
        }

        public void setFreezeCode(Object freezeCode) {
            this.freezeCode = freezeCode;
        }

        public Object getBalanceFreezeRemark() {
            return balanceFreezeRemark;
        }

        public void setBalanceFreezeRemark(Object balanceFreezeRemark) {
            this.balanceFreezeRemark = balanceFreezeRemark;
        }

        public Object getAccountFreezeRemark() {
            return accountFreezeRemark;
        }

        public void setAccountFreezeRemark(Object accountFreezeRemark) {
            this.accountFreezeRemark = accountFreezeRemark;
        }

        public Object getRakebackId() {
            return rakebackId;
        }

        public void setRakebackId(Object rakebackId) {
            this.rakebackId = rakebackId;
        }

        public Object getLevel() {
            return level;
        }

        public void setLevel(Object level) {
            this.level = level;
        }

        public Object getOhterContactInformation() {
            return ohterContactInformation;
        }

        public void setOhterContactInformation(Object ohterContactInformation) {
            this.ohterContactInformation = ohterContactInformation;
        }

        public Object getRakeback() {
            return rakeback;
        }

        public void setRakeback(Object rakeback) {
            this.rakeback = rakeback;
        }

        public Object getBackwashTotalAmount() {
            return backwashTotalAmount;
        }

        public void setBackwashTotalAmount(Object backwashTotalAmount) {
            this.backwashTotalAmount = backwashTotalAmount;
        }

        public Object getBackwashBalanceAmount() {
            return backwashBalanceAmount;
        }

        public void setBackwashBalanceAmount(Object backwashBalanceAmount) {
            this.backwashBalanceAmount = backwashBalanceAmount;
        }

        public Object getBackwashRechargeWarn() {
            return backwashRechargeWarn;
        }

        public void setBackwashRechargeWarn(Object backwashRechargeWarn) {
            this.backwashRechargeWarn = backwashRechargeWarn;
        }

        public Object getTransactionSynTime() {
            return transactionSynTime;
        }

        public void setTransactionSynTime(Object transactionSynTime) {
            this.transactionSynTime = transactionSynTime;
        }

        public Object getRechargeCount() {
            return rechargeCount;
        }

        public void setRechargeCount(Object rechargeCount) {
            this.rechargeCount = rechargeCount;
        }

        public Object getRechargeTotal() {
            return rechargeTotal;
        }

        public void setRechargeTotal(Object rechargeTotal) {
            this.rechargeTotal = rechargeTotal;
        }

        public Object getRechargeMaxAmount() {
            return rechargeMaxAmount;
        }

        public void setRechargeMaxAmount(Object rechargeMaxAmount) {
            this.rechargeMaxAmount = rechargeMaxAmount;
        }

        public Object getTxCount() {
            return txCount;
        }

        public void setTxCount(Object txCount) {
            this.txCount = txCount;
        }

        public Object getTxTotal() {
            return txTotal;
        }

        public void setTxTotal(Object txTotal) {
            this.txTotal = txTotal;
        }

        public Object getLevelLock() {
            return levelLock;
        }

        public void setLevelLock(Object levelLock) {
            this.levelLock = levelLock;
        }

        public Object getTotalProfitLoss() {
            return totalProfitLoss;
        }

        public void setTotalProfitLoss(Object totalProfitLoss) {
            this.totalProfitLoss = totalProfitLoss;
        }

        public Object getTotalTradeVolume() {
            return totalTradeVolume;
        }

        public void setTotalTradeVolume(Object totalTradeVolume) {
            this.totalTradeVolume = totalTradeVolume;
        }

        public Object getTotalEffectiveVolume() {
            return totalEffectiveVolume;
        }

        public void setTotalEffectiveVolume(Object totalEffectiveVolume) {
            this.totalEffectiveVolume = totalEffectiveVolume;
        }

        public Object getCreateChannel() {
            return createChannel;
        }

        public void setCreateChannel(Object createChannel) {
            this.createChannel = createChannel;
        }

        public Object getMailStatus() {
            return mailStatus;
        }

        public void setMailStatus(Object mailStatus) {
            this.mailStatus = mailStatus;
        }

        public Object getMobilePhoneStatus() {
            return mobilePhoneStatus;
        }

        public void setMobilePhoneStatus(Object mobilePhoneStatus) {
            this.mobilePhoneStatus = mobilePhoneStatus;
        }

        public Object getIsFirstRecharge() {
            return isFirstRecharge;
        }

        public void setIsFirstRecharge(Object isFirstRecharge) {
            this.isFirstRecharge = isFirstRecharge;
        }

        public Object getManualBackwashTotalAmount() {
            return manualBackwashTotalAmount;
        }

        public void setManualBackwashTotalAmount(Object manualBackwashTotalAmount) {
            this.manualBackwashTotalAmount = manualBackwashTotalAmount;
        }

        public Object getManualBackwashBalanceAmount() {
            return manualBackwashBalanceAmount;
        }

        public void setManualBackwashBalanceAmount(Object manualBackwashBalanceAmount) {
            this.manualBackwashBalanceAmount = manualBackwashBalanceAmount;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public Object getConstellation() {
            return constellation;
        }

        public void setConstellation(Object constellation) {
            this.constellation = constellation;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public Object getRegion() {
            return region;
        }

        public void setRegion(Object region) {
            this.region = region;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getNation() {
            return nation;
        }

        public void setNation(Object nation) {
            this.nation = nation;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUserAgentId() {
            return userAgentId;
        }

        public void setUserAgentId(Object userAgentId) {
            this.userAgentId = userAgentId;
        }

        public String getDefaultCurrency() {
            return defaultCurrency;
        }

        public void setDefaultCurrency(String defaultCurrency) {
            this.defaultCurrency = defaultCurrency;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Object getDeptId() {
            return deptId;
        }

        public void setDeptId(Object deptId) {
            this.deptId = deptId;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getFreezeType() {
            return freezeType;
        }

        public void setFreezeType(Object freezeType) {
            this.freezeType = freezeType;
        }

        public Object getFreezeStartTime() {
            return freezeStartTime;
        }

        public void setFreezeStartTime(Object freezeStartTime) {
            this.freezeStartTime = freezeStartTime;
        }

        public Object getFreezeEndTime() {
            return freezeEndTime;
        }

        public void setFreezeEndTime(Object freezeEndTime) {
            this.freezeEndTime = freezeEndTime;
        }

        public Object getUserFreezeCode() {
            return userFreezeCode;
        }

        public void setUserFreezeCode(Object userFreezeCode) {
            this.userFreezeCode = userFreezeCode;
        }

        public Object getRegisterIp() {
            return registerIp;
        }

        public void setRegisterIp(Object registerIp) {
            this.registerIp = registerIp;
        }

        public Object getAgentId() {
            return agentId;
        }

        public void setAgentId(Object agentId) {
            this.agentId = agentId;
        }

        public Object getAgentName() {
            return agentName;
        }

        public void setAgentName(Object agentName) {
            this.agentName = agentName;
        }

        public Object getAgentRealname() {
            return agentRealname;
        }

        public void setAgentRealname(Object agentRealname) {
            this.agentRealname = agentRealname;
        }

        public Object getGeneralAgentName() {
            return generalAgentName;
        }

        public void setGeneralAgentName(Object generalAgentName) {
            this.generalAgentName = generalAgentName;
        }

        public Object getGeneralAgentId() {
            return generalAgentId;
        }

        public void setGeneralAgentId(Object generalAgentId) {
            this.generalAgentId = generalAgentId;
        }

        public Object getGeneralAgentRealname() {
            return generalAgentRealname;
        }

        public void setGeneralAgentRealname(Object generalAgentRealname) {
            this.generalAgentRealname = generalAgentRealname;
        }

        public Object getRealName() {
            return realName;
        }

        public void setRealName(Object realName) {
            this.realName = realName;
        }

        public Object getDefaultLocale() {
            return defaultLocale;
        }

        public void setDefaultLocale(Object defaultLocale) {
            this.defaultLocale = defaultLocale;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public Object getRemarkcount() {
            return remarkcount;
        }

        public void setRemarkcount(Object remarkcount) {
            this.remarkcount = remarkcount;
        }

        public Object getTagcount() {
            return tagcount;
        }

        public void setTagcount(Object tagcount) {
            this.tagcount = tagcount;
        }

        public Object getDefaultTimezone() {
            return defaultTimezone;
        }

        public void setDefaultTimezone(Object defaultTimezone) {
            this.defaultTimezone = defaultTimezone;
        }

        public Object getRankName() {
            return rankName;
        }

        public void setRankName(Object rankName) {
            this.rankName = rankName;
        }

        public Object getRiskMarker() {
            return riskMarker;
        }

        public void setRiskMarker(Object riskMarker) {
            this.riskMarker = riskMarker;
        }

        public Object getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(Object mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public Object getMail() {
            return mail;
        }

        public void setMail(Object mail) {
            this.mail = mail;
        }

        public Object getQq() {
            return qq;
        }

        public void setQq(Object qq) {
            this.qq = qq;
        }

        public Object getMsn() {
            return msn;
        }

        public void setMsn(Object msn) {
            this.msn = msn;
        }

        public Object getSkype() {
            return skype;
        }

        public void setSkype(Object skype) {
            this.skype = skype;
        }

        public Object getMobilePhoneWayStatus() {
            return mobilePhoneWayStatus;
        }

        public void setMobilePhoneWayStatus(Object mobilePhoneWayStatus) {
            this.mobilePhoneWayStatus = mobilePhoneWayStatus;
        }

        public Object getMailWayStatus() {
            return mailWayStatus;
        }

        public void setMailWayStatus(Object mailWayStatus) {
            this.mailWayStatus = mailWayStatus;
        }

        public Object getQqWayStatus() {
            return qqWayStatus;
        }

        public void setQqWayStatus(Object qqWayStatus) {
            this.qqWayStatus = qqWayStatus;
        }

        public Object getMsnWayStatus() {
            return msnWayStatus;
        }

        public void setMsnWayStatus(Object msnWayStatus) {
            this.msnWayStatus = msnWayStatus;
        }

        public Object getSkypeWayStatus() {
            return skypeWayStatus;
        }

        public void setSkypeWayStatus(Object skypeWayStatus) {
            this.skypeWayStatus = skypeWayStatus;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getRakebackName() {
            return rakebackName;
        }

        public void setRakebackName(Object rakebackName) {
            this.rakebackName = rakebackName;
        }

        public Object getWeixin() {
            return weixin;
        }

        public void setWeixin(Object weixin) {
            this.weixin = weixin;
        }

        public Object getWeixinWayStatus() {
            return weixinWayStatus;
        }

        public void setWeixinWayStatus(Object weixinWayStatus) {
            this.weixinWayStatus = weixinWayStatus;
        }

        public Object getLastLoginIp() {
            return lastLoginIp;
        }

        public void setLastLoginIp(Object lastLoginIp) {
            this.lastLoginIp = lastLoginIp;
        }

        public Object getRegisterIpDictCode() {
            return registerIpDictCode;
        }

        public void setRegisterIpDictCode(Object registerIpDictCode) {
            this.registerIpDictCode = registerIpDictCode;
        }

        public Object getRegisterSite() {
            return registerSite;
        }

        public void setRegisterSite(Object registerSite) {
            this.registerSite = registerSite;
        }

        public Object getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(Object loginIp) {
            this.loginIp = loginIp;
        }

        public Object getRegistCode() {
            return registCode;
        }

        public void setRegistCode(Object registCode) {
            this.registCode = registCode;
        }

        public Object getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Object loginTime) {
            this.loginTime = loginTime;
        }

        public Object getUseLine() {
            return useLine;
        }

        public void setUseLine(Object useLine) {
            this.useLine = useLine;
        }

        public Object getLoginIpDictCode() {
            return loginIpDictCode;
        }

        public void setLoginIpDictCode(Object loginIpDictCode) {
            this.loginIpDictCode = loginIpDictCode;
        }

        public Object getRecommendUserId() {
            return recommendUserId;
        }

        public void setRecommendUserId(Object recommendUserId) {
            this.recommendUserId = recommendUserId;
        }

        public Object getImportUsername() {
            return importUsername;
        }

        public void setImportUsername(Object importUsername) {
            this.importUsername = importUsername;
        }

        public Object getFavorableTotal() {
            return favorableTotal;
        }

        public void setFavorableTotal(Object favorableTotal) {
            this.favorableTotal = favorableTotal;
        }

        public Object getBankcardNumber() {
            return bankcardNumber;
        }

        public void setBankcardNumber(Object bankcardNumber) {
            this.bankcardNumber = bankcardNumber;
        }

        public Object getMemo() {
            return memo;
        }

        public void setMemo(Object memo) {
            this.memo = memo;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getUpdateUsername() {
            return updateUsername;
        }

        public void setUpdateUsername(Object updateUsername) {
            this.updateUsername = updateUsername;
        }

        public Object getProxySystem() {
            return proxySystem;
        }

        public void setProxySystem(Object proxySystem) {
            this.proxySystem = proxySystem;
        }

        public Object getCountryCity() {
            return countryCity;
        }

        public void setCountryCity(Object countryCity) {
            this.countryCity = countryCity;
        }

        public String getPlayerStatus() {
            return playerStatus;
        }

        public void setPlayerStatus(String playerStatus) {
            this.playerStatus = playerStatus;
        }

        public boolean isRigistLessThanAMonth() {
            return rigistLessThanAMonth;
        }

        public void setRigistLessThanAMonth(boolean rigistLessThanAMonth) {
            this.rigistLessThanAMonth = rigistLessThanAMonth;
        }

        public Object getOnLineId() {
            return onLineId;
        }

        public void setOnLineId(Object onLineId) {
            this.onLineId = onLineId;
        }

        public Object getSessionKey() {
            return sessionKey;
        }

        public void setSessionKey(Object sessionKey) {
            this.sessionKey = sessionKey;
        }

        public boolean isBalanceFreeze() {
            return balanceFreeze;
        }

        public void setBalanceFreeze(boolean balanceFreeze) {
            this.balanceFreeze = balanceFreeze;
        }

        public boolean isAccountfreeze() {
            return accountfreeze;
        }

        public void setAccountfreeze(boolean accountfreeze) {
            this.accountfreeze = accountfreeze;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public double getFreezingBalance() {
            return freezingBalance;
        }

        public void setFreezingBalance(double freezingBalance) {
            this.freezingBalance = freezingBalance;
        }

        public String getCurrencySign() {
            return currencySign;
        }

        public void setCurrencySign(String currencySign) {
            this.currencySign = currencySign;
        }

        public String getPermissionPwd() {
            return permissionPwd;
        }

        public void setPermissionPwd(String permissionPwd) {
            this.permissionPwd = permissionPwd;
        }

        public Object getOldRankId() {
            return oldRankId;
        }

        public void setOldRankId(Object oldRankId) {
            this.oldRankId = oldRankId;
        }
    }
}
