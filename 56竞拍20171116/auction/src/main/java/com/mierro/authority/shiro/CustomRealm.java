package com.mierro.authority.shiro;


import com.mierro.authority.common.LoginType;
import com.mierro.authority.service.AuthorityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.Set;


/**
 * <p>Title:CustomRealm </p> <p>Description:自定义shiro数据源 </p> <p>Company:Wteam </p>
 *
 * @author Wteam 黄晓滨 86571705@qq.com
 */
public class CustomRealm extends AuthorizingRealm {

    private static Logger LOG = LogManager.getLogger(CustomPermissionCheckFilter.class);

    @Resource
    private AuthorityService authorityService;

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    // realm的认证方法，从数据库查询用户信息
    @SuppressWarnings("unchecked")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        CustomUsernamePasswordToken customUsernamePasswordToken;
        if (token instanceof CustomUsernamePasswordToken) {
            customUsernamePasswordToken = (CustomUsernamePasswordToken) token;
        } else {
            return null;
        }
        LoginType loginType = customUsernamePasswordToken.getLoginType();
        SimpleAuthenticationInfo authenticationInfo;
        com.mierro.authority.entity.AuthenticationInfo userVo;
        userVo = authorityService.selectLoginMessageByIdentifier((String) token.getPrincipal(),loginType);

        if (userVo == null){
            throw new UnknownAccountException("用户没找到");
        }

        //查询用户是否被禁用
        if(authorityService.judgeEnable(userVo.getUserId())){
            throw new LockedAccountException("用户被禁用，登陆失败");
        }

        // 判断帐号是否锁定
        if (Boolean.TRUE.equals(userVo.getId())) {
            // 抛出 帐号锁定异常
            throw new LockedAccountException();
        }

        if (((CustomUsernamePasswordToken) token).getMode() != null && ((CustomUsernamePasswordToken) token).getMode().equals("AUTO")){
            //进行自动登陆
            // 给token重新设置身份
            customUsernamePasswordToken.setUsername(userVo.getIdentifier());
            authenticationInfo = new SimpleAuthenticationInfo(
                    token,
                    "0f9158a60dadc3155a1811c6a80be85e",
                    ByteSource.Util.bytes("a8af763e5c558cf0cd7be1249c56327a"),
                    this.getName());
            ((SimplePrincipalCollection) authenticationInfo.getPrincipals()).add(userVo, getName());
        }else {
            // 给token重新设置身份
            customUsernamePasswordToken.setUsername(userVo.getIdentifier());
            authenticationInfo = new SimpleAuthenticationInfo(
                    token,
                    userVo.getCredential(),
                    ByteSource.Util.bytes(userVo.getSalt()),
                    this.getName());
            ((SimplePrincipalCollection) authenticationInfo.getPrincipals()).add(userVo, getName());
        }
        return authenticationInfo;
    }

    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions ;
        CustomUsernamePasswordToken token ;
        if (principals.getPrimaryPrincipal() instanceof CustomUsernamePasswordToken) {
            token = (CustomUsernamePasswordToken) principals.getPrimaryPrincipal();
        } else {
            return authorizationInfo;
        }

        // 判断是不是用户信息登陆
        LoginType loginType = token.getLoginType();
        com.mierro.authority.entity.AuthenticationInfo authorizationInfo1 =
                authorityService.selectLoginMessageByIdentifier(token.getUsername(),loginType);

        permissions = authorityService.findPermsByUserPrincipal(
                authorizationInfo1.getUserId(),token.getAythorityType());

        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }


}
