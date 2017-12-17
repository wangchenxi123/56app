package com.mierro.authority.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.VerifyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

/**
 * 统一异常处理器
 * Created by lhb on 2016/11/28.
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    private static Logger logger = LogManager.getLogger(CommonExceptionHandler.class.getName());


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResultMessage EmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"在该表中找不到数据，请检查传入数据是否有误");
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(UnknownAccountException.class)
    public ResultMessage UnknownAccountException(UnknownAccountException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"用户名错误或不存在");
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResultMessage IncorrectCredentialsException(IncorrectCredentialsException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"密码错误");
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(ExcessiveAttemptsException.class)
    public ResultMessage ExcessiveAttemptsException(ExcessiveAttemptsException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.ACCESSDENIED,"密码重复输入错误超过5次，30分钟后再登录");
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResultMessage AuthenticationException(AuthenticationException ex) {
        ResultMessage resultMessage;
        if (ex.getMessage().equals("prohibit_landing")){
            resultMessage =  new ResultMessage(ResponseCode.PROHIBIT_LANDING,"用户登陆失败，用户已经达到最大登陆数");
            logger.debug(ex.getMessage());
        }else{
            resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"认证失败");
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return resultMessage;
    }

    @ExceptionHandler(BindException.class)
    public ResultMessage BeanPropertyBindingResult(BindException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BUSINESS,"数据绑定错误，请检查传入格式");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(WriterException.class)
    public ResultMessage WriterException(WriterException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BUSINESS,"二维码生成失败");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultMessage HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"请求方式错误");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResultMessage UnsupportedEncodingException(UnsupportedEncodingException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"编码转换异常，操作失败");
        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResultMessage JsonProcessingException(JsonProcessingException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"操作失败(发生转换错误)");
        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultMessage ConstraintViolationException(ConstraintViolationException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"有必要参数未传入:"+ex.getMessage());
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResultMessage TransactionSystemException(TransactionSystemException ex) {
        ResultMessage resultMessage;
        try{
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getCause().getCause();
            Set<ConstraintViolation<?>> set =  constraintViolationException.getConstraintViolations();
            Iterator iter = set.iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iter.hasNext()) {
                ConstraintViolationImpl x = (ConstraintViolationImpl) iter.next();
                stringBuilder.append(x.getMessageTemplate());
            }
            resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,stringBuilder.toString());
        }catch (Exception e){
            resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"线程提交错误,操作失败");
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return resultMessage;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResultMessage MaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"文件上传失败,文件大小超过上传限制");
        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResultMessage FileNotFoundException(FileNotFoundException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"文件创建失败,操作失败");
        logger.debug(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(VerifyException.class)
    public ResultMessage VerifyException(VerifyException ex) {
        ResultMessage resultMessage = new ResultMessage(ex.getCode(),ex.getMessage());
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(ServiceException.class)
    public ResultMessage RuntimeException(ServiceException ex) {
        ResultMessage resultMessage = new ResultMessage(ex.getCode(),ex.getMessage());
//        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(Exception.class)
    public ResultMessage handleException(Exception ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"系统错误");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(ClassCastException.class)
    public ResultMessage ClassCastException(ClassCastException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"类型转换异常:"+ex.getMessage());
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultMessage MissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"缺少" + ex.getParameterName()+"参数");
        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultMessage NullPointerException(NullPointerException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"空指针异常");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultMessage MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"模型转换异常,请检查传入数据类型");
        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResultMessage DataIntegrityViolationException(DataIntegrityViolationException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BUSINESS,"操作失败，已经存在该数据");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResultMessage DataIntegrityViolationException(DataAccessResourceFailureException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"服务器网络链接异常");
        logger.error(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(ParseException.class)
    public ResultMessage ParseException(DataAccessResourceFailureException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"传入时间格式错误");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return resultMessage;
    }



    @ExceptionHandler(ServletRequestBindingException.class)
    public ResultMessage ServletRequestBindingException(ServletRequestBindingException ex) {
        ResultMessage resultMessage = new ResultMessage(ResponseCode.BAD_REQUEST,"数据转换异常");
        logger.error(ex.getMessage());
        return resultMessage;
    }
}
