package com.homework.wmj.controller;


import com.homework.wmj.Service.UserService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.BeanArrayUtils;
import com.homework.wmj.Util.Utils.StringUtils;
import com.homework.wmj.Util.Utils.ValidateUtils;
import com.homework.wmj.pojo.dto.DtoPo.UserDTO;
import com.homework.wmj.pojo.po.User;
import com.homework.wmj.pojo.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user/*")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("register")
    public Map<String,Object> register(@Valid UserDTO userDTO, BindingResult bindingResult) throws Exception {
        //1. 判断必要参数，如缺失则结束
        ValidateUtils.handlerValidateResult(bindingResult);
        User user = userService.register(userDTO);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token",user.getToken());
        resultMap.put("user", BeanArrayUtils.copyProperties(user, UserVO.class));
        return resultMap;
    }


    @RequestMapping("login")
    public Map<String,Object> loginUser(String username, String password, String cid) throws CustomException, InstantiationException, IllegalAccessException {
//        1. 判断参数
        if(StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(cid)){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"username、password、cid均不能为空（不前不对cid做检测，仅判空）");
        }
        User user = userService.login(username,password);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token",user.getToken());
        resultMap.put("user",BeanArrayUtils.copyProperties(user,UserVO.class));
        return resultMap;
    }

    @RequestMapping("logout")
    public Boolean logOut(String token) throws CustomException {
//        这里的token已经通过了鉴权，故不需要再次验证
        return userService.logout(token);
    }

    @RequestMapping("updateUserInfo")
    public Boolean updateUserInfo(User user) throws CustomException {
        if(user.getId() == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"userId不能为空");
        }
        if(!userService.userIsExist(user.getId())){
            throw new CustomException(CustomErrorCodeEnum.USER_NOT_EXIST);
        }
        user.setToken(null);
        Boolean result = userService.updateUser(user);
        return result;
    }


    @RequestMapping("queryUserById")
    public User queryUserById(Integer userId) throws CustomException {
        if(userId == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"userId不能为空");
        }
        if(!userService.userIsExist(userId)){
            throw new CustomException(CustomErrorCodeEnum.USER_NOT_EXIST);
        }
        return userService.getUserById(userId);
    }
}
