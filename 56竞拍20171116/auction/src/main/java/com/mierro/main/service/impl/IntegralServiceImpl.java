package com.mierro.main.service.impl;

import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.IntegralDao;
import com.mierro.main.entity.IntegralBean;
import com.mierro.main.service.IntegralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：
 */
@Service("IntegralService")
public class IntegralServiceImpl implements IntegralService{

    @Resource
    private IntegralDao integralDao;

    /**
     * 查询某一个用户积分总数
     * @param userId 用户id
     * @return 积分总数
     */
    public Integer findByUserId(Long userId){
        return integralDao.findByUserId(userId);
    }

    /**
     * 查询某一个用户积分流动详情
     * @param userId 用户id
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @return 积分流动详情
     */
    public Page<IntegralBean> findByUserId(Long userId, Integer pageNo, Integer pageSize){
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return integralDao.findByUserId(userId,pageable);
    }
}
