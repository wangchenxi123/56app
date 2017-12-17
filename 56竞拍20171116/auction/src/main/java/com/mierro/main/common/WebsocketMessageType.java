package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/10.
 * Remarks：
 */
public enum WebsocketMessageType {

    HEADLINES("拍卖头条"),
    BIDDER("出价人信息"),
    SELF_NOTICE("个人通知"),
    SYSTEM_NOTICE("系统通知"),
    BID_SUCCESS_NOTICE("出价成功通知"),
    BID_ERROR_NOTICE("出价失败通知"),
    SYSTEM_REBOOT("系统启动"),
    SYSTEM_SLEEP("系统睡眠"),
    ACTIVITY_TURNTABLE("活动幸运转盘"),
    ;
    private String name;

    WebsocketMessageType(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
