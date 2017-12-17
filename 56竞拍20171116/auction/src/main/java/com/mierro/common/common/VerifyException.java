package com.mierro.common.common;


/**
 * @author 黄晓滨
 * @date 17/2/9
 */
public class VerifyException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public VerifyException(){
        this.code = "400";
        this.message = "参数有误";
    }

    public VerifyException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public VerifyException(String message){
        this.code = "400";
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
