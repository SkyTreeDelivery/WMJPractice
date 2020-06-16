package com.homework.wmj.Service.ServiceImp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homework.wmj.Service.UserService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.BeanArrayUtils;
import com.homework.wmj.Util.Utils.FileUtils;
import com.homework.wmj.Util.Utils.JWTUtils;
import com.homework.wmj.Util.Utils.MD5Util;
import com.homework.wmj.mapper.UserMapper;
import com.homework.wmj.pojo.dto.DtoPo.UserDTO;
import com.homework.wmj.pojo.po.User;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserServiceImp implements UserService {


   private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    UserMapper userMapper;

    private static final String[] suffixs = new String[]{".png", ".PNG", ".jpg", ".JPG", ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};

    private static String imageDir = "/static/upload/image/user/";

    @SneakyThrows
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User register(UserDTO userDTO) throws CustomException {
        //2. 如果username，email已被注册，则提示已占用
        if (usernameIsExist(userDTO.getUsername())) {
            throw new CustomException(CustomErrorCodeEnum.USER_HAS_EXISTED);
        }

        User user = BeanArrayUtils.copyProperties(userDTO, User.class);

        user.setPassword(MD5Util.getMD5Str(user.getPassword()));
        userMapper.insert(user);
        User newUser = getUserByUsername(user.getUsername());
        return newUser;
    }

    @Override
    public Boolean verifyUser(String username, String password) {
        User user = getUserByUsername(username);
        Boolean result = null;
        try {
            result = user != null && user.getPassword() != null
                    && user.getPassword().equals(MD5Util.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return false;
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String, Object> login(String username, String password) throws CustomException {
//        2. 验证用户
        Boolean result = verifyUser(username, password);
        if (!result) {
            throw new CustomException(CustomErrorCodeEnum.USER_LOGIN_ERROR);
        }
        User userinfo = getUserByUsername(username);
        Map<String, Object> re = new HashMap<>();
        re.put("token",JWTUtils.getToken(userinfo));
        re.put("user",userinfo);
        return re;
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

    @SneakyThrows
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByToken(String token) {
        DecodedJWT decodedJWT = JWTUtils.decodedToken(token);
        if(decodedJWT == null){
            throw new CustomException(CustomErrorCodeEnum.PARAM_INVALID_TOKEN);
        }
        // 检查是否过期
        Date expiresDate = decodedJWT.getExpiresAt();
        Date now = new Date();
        if(expiresDate.before(now)){
            throw new CustomException(CustomErrorCodeEnum.PARAM_EXPIRED_TOKEN);
        }
        // 查询该用户还是否存在
        String userId = decodedJWT.getAudience().get(0);
        User user = getUserById(Integer.parseInt(userId));

        return user;
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
    public Boolean verifyToken(String token) throws CustomException {
        User user = getUserByToken(token);
        if(user == null){
            throw new CustomException(CustomErrorCodeEnum.USER_NOT_EXIST);
        }
        return true;
    }

    @SneakyThrows
    @Override
    public User updatePhoto(MultipartFile multipartFile, Integer id) {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."), originalFilename.length());
        List<String> suffixsList = Arrays.asList(suffixs);
        if(!suffixsList.contains(suffix)){
            throw new CustomException(CustomErrorCodeEnum.FILE_SUFFIX_DISMATCH);
        }

        // 得到保存image的绝对路径
        String saveDir = ResourceUtils.getURL("classpath:").getPath() + imageDir;
        // 保存图片，生成文件名
        String savedAbPath = FileUtils.saveFile(multipartFile, saveDir);

        // 如果有旧照片,删除旧照片
        User oldUser = userMapper.selectById(id);
        String oldPath = null;
        if(oldUser.getFaceImage() != null){
            oldPath = ResourceUtils.getURL("classpath:").getPath() + oldUser.getFaceImage();
        }


        // 取得绝对路径，存入数据库
        User updateUser = new User();
        updateUser.setId(id);
        // 得到新的文件名
        String fileName = savedAbPath.substring(savedAbPath.lastIndexOf("/") + 1, savedAbPath.length());
        // 保存相对路径
        updateUser.setFaceImage(imageDir + fileName);
        updateUser(updateUser);
        User newUser = userMapper.selectById(id);

        // 所有的操作全部完成，再删除老照片
        if(oldPath != null){
            FileUtils.deleteFile(oldPath);
        }
        return newUser;
    }
}