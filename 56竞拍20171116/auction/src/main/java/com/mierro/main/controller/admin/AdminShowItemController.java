package com.mierro.main.controller.admin;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.service.ShowItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 simba on 2017/8/16.
 * Remarks：晒单模块
 */
@RestController
@RequestMapping("/admin")
public class AdminShowItemController {

    @Resource
    private ShowItemService showItemService;

    /**
     * 获取所有晒单列表
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping("/show")
    public ResultMessage findShowItem(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        Page page = showItemService.findAll(pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 设置晒单状态(只能设置是否全局展示)
     * @param showId 晒单ud
     * @return
     */
    @PutMapping("/show")
    public ResultMessage settingShowItemStart(@RequestParam("showId") Long showId){
        showItemService.settingShowItemStart(showId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
