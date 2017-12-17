package com.mierro.authority.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by 黄晓滨 simba on 2017/7/13.
 * Remarks：同时在线人数过滤规则
 */
public class ShiroOnlineLimited {

    public static String kickoutUrl;

    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认禁止后者登陆
    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private SessionManager sessionManager;

    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("kickout-session");
    }

    boolean detection(String username) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Serializable sessionId = session.getId();

        //TODO 同步控制
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
            deque = new LinkedList<>();
            cache.put(username, deque);
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId)) {
            deque.push(sessionId);
        }
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try{
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //会话被踢出了
                    kickoutSession.setAttribute("kickout",true);
                    //如果可以··直接转发最好
                }
            }catch (Exception ignored){
                //获取不到session会报错，导致认证失败
            }
        }
        return true;
    }

}
