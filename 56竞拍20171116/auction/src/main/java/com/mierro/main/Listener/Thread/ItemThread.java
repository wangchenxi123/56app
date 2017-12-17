package com.mierro.main.Listener.Thread;


import com.alibaba.fastjson.JSON;
import com.mierro.authority.common.SystemSettings;
import com.mierro.authority.dao.UserMessageDao;
import com.mierro.common.common.IdWorker;
import com.mierro.common.common.SpringTool;
import com.mierro.main.dao.*;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.SystemNoticeService;
import com.mierro.robot.service.RobotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class ItemThread extends Thread{

    private static Logger logger = LogManager.getLogger(ItemThread.class);

    private Integer add = 10200;//10.2秒进行结算

    public ItemThread(String itemThreadName){
        super(itemThreadName);
    }

    public void run() {
        ItemCache.Item item;
        while (true) {
            if (isInterrupted()){
                System.out.println("发生了手动中断结算线程");
                logger.error("发生了手动中断结算线程");
                //清除缓存
                for (Map.Entry<Long, ItemCache.Item> entry : ItemCache.itemMap.entrySet()) {
                    ItemCache.Item item1= entry.getValue();
                    item1.setInterrupted(true);
                }
                break;
            }else{
                int i;
                //检测是否又商品已经到时间结算
                for(Map.Entry<Long,ItemCache.Item> e: ItemCache.itemMap.entrySet()){
                    item = e.getValue();
                    i = item.getClick_number();
                    if ((item.getInstant_time().getTime()+ add ) < new Date().getTime() && !item.isSettlement()
                            && !item.getAuction() && !SystemSettings.maintenance){
                        RobotService robotService = (RobotService) SpringTool.getBeanByClass(RobotService.class);
                        boolean auction = true;
                        try {
                            auction = robotService.isAuction(item.getItemBean().getId());
                        } catch (Exception e2){
                            logger.error("出现了机器人(重新出价错误)", e2);
                        }
                        if (auction) {
                            Long userId = robotService.getRandomRobotId(item.getItemBean().getId());
                            logger.error("出现了机器人必中方案非机器人中奖");
                            try {
                                ItemCache.auction(userId, item.getItemBean().getId(), true, false);
                            } catch (Exception e1) {
                                logger.error("出现了机器人必中方案非机器人中奖(重新出价错误)", e1);
                                logger.error("出现了异常中奖(重新出价错误)", e1);
                                // TODO: 2017/10/20  商品进入异常处理

                                //商品禁用，前台不展示
                                ItemDao itemDao = (ItemDao) SpringTool.getBeanByClass(ItemDao.class);
                                ItemBean itemBean =  itemDao.findOne(item.getItemBean().getId());
                                itemBean.setDisable(true);
                                itemBean.setFront_show(false);
                                itemDao.save(itemBean);

                                //进行数据查询通知管理员
                                SystemNoticeService systemNoticeService = (SystemNoticeService) SpringTool.getBeanByClass(SystemNoticeService.class);
                                BiddersDao biddersDao = (BiddersDao) SpringTool.getBeanByClass(BiddersDao.class);
                                CoinDao coinDao = (CoinDao) SpringTool.getBeanByClass(CoinDao.class);
                                SealedDao sealedDao = (SealedDao) SpringTool.getBeanByClass(SealedDao.class);
                                List<BiddersBean> list = biddersDao.findAllBySealedId(item.getId());

                                systemNoticeService.add("系统异常通知(商品用户中奖异常)",
                                        "出现用户异常中奖，内存缓存为："+ JSON.toJSONString(item) +
                                                ",用户所有投票记录为："+JSON.toJSONString(list));

                                //进行缓存记录(会进行退币)
                                biddersDao.delete(list);
                                coinDao.deleteByPlace(item.getId());
                                sealedDao.delete(item.getId());
                            }
                        } else {
                            item.setSettlement(true);
                            if (item.getClick_number() != i){
                                //总记录数需要回滚
                                //计算需要回滚轮速
                                int round = (item.getClick_number()-i)/item.getPlus_code();
                                item.setPrice((Double.parseDouble(item.getPrice())-(round*Double.parseDouble(item.getIncrease_the_price()))+""));
                                item.setClick_number(item.getClick_number()-i*item.getPlus_code());
                            }
                            ItemDao itemDao = (ItemDao) SpringTool.getBeanByClass(ItemDao.class);
                            UserMessageDao userMessageDao = (UserMessageDao) SpringTool.getBeanByClass(UserMessageDao.class);
                            SealedDao sealedDao = (SealedDao) SpringTool.getBeanByClass(SealedDao.class);
                            OrderDao orderDao = (OrderDao) SpringTool.getBeanByClass(OrderDao.class);
                            IdWorker idWorker = (IdWorker) SpringTool.getBeanByClass(IdWorker.class);
                            new ItemSettlementThread(e.getKey(),itemDao,robotService,userMessageDao,sealedDao
                                    ,orderDao,idWorker).start();
                        }
                    }
                }
            }
        }
    }
}
