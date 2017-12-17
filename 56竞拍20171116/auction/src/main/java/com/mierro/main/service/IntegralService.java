package com.mierro.main.service;

import com.mierro.main.entity.IntegralBean;
import org.springframework.data.domain.Page;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：
 */
public interface IntegralService {

    /**
     * 查询某一个用户积分总数
     * @param userId 用户id
     * @return 积分总数
     */
    Integer findByUserId(Long userId);

    /**
     * 查询某一个用户积分流动详情
     * @param userId 用户id
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @return 积分流动详情
     */
    Page<IntegralBean> findByUserId(Long userId, Integer pageNo, Integer pageSize);
}
