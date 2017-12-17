package com.mierro.main.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@Entity
@Table(name = "t_daily_statistics")
public class DailyStatistics {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 今天充值总额
     */
    private Integer totalRecharge;

    /**
     * 新用户充值总额
     */
    private Integer newUserRecharge;

    /**
     * 老用户充值总额
     */
    private Integer oldUserRecharge;

    /**
     * 总消费
     */
    private Integer totalConsumption;

    /**
     * 拍币消费
     */
    private Integer coinConsumption;

    /**
     * 赠币消费
     */
    private Integer giftConsumption;

    /**
     * 新增用户数
     */
    private Integer newUsers;

    /**
     * 渠道新增
     */
    private Integer channelUsers;

    /**
     * 自然新增
     */
    private Integer naturalFlow;

    /**
     * 总活跃
     */
    private Integer alwaysActive;

    /**
     * ios活跃数
     */
    private Integer iosActive;

    /**
     * android活跃数
     */
    private Integer androidActive;

    /**
     * 当天用户付款金额
     */
    private Integer paymentAmount;

    /**
     * 发货总金额
     */
    private Integer deliveryAmount;

    /**
     * 创建时间
     */
    private Date time;

    public Integer getTotalRecharge() {
        return totalRecharge;
    }

    public void setTotalRecharge(Integer totalRecharge) {
        this.totalRecharge = totalRecharge;
    }

    public Integer getNewUserRecharge() {
        return newUserRecharge;
    }

    public void setNewUserRecharge(Integer newUserRecharge) {
        this.newUserRecharge = newUserRecharge;
    }

    public Integer getOldUserRecharge() {
        return oldUserRecharge;
    }

    public void setOldUserRecharge(Integer oldUserRecharge) {
        this.oldUserRecharge = oldUserRecharge;
    }

    public Integer getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(Integer totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public Integer getCoinConsumption() {
        return coinConsumption;
    }

    public void setCoinConsumption(Integer coinConsumption) {
        this.coinConsumption = coinConsumption;
    }

    public Integer getGiftConsumption() {
        return giftConsumption;
    }

    public void setGiftConsumption(Integer giftConsumption) {
        this.giftConsumption = giftConsumption;
    }

    public Integer getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Integer newUsers) {
        this.newUsers = newUsers;
    }

    public Integer getChannelUsers() {
        return channelUsers;
    }

    public void setChannelUsers(Integer channelUsers) {
        this.channelUsers = channelUsers;
    }

    public Integer getNaturalFlow() {
        return naturalFlow;
    }

    public void setNaturalFlow(Integer naturalFlow) {
        this.naturalFlow = naturalFlow;
    }

    public Integer getAlwaysActive() {
        return alwaysActive;
    }

    public void setAlwaysActive(Integer alwaysActive) {
        this.alwaysActive = alwaysActive;
    }

    public Integer getIosActive() {
        return iosActive;
    }

    public void setIosActive(Integer iosActive) {
        this.iosActive = iosActive;
    }

    public Integer getAndroidActive() {
        return androidActive;
    }

    public void setAndroidActive(Integer androidActive) {
        this.androidActive = androidActive;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Integer deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }
}
