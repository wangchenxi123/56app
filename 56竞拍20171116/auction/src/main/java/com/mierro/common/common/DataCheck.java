package com.mierro.common.common;
import org.springframework.validation.BindingResult;

/**
 * @author 黄晓滨
 * @date 17/3/7
 * @Message 备注说明
 */
public class DataCheck {
    public static void returnError(BindingResult result){
        if (result.hasErrors()){
            StringBuilder error = new StringBuilder();
            for (int i =0 ;i<result.getErrorCount() ; i++){
                error.append(result.getAllErrors().get(i).getDefaultMessage()).append(",");
            }
            throw new VerifyException(ResponseCode.BAD_REQUEST,error.toString());
        }
    }
}
