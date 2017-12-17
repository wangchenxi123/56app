package com.mierro.main.service;

import com.mierro.main.entity.LabelBean;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public interface LabelService {

    /**
     * 分页查询所有标签
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<LabelBean> findAll(Integer pageNo, Integer pageSize);

    /**
     * 前端查询所有标签
     * @return 分页对象
     */
    List<LabelBean> findAll();

    /**
     * 查询单个标签
     * @param labelId 标签id
     * @return 标签bean
     */
    LabelBean findOne(Long labelId);

    /**
     * 删除标签
     * @param labelId 标签id
     */
    void delete(Long labelId);

    /**
     * 添加新标签
     * @param label 标签bean
     */
    void addLabel(LabelBean label);

    /**
     * 修改标签(需要标签id)
     * @param label 标签对象
     */
    void updateLabel(LabelBean label);

    /**
     * 修改标签状态
     * @param id 标签id
     */
    void updateLabelState(Long id);

}
