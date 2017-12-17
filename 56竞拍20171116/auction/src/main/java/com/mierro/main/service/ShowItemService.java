package com.mierro.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.main.entity.ShowItemBean;
import org.springframework.data.domain.Page;

/**
 * Created by 黄晓滨 simba on 2017/7/31.
 * Remarks：用户晒单
 */
public interface ShowItemService {

    /**
     * 添加一条晒单记录
     * @param showItemBean 晒单记录对象
     */
    void addShowItem(ShowItemBean showItemBean) throws JsonProcessingException;

    /**
     * 查询所有晒单记录
     * @param userId 用户id
     * @param itemId 商品id
     * @param orderId 订单id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 晒单分页对象
     */
    Page<ShowItemBean> findAll(Long userId, Long itemId, Long orderId, Integer pageNo, Integer pageSize);

    /**
     * 查询所有晒单记录
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 晒单分页对象
     */
    Page<ShowItemBean> findAll(Integer pageNo, Integer pageSize);

    /**
     * 修改是否展示前台展示
     * @param showId 晒单id
     */
    void settingShowItemStart(Long showId);
}
