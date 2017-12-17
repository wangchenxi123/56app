package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/16.
 * Remarks：
 */
public enum  ConfigSetting {
    ONE_DAY("第一天签到"),
    TWO_DAY("第二天签到"),
    THREE_DAY("第三天签到"),
    FOUR_DAY("第四天签到"),
    FIVE_DAY("第五天签到"),
    SIX_DAY("第六天签到"),
    SEVEN_DAY("第七天签到"),
    TURNTABLE("大转盘"),
    FIXED_CHEST("固定宝箱设置(4的倍数或者7的倍数获取宝箱(签到使用))"),
    MAP_KEY("高德地图key"),
    RECHARGE("充值展示"),
    GIFT_COIN("注册赠送拍币数"),
    PAY_TYPE("执行支付类型"),
    WECHAT_OPEN_APP("微信开发平台移动应用的appid和secret 例: 'appid,secret' "),
    QQ_CONNECT_APP("QQ互联移动应用的appid和appkey 例: 'appid,appkey' "),
    ;

    /**
     * 第一天签到获得积分数
     */
    public static final int one_day = 1;
    /**
     * 第二天签到获得积分数
     */
    public static final int two_day = 2;
    /**
     * 第三天签到获得积分数
     */
    public static final int three_day = 3;
    /**
     * 第四天签到获得积分数
     */
    public static final int four_day = 4;
    /**
     * 第五天签到获得积分数
     */
    public static final int five_day = 5;
    /**
     * 第六天签到获得积分数
     */
    public static final int six_day = 6;
    /**
     * 第七天签到获得积分数
     */
    public static final int seven_day = 7;
    /**
     * 大转盘
     */
    public static final int turntable = 8;

    /**
     * 固定宝箱
     */
    public static final int fixed_chest = 9;

    /**
     * 高德地图key
     */
    public static final int map_key = 10;

    /**
     * 充值展示
     */
    public static final int recharge = 11;

    /**
     * 注册赠送拍币数
     */
    public static final int gift_coin = 12;

    /**
     * 执行支付类型
     */
    public static final int pay_type = 13;

    /**
     * 微信开发平台移动应用的appid和secret
     */
    public static final int wechat_open_app = 14;

    /**
     * QQ互联移动应用的appid和appkey
     */
    public static final int qq_connect_app = 15;

    private int code;

    private String name;

    ConfigSetting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static {
        ONE_DAY.code = one_day;
        TWO_DAY.code = two_day;
        THREE_DAY.code = three_day;
        FOUR_DAY.code = four_day;
        FIVE_DAY.code = five_day;
        SIX_DAY.code = six_day;
        SEVEN_DAY.code = seven_day;
        TURNTABLE.code = turntable;
        FIXED_CHEST.code = fixed_chest;
        MAP_KEY.code = map_key;
        RECHARGE.code = recharge;
        GIFT_COIN.code = gift_coin;
        PAY_TYPE.code = pay_type;
        WECHAT_OPEN_APP.code = wechat_open_app;
        QQ_CONNECT_APP.code = qq_connect_app;
    }
}
