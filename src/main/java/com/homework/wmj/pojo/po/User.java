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
@TableName("chat_user")
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private Integer age;

    private Integer gender;

    private String nickname;

    private String email;

//    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String token;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;
}
