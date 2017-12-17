package com.mierro.common.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by 黄晓滨 simba on 2017/5/23.
 * Remarks：
 */
public class KeyGenerator implements IdentifierGenerator {

    private static Logger LOG = LogManager.getLogger(KeyGenerator.class);

    private static final IdWorker idWorker = new IdWorker(0,0);

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object obj) throws HibernateException {
        try {
            Field field = obj.getClass().getDeclaredField("id");
            field.setAccessible(true);
            Long id = (Long) field.get(obj);
            if (id == null){
                return idWorker.nextId();
            }else{
                return id;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOG.error("java反射获取id失败:"+e.getMessage());
            return idWorker.nextId();
        }
    }
}
