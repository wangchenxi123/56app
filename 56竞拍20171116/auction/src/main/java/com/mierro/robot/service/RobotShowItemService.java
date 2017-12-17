package com.mierro.robot.service;

import com.mierro.main.entity.SealedBean;
import com.mierro.robot.entity.view.AddRobotShowItemView;
import org.springframework.data.domain.Page;

/**
 * 机器人晒单模块
 * Created by tlseek on 2017/8/24.
 */
public interface RobotShowItemService {

    /**
     * 分页查看机器人未晒单中奖记录
     * @param username
     * @param itemName
     * @param front_show 是否前端展示
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<SealedBean> findSealedBeforeShow(String username, String itemName, Boolean front_show, Integer pageNo, Integer pageSize);

    /**
     * 晒单
     * @param showItem
     */
    void addShowItem(AddRobotShowItemView showItem);
}
