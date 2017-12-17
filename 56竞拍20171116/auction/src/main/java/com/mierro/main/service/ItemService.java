package com.mierro.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.main.common.RunningProgram;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：
 */
public interface ItemService {

    /**
     * 添加一个新的商品
     * @param itemBean 商品对象
     * @throws JsonProcessingException json转换错误
     */
    void addItem(ItemBean itemBean) throws JsonProcessingException;

    /**
     * 根据条件分页查找商品
     * @param name 商品名称
     * @param type 商品类型
     * @param disable 是否禁用
     * @param runningProgram 运行方式
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 商品分页对象
     */
    Page<ItemBean> findAll(String name, String type, Boolean disable, RunningProgram runningProgram,
                           Integer plusCode, Integer pageNo, Integer pageSize);

    /**
     * 查找单个商品
     * @param itemId 商品id
     * @return 商品对象
     */
    ItemBean findOne(Long itemId);

    /**
     * 删除一个商品
     * @param itemId 商品id
     */
    void delete(Long itemId);

    /**
     * 强制删除一个商品
     * @param itemId 商品id
     */
    void forcedDelete(Long itemId);

    /**
     * 更新商品信息
     * @param itemBean 商品对象
     */
    void updateItem(ItemBean itemBean) throws JsonProcessingException;

    /**
     * 查找所有商品
     * @param disable 是否禁用
     * @return 商品对象
     */
    List<ItemBean> findAll(Boolean disable) throws IOException;

    /**
     * 前段分页查询所有商品
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Map<String,Object> clientFindAll(Integer pageNo, Integer pageSize);

    /**
     * 前段分页查询所有商品
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Map<String,Object> clientFind(Integer pageNo, Integer pageSize);

    /**
     * 前端查询单个商品
     * @param itemId 页码
     * @param number 出价记录数目
     * @return 分页对象
     */
    ItemCache.Item clientFindOne(Long itemId, Integer number);

    /**
     * 修改商品的状态
     * @param itemId 商品id
     */
    void updateItemState(Long itemId);

    /**
     * 修改商品是否前端展示
     * @param itemId 商品id
     */
    void updateItemShow(Long itemId);

    /**
     * 修改商品是否前端展示
     * @param itemId 商品id
     */
    void updateItemNovice(Long itemId);

    /**
     * 投币竞拍
     * @param itemId 商品id
     * @param userId 用户id
     * @param periods 活动期数
     */
    void auction(Long itemId, Integer periods, Long userId);

    /**
     * 投币竞拍
     * @param itemId 商品id
     * @param userId 用户id
     * @param number 投票次数
     * @param periods 活动期数
     */
    void auction(Long itemId, Integer periods, Long userId, Integer number);

    /**
     * 查询某一个商品竞拍记录
     * @param item 商品id
     * @param sealedId 历史记录Id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 竞拍记录集合
     */
    Page<BiddersBean> findBidders(Long item, Long sealedId, Integer pageNo, Integer pageSize);
}
