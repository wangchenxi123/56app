package com.mierro.main.service;

import com.mierro.main.common.QuestionCategory;
import com.mierro.main.entity.CommonQuestionBean;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
public interface CommonQuestionService {

    /**
     * 按条件查询所有常见帮助
     * @param title 标题
     * @param questionCategory 类型
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<CommonQuestionBean> findAll(String title, QuestionCategory questionCategory, Integer pageNo, Integer pageSize);

    /**
     * 前端获取帮助
     * @return 所有帮助信息
     */
    List<CommonQuestionBean> findAll(QuestionCategory questionCategory);

    /**
     * 查询单个帮助信息
     * @param id 帮助信息id
     * @return 帮助信息对象
     */
    CommonQuestionBean findOne(Long id);

    /**
     * 删除一条帮助信息
     * @param id id
     */
    void delete(Long id);

    /**
     * 更新一条帮助信息(需要有id)
     * @param commonQuestionBean 帮助信息对象
     */
    void update(CommonQuestionBean commonQuestionBean);

    /**
     * 添加一条帮助信息
     * @param commonQuestionBean 帮助信息对象
     */
    void add(CommonQuestionBean commonQuestionBean);

    /**
     * 更新问题禁用状态
     * @param id 问题id
     */
    void updateState(Long id);
}
