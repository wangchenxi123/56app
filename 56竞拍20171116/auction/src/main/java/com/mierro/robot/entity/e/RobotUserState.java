package com.mierro.robot.entity.e;

/**
 * 机器人用户状态
 * Created by tlseek on 2017/8/22.
 */
public enum RobotUserState {

    waiting("等待中"),
    auction("竞拍中")
    ;

    public final String name;

    RobotUserState(String name) {
        this.name = name;
    }
}
