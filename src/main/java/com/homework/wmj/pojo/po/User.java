package com.homework.wmj.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_user")
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private String faceImage;
}
