package com.mierro.main.Listener.Thread;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.dao.UserMessageDao;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.IdWorker;
import com.mierro.common.common.ServiceException;
import com.mierro.common.websocket.UserWebsocket;
import com.mierro.main.common.ItemType;
import com.mierro.main.common.OrderState;
import com.mierro.main.common.SealType;
import com.mierro.main.common.WebsocketMessageType;
import com.mierro.main.dao.ItemDao;
import com.mierro.main.dao.OrderDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.OrderBean;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.global.WebsocketResultMessage;
import com.mierro.robot.service.RobotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class ItemSettlementThread extends Thread {

    private static Logger logger = LogManager.getLogger(ItemSettlementThread.class.getName());

    private Long itemId;

    private ItemDao itemDao;

    private RobotService robotService;

    private UserMessageDao userMessageDao;

    private SealedDao sealedDao;

    private OrderDao orderDao;

    private IdWorker idWorker;

    ItemSettlementThread(Long itemId, ItemDao itemDao, RobotService robotService,
                         UserMessageDao userMessageDao, SealedDao sealedDao,
                         OrderDao orderDao, IdWorker idWorker){
        this.itemId = itemId;
        this.robotService = robotService;
        this.userMessageDao = userMessageDao;
        this.itemDao = itemDao;
        this.sealedDao = sealedDao;
        this.orderDao = orderDao;
        this.idWorker = idWorker;
    }

    @Transactional
    public void run() {
        ItemCache.Item item = ItemCache.itemMap.get(itemId);
        //进行结算流程
        if (item == null){
            throw new ServiceException("没有查找到商品");
        }
        if (item.getAuction()){
            logger.error("正在有用户进行竞拍");
        }else{
            SealedBean sealedBean = sealedDao.findOne(item.getId());
            if (sealedBean == null){
                sealedBean = new SealedBean();
                sealedBean.setId(item.getId());
            }
            sealedBean.setItem(JSON.toJSONString(item.getItemBean()));
            sealedBean.setPeriods(item.getNumber_periods());
            sealedBean.setItemName(item.getItemBean().getName());
            sealedBean.setPicture(item.getItemBean().getSmall_picture());
            sealedBean.setMarket_price(item.getPrice());
            sealedBean.setTime(new Date());
            sealedBean.setItemId(item.getItemBean().getId());
            sealedBean.setCost(item.getItemBean().getCost().setScale(2, BigDecimal.ROUND_DOWN).toString());
            sealedBean.setTotal(item.getClick_number().toString());
            sealedBean.setIncome(item.getActual_click_number().toString());
            //有效投票数+产品付款金额-成本
            if (item.getRobot() != null && item.getRobot()){
                sealedBean.setProfit(new BigDecimal(item.getActual_click_number()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            }else{
                sealedBean.setProfit(new BigDecimal(Integer.parseInt(sealedBean.getIncome())+
                        (item.getClick_number()*Double.parseDouble(item.getIncrease_the_price())) -
                        Double.parseDouble(sealedBean.getCost())).setScale(2,BigDecimal.ROUND_DOWN).toString());
            }

            //查询中奖人信息
            Long userId = item.getUserId();

            if (userId != null) {
                UserMessage userMessage = userMessageDao.findOne(userId);
                sealedBean.setUser(JSON.toJSONString(userMessage));
                sealedBean.setName(userMessage.getUsername());
                sealedBean.setStateTime(item.getStart_time());
                sealedBean.setRobot(userMessage.getRobot());
                sealedBean.setUserId(userMessage.getId());
                sealedBean.setSealType(SealType.SEALED);
                sealedBean.setSealed(true);
                sealedDao.save(sealedBean);
                //发送通知
                WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                websocketResultMessage.setUserId(userMessage.getId().toString());
                websocketResultMessage.setTime(new Date());
                websocketResultMessage.setCode(200);
                websocketResultMessage.setItemId(itemId.toString());
                websocketResultMessage.setUsername(userMessage.getUsername());
                websocketResultMessage.setHead_pic(userMessage.getHead_pic().toString());
                websocketResultMessage.setPrice(item.getPrice());
                websocketResultMessage.setItemName(item.getItemBean().getName());
                websocketResultMessage.setAddress(userMessage.getAddress());
                websocketResultMessage.setMessage_type(WebsocketMessageType.HEADLINES);
                websocketResultMessage.setMessage("拍卖成功通知");
                UserWebsocket.sendMessage(JSON.toJSONString(websocketResultMessage));
                //生成中奖订单信息
                OrderBean orderBean = new OrderBean();
                orderBean.setId(sealedBean.getId());
                orderBean.setItem_id(item.getItemBean().getId());
                orderBean.setItem_name(item.getItemBean().getName());
                orderBean.setName(userMessage.getUsername());
                orderBean.setItem_picture(item.getItemBean().getSmall_picture().toString());
                orderBean.setPrice(item.getPrice());
                orderBean.setAmounts(item.getPrice());
                orderBean.setMarketPrice(item.getItemBean().getPrice().setScale(2,BigDecimal.ROUND_DOWN).toString());
                orderBean.setOrder_state(OrderState.WAITING_PAYMENT);
                if (item.getItemBean().getIn_kind()){
                    orderBean.setItem_type(ItemType.IN_KIND);
                }else{
                    orderBean.setItem_type(ItemType.VIRTUAL);
                }
                orderBean.setTime(new Date());
                orderBean.setRecord_id(sealedBean.getId());
                orderBean.setUserId(userMessage.getId());
                orderDao.save(orderBean);

                if (!userMessage.getRobot()){
                    if (userMessage.getNumber() == null){
                        userMessage.setNumber(1);
                    }else{
                        userMessage.setNumber(userMessage.getNumber()+1);
                    }
                }
                userMessageDao.save(userMessage);
            }else{
                //删除缓存记录
                try {
                    sealedDao.delete(item.getId());
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                //发送流拍通知
                WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                websocketResultMessage.setCode(500);
                websocketResultMessage.setTime(new Date());
                websocketResultMessage.setItemId(itemId.toString());
                websocketResultMessage.setMessage_type(WebsocketMessageType.HEADLINES);
                websocketResultMessage.setMessage("商品流拍通知");
                UserWebsocket.sendMessage(JSON.toJSONString(websocketResultMessage));
            }


            if (item.getDelete().equals(true)){
                //活动清除，删除数据库商品信息
                ItemCache.itemMap.remove(itemId);
                itemDao.delete(itemId);
            }else{
                //判断是否开始新一轮
                ItemBean itemBean = itemDao.findOne(itemId);
                if (item.getDisable().equals(true)){
                    //商品禁用，活动删除
                    ItemCache.itemMap.remove(itemId);
                    itemBean.setDisable(true);
                    itemDao.save(itemBean);
                }
                if (!itemBean.getDisable().equals(false)){
                    //活动禁用
                    ItemCache.itemMap.remove(itemId);
                }else{
                    if (!item.getInterrupted()){
                        Long mark =0L ;
                        //清空缓存
                        for (Map.Entry<Long, ConcurrentHashMap<Long,Integer>> entry : ItemCache.item_userId.entrySet()) {
                            for (Map.Entry<Long,Integer> entry2 : entry.getValue().entrySet()){
                                if (entry2.getKey().equals(this.itemId)){
                                    //保存标志位
                                    mark = entry2.getKey();
                                    break;
                                }
                            }

                            if (!mark.equals(0L)){
                                ItemCache.item_userId.get(entry.getKey()).remove(mark);
                                mark=0L;
                            }
                        }

                        mark =0L ;
                        //清空缓存
                        for (Map.Entry<Long, ConcurrentHashMap<Long,Integer>> entry : ItemCache.user_auction.entrySet()) {
                            for (Map.Entry<Long,Integer> entry2 : entry.getValue().entrySet()){
                                if (entry2.getKey().equals(this.itemId)){
                                    //保存标志位
                                    mark = entry2.getKey();
                                    break;
                                }
                            }

                            if (!mark.equals(0L)){
                                ItemCache.user_auction.get(entry.getKey()).remove(mark);
                                mark=0L;
                            }
                        }
                        //返币
                        new ReturnCoinThread(sealedBean.getId()).start();

                        //开展新一期

                        if (itemBean.getBig_picture() != null){
                            itemBean.setBigPictures(JSON.parseArray(itemBean.getBig_picture(),String.class));
                        }

                        item.setItemBean(itemBean);
                        if(userId == null){
                            item.setNumber_periods(item.getNumber_periods());
                        }else{
                            item.setNumber_periods(item.getNumber_periods()+1);
                        }
                        item.setId(idWorker.nextId());
                        item.setPrice("0.00");
                        item.setUserId(null);
                        item.setActual_click_number(0);
                        item.setClick_number(0);
                        item.setAuction(false);
                        item.setStart_time(new Date());
                        item.setInstant_time(new Date());
                        item.setSettlement(false);
                        item.setBiddersBeans(new ArrayList<>());
                        //重新写入缓存记录
                        SealedBean sealedBean2 = new SealedBean();
                        sealedBean2.setId(item.getId());
                        sealedBean2.setItem(JSON.toJSONString(itemBean));
                        sealedBean2.setPeriods(item.getNumber_periods());
                        sealedBean2.setItemName(item.getItemBean().getName());
                        sealedBean2.setPicture(item.getItemBean().getSmall_picture());
                        sealedBean2.setMarket_price(item.getPrice());
                        sealedBean2.setItemId(item.getItemBean().getId());
                        sealedBean2.setCost(item.getItemBean().getCost().setScale(2, BigDecimal.ROUND_DOWN).toString());
                        sealedBean2.setTotal(item.getClick_number().toString());
                        sealedBean2.setIncome(item.getActual_click_number().toString());
                        sealedBean2.setName("缓存初始化");
                        sealedBean2.setStateTime(item.getStart_time());
                        sealedBean2.setTime(new Date());
                        sealedBean2.setRobot(false);
                        sealedBean2.setSealType(SealType.IS_IN_PROGRESS);
                        sealedBean2.setSealed(false);
                        sealedDao.save(sealedBean2);
                        //通知机器人
                        if (robotService != null){
                            robotService.updateRecord(itemId);
                        }
                    }
                }
            }
        }
        logger.debug(itemId+"进行第"+item.getNumber_periods()+"期结算,结算时间为:"+new Date()
                +",即时时间为："+item.getInstant_time());
    }
}
