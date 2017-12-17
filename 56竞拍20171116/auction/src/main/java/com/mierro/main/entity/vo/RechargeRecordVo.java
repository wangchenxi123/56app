package com.mierro.main.entity.vo;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class RechargeRecordVo {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 充值总数
     */
    private String rechargeNumber;

    /**
     * 充值次数
     */
    private String rechargeCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public String getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(String rechargeCount) {
        this.rechargeCount = rechargeCount;
    }
}
