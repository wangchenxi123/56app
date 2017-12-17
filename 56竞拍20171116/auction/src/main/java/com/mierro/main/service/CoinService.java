package com.mierro.main.service;

import com.mierro.main.common.CoinType;
import com.mierro.main.entity.CoinBean;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/7/31.
 * Remarks：钱币
 */

public interface CoinService {

    /**
     * 添加一条钱币记录
     * @param coinBean 钱币记录
     */
    void addCoin(CoinBean coinBean);

    /**
     * 查询某一个用户某一类型钱币流动记录
     * @param userId 用户id
     * @param coinType 钱币类型
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 钱币流动记录
     */
    Page<CoinBean> findAll(Long userId, CoinType coinType, Integer pageNo, Integer pageSize);

    /**
     * 查询某一个用户某一类型钱币总数
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 钱币总数
     */
    Integer findSUM(Long userId, CoinType coinType);

    /**
     * 查询某一个用户某一类型钱币统计情况
     * @param userId 用户id
     * @param coinType 钱币类型
     * @return 统计情况
     */
    Map<String,Object> findStatistics(Long userId, CoinType coinType);
}
