package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public enum OrderState {

    WAITING_PAYMENT("待付款"),
    WAITING_CHOICE_ADDRESS("待选择收货地址"),
    WAITING_SHIP("待发货"),
    WAITING_RECEIPT("待收货"),
    WAITING_SUN_ALONE("待晒单"),
    CONSUMMATION("完成");

    private String name;

    OrderState(String name) {
        this.name = name;
    }

    public static OrderState Conversion(String message){

        for (OrderState type: values()) {
            if (type.toString().equals(message)){
                return type;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
