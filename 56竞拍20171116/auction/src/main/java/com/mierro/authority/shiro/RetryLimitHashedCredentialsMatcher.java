package com.mierro.authority.shiro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 功能: 实现登陆密码验证服务<br>
 * 相关配置: spring-com.simba.shiro-web.xml
 *
 * @author 唐亮
 * @version 1.0
 * @time 2016-1-8 15:31:10
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    /**
     * log4j实例对象.
     */
    private static Logger logger = LogManager.getLogger(RetryLimitHashedCredentialsMatcher.class
            .getName());

    private Cache<String, AtomicInteger> passwordRetryCache;

    private ShiroOnlineLimited shiroOnlineLimited;

    public void setShiroOnlineLimited(ShiroOnlineLimited shiroOnlineLimited) {
        this.shiroOnlineLimited = shiroOnlineLimited;
    }

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        // 重复登录超过5次
        if (retryCount.incrementAndGet() > 5) {
            logger.error("用户：" + token.getCredentials()+"登陆失败超过5次");
            System.out.println(retryCount.incrementAndGet());
            throw new ExcessiveAttemptsException();
        }

        //同时在线人数检测
//        shiroOnlineLimited.detection(username);

        // 密码是否匹配
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }



}
