package com.homework.wmj.controller;

import com.homework.wmj.Service.ProvinceService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.BeanArrayUtils;
import com.homework.wmj.pojo.dto.DtoPo.ProvinceDTO;
import com.homework.wmj.pojo.po.Province;
import com.homework.wmj.pojo.vo.ProvinceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/province/*")
public class ProvinceController {

    @Autowired
    ProvinceService provinceService;

    @RequestMapping("getProvinceBaseInfo")
    public List<Province> getProvinceBaseInfo(){
        return provinceService.getProvinceBaseInfo();
    }

    @RequestMapping("updateProvince")
    public ProvinceVO updateProvince(ProvinceDTO provinceDTO) throws Exception{
        if(provinceDTO == null){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR);
        }
        if(provinceDTO.getId() == null){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"必须携带id字段");
        }
        // 不保存image，在其他controller中保存图片
        provinceDTO.setImage(null);
        Province provinceSave = BeanArrayUtils.copyProperties(provinceDTO, Province.class);
        Province provinceNew = provinceService.updateProvince(provinceSave);
        ProvinceVO provinceVO = BeanArrayUtils.copyProperties(provinceNew, ProvinceVO.class);
        return  provinceVO;
    }

    @RequestMapping("updateImage")
    public ProvinceVO updateImage(@RequestParam("file") MultipartFile multipartFile, Integer id) throws Exception{
        if(multipartFile == null || multipartFile.getSize() == 0){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"保存的图片没有上传成功！");
        }
        if(id == null || !provinceService.isExistById(id)){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"id不存在！");
        }
        Province province = provinceService.updatePhoto(multipartFile, id);
        ProvinceVO provinceVO = BeanArrayUtils.copyProperties(province, ProvinceVO.class);;
        return provinceVO;
    }

    @RequestMapping("listProvinces")
    public List<ProvinceVO> listProvinces(Boolean requireGeom) throws Exception {
        requireGeom = requireGeom != null ? requireGeom : false;
        List<Province> provinces = provinceService.listProvinces(requireGeom);
        List<ProvinceVO> provinceVOS = BeanArrayUtils.copyListProperties(provinces, ProvinceVO.class);
        return provinceVOS;
    }

    @RequestMapping("getProvinceById")
    public ProvinceVO getProvinceById(Integer id, Boolean requireGeom) throws Exception {
        if(id == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"未携带id参数！");
        }
        requireGeom = requireGeom != null ? requireGeom : false;
        Province province = provinceService.getProvinceById(id,requireGeom);
        ProvinceVO provinceVO = BeanArrayUtils.copyProperties(province, ProvinceVO.class);
        return provinceVO;
    }

    @RequestMapping("getProvinceByAdcode")
    public ProvinceVO getProvinceByAdcode(String adcode, Boolean requireGeom) throws Exception{
        if(adcode == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"未携带adcode参数！");
        }
        requireGeom = requireGeom != null ? requireGeom : false;
        Province province = provinceService.getProvinceByName(adcode,requireGeom);
        ProvinceVO provinceVO = BeanArrayUtils.copyProperties(province, ProvinceVO.class);
        return provinceVO;
    }

    @RequestMapping("getProvinceByName")
    public ProvinceVO getProvinceByName(String name, Boolean requireGeom) throws Exception{
        if(name == null ){
            throw new CustomException(CustomErrorCodeEnum.PARAM_VERIFY_ERROR,"未携带name参数！");
        }
        requireGeom = requireGeom != null ? requireGeom : false;
        Province province = provinceService.getProvinceByName(name,requireGeom);
        ProvinceVO provinceVO = BeanArrayUtils.copyProperties(province, ProvinceVO.class);
        return provinceVO;
    }

}
