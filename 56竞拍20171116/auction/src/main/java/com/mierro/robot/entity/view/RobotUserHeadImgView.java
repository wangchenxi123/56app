package com.mierro.robot.entity.view;

/**
 * 机器人用户头像信息
 * Created by tlseek on 2017/8/18.
 */
public interface RobotUserHeadImgView {

    /**
     * 获取头像文件ID
     * @return
     */
    Long getHeadImageFileId() ;

    void setHeadImageFileId(Long headImageFileId);

    /**
     * 获取头像文件名
     * @return
     */
    String getHeadImageName() ;

    void setHeadImageName(String headImageName);
}
