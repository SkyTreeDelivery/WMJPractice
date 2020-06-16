package com.homework.wmj.controller;

import com.homework.wmj.Service.DataService;
import com.homework.wmj.Util.Utils.GeoTools.DataStoreFactory;
import com.homework.wmj.Util.Utils.GeoTools.Geotools;
import com.homework.wmj.Util.Utils.GeoTools.PGDataConfig;
import org.geotools.data.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/data/")
public class DataController {

    @Autowired
    DataService dataService;

    @RequestMapping("getAll/{tableName}")
    public String getAllGeoData(@PathVariable("tableName") String tableName) throws IOException {
        PGDataConfig pgDataConfig = new PGDataConfig();
        DataStore postgisDataStore = DataStoreFactory.getPostgisDataStore(pgDataConfig);
        String geojson = Geotools.pgtable2geojson(tableName, postgisDataStore);
        postgisDataStore.dispose();
        return geojson;
    }

    @RequestMapping("getAllTableNames")
    public List<String> getAllTableNames(){
        return dataService.getAllTableNames();
    }

    @RequestMapping("upload")
    public boolean upload(MultipartFile file,String username){
        System.out.println(file);
        System.out.println(username);
        return true;
    }



}
