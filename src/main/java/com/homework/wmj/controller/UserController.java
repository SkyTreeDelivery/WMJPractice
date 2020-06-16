package com.homework.wmj.controller;


import com.homework.wmj.Service.UserService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.BeanArrayUtils;
import com.homework.wmj.Util.Utils.StringUtils;
import com.homework.wmj.Util.Utils.ValidateUtils;
import com.homework.wmj.pojo.dto.DtoPo.UserDTO;
import com.homework.wmj.pojo.po.User;
import com.homework.wmj.pojo.vo.ProvinceVO;
import com.homework.wmj.pojo.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/user/*")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("register")
    public UserVO register(@Valid UserDTO userDTO, BindingResult bindingResult) throws Exception {
        //1. 判断必要参数，如缺失则结束
        ValidateUtils.handlerValidateResult(bindingResult);
        User user = userService.register(userDTO);
        UserVO userVO = BeanArrayUtils.copyProperties(user, UserVO.class);
        return userVO;
    }


    @RequestMapping("login")
    public Map<String,Object> loginUser(String username, String password, HttpServletResponse response) throws CustomException, InstantiationException, IllegalAccessException {
//        1. 判断参数
        if(StringUtils.isBlank(username)
                || StringUtils.isBlank(password)){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"username、password");
        }
        Map<String, Object> re = userService.login(username, password);
        User user  = (User)re.get("user");
        UserVO userVO = BeanArrayUtils.copyProperties(user, UserVO.class);
        re.put("user",userVO);
        return re;
    }

    @RequestMapping("verifyUser")
    public boolean verifyUser(String username,String password){
        Boolean result = userService.verifyUser(username, password);
        return result;
    }

    @RequestMapping("logout")
    public boolean logOut(String token,HttpServletResponse response) throws CustomException {
//        这里的token已经通过了鉴权，故不需要再次验证
       Boolean result = userService.logout(token);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(0); // 单位：秒
        response.addCookie(cookie);
        return result;
    }

    @RequestMapping("updateUserInfo")
    public boolean updateUserInfo(User user) throws CustomException {
        if(user.getId() == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"userId不能为空");
        }
        if(!userService.userIsExist(user.getId())){
            throw new CustomException(CustomErrorCodeEnum.USER_NOT_EXIST);
        }
        boolean result = userService.updateUser(user);
        return result;
    }

    @RequestMapping("updateImage")
    public UserVO updateImage(@RequestParam("file") MultipartFile multipartFile, Integer id) throws Exception{
        if(multipartFile == null || multipartFile.getSize() == 0){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"保存的图片没有上传成功！");
        }
        if(id == null || !userService.userIsExist(id)){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"id不存在！");
        }
        User user = userService.updatePhoto(multipartFile, id);
        UserVO userVO = BeanArrayUtils.copyProperties(user, UserVO.class);
        return userVO;
    }


    @RequestMapping("queryUserById")
    public UserVO queryUserById(Integer userId) throws CustomException, InstantiationException, IllegalAccessException {
        if(userId == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"userId不能为空");
        }
        if(!userService.userIsExist(userId)){
            throw new CustomException(CustomErrorCodeEnum.USER_NOT_EXIST);
        }
        User user = userService.getUserById(userId);
        UserVO userVO = BeanArrayUtils.copyProperties(user, UserVO.class);
        return userVO;
    }

    @RequestMapping("queryUserByToken")
    public UserVO queryUserByToken(@RequestHeader("Authorization") String token) throws CustomException, InstantiationException, IllegalAccessException {
        if(token == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"token不能为空");
        }
        User user = userService.getUserByToken(token);
        UserVO userVO = BeanArrayUtils.copyProperties(user, UserVO.class);
        return userVO;
    }

    @RequestMapping("userIsExistByUsername")
    public boolean userIsExistByUsername(String username) throws CustomException {
        if(StringUtils.isBlank(username) ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR
                    ,"username不存在");
        }
        return userService.usernameIsExist(username);
    }
}
