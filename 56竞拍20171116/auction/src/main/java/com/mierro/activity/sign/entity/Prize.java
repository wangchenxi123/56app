package com.mierro.activity.sign.entity;

import com.mierro.main.common.CoinType;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class Prize {

    /**
     * 控制参数，例如 参数值为4，效果是：连续签到天数达到4天，发放奖励
     */
    private Integer number;

    /**
     * 奖励的拍币或赠币数量
     */
    private Integer coin;

    /**
     * 奖励虚拟币类型
     */
    private CoinType coinType;

    /**
     * 奖励积分类型
     */
    private Integer integral;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public CoinType getCoinType() {
        return coinType;
    }

    public void setCoinType(CoinType coinType) {
        this.coinType = coinType;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
