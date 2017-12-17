package com.mierro.authority.common;

import com.mierro.common.common.VerifyException;

/**
 * Created by lhb on 2016/12/20.
 * 登录类型枚举
 * QQ EMAIL WECHAT BLOG TELEPHONE等类型
 */
public enum LoginType {
    QQ("QQ登陆"),
    EMAIL("邮箱登陆"),
    ACCOUNT("用户名登陆"),
    WECHAT("微信登陆"),
    TELEPHONE("手机登陆");

    private String name;

    LoginType(String name) {
        this.name = name;
    }

    /**
     * 将字符串转换成enum
     * @param loginType 登陆字符串
     * @return 登陆方式
     */
    public static LoginType parse(String loginType){
        for (LoginType type: values()) {
            if (type.name().equals(loginType) ) return  type;
        }
        return null;
    }

    public static LoginType verification(String loginType) throws VerifyException {
        for (LoginType type: values()) {
            if (type.name().equals(loginType) ) return  type;
        }
        throw new VerifyException("没有这种登录类型");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
