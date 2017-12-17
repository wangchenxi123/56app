package com.mierro.main.global;

import com.mierro.authority.entity.UserMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 黄晓滨 simba on 2017/8/10.
 * Remarks：
 */
public class UserCache {
    public static ConcurrentHashMap<Long,UserMessage> userCache = new ConcurrentHashMap<>();
}
