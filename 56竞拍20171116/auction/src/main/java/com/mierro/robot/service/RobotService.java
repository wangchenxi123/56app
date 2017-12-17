package com.mierro.robot.service;

import com.mierro.main.entity.ItemBean;
import com.mierro.robot.entity.RobotItemBean;
import com.mierro.robot.entity.e.RobotConfig;
import com.mierro.robot.entity.view.RechargeCoinConsume;

/**
 * 机器人业务接口
 * Created by tlseek on 2017/8/10.
 */
public interface RobotService {

    /**
     * 获取随机机器人ID
     * @return
     */
    Long getRandomRobotId(Long itemId);


    /**
     * 根据商品ID获取
     * @param item
     * @return
     */
    RobotItemBean getRobotItemBean(ItemBean item);

    /**
     * 更新机器人商品, 并刷新商品对应的机器人用户
     * @param itemId
     * @return
     */
    void updateRecord(Long itemId);

    /**
     * 更新机器人商品
     * @param itemId
     * @param refreshItemRobotUser 刷新商品对应的机器人用户
     */
    void updateRecord(Long itemId, Boolean refreshItemRobotUser);

    /**
     *
     */
    void errorItemHandler(String message, Long itemId);

    /**
     * 机器人后台服务是否启动中
     * @return
     */
    boolean isBackgroundServiceRunning();

    /**
     * 停止或启动后台服务
     * @return true 为 启动, false 为停止
     */
    boolean startOrStopBackgroundService();

    /**
     * 商品是否竞拍
     * @param itemId
     * @return
     */
    boolean isAuction(Long itemId);
    /**
     * 获取机器人配置信息
     * @param key
     * @return
     */
    String getConfig(RobotConfig key);

    /**
     * 设置机器人配置信息
     * @param key
     * @param value
     * @return
     */
    String putConfig(RobotConfig key, String value);

    /**
     * 用户拍币消耗信息
     * @param userId
     * @return
     */
    RechargeCoinConsume userCoinCousumeInfo(Long userId);
}
