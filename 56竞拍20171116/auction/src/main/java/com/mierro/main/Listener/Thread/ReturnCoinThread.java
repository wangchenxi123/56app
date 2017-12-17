package com.mierro.main.Listener.Thread;

import com.mierro.common.common.SpringTool;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.dao.BiddersDao;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.entity.SealedBean;

import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class ReturnCoinThread extends Thread {

    private Long sealedId;

    ReturnCoinThread(Long sealedId){
        this.sealedId = sealedId;
    }

    @Override
    public void run() {
        //进行拍币返还
        SealedDao sealedDao = (SealedDao) SpringTool.getBeanByClass(SealedDao.class);
        BiddersDao biddersDao = (BiddersDao) SpringTool.getBeanByClass(BiddersDao.class);
        CoinDao coinDao = (CoinDao) SpringTool.getBeanByClass(CoinDao.class);
        SealedBean sealedBean = null;
        if (sealedId != null){
            sealedBean = sealedDao.findOne(sealedId);
        }
        if (sealedBean != null){
            List<BiddersBean> biddersBeans = biddersDao.findAllBySealedId(sealedId);
            for (BiddersBean biddersBean : biddersBeans){
                if (!biddersBean.getUserId().equals(sealedBean.getUserId())){
                    Integer coin = coinDao.findByUserIdAndPlaceAndCoinType(biddersBean.getUserId(),sealedId,CoinType.RECHARGE);
                    if (coin != null && coin != 0){
                        if ((int)(coin *0.3) != 0){
                            coin = Math.abs(coin);
                            CoinBean coinBean = new CoinBean();
                            coinBean.setTime(new Date());
                            coinBean.setCoinType(CoinType.GIFT);
                            coinBean.setNumber((int) (coin * 0.3));
                            coinBean.setPromotion(false);
                            coinBean.setSource(CoinSource.RETURN);
                            coinBean.setUserId(biddersBean.getUserId());
                            coinBean.setReason("商品"+sealedBean.getItemName()+"竞拍返回30%");
                            coinDao.save(coinBean);
                        }
                    }
                }
            }
        }
    }
}
