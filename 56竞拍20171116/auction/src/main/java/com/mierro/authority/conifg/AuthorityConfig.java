package com.mierro.authority.conifg;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public enum AuthorityConfig {
    super_administrator("超级管理员");
    private String name;
    private Long id;

    AuthorityConfig(String name) {
        this.name = name;
    }

    public static final Long SUPER_ADMINISTRATOR = 1L;

    static {
        super_administrator.id = SUPER_ADMINISTRATOR;
    }
}
