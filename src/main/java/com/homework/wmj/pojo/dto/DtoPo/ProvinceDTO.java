package com.homework.wmj.pojo.dto.DtoPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDTO {

    private Integer id;

    private String name;

    private String adcode;

    private Integer geoCount;

    private String geom;

    private String image;

    private String intro;
}
