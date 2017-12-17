package com.mierro.main.controller.admin;

import com.mierro.authority.common.SystemSettings;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.service.SystemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@RestController
@RequestMapping("/admin")
public class AdminSystemController {

    @Resource
    private SystemService systemService;

    /**
     * 查询充值展示标志位
     * @return 标志位
     */
    @GetMapping("/setting/recharge")
    public ResultMessage findAll() {
        Boolean start = systemService.findRecharge();
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", start);
    }

    /**
     * 设置充值展示标志位
     * @return 操作码
     */
    @PutMapping("/setting/recharge")
    public ResultMessage setting() {
        systemService.setting_recharge();
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 修改注册赠送拍币数
     * @return 操作码
     */
    @PutMapping("/setting/gift_coin")
    public ResultMessage setting_gift_coin(@RequestParam("number") Integer number) {
        systemService.setting_gift_coin(number);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 查询注册赠送拍币数
     * @return 标志位
     */
    @GetMapping("/setting/gift_coin")
    public ResultMessage find_gift_coin() {
        String number = systemService.find_gift_coin();
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", number);
    }

    /**
     * 查询系统维护设置
     * @return 是否进入系统维护
     */
    @GetMapping("/setting/maintenance")
    public ResultMessage find_system_maintenance(){
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", SystemSettings.maintenance);
    }

    /**
     * 设置是否进入系统维护
     * @return 操作码
     */
    @PutMapping("/setting/maintenance")
    public ResultMessage setting_system_maintenance(){
        SystemSettings.maintenance = !SystemSettings.maintenance;
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }


}
