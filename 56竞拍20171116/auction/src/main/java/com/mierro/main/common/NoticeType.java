package com.mierro.main.common;

/**
 * 用户通知类型
 * Created by tlseek on 2017/8/25.
 */
public enum NoticeType {

    PERSONAL("个人通知"),
    ANNOUNCEMENT("公告通知");

    public final String name;

    NoticeType(String name) {
        this.name = name;
    }
}
