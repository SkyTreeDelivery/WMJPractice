package com.homework.wmj.Service.ServiceImp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homework.wmj.Service.ProvinceService;
import com.homework.wmj.Util.Enum.EnumImp.CustomErrorCodeEnum;
import com.homework.wmj.Util.Exception.CustomException;
import com.homework.wmj.Util.Utils.FileUtils;
import com.homework.wmj.Util.Utils.StringUtils;
import com.homework.wmj.mapper.ProvinceMapper;
import com.homework.wmj.pojo.po.Province;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@PropertySource({"classpath:config/postgis_config/table_columns.properties"})
public class ProvinceServiceImp implements ProvinceService {

    @Autowired
    ProvinceMapper provinceMapper;

    @Value("${province}")
    String province_columns;

    private static final String[] suffixs = new String[]{".png", ".PNG", ".jpg", ".JPG", ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};


    private static String imageDir = "/static/upload/image/province/";

    @Override
    public List<Province> getProvinceBaseInfo() {
        QueryWrapper<Province> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("adcode,name");
        return provinceMapper.selectList(queryWrapper);
    }

    @Override
    public Province updateProvince(Province province) {
        provinceMapper.updateById(province);
        return provinceMapper.selectById(province.getId());
    }

    @SneakyThrows
    @Override
    public Province updatePhoto(MultipartFile multipartFile, Integer id){
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."), originalFilename.length());
        List<String> suffixsList = Arrays.asList(suffixs);
        if(!suffixsList.contains(suffix)){
            throw new CustomException(CustomErrorCodeEnum.FILE_SUFFIX_DISMATCH);
        }

        // 得到保存image的绝对路径
        String saveDir = ResourceUtils.getURL("classpath:").getPath() + imageDir;
        // 保存图片，生成文件名
        String savedAbPath = FileUtils.saveFile(multipartFile, saveDir);

        // 如果有旧照片,删除旧照片
        Province oldProvince = provinceMapper.selectById(id);
        String oldPath = null;
        if(oldProvince.getImage() != null){
            oldPath = ResourceUtils.getURL("classpath:").getPath() + oldProvince.getImage();
        }


        // 取得绝对路径，存入数据库
        Province updateProvince = new Province();
        updateProvince.setId(id);
        // 得到新的文件名
        String fileName = savedAbPath.substring(savedAbPath.lastIndexOf("/") + 1, savedAbPath.length());
        // 保存相对路径
        updateProvince.setImage(imageDir + fileName);
        updateProvince(updateProvince);
        Province newProvince = provinceMapper.selectById(id);

        // 所有的操作全部完成，再删除老照片
        if(oldPath != null){
            FileUtils.deleteFile(oldPath);
        }
        return newProvince;
    }

    @Override
    public List<Province> listProvinces(Boolean requireGeom) {
        String invokeColumns = requireGeom ? province_columns :
                StringUtils.excludeColumn(province_columns, Arrays.asList("geom"));
        QueryWrapper<Province> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(invokeColumns);
        return provinceMapper.selectList(queryWrapper);
    }

    @Override
    public Province getProvinceById(Integer id, Boolean requireGeom) {
        String invokeColumns = requireGeom ? province_columns :
                StringUtils.excludeColumn(province_columns, Arrays.asList("geom"));
        QueryWrapper<Province> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(invokeColumns).eq("id",id);
        return provinceMapper.selectOne(queryWrapper);
    }

    @Override
    public Province getProvinceByAdcode(String adcode, Boolean requireGeom) {
        String invokeColumns = requireGeom ? province_columns :
                StringUtils.excludeColumn(province_columns, Arrays.asList("geom"));
        QueryWrapper<Province> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(invokeColumns).eq("adcode",adcode);
        return provinceMapper.selectOne(queryWrapper);
    }

    @Override
    public Province getProvinceByName(String name, Boolean requireGeom) {
        String invokeColumns = requireGeom ? province_columns :
                StringUtils.excludeColumn(province_columns, Arrays.asList("geom"));
        QueryWrapper<Province> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(invokeColumns).eq("name",name);
        return provinceMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean isExistById(Integer id) {
        Province province = provinceMapper.selectById(id);
        return province != null;
    }
}
