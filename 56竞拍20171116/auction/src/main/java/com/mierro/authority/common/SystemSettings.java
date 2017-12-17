package com.mierro.authority.common;

/**
 * Created by 黄晓滨 Simba on 2017/4/21.
 * Remarks：
 */
public class SystemSettings {

    //检测系统是否处于维护期//需手动替换
    public volatile static boolean maintenance = false;

    //超级管理员ID 或平台ID
    public static final Long supermanager = 1L;

}
