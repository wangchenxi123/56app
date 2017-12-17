package com.mierro.main.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.websocket.UserWebsocket;
import com.mierro.main.common.ItemType;
import com.mierro.main.common.OrderState;
import com.mierro.main.common.WebsocketMessageType;
import com.mierro.main.entity.AddressBean;
import com.mierro.main.entity.OrderBean;
import com.mierro.main.global.WebsocketResultMessage;
import com.mierro.main.service.NoticeService;
import com.mierro.main.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：中奖管理
 */
@RestController
@RequestMapping("/admin")
public class AdminWinningController {

    @Resource
    private OrderService orderService;

    @Resource
    private NoticeService noticeService;

    /**
     * 根据条件查询中奖人
     * @param itemName 商品名称
     * @param orderState 订单状态
     * @param itemType 商品类型
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/orders")
    public ResultMessage findAll(@RequestParam(value = "itemName",required = false) String itemName,
                                 @RequestParam(value = "orderState",required = false) OrderState orderState,
                                 @RequestParam(value = "robot",required = false) Boolean robot,
                                 @RequestParam(value = "itemType",required = false) ItemType itemType,
                                 @RequestParam(value = "pageNo") Integer pageNo,
                                 @RequestParam(value = "pageSize") Integer pageSize){
        Page<OrderBean> page = orderService.findAll(itemName,orderState,itemType,robot,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 查询某订单详情
     * @param orderId 订单id
     * @return 订单对象
     */
    @GetMapping("/order")
    public ResultMessage findAll(@RequestParam("orderId") Long orderId){
        OrderBean orderBean = orderService.findOne(orderId);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",orderBean);
    }

    /**
     * 查找发货地址
     * @param orderId 订单id
     * @return 地址对象
     */
    @GetMapping("/order/address")
    public ResultMessage findAddress(@RequestParam("orderId") Long orderId){
        AddressBean page = orderService.findAddress(orderId);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 添加订单发货详情
     * @param orderId 订单id
     * @param expressNumber 快递编号
     * @param card 卡号
     * @param density 卡密
     */
    @PutMapping("/order")
    public ResultMessage update(@RequestParam("orderId") Long orderId,
                                @RequestParam(value = "expressNumber",required = false) String expressNumber,
                                @RequestParam(value = "card",required = false) String card,
                                @RequestParam(value = "density",required = false) String density){
        orderService.update(orderId,expressNumber,card,density);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 发送订单系统通知
     * @param userId 中奖人id
     * @param title 标题
     * @param context 内容
     * @throws IOException 通知发送失败
     */
    @PostMapping("/order/notice")
    public ResultMessage SendNotice(@RequestParam("userId") Long userId,
                                    @RequestParam("title") String title,
                                    @RequestParam("context") String context) throws IOException {
        noticeService.addPersonal(title,context,userId);
        //检查用户是否在线，在线则推送通知
        WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
        websocketResultMessage.setTime(new Date());
        websocketResultMessage.setMessage_type(WebsocketMessageType.SELF_NOTICE);
        websocketResultMessage.setMessage("收到一条个人系统通知");
        websocketResultMessage.setUserId(userId.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        UserWebsocket.sendMessage(userId,objectMapper.writeValueAsString(websocketResultMessage));
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
