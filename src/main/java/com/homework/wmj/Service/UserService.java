package com.homework.wmj.Service;


import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.pojo.dto.DtoPo.UserDTO;
import com.homework.wmj.pojo.po.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface UserService {

    /*====================== User ==============================*/
    User register(UserDTO userDTO) throws CustomException;

    Boolean verifyUser(String username, String password);

    Map<String, Object> login(String username, String password) throws CustomException;

    Boolean logout(String token) throws CustomException;

    Boolean updateUser(User user);

    User getUserById(Integer userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User getUserByToken(String token);

    Boolean tokenIsExist(String token);

    Boolean userIsExist(Integer userId);

    Boolean usernameIsExist(String username);

    Boolean emailIsExist(String email);

    Boolean removeUserById(Integer userId);

    Boolean verifyToken(String token) throws CustomException;

    User updatePhoto(MultipartFile multipartFile, Integer id);

}
