package com.mierro.robot.service;

import com.mierro.main.entity.ItemBean;
import com.mierro.robot.entity.RobotItemBean;
import org.springframework.data.domain.Page;

/**
 * 机器人商品设置
 * Created by tlseek on 2017/8/22.
 */
public interface RobotItemService {

    /**
     * 加载默认信息
     */
    void loading();

    /**
     * 分类查询商品信息
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<RobotItemBean> find(String name, Integer pageNo, Integer pageSize);

    /**
     * 修改机器人商品信息
     * @param robotItem
     */
    void update(RobotItemBean robotItem);

    /**
     * 根据商品ID获取
     * @param item
     * @return
     */
    RobotItemBean getRobotItemBean(ItemBean item);
}
