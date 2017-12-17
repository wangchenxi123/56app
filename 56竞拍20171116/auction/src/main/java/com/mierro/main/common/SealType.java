package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public enum SealType {
    ALL("所有"),
    WINNING("我拍中"),
    FAILURE("未拍中"),
    IS_IN_PROGRESS("正在进行时"),
    WAITING_PAYMENT("待付款"),
    WAITING_CHOICE_ADDRESS("待选择收货地址"),
    WAITING_SHIP("待发货"),
    WAITING_RECEIPT("已出货"),
    WAITING_SUN_ALONE("待晒单"),
    SEALED("已经封存");

    private String message;

    SealType(String message) {
        this.message = message;
    }

    public static SealType Conversion(String message){
        for (SealType type: values()) {
            if (type.toString().equals(message)){
                return type;
            }
        }
        return null;
    }
}
