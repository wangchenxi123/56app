package com.mierro.robot.service;

import com.mierro.authority.entity.UserMessage;
import com.mierro.robot.entity.view.AddOrUpdateRobotUserView;
import com.mierro.robot.entity.view.RobotUserHeadImgView;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 机器人用户模块
 * Created by 唐亮 on 2017/8/18.
 */
public interface RobotUserService {

    /**
     * 加载默认信息
     */
    void loading();

    /**
     * 加载默认机器人头像图片
     */
    void loadHeadImg();

    /**
     * 分页查询用户
     * @param name 用户名
     * @param disable 禁用
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<UserMessage> findAll(String name, Boolean disable, Integer pageNo, Integer pageSize);

    /**
     * 获取机器人用户头像列表
     * @return
     */
    List<RobotUserHeadImgView> findDefaultRobotUserHeadImg();
    /**
     * 查看机器人用户信息
     * @param userId
     * @return
     */
    UserMessage find(Long userId);
    /**
     * 添加用户
     * @param user
     */
    void addUser(AddOrUpdateRobotUserView user);
    /**
     * 添加用户
     * @param user
     */
    void addUser(List<AddOrUpdateRobotUserView> user);
    /**
     * 编辑用户
     * @param user
     */
    void updateUser(AddOrUpdateRobotUserView user);

    /**
     * 获取随机机器人ID
     * @return
     */
    Long getRandomRobotId(Long itemId);
    /**
     * 获取随机机器人ID
     * @return
     */
    void refreshRandomRobot(Long itemId);
}
