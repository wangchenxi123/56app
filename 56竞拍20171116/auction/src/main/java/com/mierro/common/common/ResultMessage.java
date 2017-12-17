package com.mierro.common.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄晓滨 on 2016/12/23.
 * Remarks：接口统一返回参数
 */
public class ResultMessage implements Serializable {

    /**
     * 响应码
     */
    private String responseCode;
    /**
     * 响应码详细说明
     */
    private String message;
    /**
     * 响应数据
     */
    private Map<String,Object> resultParm = new HashMap<>();

    public ResultMessage(String responseCode,String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public ResultMessage putParam(String key, Object value) {
        this.resultParm.put(key, value);
        return this;
    }

    public Map<String, Object> getResultParm() {
        return this.resultParm;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
