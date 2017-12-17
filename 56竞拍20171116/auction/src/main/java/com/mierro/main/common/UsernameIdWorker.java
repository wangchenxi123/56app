package com.mierro.main.common;

import com.mierro.common.common.IdWorker;

/**
 * Created by 唐亮 on 2017/10/8.
 */
public class UsernameIdWorker {

    private static class IdWorkerHolder {
        public static IdWorker idWorker = new IdWorker();
    }

    private static long lastid = IdWorkerHolder.idWorker.nextId() >> 22;

    public static long nextId(){
        long nextId = IdWorkerHolder.idWorker.nextId() >> 22;
        while (nextId == lastid){
            nextId = IdWorkerHolder.idWorker.nextId() >> 22;
        }
        return lastid = nextId;
    }
}
