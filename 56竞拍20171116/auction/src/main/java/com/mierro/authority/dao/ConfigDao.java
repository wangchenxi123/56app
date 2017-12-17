package com.mierro.authority.dao;


import com.mierro.authority.entity.ConfigBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Constructor;

/**
 * Created by 黄晓滨 simba on 2017/3/23.
 * Remarks：
 */
public interface ConfigDao extends JpaRepository<ConfigBean,Long> {

    ConfigBean findByKeyId(Integer keyId);

    /**
     * 获取配置信息
     * @param keyId
     * @param defaultValue 不能为null
     * @param <T>
     * @return
     */
    default <T> T value(Integer keyId, T defaultValue) {
        if (defaultValue == null) {
            return null;
        }
        ConfigBean bean = findByKeyId(keyId);
        if (bean != null) {
            try {
                Constructor constructor = defaultValue.getClass().getConstructor(String.class);
                if (bean.getValue() !=null && !"".equals(bean.getValue())) {
                    return (T) constructor.newInstance(bean.getValue());
                }
            } catch (Exception e) {}
        }
        return defaultValue;
    }

    /**
     * 获取配置信息
     * @param keyId
     * @return
     */
    default String value(Integer keyId) {
        ConfigBean bean = findByKeyId(keyId);
        if (bean != null) {
            return bean.getValue();
        }
        return null;
    }
}
