package com.mierro.main.service;


/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public interface SystemService {

    /**
     * 删除缓存临时记录
     */
    void deleteCache();

    /**
     * 设置前端是否显示充值按钮
     */
    void setting_recharge();

    /**
     * 查询充值展示标志位
     */
    Boolean findRecharge();

    /**
     * 修改注册赠送拍币数
     * @param number 数目
     */
    void setting_gift_coin(Integer number);

    /**
     * 查询注册赠送拍币数
     */
    String find_gift_coin();


}
