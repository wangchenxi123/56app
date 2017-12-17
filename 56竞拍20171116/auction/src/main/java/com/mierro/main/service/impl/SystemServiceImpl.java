package com.mierro.main.service.impl;

import com.mierro.authority.dao.ConfigDao;
import com.mierro.authority.entity.ConfigBean;
import com.mierro.common.common.ServiceException;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.ConfigSetting;
import com.mierro.main.dao.BiddersDao;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.service.SystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@Service("SystemService")
public class SystemServiceImpl implements SystemService {

    @Resource
    private SealedDao sealedDao;

    @Resource
    private BiddersDao biddersDao;

    @Resource
    private CoinDao coinDao;

    @Resource
    private ConfigDao configDao;

    @Override
    @Transactional
    public void deleteCache() {
        //删除缓存临时记录
        List<SealedBean> sealedBeans =  sealedDao.findAllCache();
        if (!sealedBeans.isEmpty()){
            sealedDao.delete(sealedBeans);
        }
        //删除出价记录
        for (SealedBean sealedBean : sealedBeans){
            List<BiddersBean> biddersBeans = biddersDao.findAllBySealedId(sealedBean.getId());
            //返回真实用户拍币或者赠币
            for(BiddersBean biddersBean : biddersBeans){
                Integer gift = coinDao.findByUserIdAndPlace(biddersBean.getUserId(),sealedBean.getId(), CoinType.GIFT);
                Integer coin = coinDao.findByUserIdAndPlace(biddersBean.getUserId(),sealedBean.getId(), CoinType.RECHARGE);

                if (gift != null){
                    CoinBean coinBean = new CoinBean();
                    coinBean.setTime(new Date());
                    coinBean.setCoinType(CoinType.GIFT);
                    coinBean.setSource(CoinSource.ABNORMAL);
                    coinBean.setNumber(Math.abs(gift));
                    coinBean.setUserId(biddersBean.getUserId());
                    coinBean.setReason("竞拍"+sealedBean.getItemName()+"系统异常赠币返回");
                    coinBean.setPromotion(false);
                    coinDao.save(coinBean);
                }

                if (coin != null){
                    CoinBean coinBean = new CoinBean();
                    coinBean.setTime(new Date());
                    coinBean.setCoinType(CoinType.RECHARGE);
                    coinBean.setNumber(Math.abs(coin));
                    coinBean.setSource(CoinSource.ABNORMAL);
                    coinBean.setUserId(biddersBean.getUserId());
                    coinBean.setReason("竞拍"+sealedBean.getItem()+"系统异常拍币返回");
                    coinBean.setPromotion(false);
                    coinDao.save(coinBean);
                }
            }
            biddersDao.deleteBySealedId(sealedBean.getId());
        }
    }

    @Override
    @Transactional
    public void setting_recharge() {
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.recharge);
        if (configBean.getValue().equals("true")){
            configBean.setValue("false");
        }else{
            configBean.setValue("true");
        }
        configDao.save(configBean);
    }

    @Override
    @Transactional
    public Boolean findRecharge() {
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.recharge);
        Boolean start;
        if (configBean.getValue().equals("true")){
            start = true;
        }else{
            start = false;
        }
        return start;
    }

    @Override
    @Transactional
    public void setting_gift_coin(Integer number) {
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.gift_coin);
        configBean.setValue(Math.abs(number)+"");
    }

    @Override
    @Transactional(readOnly = true)
    public String find_gift_coin() {
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.gift_coin);
        return configBean.getValue();
    }

}
