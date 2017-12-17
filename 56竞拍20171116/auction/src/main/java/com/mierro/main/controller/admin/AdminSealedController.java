package com.mierro.main.controller.admin;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.service.SealedService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@RestController
@RequestMapping("/admin")
public class AdminSealedController {

    @Resource
    private SealedService sealedService;

    /**
     * 获取所有封存记录
     * @param itemName 商品名称
     * @param robot 是否机器人
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 记录分页对象
     */
    @GetMapping("/seals")
    public ResultMessage findAll(@RequestParam(value = "itemName",required = false) String itemName,
                                 @RequestParam(value = "robot",required = false) Boolean robot,
                                 @RequestParam(value = "pageNo") Integer pageNo,
                                 @RequestParam(value = "pageSize") Integer pageSize){
        Page page = sealedService.findAll(itemName,robot,null,null,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",page);
    }

    /**
     * 查询某一封存记录详情
     * @param sealId 封存记录id
     * @return 封存记录详情
     */
    @GetMapping("/seal")
    public ResultMessage findOne(@RequestParam(value = "sealId") Long sealId){
        SealedBean page = sealedService.findOne(sealId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",page);
    }

    /**
     * 获取出价记录
     * @param sealId 封存记录id
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @return 出价记录分页对象
     */
    @GetMapping("/bidders")
    public ResultMessage findAll(@RequestParam(value = "sealId") Long sealId,
                                 @RequestParam(value = "pageNo") Integer pageNo,
                                 @RequestParam(value = "pageSize") Integer pageSize){
        Page page = sealedService.findAllBidders(sealId,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",page);
    }
}
