package com.mierro.main.controller.client;

import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.CollectionBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.CollectionService;
import com.mierro.main.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：
 */
@RestController
@RequestMapping("/client")
public class ClientItemController {

    @Resource
    private ItemService itemService;

    @Resource
    private CollectionService collectionService;

    /**
     * 首页查询所有商品
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 商品分页对象
     */
    @GetMapping("/items")
    public ResultMessage findAll(@RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize){
        Map map = itemService.clientFindAll(pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 首页查询所有新手商品
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 商品分页对象
     */
    @GetMapping("/novice/items")
    public ResultMessage find(@RequestParam("pageNo") Integer pageNo,
                              @RequestParam("pageSize") Integer pageSize){
        Map map = itemService.clientFind(pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 首页查询单个商品
     * @param itemId 页码
     * @param number 出价记录数目
     * @return 商品分页对象
     */
    @GetMapping("/item")
    public ResultMessage findAll(@RequestParam("itemId") Long itemId,
                                 @RequestParam("number") Integer number,
                                 @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        ItemCache.Item item = itemService.clientFindOne(itemId,number);
        Map<String,Object> map = new HashMap<>();
        map.put("resource",item);
        map.put("time",new Date());

        if (user.getId() == null || ItemCache.item_userId.get(user.getId()) == null ){
            map.put("number",0);
        }else{
            if (ItemCache.item_userId.get(user.getId()) != null){
                if (ItemCache.item_userId.get(user.getId()).get(itemId) == null){
                    map.put("number",0);
                }else{
                    map.put("number",ItemCache.item_userId.get(user.getId()).get(itemId));
                }
            }else{
                map.put("number",0);
            }
        }
        if (user.getId() == null || ItemCache.user_auction.get(user.getId()) == null){
            map.put("auction",0);
        }else{
            if (ItemCache.user_auction.get(user.getId()).get(itemId) == null){
                map.put("auction",0);
            }else{
                map.put("auction",ItemCache.user_auction.get(user.getId()).get(itemId));
            }
        }
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 用户对某一个商品进行竞拍操作
     * @param itemId 商品id
     * @param periods 期数
     * @return 操作码
     * @throws IOException 通知发送失败
     */
    @PostMapping("/shot")
    public ResultMessage auction(@RequestParam("itemId") Long itemId,
                                 @RequestParam("periods") Integer periods,
                                 @RequestParam(value = "number",required = false) Integer number,
                                 @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) throws IOException {

        if (number == null || number == 1){
            itemService.auction(itemId,periods,user.getId());
        }else{
            itemService.auction(itemId,periods,user.getId(),number);
        }
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 查询某一个商品竞拍记录
     * @param itemId 商品id
     * @param sealedId 历史记录Id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 竞拍记录集合
     */
    @GetMapping("/bidders")
    public ResultMessage selectBidders(@RequestParam(value = "itemId",required = false) Long itemId,
                                       @RequestParam(value = "sealedId",required = false) Long sealedId,
                                       @RequestParam("pageNo") Integer pageNo,
                                       @RequestParam("pageSize") Integer pageSize){
        Page map = itemService.findBidders(itemId,sealedId,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",map);
    }

    /**
     * 查询用户是否收藏某商品
     * @param itemId 商品id
     * @return 返回resource == 空 即该用户没有收藏
     */
    @GetMapping("/is_collection")
    public ResultMessage isCollection(@RequestParam("itemId") Long itemId,
                                      @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        CollectionBean collectionBean = collectionService.findOne(user.getId(),itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",collectionBean);
    }

}
