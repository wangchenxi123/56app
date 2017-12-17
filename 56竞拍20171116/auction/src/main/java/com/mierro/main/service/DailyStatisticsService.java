package com.mierro.main.service;

import com.mierro.authority.entity.UserMessage;
import com.mierro.main.entity.DailyStatistics;
import org.springframework.data.domain.Page;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public interface DailyStatisticsService {

    //查询统计信息
    Page<DailyStatistics> findAll(Integer pageNo, Integer pageSize);

    /**
     * 获取即时统计数据
     * @return
     */
    DailyStatistics findImmediate();

    /**
     * 用户盈亏统计
     * @param balance 是否进行余额排序(true为是，false为否)
     * @param income 是否进行收益排序
     * @param totalRecharge 是否进行总充值额排序
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @return
     */
    Page<UserMessage> userProfit(Boolean balance, Boolean income, Boolean totalRecharge, String sortType, Integer pageNo, Integer pageSize);
}
