package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@Entity
@Table(name = "t_statistic")
public class StatisticsBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 平台总用户数
     */
    @Column(updatable = false)
    private Integer user_number;

    /**
     * 平台用户总拍币数
     */
    @Column(updatable = false)
    private Integer coin_number;

    /**
     * 平台用户总赠币数
     */
    @Column(updatable = false)
    private Integer gift_number;

    /**
     * 平台用户总积分
     */
    @Column(updatable = false)
    private Integer integral_number;

    /**
     * 平台盈利总值
     */
    @Column(updatable = false)
    private String profit_number;

    /**
     * 保存时间
     */
    @Column(updatable = false)
    private Date time;

    public Integer getUser_number() {
        return user_number;
    }

    public void setUser_number(Integer user_number) {
        this.user_number = user_number;
    }

    public Integer getCoin_number() {
        return coin_number;
    }

    public void setCoin_number(Integer coin_number) {
        this.coin_number = coin_number;
    }

    public Integer getGift_number() {
        return gift_number;
    }

    public void setGift_number(Integer gift_number) {
        this.gift_number = gift_number;
    }

    public Integer getIntegral_number() {
        return integral_number;
    }

    public void setIntegral_number(Integer integral_number) {
        this.integral_number = integral_number;
    }

    public String getProfit_number() {
        return profit_number;
    }

    public void setProfit_number(String profit_number) {
        this.profit_number = profit_number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
