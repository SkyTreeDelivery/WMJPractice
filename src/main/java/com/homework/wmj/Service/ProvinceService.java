package com.homework.wmj.Service;

import com.homework.wmj.pojo.po.Province;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProvinceService {

    List<Province> getProvinceBaseInfo();

    Province updateProvince(Province province);

    Province updatePhoto(MultipartFile multipartFile, Integer id);

    List<Province> listProvinces(Boolean requireGeom);

    Province getProvinceById(Integer id, Boolean requireGeom);

    Province getProvinceByAdcode(String adcode, Boolean requireGeom);

    Province getProvinceByName(String name, Boolean requireGeom);

    boolean isExistById(Integer id);
}
