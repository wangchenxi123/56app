package com.mierro.robot.service.impl;

import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.SystemNoticeService;
import com.mierro.robot.dao.RobotCoinDao;
import com.mierro.robot.dao.RobotConfigDao;
import com.mierro.robot.dao.RobotSealedDao;
import com.mierro.robot.entity.RobotConfigBean;
import com.mierro.robot.entity.RobotItemBean;
import com.mierro.robot.entity.e.RobotConfig;
import com.mierro.robot.entity.view.RechargeCoinConsume;
import com.mierro.robot.service.RobotItemService;
import com.mierro.robot.service.RobotService;
import com.mierro.robot.service.RobotUserService;
import com.mierro.robot.thread.RobotBackgroundThread;
import com.mierro.robot.utils.Tool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.math.BigDecimal;


/**
 * 机器人模块业务实现
 * Created by tlseek on 2017/8/10.
 */
@Service("robotService")
public class RobotServiceImpl implements RobotService {

    private static Logger LOG = LogManager.getLogger(RobotServiceImpl.class.getName());

    @Resource
    private RobotConfigDao robotConfigDao;
    @Resource
    RobotSealedDao robotSealedDao;
    @Resource
    RobotCoinDao robotCoinDao;
    @Resource
    RobotUserService robotUserService;
    @Resource
    RobotItemService robotItemService;
    @Resource
    SystemNoticeService systemNoticeService;

    private volatile RobotBackgroundThread robotBackground = null;
    private volatile WatchThread watchThread;

    boolean isInit = false;
    @PostConstruct
    public void init() {
        if (isInit) {
            return;
        }
        // 初始化config配置
        if (robotConfigDao.count() != RobotConfig.values().length) {
            for (RobotConfig robotConfig : RobotConfig.values()) {
                RobotConfigBean bean = new RobotConfigBean();
                bean.setName(robotConfig);
                bean.setMessage(robotConfig.name);
                bean.setValue(robotConfig.defaultValue);
                try {
                    robotConfigDao.save(bean);
                } catch (Exception e){
                    LOG.debug("初始化机器人配置表 属性:" + robotConfig + " " + e.getMessage());
                }
            }
        }

        // 机器人初始化前, 先启动机器人后台进程
        robotBackground = new RobotBackgroundThread(this);
        robotBackground.start();

        // 机器人后台守护线程
        watchThread = new WatchThread();
        watchThread.start();

        LOG.debug("初始化机器人用户默认信息......");
        try {
            robotUserService.loading();
        } catch (Exception e){
            LOG.error("初始化机器人用户默认信息失败",e);
        }
        LOG.debug("初始化机器人商品默认信息......");
        try {
            robotItemService.loading();
        } catch (Exception e){
            LOG.error("初始化机器人商品默认信息失败",e);
        }
        isInit = true;
    }

    @PreDestroy
    public void destory() {
        watchThread.end();
        robotBackground.end();
    }

    @Override
    public Long getRandomRobotId(Long itemId) {
        return robotUserService.getRandomRobotId(itemId);
    }

    @Override
    public RobotItemBean getRobotItemBean(ItemBean item) {
        RobotItemBean robotItemBean = robotItemService.getRobotItemBean(item);
        String string = robotSealedDao.averageMarketPrice(item.getId());
        if (string != null) {
            try {
                Float aFloat = Float.valueOf(string);
                if (aFloat > 1f) {
                    robotItemBean.setAverageMarketPrice(new BigDecimal(string));
                }
            } catch (Exception e){
                LOG.error("",e);
            }
        }
        return robotItemBean;
    }

    @Override
    public void updateRecord(Long itemId) {
        robotBackground.updateTerm(itemId);
        robotUserService.refreshRandomRobot(itemId);
    }

    @Override
    public void updateRecord(Long itemId, Boolean refreshItemRobotUser) {
        robotBackground.updateTerm(itemId);
        if(refreshItemRobotUser)
            robotUserService.refreshRandomRobot(itemId);
    }

    @Override
    public void errorItemHandler(String message, Long itemId) {
        systemNoticeService.add("机器人线程后台处理商品(" + itemId+")未知异常", message);
        ItemCache.removeItem(itemId);
    }

    @Override
    public boolean isBackgroundServiceRunning() {
        return robotBackground.isRunning();
    }

    @Override
    public boolean startOrStopBackgroundService() {
        if (robotBackground.isRunning()) {
            watchThread.end();
            robotBackground.end();
        } else {
            watchThread.end();
            robotBackground = new RobotBackgroundThread(this);
            robotBackground.start();
            watchThread = new WatchThread();
            watchThread.start();
        }
        return robotBackground.isRunning();
    }

    @Override
    public boolean isAuction(Long itemId) {
        return robotBackground.isAuction(itemId);
    }

    @Override
    public String getConfig(RobotConfig key) {
        return robotConfigDao.get(key);
    }

    @Override
    public String putConfig(RobotConfig key, String value) {
        if (key.equals(RobotConfig.COIN_CONSUME_RATE)) {
            robotBackground.setCoinCunsumeRate(Float.valueOf(value));
        } else if (key.equals(RobotConfig.MIN_MARKET_PRICE)) {
            robotBackground.setMinMarketPrice(Float.valueOf(value));
        }
        robotConfigDao.put(key,value);
        return value;
    }

    @Override
    public RechargeCoinConsume userCoinCousumeInfo(Long userId) {
        Integer recharge = Tool.getOrDefault(robotCoinDao.findRechargeByUserIdAndCoinType(userId, CoinType.RECHARGE, CoinSource.RECHARGE), 0);
        Integer gift = Tool.getOrDefault(robotCoinDao.findRechargeByUserIdAndCoinType(userId, CoinType.RECHARGE, CoinSource.Gift), 0);
        return new RechargeCoinConsume(
                recharge + gift,
                Tool.getOrDefault(robotCoinDao.findByUserIdAndCoinType(userId, CoinType.RECHARGE), 0),
                Tool.getOrDefault(robotCoinDao.findRechargeByUserIdAndCoinType(userId, CoinType.RECHARGE, CoinSource.WITHHOLDING_FEE), 0),
                Tool.getOrDefault(Float.valueOf(Tool.getOrDefault(robotSealedDao.sumCost(userId), "0")), 0f)
        );
    }

    /**
     * 守护进程
     */
    class WatchThread extends Thread {

        volatile boolean stop = false;
        volatile boolean end = false;

        @Override
        public void run() {
            while (!stop) {
//                LOG.debug("守护线程检测");
                if (!isBackgroundServiceRunning()) {
                    LOG.info("重启机器人进程");
                    robotBackground = new RobotBackgroundThread(RobotServiceImpl.this);
                    robotBackground.start();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            end = true;
        }

        public void end(){
            stop = true;
            while (!end);
        }
    }
}
