package com.mierro.main.service.impl;

import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.BiddersDao;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.service.BiddersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/19.
 * Remarks：
 */
@Service("BiddersService")
public class BiddersServiceImpl implements BiddersService {

    @Resource
    private BiddersDao biddersDao;

    @Override
    @Transactional
    public void save(List<BiddersBean> biddersBeans) {
        biddersDao.save(biddersBeans);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer findBySealedNumber(Long sealed,Long userId) {
        Integer number = biddersDao.findAllNumberBySealedId(sealed,userId);
        if (number == null) {
            number = 0;
        }
        return number;
    }

    @Override
    public Page<BiddersBean> findBySealed(Long sealed, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return biddersDao.findAllBySealedId(sealed,pageable);
    }
}
