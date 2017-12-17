package com.mierro.common.common;
import java.util.function.Supplier;

/**
 * Created by 黄晓滨 on 2017/1/13.
 * 响应状态码
 */
public enum  ResponseCode {
    ok,
    bad_request,
    unauthorized,
    forbidden,
    notfound,
    business,
    accessdenied,
    internal_srever_error,
    system_maintenance,
    not_find_proxy,
    prohibit_landing,
    insufficient_coins,
    activity_ends
    ;
    /**
     * 活动结束
     */
    public static final String ACTIVITY_ENDS = "411";
    /**
     * 钱币不足
     */
    public static final String INSUFFICIENT_COINS = "410";
    /**
     * 操作成功
     */
    public static final String OK = "200";
    /**
     * 语义错误、请求参数错误
     */
    public static final String BAD_REQUEST = "400";
    /**
     * 未登录
     */
    public static final String UNAUTHORIZED = "401";
    /**
     * 无权限操作
     */
    public static final String FORBIDDEN = "403";
    /**
     * 找不到网页或者接口
     */
    public static final String NOTFOUND = "404";
    /**
     * 业务失败
     */
    public static final String BUSINESS = "405";
    /**
     * 服务器拒绝访问
     */
    public static final String ACCESSDENIED = "406";
    /**
     * 系统维护
     */
    public static final String SYSTEM_MAINTENANCE = "407";
    /**
     * 找不到代理或子代理
     */
    public static final String NOT_FIND_PROXY = "408";
    /**
     * 服务端错误
     */
    public static final String INTERNAL_SREVER_ERROR = "500";
    /**
     * 用户同时在线超过限制
     */
    public static final String PROHIBIT_LANDING = "409";

    //错误码
    private String code;

    static  {
        ok.code = OK;
        bad_request.code = BAD_REQUEST;
        unauthorized.code = UNAUTHORIZED;
        forbidden.code = FORBIDDEN;
        notfound.code = NOTFOUND;
        business.code = BUSINESS;
        accessdenied.code = ACCESSDENIED;
        internal_srever_error.code = INTERNAL_SREVER_ERROR;
        system_maintenance.code = SYSTEM_MAINTENANCE;
        not_find_proxy.code = NOT_FIND_PROXY;
        prohibit_landing.code = PROHIBIT_LANDING;
        insufficient_coins.code = INSUFFICIENT_COINS;
        activity_ends.code = ACTIVITY_ENDS;
    }

    public String getCode(){
        return code;
    }

    /**
     * 不能为null , 否则抛出异常
     * @param supplier
     * @param message 异常中信息
     */
    public <T> T notNull(Supplier<T> supplier , String message){
        T t = supplier.get();
        if (t == null) {
            throw new ServiceException(this.code, message);
        }
        return t;
    }

    /**
     * t不能为null , 否则抛出异常
     * @param t
     * @param message 异常中信息
     */
    public <T> T notNull(T t , String message){
        if (t == null) {
            throw new ServiceException(this.code, message);
        }
        return t;
    }
    /**
     * 验证是否为真
     * @param bool
     * @param message
     */
    public void assertTrue(boolean bool , String message){
        if (!bool) {
            throw new ServiceException(this.code, message);
        }
    }

    public void failure(String message){
        throw new ServiceException(this.code, message);
    }
}
