package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/16.
 * Remarks：
 */
public enum OperationalType {

    CHECK_IN("签到"),
    EXCHANGE("兑换"),
    GIFT("赠送");

    private String name;

    OperationalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
