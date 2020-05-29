package com.homework.wmj.Service.ServiceImp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homework.wmj.Service.UserService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.BeanArrayUtils;
import com.homework.wmj.Util.Utils.MD5Util;
import com.homework.wmj.Util.Utils.ValidateUtils;
import com.homework.wmj.mapper.UserMapper;
import com.homework.wmj.pojo.dto.DtoPo.UserDTO;
import com.homework.wmj.pojo.po.User;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {


   private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Resource
    UserMapper userMapper;


    @SneakyThrows
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User register(UserDTO userDTO) throws CustomException {
        //2. 如果username，email已被注册，则提示已占用
        if (usernameIsExist(userDTO.getUsername())
                || emailIsExist(userDTO.getEmail())) {
            throw new CustomException(CustomErrorCodeEnum.USER_HAS_EXISTED);
        }

        User user = BeanArrayUtils.copyProperties(userDTO, User.class);
        //3. 注册用户，注册完成后自动登录，返回token
        String token = getUniToken();
        user.setToken(token);

        user.setPassword(MD5Util.getMD5Str(user.getPassword()));
        userMapper.insert(user);
        User newUser = getUserByUsername(user.getUsername());
        return newUser;
    }

    private String getUniToken() {
        while (true) {
            String token = UUID.randomUUID().toString();
            Boolean result = tokenIsExist(token);
            if (!result) {
                return token;
            }
        }
    }

    @Override
    public Boolean verifyUser(String username, String password) {
        User user = getUserByUsername(username);
        try {
            Boolean result = user != null && user.getPassword() != null
                    && user.getPassword().equals(MD5Util.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User login(String username, String password) throws CustomException {
//        2. 验证用户
        Boolean result = verifyUser(username, password);
        if (!result) {
            throw new CustomException(CustomErrorCodeEnum.USER_LOGIN_ERROR);
        }
        User userinfo = getUserByUsername(username);
//        3. 生成新的用户token
        String token = getUniToken();
        User updateUser = new User();
        updateUser.setId(userinfo.getId());
        updateUser.setToken(token);
        User newUser = getUserByUsername(username);
        return newUser;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean logout(String token) throws CustomException {
        User user = getUserByToken(token);
        if (user == null) {
            throw new CustomException(CustomErrorCodeEnum.USER_NOT_EXIST);
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setToken(ValidateUtils.DEFAULT_TOKEN);
        updateUser(updateUser);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean updateUser(User user) {
//        md5加密
        if (user.getPassword() != null) {
            try {
                user.setPassword(MD5Util.getMD5Str(user.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                logger.error(e.getMessage(),e);
                return false;
            }
        }
        return userMapper.updateById(user) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByToken(String token) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("token", token);
        return userMapper.selectOne(wrapper);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean tokenIsExist(String token) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("token", token);
        return userMapper.selectCount(wrapper) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean userIsExist(Integer userId) {
        return userMapper.selectById(userId) != null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean usernameIsExist(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectCount(wrapper) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean emailIsExist(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return userMapper.selectCount(wrapper) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean removeUserById(Integer userId) {
        return userMapper.deleteById(userId) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean verifyToken(String token) {
        User user = getUserByToken(token);
        return user != null;
    }
}