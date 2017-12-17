package com.mierro.main.service.impl;

import com.mierro.common.common.VerifyException;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.service.CoinService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/7/31.
 * Remarks：钱币
 */
@Service("CoinService")
public class CoinServiceImpl implements CoinService{

    @Resource
    private CoinDao coinDao;

    @Override
    @Transactional
    public void addCoin(CoinBean coinBean) {
        coinBean.setTime(new Date());
        coinDao.save(coinBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CoinBean> findAll(Long userId, CoinType coinType, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return coinDao.findByUserIdAndCoinType(userId,coinType,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer findSUM(Long userId, CoinType coinType) {
        return coinDao.findByUserIdAndCoinType(userId,coinType);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findStatistics(Long userId, CoinType coinType) {
        Map<String, Object> map = new HashMap<>();
        //充值总数
        Integer recharge =  coinDao.findRechargeByUserIdAndCoinType(userId,coinType, CoinSource.RECHARGE);
        if (recharge == null){
            recharge = 0;
        }
        map.put("recharge",recharge);
        //消耗总数
        Integer consumption =  coinDao.findConsumptionByUserIdAndCoinTypeAndUse(userId,coinType, CoinSource.USE);
        if (consumption == null){
            consumption = 0;
        }
        //计算是否返还
        Integer returnNumber = coinDao.findConsumptionByUserIdAndCoinType(userId,coinType, CoinSource.ABNORMAL);
        if (returnNumber == null){
            returnNumber = 0;
        }
        Integer RETURN = coinDao.findConsumptionByUserIdAndCoinType(userId,coinType, CoinSource.RETURN);
        if (RETURN == null){
            RETURN = 0;
        }
        returnNumber =returnNumber + RETURN;
        consumption = Math.abs(consumption) - returnNumber;
        map.put("consumption",consumption);

        //赠送总数
        Integer gift =  coinDao.findConsumptionByUserIdAndCoinType(userId,coinType, CoinSource.Gift);
        if (gift == null){
            gift = 0;
        }
        map.put("gift",gift);

        //剩余数
        Integer remaining =  coinDao.findByUserIdAndCoinType(userId,coinType);
        if (remaining == null){
            remaining = 0;
        }
        map.put("remaining",remaining);
        return map;
    }
}
