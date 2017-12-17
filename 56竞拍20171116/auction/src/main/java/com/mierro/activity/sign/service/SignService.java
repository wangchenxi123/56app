package com.mierro.activity.sign.service;

import com.mierro.activity.sign.entity.ExchangeKey;
import com.mierro.activity.sign.entity.Prize;

import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public interface SignService {

    /**
     * 签到
     * @param userId 用户id
     */
    Prize reported(Long userId);

    /**
     * 获取用户签到信息
     * @param userId 用户id
     */
    Map<String,Object> reportMessage(Long userId);

    /**
     * 领取宝箱
     * @param userId 用户id
     */
    Prize receiveTreasure(Long userId);

    /**
     * 积分兑换赠币
     * @param userId 用户id
     * @param exchangeKey 兑换规则
     */
    void exchange(Long userId, ExchangeKey exchangeKey);

    /**
     * 设置固定奖励
     * @param prizeList 奖励集合
     */
    void settingFixedReward(List<Prize> prizeList);
}
