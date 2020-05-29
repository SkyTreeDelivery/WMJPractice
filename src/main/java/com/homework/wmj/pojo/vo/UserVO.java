package com.homework.wmj.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    @JSONField(ordinal = 0)
    private Integer id;
    private String username;
    private Integer age;
    private Integer gender;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String email;
}
