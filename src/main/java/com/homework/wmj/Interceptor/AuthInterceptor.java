package com.homework.wmj.Interceptor;


import com.homework.wmj.Service.UserService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从header和param中取得token，如都没有或者结果为空字符串，则抛出异常
        String token = request.getHeader("Authorization");
        if(StringUtils.isBlank(token)){
            throw new CustomException(CustomErrorCodeEnum.PARAM_NOT_TOKEN);
        }
        //如token没有被使用，则抛出异常
        Boolean result = userService.verifyToken(token);
        if(!result){
            throw new CustomException(CustomErrorCodeEnum.PARAM_INVALID_TOKEN);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
