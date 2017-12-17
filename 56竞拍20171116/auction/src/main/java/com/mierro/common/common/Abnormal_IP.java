package com.mierro.common.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 黄晓滨 simba on 2017/7/18.
 * Remarks：异常IP记录表
 */
public class Abnormal_IP {

    private static Map<String,Integer> ipMap = new ConcurrentHashMap<>();

    private static Map<Long,Integer> userIdMap = new ConcurrentHashMap<>();

    /**
     * 记录ip
     */
    public static void recordIp(String ip){
        Integer number = ipMap.get(ip);
        if (number == null || number <= 0){
            ipMap.put(ip,1);
        }else {
            ipMap.put(ip,number+1);
        }
    }

    public static Map<String, Integer> getIpMap() {
        return ipMap;
    }

    public static Boolean checkingIp(String ip) {

        Integer number = ipMap.get(ip);
        if (number == null){
            return true;
        }else{
            if (number < 5){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * 记录用户id
     */
    public static void recordUserId(Long userId){
        Integer number = userIdMap.get(userId);
        if (number == null || number <= 0){
            userIdMap.put(userId,1);
        }else {
            userIdMap.put(userId,number+1);
        }
    }

    public static Map<Long, Integer> getUserIdMap() {
        return userIdMap;
    }
}
