package com.homework.wmj.pojo.dto.DtoPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO  implements Serializable {
    @NotNull(message = "username不能为空")
    @Size(min = 6, max = 15,message = "用户名在6-15个字符范围内")
    private String username;

    @NotNull(message = "password不能为空")
    @Size(min = 6,max = 15,message = "密码在6-15个字符范围内")
    private String password;

    @NotNull(message = "nickname不能为空")
    @Size(max = 16,message = "nickname最大为16个字符")
    private String nickname;
}
