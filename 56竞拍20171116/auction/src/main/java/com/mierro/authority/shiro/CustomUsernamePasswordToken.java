package com.mierro.authority.shiro;

import com.mierro.authority.common.LoginType;
import org.apache.shiro.authc.UsernamePasswordToken;


/**
 * 登录认证，需要确认登录使用的类型。
 *
 * @author 唐亮
 * @version 2016年6月13日
 * @see CustomUsernamePasswordToken
 */
public class CustomUsernamePasswordToken extends UsernamePasswordToken {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 登陆端类型
     */
    private com.mierro.authority.common.AythorityType  AythorityType;

    private LoginType loginType;

    private String mode;


    /**
     * @param principal  身份，从前台传来时，即主体的标识属性，可以是任何东西，如用户名、邮箱等，
     * @param credential 凭证，即只有主体知道的安全值，如密码/数字证书等
     * @param loginType  登陆类型
     */
    public CustomUsernamePasswordToken(String principal, String credential, com.mierro.authority.common.AythorityType  AythorityType, LoginType loginType) {
        super(principal, credential);
        this.AythorityType = AythorityType;
        this.loginType = loginType;
    }

    public CustomUsernamePasswordToken(String principal, String credential, com.mierro.authority.common.AythorityType  AythorityType,
                                       LoginType loginType, String mode) {
        super(principal, credential);
        this.AythorityType = AythorityType;
        this.loginType = loginType;
        this.mode = mode;

    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public com.mierro.authority.common.AythorityType  getAythorityType() {
        return AythorityType;
    }

    public void setAythorityType(com.mierro.authority.common.AythorityType  aythorityType) {
        AythorityType = aythorityType;
    }

    public String getMode() {
        return mode;
    }
}
