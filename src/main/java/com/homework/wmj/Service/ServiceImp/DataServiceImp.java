package com.homework.wmj.Service.ServiceImp;

import com.homework.wmj.Service.DataService;
import com.homework.wmj.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataServiceImp implements DataService {

    @Autowired
    DataMapper dataMapper;

    @Override
    public List<String> getAllTableNames() {
        List<String> allTableNames = dataMapper.getAllTableNames();
        List<String> excludeArray =  new ArrayList(Arrays.asList("spatial_ref_sys"));
        for(int i = 0; i < allTableNames.size(); i++){
            for(int j = 0; j < excludeArray.size();j++){
                if(allTableNames.get(i).equals(excludeArray.get(j))){
                    allTableNames.remove(i);
                    excludeArray.remove(j);
                }
            }
        }
        return allTableNames;
    }
}
