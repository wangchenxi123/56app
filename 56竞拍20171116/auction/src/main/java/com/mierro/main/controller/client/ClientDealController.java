package com.mierro.main.controller.client;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.BiddersService;
import com.mierro.main.service.OrderService;
import com.mierro.main.service.SealedService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/16.
 * Remarks：
 */
@RestController
@RequestMapping("/client")
public class ClientDealController {

    @Resource
    private OrderService orderService;

    @Resource
    private SealedService sealedService;

    @Resource
    private BiddersService biddersService;

    /**
     * 查询最新成交
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/transactions")
    public ResultMessage findAll(@RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize){
        Page page = orderService.ClientFindAll(pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 根据最新成交记录封存id查找该期记录
     * @param record_id 封存记录id
     * @return 记录商品对象
     * @throws IOException json转换错误
     */
    @GetMapping("/transaction")
    public ResultMessage findOne(@RequestParam("record_id") Long record_id,
                                 @ModelAttribute(SystemModelHandler.CURRENT_USER) User user) throws IOException {
        SealedBean sealedBean = sealedService.findOne(record_id);
        ItemBean itemBean = sealedBean.getItemMessage();
        itemBean.setControl_line(null);
        itemBean.setRunning_program(null);
        itemBean.setCost(null);
        itemBean.setDisable(null);
        itemBean.setPlus_code(null);
        ItemCache.Item item = new ItemCache.Item();
        item.setIncrease_the_price(itemBean.getIncrease_the_price());
        item.setPrice(sealedBean.getMarket_price());
        item.setItemBean(itemBean);
        UserMessage old = JSON.parseObject(sealedBean.getUser(),UserMessage.class);
        UserMessage userMessage = new UserMessage();
        userMessage.setUsername(old.getUsername());
        userMessage.setHead_pic(old.getHead_pic());
        userMessage.setId(old.getId());
        userMessage.setAddress(old.getAddress());
        Page<BiddersBean> biddersBeans =  biddersService.findBySealed(record_id,1,10);

        item.getBiddersBeans().addAll(biddersBeans.getContent());
        if (item.getItemBean().getBig_picture()!= null){
            item.getItemBean().setBigPictures(JSON.parseArray(item.getItemBean().getBig_picture(),String.class));
        }
        Map<String,Object> map = new HashMap<>();

        if (user.getId() == null){
            map.put("number",0);
        }else{
            Integer number = biddersService.findBySealedNumber(record_id,user.getId());
            map.put("number",number);
        }

        map.put("item",item);
        map.put("user",userMessage);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 根据商品id查询商品活动历史记录
     * @param itemId 商品id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 活动记录集合
     */
    @GetMapping("/transaction/history")
    public ResultMessage history(@RequestParam("itemId") Long itemId,
                                 @RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize){
        Page page = sealedService.findHistory(itemId,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }
}
