package com.mierro.main.service.impl;

import com.mierro.authority.common.LoginEquipment;
import com.mierro.authority.dao.UserDao;
import com.mierro.authority.dao.UserMessageDao;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.SpringTool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.OrderState;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.DailyStatisticsDao;
import com.mierro.main.dao.OrderDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.DailyStatistics;
import com.mierro.main.service.DailyStatisticsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@Service("DailyStatisticsService")
public class DailyStatisticsServiceImpl implements DailyStatisticsService {

    @Resource
    private DailyStatisticsDao dailyStatisticsDao;
    @Resource
    private CoinDao coinDao;
    @Resource
    private SealedDao sealedDao;
    @Resource
    private UserMessageDao userMessageDao;

    @Override
    @Transactional(readOnly = true)
    public Page<DailyStatistics> findAll(Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.ASC,"time");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return dailyStatisticsDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public DailyStatistics findImmediate() {
        //运营状况统计
        DailyStatistics dailyStatistics = new DailyStatistics();
        //获取老用户id集合
        List<Long> winningUsers = sealedDao.findWinningUsers();
        //获取新用户id集合
        List<Long> newUsers;
        if (winningUsers != null){
            newUsers = userMessageDao.findAllNewUsers(winningUsers);
        }else{
            newUsers = userMessageDao.findAllNewUsers();
        }

        //计算时间区间
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = LocalDate.now().atStartOfDay(zoneId);
        Date startTime = Date.from(zdt.toInstant());
        Date endTime = new Date();

        //今天充值总额
        Integer totalRecharge = coinDao.findByCoinTypeAndCoinSourceAndTime(CoinType.RECHARGE, CoinSource.RECHARGE,
                startTime,endTime);
        if (totalRecharge == null){
            totalRecharge = 0;
        }
        dailyStatistics.setTotalRecharge(totalRecharge);

        //新用户充值总额
        Integer newUserRecharge;
        if (newUsers != null){
            newUserRecharge = coinDao.findByNewUserRechargeAndCoinTypeAndCoinSourceAndTime(newUsers,CoinType.RECHARGE,
                    CoinSource.RECHARGE,startTime,endTime);
            if (newUserRecharge == null){
                newUserRecharge = 0;
            }
        }else{
            newUserRecharge = 0;
        }
        dailyStatistics.setNewUserRecharge(newUserRecharge);

        //老用户充值总额
        Integer oldUserRecharge;
        if (winningUsers != null){
            oldUserRecharge = coinDao.findByNewUserRechargeAndCoinTypeAndCoinSourceAndTime(winningUsers,CoinType.RECHARGE,
                    CoinSource.RECHARGE,startTime,endTime);
            if (oldUserRecharge == null){
                oldUserRecharge = 0;
            }
        }else{
            oldUserRecharge = 0;
        }
        dailyStatistics.setOldUserRecharge(oldUserRecharge);

        //拍币消费
        Integer coinConsumption = coinDao.findByNewUserRechargeAndCoinTypeAndCoinSourceAndTime(winningUsers,
                CoinType.RECHARGE,CoinSource.USE,startTime,endTime);
        if (coinConsumption == null){
            coinConsumption = 0;
        }
        dailyStatistics.setCoinConsumption(Math.abs(coinConsumption));

        //赠币消费
        Integer giftConsumption = coinDao.findByNewUserRechargeAndCoinTypeAndCoinSourceAndTime(winningUsers,
                CoinType.GIFT,CoinSource.USE,startTime,endTime);
        if (giftConsumption == null){
            giftConsumption = 0;
        }
        dailyStatistics.setCoinConsumption(Math.abs(giftConsumption));
        //总消费
        dailyStatistics.setTotalConsumption(Math.abs(coinConsumption+giftConsumption));

        //新增用户数
        UserDao userDao = (UserDao) SpringTool.getBeanByClass(UserDao.class);
        Integer newUser = userDao.findCountNumber(startTime,endTime);
        if (newUser == null){
            newUser =0;
        }
        dailyStatistics.setNewUsers(newUser);

        //ios活跃数
        Integer iosActive = userDao.findCountNumberAndLoginEquipment(
                new ArrayList<>(Arrays.asList(LoginEquipment.IPAD, LoginEquipment.IPHONE,
                        LoginEquipment.MAC, LoginEquipment.IPAD, LoginEquipment.IPOD)),startTime,endTime);
        if (iosActive == null){
            iosActive =0;
        }
        dailyStatistics.setIosActive(iosActive);

        //android活跃数
        Integer androidActive = userDao.findCountNumberAndLoginEquipment(
                new ArrayList<>(Arrays.asList(LoginEquipment.PAD, LoginEquipment.ANDROID)),startTime,endTime);
        if (androidActive == null){
            androidActive =0;
        }
        dailyStatistics.setIosActive(androidActive);

        //用户拍中付款金额
        OrderDao orderDao = (OrderDao) SpringTool.getBeanByClass(OrderDao.class);
        Integer paymentAmount = orderDao.findAll(startTime,endTime);
        if (paymentAmount == null){
            paymentAmount =0;
        }
        dailyStatistics.setPaymentAmount(paymentAmount);

        //平台发货总额
        Integer deliveryAmount = orderDao.findAllTotalShip(startTime,endTime);
        if (deliveryAmount == null){
            deliveryAmount =0;
        }
        dailyStatistics.setDeliveryAmount(deliveryAmount);

        dailyStatistics.setTime(new Date());
        return dailyStatistics;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserMessage> userProfit(Boolean balance, Boolean income, Boolean totalRecharge, String sortType,
                                        Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Pageable pageable = new PageRequest(--pageNo,pageSize);
        Page<UserMessage> userMessages;
        if (balance != null) {
            //用户剩余余额进行排序
            if (sortType == null || sortType.equals("DESC")){
                userMessages = dailyStatisticsDao.findUserSortBalanceDesc(CoinType.RECHARGE,pageable);
            }else{
                userMessages = dailyStatisticsDao.findUserSortBalanceAsc(CoinType.RECHARGE,pageable);
            }

        } else if (income != null) {
            //对用户的收益进行排序
            if (sortType == null || sortType.equals("DESC")){
                userMessages = dailyStatisticsDao.findUserSortIncomeDesc(CoinType.RECHARGE,CoinSource.RECHARGE,
                        OrderState.WAITING_PAYMENT,pageable);
            }else{
                userMessages = dailyStatisticsDao.findUserSortIncomeDesc(CoinType.RECHARGE,CoinSource.RECHARGE,
                        OrderState.WAITING_PAYMENT,pageable);
            }

        } else if (totalRecharge != null) {
            //对总充值金额进行排序
            if (sortType == null || sortType.equals("DESC")){
                userMessages = dailyStatisticsDao.findUserSortTotalRechargeDesc
                        (CoinType.RECHARGE,CoinSource.RECHARGE,pageable);
            }else{
                userMessages = dailyStatisticsDao.findUserSortIncomeAsc(CoinType.RECHARGE,CoinSource.RECHARGE,
                        OrderState.WAITING_PAYMENT,pageable);
            }
        }else{
            userMessages = dailyStatisticsDao.findUserProfit(pageable);
        }
        return userMessages.map(userMessage -> {
            //查询总充值额
            Integer total_recharge = coinDao.findConsumptionByUserIdAndCoinType
                    (userMessage.getId(), CoinType.RECHARGE, CoinSource.RECHARGE);
            if (total_recharge == null){
                total_recharge = 0;
            }
            //查询余额
            Integer balanceNumber = coinDao.findByUserIdAndCoinType(userMessage.getId(),CoinType.RECHARGE);
            if (balanceNumber == null){
                balanceNumber = 0;
            }
            //查询中奖商品总额度(奖品总成本-订单应付金额)
            Integer reward = dailyStatisticsDao.userReward(userMessage.getId());
            if (reward == null){
                reward = 0;
            }
            userMessage.setTotal_recharge(total_recharge);
            userMessage.setBalanceNumber(balanceNumber);
            userMessage.setIncome((total_recharge-reward));
            return userMessage;
        });
    }
}
