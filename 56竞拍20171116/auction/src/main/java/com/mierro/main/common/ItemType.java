package com.mierro.main.common;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public enum ItemType {

    VIRTUAL("虚拟"),
    IN_KIND("实物")
    ;

    private String name;

    ItemType(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
