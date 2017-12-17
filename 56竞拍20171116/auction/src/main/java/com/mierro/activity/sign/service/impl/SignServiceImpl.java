package com.mierro.activity.sign.service.impl;

import com.alibaba.fastjson.JSON;
import com.mierro.activity.sign.entity.ExchangeKey;
import com.mierro.activity.sign.service.SignService;
import com.mierro.authority.dao.*;
import com.mierro.authority.entity.ConfigBean;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ServiceException;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.ConfigSetting;
import com.mierro.main.common.OperationalType;
import com.mierro.main.dao.*;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.entity.IntegralBean;
import com.mierro.activity.sign.entity.Prize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@Service("SignService")
public class SignServiceImpl implements SignService {

    @Resource
    private UserMessageDao userMessageDao;
    @Resource
    private CoinDao coinDao;
    @Resource
    private ConfigDao configDao;
    @Resource
    private IntegralDao integralDao;

    @Override
    @Transactional
    public Prize reported(Long userId) {
        UserMessage userMessage = userMessageDao.findOne(userId);
        Integer temp = userMessage.getDays();
        Date integralDate = integralDao.findLatest(userId, OperationalType.CHECK_IN);
        //判断是否允许签到

        LocalDate localDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date endTime = Date.from(zdt.toInstant());
        if (integralDate != null && endTime.getTime() < integralDate.getTime()){
            throw new ServiceException("您已经签到过了");
        }

        //判断当前时间跟积分最新记录时间是否差一天
        if (integralDate == null || (new Date().getTime() - integralDate.getTime()) > 86400000){
            temp = null;
        }
        IntegralBean integralBean = new IntegralBean();
        integralBean.setUserId(userId);
        String number;
        if (temp == null){
            number = configDao.findByKeyId(ConfigSetting.one_day).getValue();
            integralBean.setReason("第一天签到");
            userMessage.setDays(1);
        }else if(temp == 1){
            number = configDao.findByKeyId(ConfigSetting.two_day).getValue();
            integralBean.setReason("第二天签到");
            userMessage.setDays(2);
        }else if(temp == 2){
            number = configDao.findByKeyId(ConfigSetting.three_day).getValue();
            integralBean.setReason("第三天签到");
            userMessage.setDays(3);
        }else if(temp == 3){
            number = configDao.findByKeyId(ConfigSetting.four_day).getValue();
            integralBean.setReason("第四天签到");
            userMessage.setDays(4);
        }else if(temp == 4){
            number = configDao.findByKeyId(ConfigSetting.five_day).getValue();
            integralBean.setReason("第五天签到");
            userMessage.setDays(5);
        }else if(temp == 5){
            number = configDao.findByKeyId(ConfigSetting.six_day).getValue();
            integralBean.setReason("第六天签到");
            userMessage.setDays(6);
        }else if(temp == 6){
            number = configDao.findByKeyId(ConfigSetting.seven_day).getValue();
            integralBean.setReason("第七天签到");
            userMessage.setDays(7);
        }else{
            number = configDao.findByKeyId(ConfigSetting.one_day).getValue();
            integralBean.setReason("第一天签到");
            userMessage.setDays(1);
        }
        integralBean.setNumber(Integer.parseInt(number));
        integralBean.setTime(new Date());
        integralBean.setOperationalType(OperationalType.CHECK_IN);
        userMessageDao.save(userMessage);
        integralDao.save(integralBean);
        //进行随机抽奖
        return lottery(userId);
    }

    @Override
    @Transactional
    public Map<String, Object> reportMessage(Long userId) {
        //获取用户连续签到天数
        //判断当前时间跟积分最新记录时间是否差一天
        UserMessage userMessage = userMessageDao.findOne(userId);
        Integer temp = userMessage.getDays();
//        Date integralDate = integralDao.findLatest(userId, OperationalType.CHECK_IN);
//        if (integralDate == null || (new Date().getTime() - integralDate.getTime()) > 86400000){
//            temp = 0;
//        }
        if (temp == null){
            temp = 0;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("days",temp);
        return map;
    }

    @Override
    @Transactional
    public Prize receiveTreasure(Long userId) {
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.fixed_chest);
        List<Prize> prizes = JSON.parseArray(configBean.getValue(),Prize.class);
        if (prizes != null && !prizes.isEmpty()){
            UserMessage userMessage = userMessageDao.findOne(userId);
            for (Prize prize : prizes){
                if (userMessage.getDays().equals(prize.getNumber())){
                    //领奖
                    if (prize.getIntegral() != null){
                        //判断是否到达领奖条件
                        IntegralBean integral = integralDao.findOne(userId, OperationalType.EXCHANGE);
                        if (integral != null && integral.getNumber().equals(prize.getIntegral())
                                && integral.getReason().equals("连续登录奖励宝箱") &&
                                (integral.getTime().getTime() + 86400000) > new Date().getTime()){
                            throw new ServiceException(ResponseCode.BUSINESS,"您今天已经领过啦");
                        }

                        IntegralBean integralBean = new IntegralBean();
                        integralBean.setReason("连续登录奖励宝箱");
                        integralBean.setUserId(userId);
                        integralBean.setNumber(prize.getIntegral());
                        integralBean.setOperationalType(OperationalType.EXCHANGE);
                        integralBean.setTime(new Date());
                        integralDao.save(integralBean);
                    }
                    if (prize.getCoin() != null){
                        CoinBean coinBean = new CoinBean();
                        coinBean.setTime(new Date());
                        coinBean.setCoinType(prize.getCoinType());
                        coinBean.setNumber(prize.getCoin());
                        coinBean.setUserId(userId);
                        coinBean.setReason("连续登录奖励宝箱");
                        coinBean.setPromotion(false);
                        coinDao.save(coinBean);
                    }
                    return prize;
                }
            }
            throw new ServiceException(ResponseCode.ACCESSDENIED,"没有到达领取条件，请继续加油");
        }
        throw new ServiceException(ResponseCode.ACTIVITY_ENDS,"活动没有开启");
    }

    @Override
    @Transactional
    public void exchange(Long userId, ExchangeKey exchangeKey) {
        //检测用户积分数
        Integer number = integralDao.findByUserId(userId);
        if (number == null || number < exchangeKey.getIntegral()){
            throw new ServiceException(ResponseCode.BUSINESS,"积分不足");
        }

        //扣除积分
        IntegralBean integralBean = new IntegralBean();
        integralBean.setReason("积分兑换扣除");
        integralBean.setUserId(userId);
        integralBean.setNumber(exchangeKey.getIntegral()*-1);
        integralBean.setOperationalType(OperationalType.GIFT);
        integralBean.setTime(new Date());
        integralDao.save(integralBean);
        //添加拍币
        CoinBean coinBean = new CoinBean();
        coinBean.setTime(new Date());
        coinBean.setCoinType(CoinType.GIFT);
        coinBean.setNumber(exchangeKey.getCoin());
        coinBean.setUserId(userId);
        coinBean.setReason("积分兑换增加");
        coinBean.setPromotion(false);
        coinDao.save(coinBean);
    }

    @Override
    @Transactional
    public void settingFixedReward(List<Prize> prizeList) {
        //校验传入参数值是否正确
        for (Prize prize :prizeList){
            if (prize.getNumber() == null){
                throw new ServiceException(ResponseCode.BUSINESS,"必须传入控制参数");
            }else{
                if ((prize.getCoin() != null && prize.getCoinType() == null) ||
                        prize.getCoin() == null && prize.getCoinType() != null){
                    throw new ServiceException(ResponseCode.BUSINESS,"需要赠送拍币，则需要传入拍币类型和赠送数量");
                }
            }
        }
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.fixed_chest);
        configBean.setValue(JSON.toJSONString(prizeList));
        configDao.save(configBean);
    }


    //签到随机奖励抽奖
    private Prize lottery(Long userId){
        return null;
    }
}
