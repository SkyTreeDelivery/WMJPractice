package com.homework.wmj.Advice;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.homework.wmj.Util.Enum.EnumImp.HttpResponseEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.HttpResultBuiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  全局的异常处理切面
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResultBuiler.HttpResult<Object> exceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response){
        logger.info(ex.getMessage());
        ex.printStackTrace();
        if(ex instanceof CustomException){    //处理自定义错误
            response.setStatus(HttpResponseEnum.OK.code);
            response.setCharacterEncoding("utf-8");
            CustomException e = (CustomException) ex;
            return HttpResultBuiler.exception(e);
        }else{                                 //处理其他错误，默认为系统错误500
            response.setStatus(HttpResponseEnum.INTERNAL_SERVER_ERROR.code);
            response.setCharacterEncoding("utf-8");
            return HttpResultBuiler.error500(ex.getMessage());
        }
    }

    /**
     *  处理token验证异常 JWTVerificationException
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(JWTVerificationException.class)
    @ResponseBody
    public HttpResultBuiler.HttpResult<Object> TokenExpiredExceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response){
        logger.info(ex.getMessage());
        ex.printStackTrace();
        // 返回403错误
        return HttpResultBuiler.error(HttpResponseEnum.FORBIDDEN,ex.getMessage());
    }
}
