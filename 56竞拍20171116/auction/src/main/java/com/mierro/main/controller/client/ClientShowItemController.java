package com.mierro.main.controller.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.ServiceException;
import com.mierro.main.entity.ShowItemBean;
import com.mierro.main.service.ShowItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by 黄晓滨 simba on 2017/8/16.
 * Remarks：晒单模块
 */
@RestController
@RequestMapping("/client")
public class ClientShowItemController {

    @Resource
    private ShowItemService showItemService;

    /**
     * 前台查询晒单
     * @param userId 用户id
     * @param itemId 商品id
     * @param orderId 订单id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 晒单分页对象
     */
    @GetMapping("/show")
    public ResultMessage findAll(@RequestParam(value = "userId",required = false) Long userId,
                                 @RequestParam(value = "itemId",required = false) Long itemId,
                                 @RequestParam(value = "orderId",required = false) Long orderId,
                                 @RequestParam(value = "pageNo") Integer pageNo,
                                 @RequestParam(value = "pageSize") Integer pageSize,
                                 @ModelAttribute(SystemModelHandler.CURRENT_USER) User user){
        if (userId != null && user.getId() == null){
            throw new ServiceException(ResponseCode.UNAUTHORIZED,"请先登录");
        }

        if (userId != null && (user.getId() == null || !user.getId().equals(userId))){
            return new ResultMessage(ResponseCode.ACCESSDENIED,"操作失败");
        }

        Page page = showItemService.findAll(userId,itemId,orderId,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 用户添加晒单
     * @param showItem 晒单对象
     * @return 操作码
     * @throws JsonProcessingException json转化错误
     */
    @PostMapping("/show")
    public ResultMessage add(@ModelAttribute("showItem")@Valid ShowItemBean showItem,
                             @ModelAttribute(SystemModelHandler.CURRENT_USER) User user) throws JsonProcessingException {
        if(user.getId() == null){
            return new ResultMessage(ResponseCode.UNAUTHORIZED,"操作失败");
        }
        showItem.setUserId(user.getId());
        showItemService.addShowItem(showItem);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
