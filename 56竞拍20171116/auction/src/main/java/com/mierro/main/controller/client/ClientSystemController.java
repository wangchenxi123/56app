package com.mierro.main.controller.client;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.service.SystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@RestController
@RequestMapping("/client")
public class ClientSystemController {

    @Resource
    private SystemService systemService;

    /**
     * 获取系统当前时间
     * @return 系统当前时间
     */
    @GetMapping("/system_time")
    public ResultMessage getSystemTime(){
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",new Date());
    }

    /**
     * 查询充值展示标志位
     * @return 标志位
     */
    @GetMapping("/setting/recharge")
    public ResultMessage findAll() {
        Boolean start = systemService.findRecharge();
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", start);
    }
}
