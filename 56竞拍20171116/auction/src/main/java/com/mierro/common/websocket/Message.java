package com.mierro.common.websocket;

import javax.websocket.Session;

/**
 * Created by 黄晓滨 simba on 2017/7/28.
 * Remarks：
 */
public class Message {

    private UserWebsocket userWebsocket;

    private Session session;

    public Message(Session session,UserWebsocket websocket){
        this.session = session;
        this.userWebsocket = websocket;
    }

    public UserWebsocket getUserWebsocket() {
        return userWebsocket;
    }

    public void setUserWebsocket(UserWebsocket userWebsocket) {
        this.userWebsocket = userWebsocket;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
