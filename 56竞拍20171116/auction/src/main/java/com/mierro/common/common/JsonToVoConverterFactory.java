package com.mierro.common.common;

import com.alibaba.fastjson.JSON;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 将 json 格式的字符串转换成实现了的 IValueObject 接口的 javaBean
 *
 * @author 唐亮
 */
public class JsonToVoConverterFactory implements ConverterFactory<String, IValueObject> {

    @Override
    public <T extends IValueObject> Converter<String, T> getConverter(Class<T> targetType) {
        return new JsonToVo<>(targetType);
    }

    private class JsonToVo<T extends IValueObject> implements Converter<String, T> {

        private final Class<T> voClass;

        private JsonToVo(Class<T> voClass) {
            this.voClass = voClass;
        }

        @Override
        public T convert(String source) {
            if (source != null) {
                try {
                    return JSON.parseObject(source, voClass);
                } catch (Exception e){
                    throw new VerifyException("json 格式错误："+e.getMessage());
                }
            }
            return null;
        }
    }
}
