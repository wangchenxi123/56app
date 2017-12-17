package com.mierro.robot.controller;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.robot.entity.e.RobotConfig;
import com.mierro.robot.service.RobotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理端机器人模块
 * Created by tlseek on 2017/8/10.
 */
@RestController
@RequestMapping("/admin")
public class AdminRobotController {

    @Resource
    RobotService robotService;

    /**
     * 机器人后台服务是否启动中
     */
    @GetMapping("/robot/isBackgroundServiceRunning")
    public ResultMessage isBackgroundServiceRunning(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", robotService.isBackgroundServiceRunning());
    }

    /**
     * 停止或启动后台服务
     * @return true 为 启动, false 为停止
     */
    @PostMapping("/robot/startOrStopBackgroundService")
    public ResultMessage startOrStopBackgroundService(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", robotService.startOrStopBackgroundService());
    }

    /**
     * 获取每个商品分配的机器人数目区间 例如: 1,5
     */
    @GetMapping("/robot/allocUserNumRange")
    public ResultMessage allocUserNumRange(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", robotService.getConfig(RobotConfig.ALLOC_USER_NUM_RANGE));
    }

    /**
     * 设置每个商品分配的机器人数目区间
     * @return true 为 启动, false 为停止
     */
    @PostMapping("/robot/allocUserNumRange")
    public ResultMessage allocUserNumRange(@RequestParam("min")Integer min, @RequestParam("max")Integer max){
        if (min <= 2) {
            return new ResultMessage(ResponseCode.BUSINESS,"最小值要大于2");
        }
        if (min > max) {
            return new ResultMessage(ResponseCode.BUSINESS,"最小值小于最大值");
        }
        String value = robotService.putConfig(RobotConfig.ALLOC_USER_NUM_RANGE, min + "," + max);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", value);
    }
    /**
     * 获取允许机器人竞拍的次数范围 例如: 1,5
     */
    @GetMapping("/robot/allowUserAuctionRange")
    public ResultMessage allowUserAuctionRange(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", robotService.getConfig(RobotConfig.ALLOW_USER_AUCTION_RANGE));
    }

    /**
     * 设置允许机器人竞拍的次数范围 例如: 1,5
     * @return true 为 启动, false 为停止
     */
    @PostMapping("/robot/allowUserAuctionRange")
    public ResultMessage allowUserAuctionRange(@RequestParam("min")Integer min, @RequestParam("max")Integer max){
        if (min <= 2) {
            return new ResultMessage(ResponseCode.BUSINESS,"最小值要大于2");
        }
        if (min > max) {
            return new ResultMessage(ResponseCode.BUSINESS,"最小值小于最大值");
        }
        String value = robotService.putConfig(RobotConfig.ALLOW_USER_AUCTION_RANGE, min + "," + max);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", value);
    }

    /**
     * 获取使用机器人运行方案5:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1] 例如: 0.8
     */
    @GetMapping("/robot/coinConsumeRate")
    public ResultMessage coinConsumeRate(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", robotService.getConfig(RobotConfig.COIN_CONSUME_RATE));
    }

    /**
     * 设置使用机器人运行方案5:根据用户拍币消耗数分配中奖时, 用户中奖的金额占消耗拍币的比率 (0, 1] 例如: 0.8
     */
    @PostMapping("/robot/coinConsumeRate")
    public ResultMessage coinConsumeRate(@RequestParam("rate")Float rate){
        if (rate <= 0) {
            return new ResultMessage(ResponseCode.BUSINESS,"要大于0");
        }
        if (rate >= 1) {
            return new ResultMessage(ResponseCode.BUSINESS,"小于1");
        }
        String value = robotService.putConfig(RobotConfig.COIN_CONSUME_RATE, String.format("%.2f",rate));
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", value);
    }

    /**
     * 获取最小成交价
     */
    @GetMapping("/robot/minMarketPrice")
    public ResultMessage minMarketPrice(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", robotService.getConfig(RobotConfig.MIN_MARKET_PRICE));
    }

    /**
     * 设置最小成交价
     */
    @PostMapping("/robot/minMarketPrice")
    public ResultMessage minMarketPrice(@RequestParam("price")Float price){
        if (price < 0) {
            return new ResultMessage(ResponseCode.BUSINESS,"要大于等于0");
        }
        String value = robotService.putConfig(RobotConfig.MIN_MARKET_PRICE, String.format("%.2f",price));
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource", value);
    }


}
