package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public enum NoticeState {

    UNREAD("未读"),
    READ("已读");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    NoticeState(String name) {
        this.name = name;
    }
}
