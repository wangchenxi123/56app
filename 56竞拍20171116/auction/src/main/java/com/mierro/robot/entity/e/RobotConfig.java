package com.mierro.robot.entity.e;

import com.alibaba.fastjson.JSON;
import com.mierro.robot.entity.view.AddOrUpdateRobotUserView;

import java.util.Arrays;
import java.util.function.Function;

/**
 * 机器人配置信息
 * Created by tlseek on 2017/8/18.
 */
public enum RobotConfig {

    ROBOT_USER_HEADIMGS("机器人用户头像图片列表", "[]", str -> JSON.parseArray(mapEmpty(str, "[]"), AddOrUpdateRobotUserView.class)),
    ALLOC_USER_NUM_RANGE("每个商品分配的机器人数目区间 例如: 1,5", "1,5", str -> Arrays.stream(str.split(",")).map(Integer::valueOf).toArray(Integer[]::new)),
    ALLOW_USER_AUCTION_RANGE("允许机器人竞拍的次数范围 例如: 1,5","20,30", str -> Arrays.stream(str.split(",")).map(Integer::valueOf).toArray(Integer[]::new)),
    RESET_USER_HEADIMGES("重设所有机器人头像", "true", str -> Boolean.valueOf(mapEmpty(str, "false"))),
    LOAD_USERS("加载默认机器人列表", "true", str -> Boolean.valueOf(mapEmpty(str, "false"))),
    COIN_CONSUME_RATE("使用机器人运行方案:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1]", "0.8", str -> Float.valueOf(mapEmpty(str, "0.8"))),
    MIN_MARKET_PRICE("最小成交价", "1", str -> Float.valueOf(mapEmpty(str, "1"))),
    ;

    public final String name;
    public final String defaultValue;
    // 反序列化
    private final Function<String, Object> deserialize;

    RobotConfig(String name, String defaultValue, Function<String, Object> deserialize) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.deserialize = deserialize;
    }

    private static String mapEmpty(String str , String defaultValue) {
        return str != null && !"".equals(str) ? str : defaultValue;
    }

    public <T> T deserialize(String str){
        return (T)deserialize.apply(str);
    }
}
