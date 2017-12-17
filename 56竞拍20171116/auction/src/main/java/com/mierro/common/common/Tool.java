package com.mierro.common.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @author 黄晓滨
 * @date 17/2/8
 */
public class Tool {
    /**
     * :数据校验器,相同数据跳过,不同数据参数1覆盖参数2
     * @param newData 新数据
     * @param oldData 老数据
     * @param <T> 新老的对象类型
     * @return 老数据
     */
    @SuppressWarnings({"deprecation"})//抑制警告
    public static <T>T dataCheckout(T newData, T oldData){
        Class checkoutOneSuperClass = null;// 父类
        Field[] resourceFields ;
        //检测传入数据是否有父类,有则获取父类.class
        if(newData.getClass().getSuperclass() != null){
            checkoutOneSuperClass = newData.getClass().getSuperclass();
        }
        if (newData.getClass().getSuperclass() != null){
            //待转换类含有父类
            checkoutOneSuperClass = newData.getClass().getSuperclass();
            //待转换类进行资源整合
            resourceFields = newData.getClass().getDeclaredFields();
            Field[] resourcetemp = checkoutOneSuperClass.getDeclaredFields();
            int resourceFieldsLen = resourceFields.length;
            resourceFields= Arrays.copyOf(resourceFields,resourceFields.length+resourcetemp.length);//扩容
            for (int i = resourceFieldsLen,j =0;i<resourceFieldsLen+resourcetemp.length;i++,j++){
                resourceFields[i] = resourcetemp[j];
            }
        }else{
            resourceFields = newData.getClass().getDeclaredFields();
        }
        Field oneField ;
        Field twoField;
        for (Field field:resourceFields){
            try {
                oneField = newData.getClass().getDeclaredField(field.getName());
                twoField = oldData.getClass().getDeclaredField(field.getName());
                oneField.setAccessible(true);
                twoField.setAccessible(true);
                field.setAccessible(true);
                if (!oneField.get(newData).equals(twoField.get(oldData))){
                    field.set(oldData,oneField.get(newData));
                }
            } catch (NoSuchFieldException e) {
                //转父类查找
                try {
                    assert checkoutOneSuperClass != null;
                    Method method = checkoutOneSuperClass.getDeclaredMethod("get"+ toUpperCaseFirstOne(field.getName()));
                    field.setAccessible(true);
                    if (!method.invoke(newData).equals(method.invoke(oldData))){
                        field.set(oldData,method.invoke(newData));
                    }
                }catch (Exception ignored){}
            }catch (Exception ignored){}
        }
        return oldData;
    }

    /**
     * 字符串首字母小写转大写
     * @param s 字符串
     * @return 字符串
     */
    private static String toUpperCaseFirstOne(String s)
    {
        byte[] items = s.getBytes();
        items[0] = (byte)((char)items[0] - ( 'a' - 'A'));
        return new String(items);
    }


    public static String getRealIp(HttpServletRequest request){
        //获取真实ip
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress= inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public static String getRealIp(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //获取真实ip
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /**
     * 获取 T
     * @param t
     * @param supplier
     * @return
     */
    public static <T> T getOrDefault(T t, Supplier<T> supplier){
        return t == null ? supplier.get() : t;
    }
    /**
     * 获取 T
     * @param t
     * @param defaultValue
     * @return
     */
    public static <T> T getOrDefault(T t, T defaultValue){
        return t == null ? defaultValue : t;
    }
}
