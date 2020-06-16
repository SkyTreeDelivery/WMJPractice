package com.homework.wmj.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceVO  implements Serializable {
    private Integer id;

    private String name;

    private String adcode;

    private Integer geoCount;

    private String geom;

    private String image;

    private String intro;
}
