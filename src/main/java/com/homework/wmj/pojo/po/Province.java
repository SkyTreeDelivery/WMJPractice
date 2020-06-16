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
@TableName("province")
public class Province   implements Serializable {
    private Integer id;

    private String name;

    private String adcode;

    private Integer geoCount;

    private String geom;

    private String image;

    private String intro;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;
}