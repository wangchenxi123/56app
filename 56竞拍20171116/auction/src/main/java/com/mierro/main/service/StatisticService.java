package com.mierro.main.service;

import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.StatisticsBean;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public interface StatisticService {

    /**
     * 获取即时系统统计信息
     * @param userId 用户id
     * @param httpServletRequest 用户请求
     * @return 统计信息
     */
    StatisticsBean getSystemBasisMessage(Long userId, HttpServletRequest httpServletRequest);

    /**
     * 分页获取系统每日统计
     * @param httpServletRequest
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 每日统计集合
     */
    Page<StatisticsBean> getSystemBasisMessage(HttpServletRequest httpServletRequest, Integer pageNo, Integer pageSize);

    /**
     * 查询所有已经充值的用户集合
     * @param countNumber 充值数目
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Map<String,Object> findRechargeRecording(String countNumber, Integer pageNo, Integer pageSize);

    /**
     * 获取用户热度(即时)
     * @param itemId 商品id
     * @param buyCount 购买总次数
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 用户热度信息
     */
    Page<ItemBean> itemHeat(Long itemId, Integer buyCount, Integer pageNo, Integer pageSize);
}
