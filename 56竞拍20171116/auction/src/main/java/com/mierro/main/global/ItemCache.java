package com.mierro.main.global;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.authority.dao.UserMessageDao;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.*;
import com.mierro.common.websocket.UserWebsocket;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.SealType;
import com.mierro.main.common.WebsocketMessageType;
import com.mierro.main.dao.BiddersDao;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.SealedBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：
 */
public class ItemCache {

    private static IdWorker idWorker;

    static {
        idWorker = (IdWorker) SpringTool.getBeanByClass(IdWorker.class);
    }

    public static class Item{
        /**
         * 单轮id
         */
        private Long id;

        /**
         * 商品bean
         */
        private ItemBean itemBean;

        /**
         * 期数
         */
        private Integer number_periods;

        /**
         * 开始时间
         */
        private Date start_time;

        /**
         * 当前价格
         */
        private String price;

        /**
         * 总共拍卖数
         */
        private Integer click_number;

        /**
         * 实际拍卖数
         */
        private Integer actual_click_number;

        /**
         * 即时时间
         */
        private Date instant_time;

        /**
         * 删除标志位
         */
        private Boolean isDelete;

        /**
         * 禁用标志位
         */
        private Boolean isDisable;

        /**
         * 是否进入结算
         */
        private volatile boolean settlement;

        /**
         * 每轮加码数目
         */
        private Integer plus_code;

        /**
         * 每轮加价
         */
        private String increase_the_price;

        /**
         * 当前竞拍者是否是机器人
         */
        @JSONField(serialize = false)
        private Boolean robot;

        /**
         * 最新竞拍者用户id
         */
        @JSONField(serialize = false)
        private Long userId;

        /**
         * 活动是否中断
         */
        private Boolean interrupted;

        /**
         * 竞拍人信息保存
         */
        private List<BiddersBean> biddersBeans = new ArrayList<>();

        /**
         * 竞拍中(true，有人正在竞拍，false)
         */
        private Boolean auction;

        //竞拍投票
        @Transactional
        synchronized void auction(Long userId,Boolean robot,Boolean automatic) throws IOException {
            if ((this.getInstant_time().getTime()+ 10000 ) < new Date().getTime()) {
                auction = false;
                throw new ServiceException("投票失败,该活动已经进入结算周期,"+
                        this.getItemBean().getId()+"  "+this.getItemBean().getName());
            }

            auction = true;
            if (this.settlement){
                auction = false;
                throw new ServiceException("投票失败,该活动已经进入结算周期,"+
                        this.getItemBean().getId()+"  "+this.getItemBean().getName());
            }
            UserMessage userMessage = UserCache.userCache.get(userId);
            if (robot.equals(true)){
                //查询机器人资料
                UserMessageDao userMessageDao = (UserMessageDao) SpringTool.getBeanByClass(UserMessageDao.class);
                userMessage = userMessageDao.findOne(userId);
            }

            if (userMessage == null && !automatic && robot.equals(false)){
                auction = false;
                throw new ServiceException("竞拍失败，请尝试重新登录,或者重启软件");
            }
            auction = true;
            if (userMessage == null){
                UserMessageDao userMessageDao = (UserMessageDao) SpringTool.getBeanByClass(UserMessageDao.class);
                userMessage = userMessageDao.findOne(userId);
            }

            this.click_number = this.click_number+this.plus_code;

            this.price = new BigDecimal(Float.parseFloat(this.price)+Float.parseFloat(this.increase_the_price))
                    .setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();

            if (robot){
                //商品单轮实际收益
                this.robot = true;
            }else{
                this.robot = false;

                //进行钱币扣除
                CoinDao coinDao = (CoinDao) SpringTool.getBeanByClass(CoinDao.class);
                if (!this.getItemBean().getIn_kind()){
                    if (!automatic){
                        Integer coin = coinDao.findByUserIdAndCoinType(userId, CoinType.RECHARGE);
                        if (coin != null && coin >= this.plus_code){
                            CoinBean coinBean = new CoinBean();
                            coinBean.setTime(new Date());
                            coinBean.setCoinType(CoinType.RECHARGE);
                            coinBean.setNumber(this.plus_code*-1);
                            coinBean.setPromotion(false);
                            coinBean.setSource(CoinSource.USE);
                            coinBean.setUserId(userId);
                            coinBean.setReason("商品："+itemBean.getName() +"(id:)"+itemBean.getId()+"第"+
                                    this.number_periods+"期投注");
                            coinBean.setPlace(this.id);
                            coinDao.save(coinBean);
                        }else{
                            throw new ServiceException(ResponseCode.INSUFFICIENT_COINS,"钱币不足");
                        }
                        //商品单轮实际收益
                        this.actual_click_number += (this.plus_code);
                    }else{
                        CoinBean coinBean = coinDao.findCoinBeanByUserIdAndPlace(userId,this.id,CoinType.RECHARGE, CoinSource.WITHHOLDING_FEE);

                        if (coinBean.getNumber() >=0){
                            coinDao.delete(coinBean);
                        }else {
                            coinBean.setNumber(coinBean.getNumber() + this.plus_code);
                            if (coinBean.getNumber() >= 0){
                                coinDao.delete(coinBean);
                            }else{
                                coinDao.save(coinBean);
                            }
                            CoinBean coinBean1 = new CoinBean();
                            coinBean1.setTime(new Date());
                            coinBean1.setCoinType(CoinType.RECHARGE);
                            coinBean1.setNumber(this.plus_code*-1);
                            coinBean1.setPromotion(false);
                            coinBean1.setSource(CoinSource.USE);
                            coinBean1.setUserId(userId);
                            coinBean1.setReason("商品："+itemBean.getName() +"(id:)"+itemBean.getId()+"第"+
                                    this.number_periods+"期投注");
                            coinBean1.setPlace(this.id);
                            coinDao.save(coinBean1);

                        }
                        //商品单轮实际收益
                        this.actual_click_number += (this.plus_code);
                    }
                }else {//真实物品
                    Integer gift = coinDao.findByUserIdAndCoinType(userId, CoinType.GIFT);
                    if (!automatic){
                        if (gift == null) gift = 0;
                        if (gift >= this.plus_code) {//赠币足够
                            CoinBean coinBean = new CoinBean();
                            coinBean.setTime(new Date());
                            coinBean.setCoinType(CoinType.GIFT);
                            coinBean.setNumber(this.plus_code*-1);
                            coinBean.setPromotion(false);
                            coinBean.setSource(CoinSource.USE);
                            coinBean.setUserId(userId);
                            coinBean.setReason("商品：" + itemBean.getName() + "(id:)" + itemBean.getId() + "第" +
                                    this.number_periods + "期投注");
                            coinBean.setPlace(id);
                            coinDao.save(coinBean);
                        } else {//赠币不足
                            Integer coin = coinDao.findByUserIdAndCoinType(userId, CoinType.GIFT, CoinType.RECHARGE);//查询用户赠币+拍币总数

                            if (coin == null || coin < this.plus_code) {//总数不足
                                throw new ServiceException(ResponseCode.INSUFFICIENT_COINS, "钱币补足");
                            }
                            //消费方案2(赠币不够先消耗赠币然后拍币补足)
                            if (gift != 0){
                                CoinBean coinBean = new CoinBean();
                                coinBean.setTime(new Date());
                                coinBean.setCoinType(CoinType.GIFT);
                                coinBean.setNumber(gift * -1);
                                coinBean.setPromotion(false);
                                coinBean.setSource(CoinSource.USE);
                                coinBean.setUserId(userId);
                                coinBean.setReason("商品：" + itemBean.getName() + "(id:)" + itemBean.getId() + "第" +
                                        this.number_periods + "期投注");
                                coinBean.setPlace(id);
                                coinDao.save(coinBean);
                            }
                            CoinBean coinBean1 = new CoinBean();
                            coinBean1.setTime(new Date());
                            coinBean1.setCoinType(CoinType.RECHARGE);
                            coinBean1.setSource(CoinSource.USE);
                            coinBean1.setNumber((this.plus_code - gift) * -1);
                            coinBean1.setPromotion(false);
                            coinBean1.setUserId(userId);
                            if (gift == 0){
                                coinBean1.setReason("商品：" + itemBean.getName() + "(id:)" + itemBean.getId() + "第" + this.number_periods + "期投注(补足)");
                            }else{
                                coinBean1.setReason("商品：" + itemBean.getName() + "(id:)" + itemBean.getId() + "第" + this.number_periods + "期投注");
                            }
                            coinBean1.setPlace(id);
                            coinDao.save(coinBean1);
                            this.actual_click_number += (this.plus_code-gift);
                        }
                    }else{
                        this.actual_click_number += (this.plus_code-gift);

                        CoinBean advance = coinDao.findCoinBeanByUserIdAndPlace(userId,this.id,CoinType.GIFT, CoinSource.WITHHOLDING_FEE);
                        if (advance == null){
                            CoinBean coinBean = coinDao.findCoinBeanByUserIdAndPlace(userId,this.id,CoinType.RECHARGE, CoinSource.WITHHOLDING_FEE);
                            if (coinBean.getNumber() >=0){
                                coinDao.delete(coinBean);
                            }else {
                                coinBean.setNumber(coinBean.getNumber() + this.plus_code);
                                if (coinBean.getNumber() >= 0){
                                    coinDao.delete(coinBean);
                                }else{
                                    coinDao.save(coinBean);
                                }
                                CoinBean coinBean1 = new CoinBean();
                                coinBean1.setTime(new Date());
                                coinBean1.setCoinType(CoinType.RECHARGE);
                                coinBean1.setNumber(this.plus_code*-1);
                                coinBean1.setPromotion(false);
                                coinBean1.setSource(CoinSource.USE);
                                coinBean1.setUserId(userId);
                                coinBean1.setReason("商品："+itemBean.getName() +"(id:)"+itemBean.getId()+"第"+
                                        this.number_periods+"期投注");
                                coinBean1.setPlace(this.id);
                                coinDao.save(coinBean1);
                            }
                        }else{
                            if (advance.getNumber() >=0){
                                coinDao.delete(advance);
                            }else{
                                //赠币不足扣费
                                if (Math.abs(advance.getNumber())<this.plus_code){
                                    CoinBean coinBean1 = new CoinBean();
                                    coinBean1.setTime(new Date());
                                    coinBean1.setCoinType(CoinType.GIFT);
                                    coinBean1.setNumber(advance.getNumber());
                                    coinBean1.setPromotion(false);
                                    coinBean1.setSource(CoinSource.USE);
                                    coinBean1.setUserId(userId);
                                    coinBean1.setReason("商品："+itemBean.getName() +"(id:)"+itemBean.getId()+"第"+
                                            this.number_periods+"期投注");
                                    coinBean1.setPlace(this.id);
                                    coinDao.save(coinBean1);

                                    CoinBean coinBean = coinDao.findCoinBeanByUserIdAndPlace(userId,this.id,CoinType.RECHARGE, CoinSource.WITHHOLDING_FEE);

                                    coinBean.setNumber(coinBean.getNumber()+advance.getNumber()+this.plus_code);
                                    coinDao.save(coinBean);
                                    //补扣
                                    CoinBean coinBean2 = new CoinBean();
                                    coinBean2.setTime(new Date());
                                    coinBean2.setCoinType(CoinType.RECHARGE);
                                    coinBean2.setNumber((advance.getNumber()+this.plus_code)*-1);
                                    coinBean2.setPromotion(false);
                                    coinBean2.setSource(CoinSource.USE);
                                    coinBean2.setUserId(userId);
                                    coinBean2.setReason("商品："+itemBean.getName() +"(id:)"+itemBean.getId()+"第"+
                                            this.number_periods+"期投注");
                                    coinBean2.setPlace(this.id);
                                    coinDao.save(coinBean2);

                                    coinDao.delete(advance);
                                }else{
                                    advance.setNumber(advance.getNumber()+ this.plus_code);
                                    if (advance.getNumber() >= 0){
                                        coinDao.delete(advance);
                                    }else{
                                        coinDao.save(advance);
                                    }
                                    CoinBean coinBean1 = new CoinBean();
                                    coinBean1.setTime(new Date());
                                    coinBean1.setCoinType(CoinType.GIFT);
                                    coinBean1.setNumber(this.plus_code*-1);
                                    coinBean1.setPromotion(false);
                                    coinBean1.setSource(CoinSource.USE);
                                    coinBean1.setUserId(userId);
                                    coinBean1.setReason("商品："+itemBean.getName() +"(id:)"+itemBean.getId()+"第"+
                                            this.number_periods+"期投注");
                                    coinBean1.setPlace(this.id);
                                    coinDao.save(coinBean1);
                                }
                            }
                        }
                    }
                }
            }

            this.userId = userId;
            this.setInstant_time(new Date());
            this.auction = false;
            //记录信息
            BiddersBean biddersMessage = new BiddersBean();
            biddersMessage.setAddress(userMessage.getAddress());
            biddersMessage.setAvatar(userMessage.getHead_pic());
            biddersMessage.setName(userMessage.getUsername());
            biddersMessage.setUserId(userId);
            biddersMessage.setSealedId(this.id);
            biddersMessage.setItemId(this.itemBean.getId());
            biddersMessage.setPrice(this.price);
            biddersMessage.setTime(new Date());
            BiddersDao biddersDao = (BiddersDao) SpringTool.getBeanByClass(BiddersDao.class);
            biddersDao.save(biddersMessage);

            //发送通知
            WebsocketResultMessage websocketResultMessage = new WebsocketResultMessage();
            websocketResultMessage.setAddress(userMessage.getAddress());
            websocketResultMessage.setUserId(userId.toString());
            websocketResultMessage.setItemId(this.itemBean.getId().toString());
            websocketResultMessage.setHead_pic(userMessage.getHead_pic().toString());
            websocketResultMessage.setTime(new Date());
            websocketResultMessage.setItemName(this.itemBean.getName());
            websocketResultMessage.setMessage("竞拍成功通知");
            websocketResultMessage.setUsername(userMessage.getUsername());
            websocketResultMessage.setMessage_type(WebsocketMessageType.BIDDER);
            websocketResultMessage.setPrice(this.price);
            //通知连线的所有人
            UserWebsocket.sendMessage(JSON.toJSONString(websocketResultMessage));
        }

        public ItemBean getItemBean() {
            return itemBean;
        }

        public void setItemBean(ItemBean itemBean) {
            this.itemBean = itemBean;
        }

        public Integer getNumber_periods() {
            return number_periods;
        }

        public void setNumber_periods(Integer number_periods) {
            this.number_periods = number_periods;
        }

        public Date getStart_time() {
            return start_time;
        }

        public void setStart_time(Date start_time) {
            this.start_time = start_time;
        }

        public Integer getClick_number() {
            return click_number;
        }

        public void setClick_number(Integer click_number) {
            this.click_number = click_number;
        }

        public Integer getActual_click_number() {
            return actual_click_number;
        }

        public void setActual_click_number(Integer actual_click_number) {
            this.actual_click_number = actual_click_number;
        }

        public Date getInstant_time() {
            return instant_time;
        }

        public void setInstant_time(Date instant_time) {
            this.instant_time = instant_time;
        }

        public Boolean getDelete() {
            return isDelete;
        }

        public void setDelete(Boolean delete) {
            isDelete = delete;
        }

        public boolean isSettlement() {
            return settlement;
        }

        public void setSettlement(boolean settlement) {
            this.settlement = settlement;
        }

        public Integer getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(Integer plus_code) {
            this.plus_code = plus_code;
        }

        public String getIncrease_the_price() {
            return increase_the_price;
        }

        public void setIncrease_the_price(String increase_the_price) {
            this.increase_the_price = increase_the_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getRobot() {
            return robot;
        }

        public void setRobot(Boolean robot) {
            this.robot = robot;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Boolean getDisable() {
            return isDisable;
        }

        public void setDisable(Boolean disable) {
            isDisable = disable;
        }

        public List<BiddersBean> getBiddersBeans() {
            return biddersBeans;
        }

        public void setBiddersBeans(List<BiddersBean> biddersBeans) {
            this.biddersBeans = biddersBeans;
        }

        public Boolean getInterrupted() {
            return interrupted;
        }

        public void setInterrupted(Boolean interrupted) {
            this.interrupted = interrupted;
        }

        public Boolean getAuction() {
            return auction;
        }

        public void setAuction(Boolean auction) {
            this.auction = auction;
        }
    }

    public static ConcurrentHashMap<Long,ConcurrentHashMap<Long,Thread>> user_item_thread = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long,ConcurrentHashMap<Long,Integer>> item_userId = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long,ConcurrentHashMap<Long,Integer>> user_auction = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long,Item> itemMap = new ConcurrentHashMap<>();

    public static List<Item> getItemMap(List<Long> itemIds){
        List<Item> items = new ArrayList<>();
        for (Long id: itemIds){
            if (itemMap.get(id) != null){
                Item item = Tool.dataCheckout(itemMap.get(id),new Item()) ;
                ItemBean itemBean = Tool.dataCheckout(itemMap.get(id).getItemBean(),new ItemBean());
                item.setPlus_code(itemBean.getPlus_code());
                item.setIncrease_the_price(itemBean.getIncrease_the_price());
                itemBean.setCost(null);
                itemBean.setControl_line(null);
                itemBean.setPurchaseAddress(null);
                itemBean.setRunning_program(null);
                itemBean.setBig_picture(null);
                itemBean.setLabel_str(null);
                item.setActual_click_number(null);
                item.setStart_time(null);
                item.setItemBean(itemBean);
                items.add(item);
            }
        }
        return items;
    }

    public static Item getItemMap(Long itemId){
        if (itemMap.get(itemId) != null){
            Item item = Tool.dataCheckout(itemMap.get(itemId),new Item()) ;
            ItemBean itemBean = Tool.dataCheckout(itemMap.get(itemId).getItemBean(),new ItemBean());
            item.setPlus_code(itemBean.getPlus_code());
            item.setIncrease_the_price(itemBean.getIncrease_the_price());
            itemBean.setBigPictures(JSON.parseArray(itemBean.getBig_picture(),String.class));
            itemBean.setCost(null);
            itemBean.setControl_line(null);
            itemBean.setRunning_program(null);
            itemBean.setPurchaseAddress(null);
            itemBean.setBig_picture(null);
            itemBean.setLabel_str(null);
            item.setActual_click_number(null);
            item.setStart_time(null);
            item.setItemBean(itemBean);
            return item;
        }else{
            throw new ServiceException("该商品没有开展活动,获取失败");
        }
    }

    private static Item getItemMap2(Long itemId){
        if (itemMap.get(itemId) != null){
            return itemMap.get(itemId);
        }else{
            throw new ServiceException("该商品没有开展活动,获取失败");
        }
    }

    public static Boolean load(List<ItemBean> itemBeans){
        for (ItemBean itemBean : itemBeans){
            Item item = new Item();
            item.setId(idWorker.nextId());
            item.setItemBean(itemBean);
            item.setNumber_periods(1);
            item.setPrice("0.00");
            item.interrupted = false;
            item.setAuction(false);
            item.setStart_time(new Date());
            item.setIncrease_the_price(itemBean.getIncrease_the_price());
            item.setActual_click_number(0);
            item.setDelete(false);
            item.setPlus_code(itemBean.getPlus_code());
            item.setClick_number(0);
            item.setInstant_time(new Date());
            item.settlement = false;
            item.setDisable(false);
            itemMap.put(itemBean.getId(),item);

            SealedBean sealedBean = new SealedBean();
            sealedBean.setId(item.getId());
            sealedBean.setItem(JSON.toJSONString(itemBean));
            sealedBean.setPeriods(item.getNumber_periods());
            sealedBean.setItemName(item.getItemBean().getName());
            sealedBean.setPicture(item.getItemBean().getSmall_picture());
            sealedBean.setMarket_price(item.getPrice());
            sealedBean.setItemId(item.getItemBean().getId());
            sealedBean.setCost(item.getItemBean().getCost().setScale(2, BigDecimal.ROUND_DOWN).toString());
            sealedBean.setTotal(item.getClick_number().toString());
            sealedBean.setIncome(item.getActual_click_number().toString());
            sealedBean.setName("缓存初始化");
            sealedBean.setStateTime(item.getStart_time());
            sealedBean.setTime(new Date());
            sealedBean.setRobot(false);
            SealedDao sealedDao = (SealedDao) SpringTool.getBeanByClass(SealedDao.class);
            sealedBean.setSealType(SealType.IS_IN_PROGRESS);
            sealedBean.setSealed(false);
            sealedDao.save(sealedBean);

        }
        return true;
    }

    public static void load(ItemBean itemBean){
        Item item = new Item();
        item.setId(idWorker.nextId());
        item.setItemBean(itemBean);
        item.setNumber_periods(1);
        item.setPlus_code(itemBean.getPlus_code());
        item.setIncrease_the_price(itemBean.getIncrease_the_price());
        item.setPrice("0.00");
        item.setAuction(false);
        item.setStart_time(new Date());
        item.setActual_click_number(0);
        item.setDisable(false);
        item.setDelete(false);
        item.setClick_number(0);
        item.setInstant_time(new Date());
        item.settlement = false;
        item.interrupted = false;
        itemMap.put(itemBean.getId(),item);
        SealedBean sealedBean = new SealedBean();
        sealedBean.setId(item.getId());
        sealedBean.setItem(JSON.toJSONString(itemBean));
        sealedBean.setPeriods(item.getNumber_periods());
        sealedBean.setItemName(item.getItemBean().getName());
        sealedBean.setPicture(item.getItemBean().getSmall_picture());
        sealedBean.setMarket_price(item.getPrice());
        sealedBean.setItemId(item.getItemBean().getId());
        sealedBean.setCost(item.getItemBean().getCost().setScale(2, BigDecimal.ROUND_DOWN).toString());
        sealedBean.setTotal(item.getClick_number().toString());
        sealedBean.setIncome(item.getActual_click_number().toString());
        sealedBean.setName("缓存初始化");
        sealedBean.setStateTime(item.getStart_time());
        sealedBean.setTime(new Date());
        sealedBean.setRobot(false);
        SealedDao sealedDao = (SealedDao) SpringTool.getBeanByClass(SealedDao.class);
        sealedBean.setSealType(SealType.IS_IN_PROGRESS);
        sealedBean.setSealed(false);
        sealedDao.save(sealedBean);
        //生成临时保存记录

    }

    public static void removeItem(Long itemId){
        SealedDao sealedDao = (SealedDao) SpringTool.getBeanByClass(SealedDao.class);
        SealedBean sealedBean = sealedDao.findItemId(itemId);
        if (sealedBean != null){
            sealedDao.save(sealedBean);
        }
        itemMap.remove(itemId);
    }

    public static void delete(Long itemId){
        Item item = itemMap.get(itemId);
        item.setDelete(true);
    }

    //竞拍投票
    public static void auction(Long userId , Long itemId, Boolean robot ,Boolean automatic) throws IOException {
        Item item = getItemMap2(itemId);

        if (item == null){
            throw new ServiceException(ResponseCode.ACTIVITY_ENDS,"找不到该活动");
        }
        item.auction = true;
        if (item.getInterrupted()){
            item.auction = false;
            throw new ServiceException(ResponseCode.BUSINESS,"该活动已经中断投票,投票失败");
        }

        if (item.getUserId() != null && item.getUserId().equals(userId)){
            item.auction = false;
            throw new ServiceException(ResponseCode.BUSINESS,"您已经是当前最新竞拍者了");
        }

        if (!robot){
            try{
                if (item_userId.get(userId) == null){
                    ConcurrentHashMap<Long,Integer> map = new ConcurrentHashMap<>();
                    map.put(itemId,0);
                    item_userId.put(userId,map);
                }
                Integer number = item_userId.get(userId).get(itemId);
                if (number == null) number = 0;
                number = number +item.plus_code;
                item_userId.get(userId).put(itemId,number);
            }catch (Exception e){
                item.auction = false;
                e.printStackTrace();
                throw new ServiceException("竞拍失败，发生未知异常");
            }
        }
        item.auction(userId,robot,automatic);
    }
}
