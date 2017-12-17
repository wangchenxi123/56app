package com.mierro.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.main.entity.SortBean;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/6/1.
 * Remarks：
 */
public interface SortService {

    /**
     * 查询所有商品分类
     * @return 商品分类集合(不带商品详情)
     */
    List<SortBean> findAll();

    /**
     * 查询所有商品分类
     * @return 商品分类集合(不带商品详情)
     */
    Page<SortBean> findAll(Integer pageNo, Integer pageSize);

    /**
     * 查询所有商品分类
     * @return 商品分类集合(不带商品详情)
     */
    Page<SortBean> clientFindAll(Integer pageNo, Integer pageSize);

    /**
     * 查询单个商品分类详情
     * @param id 商品分类id
     * @return 商品分类详情
     */
    SortBean findOne(Long id) throws IOException;

    /**
     * 查询单个商品分类详情
     * @param id 商品分类id
     * @return 商品分类详情
     */
    SortBean ClientFindOne(Long id) throws IOException;

    /**
     * 客户端查找分类
     * @param id 分类id
     * @return 分类对象
     * @throws IOException json转换错误
     */
    SortBean clientFindOne(Long id) throws IOException;

    /**
     * 添加一个商品分类
     * @param sort 商品分类bean
     */
    void addSort(SortBean sort) throws JsonProcessingException;

    /**
     * 删除一个商品分类
     * @param id 商品分类
     */
    void deleteSort(Long id);

    /**
     * 修改一个商品分类信息
     * @param sort 商品分类
     */
    void updateSort(SortBean sort) throws JsonProcessingException;

    /**
     * 更新商品分类禁用状态
     * @param id 商品id
     */
    void updateState(Long id);
}
