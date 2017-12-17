package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：
 */
public enum CoinType {

    RECHARGE("拍币"),
    GIFT("赠币");

    private String name;
    CoinType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
