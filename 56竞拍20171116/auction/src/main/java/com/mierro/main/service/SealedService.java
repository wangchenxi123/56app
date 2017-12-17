package com.mierro.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.SealedBean;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
public interface SealedService {

    /**
     * 查询所有封存记录
     * @param itemName 商品名称
     * @param robot 是否机器人
     * @param stateTime 封存时间
     * @param endTime 封存时间
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<SealedBean> findAll(String itemName, Boolean robot, Date stateTime, Date endTime, Integer pageNo, Integer pageSize);

    /**
     * 查询详情
     * @param id 记录id
     * @return 记录对象
     */
    SealedBean findOne(Long id);

    /**
     * 添加一条记录
     * @param sealedBean 记录对象
     */
    void add(SealedBean sealedBean) throws JsonProcessingException;

    /**
     * 根据封存id查找出价记录
     * @param sealedId 封存id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 出价记录id
     */
    Page<BiddersBean> findAllBidders(Long sealedId, Integer pageNo, Integer pageSize);

    /**
     * 根据商品Id，查找商品历史封存记录
     * @param itemId
     * @return
     */
    Page<SealedBean> findHistory(Long itemId, Integer pageNo, Integer pageSize);

    /**
     * 根据用户id查询用户竞拍过的活动(带投入总数)
     * @param userId 用户id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 历史活动记录表
     */
    Page<SealedBean> findSealedByBidder(Long userId, Integer pageNo, Integer pageSize);
}
