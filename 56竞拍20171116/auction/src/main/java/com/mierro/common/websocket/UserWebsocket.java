package com.mierro.common.websocket;

import com.mierro.authority.dao.UserMessageDao;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.SpringTool;
import com.mierro.main.global.UserCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 黄晓滨 simba on 2017/7/28.
 * Remarks：
 */

//@ServerEndpoint(value = "/websocket/{userId}",configurator=GetHttpSessionConfigurator.class)
@ServerEndpoint(value = "/websocket/{userId}")
public class UserWebsocket {

    private static ReentrantLock lock = new ReentrantLock(true);//公平锁对象

    private static Logger logger = LogManager.getLogger(UserWebsocket.class.getName());

    private static int onlineNumber;

    public static ConcurrentHashMap<Long,Message> concurrentHashMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Session,SendMessage> userWebsocket = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Session,Long> userIds = new ConcurrentHashMap<>();

    class SendMessage {
//        private Lock lock = new ReentrantLock();// 锁对象

        private Session session;

        public SendMessage(Session session){
            this.session = session;
        }

        public synchronized void sendMessage(String message){
            try {
                if (session.isOpen()){
                    session.getAsyncRemote().sendText(message);
                }else{
                    userWebsocket.remove(session);
                }
            }catch (IllegalStateException e){

            }finally {

            }

        }
    }

    /**
     * 创建一个连接
     * @param session 当前用户session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId") Long userId){
        if (userId != 0){
            //缓存用户信息
            UserMessageDao userMessageDao = (UserMessageDao) SpringTool.getBeanByClass(UserMessageDao.class);
            UserMessage userMessage = userMessageDao.findOne(userId);
            if (userMessage != null){
                UserWebsocket.userIds.put(session,userId);
                Message message = new Message(session,this);
                addOnlineCount();
                UserWebsocket.concurrentHashMap.put(userId,message);
                //保存到用户cache
                UserCache.userCache.put(userId,userMessage);
            }
        }
        //添加进入普通session缓存
        userWebsocket.put(session,new SendMessage(session));
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message){
        for(Map.Entry<Session, SendMessage> item: userWebsocket.entrySet()){
            item.getValue().sendMessage("服务器接收:"+message);
        }
    }

    /**
     * 发送信息
     * @param message 需要发送的信息
     */
    public static void sendMessage(String message){
        lock.lock();
        try {
            for(Map.Entry<Session, SendMessage> item: userWebsocket.entrySet()) {
                SendMessage sendMessage = item.getValue();
                sendMessage.sendMessage(message);
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * 群发相同的信息
     * @param userId 用户id集合
     * @param message 需要发送的信息
     * @throws IOException Io错误
     */
    public static void sendMessage(Long userId, String message){
        lock.lock();
        try {
            Message userMessage;
            userMessage = UserWebsocket.concurrentHashMap.get(userId);
            if (userMessage != null){
                userMessage.getSession().getAsyncRemote().sendText(message);
            }
        }finally {
            lock.unlock();
        }

    }

    /**
     * 发生错误时调用
     * @param error 错误信息
     */
    @OnError
    public void onError(Throwable error,Session session){
        userWebsocket.remove(session);
        Long userId = UserWebsocket.userIds.get(session);
        if (userId != null){
            concurrentHashMap.remove(userId);
            subOnlineCount();           //在线数减1
            UserCache.userCache.remove(userId);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session){
        userWebsocket.remove(session);
        Long userId = UserWebsocket.userIds.get(session);
        if (userId != null){
            concurrentHashMap.remove(userId);
            subOnlineCount();           //在线数减1
            UserCache.userCache.remove(userId);
        }
    }

    private static synchronized void addOnlineCount() {
        UserWebsocket.onlineNumber ++;
    }

    private static synchronized void subOnlineCount() {
        UserWebsocket.onlineNumber --;
    }

    public static int getOnlineNumber() {
        return onlineNumber;
    }
}
