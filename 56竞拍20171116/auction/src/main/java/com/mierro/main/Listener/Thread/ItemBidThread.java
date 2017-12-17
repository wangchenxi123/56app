package com.mierro.main.Listener.Thread;

import com.alibaba.fastjson.JSON;
import com.mierro.common.common.SpringTool;
import com.mierro.common.websocket.UserWebsocket;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.WebsocketMessageType;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.global.WebsocketResultMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class ItemBidThread extends Thread{

    private static Logger logger = LogManager.getLogger(ItemBidThread.class.getName());

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long itemId;

    /**
     * 活动期数
     */
    private Integer periods;

    /**
     * 剩余数目
     */
    private Integer number;

    /**
     * 总数目
     */
    private Integer allNumber;

    /**
     * 中断机制
     */
    private Boolean fuses = false;

    public ItemBidThread(Long userId, Long itemId, Integer periods, Integer number){
        this.userId = userId;
        this.itemId = itemId;
        this.periods = periods;
        this.number = number;
        this.allNumber = number;
    }

    public void run(){
        ItemCache.Item item = ItemCache.itemMap.get(itemId);

        if (ItemCache.user_auction.get(userId) == null){
            ConcurrentHashMap<Long,Integer> map = new ConcurrentHashMap<>();
            map.put(itemId,number);
            ItemCache.user_auction.put(userId,map);
        }else{
            if (ItemCache.user_auction.get(userId).get(itemId) == null){
                ItemCache.user_auction.get(userId).put(itemId,number);
            }else{
                if (ItemCache.user_auction.get(userId).get(itemId).equals(0)){
                    ItemCache.user_auction.get(userId).put(itemId,number);
                }else {
                    WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                    websocketResultMessage.setUserId(userId.toString());
                    websocketResultMessage.setItemId(item.getItemBean().getId().toString());
                    websocketResultMessage.setTime(new Date());
                    websocketResultMessage.setItemName(item.getItemBean().getName());
                    websocketResultMessage.setMessage("该商品您已经进行过排队竞拍操作了");
                    websocketResultMessage.setMessage_type(WebsocketMessageType.BID_ERROR_NOTICE);
                    websocketResultMessage.setPrice(item.getPrice());
                    //通知出价用户
                    UserWebsocket.sendMessage(userId,JSON.toJSONString(websocketResultMessage));
                }
            }
        }

        System.out.println("开始竞拍");
        //进行钱币扣除
        CoinDao coinDao = (CoinDao) SpringTool.getBeanByClass(CoinDao.class);
        if (!item.getItemBean().getIn_kind()){
            Integer coin = coinDao.findByUserIdAndCoinType(userId, CoinType.RECHARGE);
            if (coin != null && coin >= (this.allNumber*item.getPlus_code())){
                CoinBean coinBean = new CoinBean();
                coinBean.setTime(new Date());
                coinBean.setCoinType(CoinType.RECHARGE);
                coinBean.setNumber((this.allNumber*item.getPlus_code())*-1);
                coinBean.setPromotion(false);
                coinBean.setSource(CoinSource.WITHHOLDING_FEE);
                coinBean.setUserId(userId);
                coinBean.setReason("商品："+item.getItemBean().getName()+"(id:)"+item.getItemBean().getId()+"第"+
                        item.getNumber_periods()+"期投注");
                coinBean.setPlace(item.getId());
                coinDao.save(coinBean);
            }else{
                WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                websocketResultMessage.setUserId(userId.toString());
                websocketResultMessage.setItemId(item.getItemBean().getId().toString());
                websocketResultMessage.setTime(new Date());
                websocketResultMessage.setItemName(item.getItemBean().getName());
                websocketResultMessage.setMessage("钱币不足");
                websocketResultMessage.setMessage_type(WebsocketMessageType.BID_ERROR_NOTICE);
                websocketResultMessage.setPrice(item.getPrice());
                //通知出价用户
                UserWebsocket.sendMessage(userId,JSON.toJSONString(websocketResultMessage));
            }
            //商品单轮实际收益
        }else {//真实物品
            Integer gift = coinDao.findByUserIdAndCoinType(userId, CoinType.GIFT);
            if (gift == null) gift = 0;
            if (gift >= (this.allNumber*item.getPlus_code())) {//赠币足够
                CoinBean coinBean = new CoinBean();
                coinBean.setTime(new Date());
                coinBean.setCoinType(CoinType.GIFT);
                coinBean.setNumber(this.allNumber*item.getPlus_code()*-1);
                coinBean.setPromotion(false);
                coinBean.setSource(CoinSource.WITHHOLDING_FEE);
                coinBean.setUserId(userId);
                coinBean.setReason("商品："+item.getItemBean().getName()+"(id:)"+item.getItemBean().getId()+"第"+
                        item.getNumber_periods()+"期投注(预扣费)");
                coinBean.setPlace(item.getId());
                coinDao.save(coinBean);
            } else {//赠币不足
                Integer coin = coinDao.findByUserIdAndCoinType(userId, CoinType.GIFT, CoinType.RECHARGE);//查询用户赠币+拍币总数
                if (coin == null || coin < this.allNumber*item.getPlus_code()) {//总数不足
                    WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                    websocketResultMessage.setUserId(userId.toString());
                    websocketResultMessage.setItemId(item.getItemBean().getId().toString());
                    websocketResultMessage.setTime(new Date());
                    websocketResultMessage.setItemName(item.getItemBean().getName());
                    websocketResultMessage.setMessage("钱币不足");
                    websocketResultMessage.setMessage_type(WebsocketMessageType.BID_ERROR_NOTICE);
                    websocketResultMessage.setPrice(item.getPrice());
                    //通知出价用户
                    UserWebsocket.sendMessage(userId,JSON.toJSONString(websocketResultMessage));
                }
                //消费方案2(赠币不够先消耗赠币然后拍币补足)
                CoinBean coinBean = new CoinBean();
                coinBean.setTime(new Date());
                coinBean.setCoinType(CoinType.GIFT);
                coinBean.setNumber(gift * -1);
                coinBean.setPromotion(false);
                coinBean.setUserId(userId);
                coinBean.setSource(CoinSource.WITHHOLDING_FEE);
                coinBean.setReason("商品：" + item.getItemBean().getName() + "(id:)" + item.getItemBean().getId() + "第" +
                        item.getNumber_periods() + "期投注(预扣费)");
                coinBean.setPlace(item.getId());
                coinDao.save(coinBean);
                CoinBean coinBean1 = new CoinBean();
                coinBean1.setTime(new Date());
                coinBean1.setCoinType(CoinType.RECHARGE);
                coinBean1.setNumber((this.allNumber *item.getPlus_code() - gift) * -1);
                coinBean1.setPromotion(false);
                coinBean1.setSource(CoinSource.WITHHOLDING_FEE);
                coinBean1.setUserId(userId);
                coinBean1.setReason("商品：" + item.getItemBean().getName() + "(id:)" + item.getItemBean().getId() + "第" +
                        item.getNumber_periods() + "期投注(预扣费)");
                coinBean1.setPlace(item.getId());
                coinDao.save(coinBean1);
            }

//            Long mark =0L ;
//            //清空缓存
//            for (Map.Entry<Long, ConcurrentHashMap<Long,Integer>> entry : ItemCache.user_auction.entrySet()) {
//                for (Map.Entry<Long,Integer> entry2 : entry.getValue().entrySet()){
//                    if (entry2.getKey().equals(this.itemId)){
//                        //保存标志位
//                        mark = entry2.getKey();
//                        break;
//                    }
//                }
//
//                if (!mark.equals(0L)){
//                    ItemCache.user_auction.get(entry.getKey()).remove(mark);
//                    mark=0L;
//                }
//            }
        }

        //循环等待竞拍
        while (true){
            if (!fuses){
                item = ItemCache.itemMap.get(itemId);
                //判断活动是否过期
                if (!periods.equals(item.getNumber_periods()) || item.isSettlement() ||
                        (item.getInstant_time().getTime() + 10200) < new Date().getTime()){
                    System.out.println("进入线程异常结算");
                    //发送错误通知
                    if (!item.getUserId().equals(userId)){
                        WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                        websocketResultMessage.setUserId(userId.toString());
                        websocketResultMessage.setItemId(item.getItemBean().getId().toString());
                        websocketResultMessage.setTime(new Date());
                        websocketResultMessage.setItemName(item.getItemBean().getName());
                        websocketResultMessage.setMessage("出价失败通知，系统自动出价错误，竞拍商品"+
                                item.getItemBean().getName()+ "活动已经结束,竞拍失败，剩余金币系统自动退回");
                        websocketResultMessage.setMessage_type(WebsocketMessageType.BID_ERROR_NOTICE);
                        websocketResultMessage.setPrice(item.getPrice());
                        //通知出价用户
                        UserWebsocket.sendMessage(userId,JSON.toJSONString(websocketResultMessage));
                    }

                    //<!-----------------------------------退回拍币----------------------------------------->
                    //计算扣除总数
                    Integer coinNumber = this.allNumber - this.number;
                    //查找预扣费赠币币总数
                    Integer giftNumber =coinDao.findByUserIdAndPlaceAndCoinType(userId,item.getId(), CoinType.GIFT);
                    if (giftNumber == null ||giftNumber == 0){
                        //消耗的全部都是拍币，返回拍币
                        CoinBean coinBean2 = new CoinBean();
                        coinBean2.setTime(new Date());
                        coinBean2.setCoinType(CoinType.RECHARGE);
                        coinBean2.setNumber(this.number);
                        coinBean2.setPromotion(false);
                        coinBean2.setSource(CoinSource.ABNORMAL);
                        coinBean2.setUserId(userId);
                        coinBean2.setReason("商品："+item.getItemBean().getName()+"(id:)"+item.getItemBean().getId()+"第"+
                                item.getNumber_periods()+"期投注返回剩余拍币");
                        coinBean2.setPlace(item.getId());
                        coinDao.save(coinBean2);

                    }else{
                        giftNumber = Math.abs(giftNumber);
                        //扣除的全部都是赠币，并且，用户赠币还有剩余，需返回所有预扣除拍币跟剩余赠币
                        if (coinNumber < giftNumber){
                            //返回赠币
                            CoinBean coinBean2 = new CoinBean();
                            coinBean2.setTime(new Date());
                            coinBean2.setCoinType(CoinType.GIFT);
                            coinBean2.setNumber(giftNumber-coinNumber);
                            coinBean2.setPromotion(false);
                            coinBean2.setSource(CoinSource.ABNORMAL);
                            coinBean2.setUserId(userId);
                            coinBean2.setReason("商品："+item.getItemBean().getName()+"(id:)"+item.getItemBean().getId()+"第"+
                                    item.getNumber_periods()+"期投注返回剩余赠币");
                            coinBean2.setPlace(item.getId());
                            coinDao.save(coinBean2);
                            //查找预扣费拍币数
                            Integer recharge =coinDao.findByUserIdAndPlaceAndCoinType(userId,item.getId(), CoinType.RECHARGE);

                            if (recharge != null){
                                recharge = Math.abs(recharge);
                                //进行拍币返回
                                CoinBean coinBean3 = new CoinBean();
                                coinBean3.setTime(new Date());
                                coinBean3.setCoinType(CoinType.RECHARGE);
                                coinBean3.setNumber(recharge);
                                coinBean3.setPromotion(false);
                                coinBean3.setSource(CoinSource.ABNORMAL);
                                coinBean3.setUserId(userId);
                                coinBean3.setReason("商品："+item.getItemBean().getName()+"(id:)"+item.getItemBean().getId()+"第"+
                                        item.getNumber_periods()+"期投注返回剩余拍币");
                                coinBean3.setPlace(item.getId());
                                coinDao.save(coinBean3);
                            }
                        }else{
                            //赠币全部扣除，只返回拍币数
                            //查找预扣费拍币数
                            Integer recharge =coinDao.findByUserIdAndPlaceAndCoinType(userId,item.getId(), CoinType.RECHARGE);
                            if (recharge != null){
                                recharge = Math.abs(recharge);
                                //进行拍币返回
                                CoinBean coinBean3 = new CoinBean();
                                coinBean3.setTime(new Date());
                                coinBean3.setCoinType(CoinType.RECHARGE);
                                //预扣费拍币数- (拍币消耗总数 - 赠币消耗总数)
                                coinBean3.setNumber(recharge - (coinNumber - giftNumber));
                                coinBean3.setPromotion(false);
                                coinBean3.setSource(CoinSource.ABNORMAL);
                                coinBean3.setUserId(userId);
                                coinBean3.setReason("商品："+item.getItemBean().getName()+"(id:)"+item.getItemBean().getId()+"第"+
                                        item.getNumber_periods()+"期投注返回剩余拍币");
                                coinBean3.setPlace(item.getId());
                                coinDao.save(coinBean3);
                            }
                        }
                    }

                    //去除预扣费记录
                    CoinBean coinBean = coinDao.findCoinBeanByUserIdAndPlace(userId,item.getId(), CoinType.GIFT, CoinSource.WITHHOLDING_FEE);
                    if (coinBean != null){
                        coinBean.setSource(CoinSource.USE);
                        coinBean.setReason(coinBean.getReason()+"(以投注返回)");
                        coinDao.save(coinBean);
                    }

                    CoinBean coinBean2 = coinDao.findCoinBeanByUserIdAndPlace(userId,item.getId(), CoinType.RECHARGE, CoinSource.WITHHOLDING_FEE);
                    if (coinBean2 != null){
                        coinBean2.setSource(CoinSource.USE);
                        coinBean2.setReason(coinBean2.getReason()+"(以投注返回)");
                        coinDao.save(coinBean2);
                    }

                    //发送系统错误通知

                    //发送出价通知
                    WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                    websocketResultMessage.setUserId(userId.toString());
                    websocketResultMessage.setItemId(item.getItemBean().getId().toString());
                    websocketResultMessage.setTime(new Date());
                    websocketResultMessage.setItemName(item.getItemBean().getName());
                    websocketResultMessage.setMessage("自动出价失败：出现了异常，消耗拍币随后退还");
                    websocketResultMessage.setMessage_type(WebsocketMessageType.BID_ERROR_NOTICE);
                    break;
                }else {
                    //<!------------------------判断竞拍时间--------------------------------->
                    //计算当前时间跟商品即时时间差异
                    if (!number.equals(allNumber)) {
                        calculationTime(itemId);
                    }

                    //差异小于1.5秒,直接进行进行竞拍
                    try {
                        if (item.getUserId() == null || !item.getUserId().equals(userId)) {
                            try {
                                ItemCache.auction(userId, itemId, false, true);
                            }catch (Exception e){
                                e.printStackTrace();
                                logger.error("投票失败:" + e);
                            }
                            System.out.println("用户id :" + userId + ",服务器时间：" + new Date().getTime() +
                                    ",即时时间：" + item.getInstant_time().getTime() + "," +
                                    (new Date().getTime() - item.getInstant_time().getTime()));
                            sleep(500);
                            this.number--;
                            ItemCache.user_auction.get(userId).put(itemId, number);

                            //发送出价通知
                            WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
                            websocketResultMessage.setUserId(userId.toString());
                            websocketResultMessage.setItemId(item.getItemBean().getId().toString());
                            websocketResultMessage.setTime(new Date());
                            websocketResultMessage.setNumber(number);
                            websocketResultMessage.setItemName(item.getItemBean().getName());
                            websocketResultMessage.setMessage("出价成功通知");
                            websocketResultMessage.setMessage_type(WebsocketMessageType.BID_SUCCESS_NOTICE);
                            websocketResultMessage.setPrice(item.getPrice());
                            //通知出价用户
                            try {
                                UserWebsocket.sendMessage(userId, JSON.toJSONString(websocketResultMessage));
                            } catch (Exception e) {
                                System.out.println("出错了"+e);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("投票失败:" + e);
                        continue;
                    }

                    //结束判断
                    if (this.number <= 0) {
                        break;
                    }
                }
            }
        }

        Long mark =0L ;
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

        for (Map.Entry<Long, ConcurrentHashMap<Long,Thread>> entry : ItemCache.user_item_thread.entrySet()) {
            for (Map.Entry<Long,Thread> entry2 : entry.getValue().entrySet()){
                if (entry2.getKey().equals(this.itemId)){
                    //保存标志位
                    mark = entry2.getKey();
                    break;
                }
            }

            if (!mark.equals(0L)){
                ItemCache.user_item_thread.get(entry.getKey()).remove(mark);
                mark=0L;
            }
        }
    }

    private void calculationTime(Long itemId){
        ItemCache.Item item = ItemCache.getItemMap(itemId);
        Long time = item.getInstant_time().getTime() + (10000) - new Date().getTime();
        if (time >= 4000){
            //差异大于1.1秒,可以进行睡眠
            try {
                //重新计算睡眠时间
//                System.out.println(time-1500);
                sleep(time-4000);
            } catch (InterruptedException e) {
                logger.error("竞拍线程中断错误");
            }
            calculationTime(itemId);
        }
    }

    public Boolean getFuses() {
        return fuses;
    }

    public void setFuses(Boolean fuses) {
        this.fuses = fuses;
    }
}
