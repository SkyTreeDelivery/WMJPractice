package com.homework.wmj.Util.Utils.GeoTools;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataStoreFactory {

    private static Logger logger = LoggerFactory.getLogger(DataStoreFactory.class);

    public static DataStore getPostgisDataStore(PGDataConfig pgDataConfig){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
        params.put(PostgisNGDataStoreFactory.HOST.key, pgDataConfig.getHost());
        params.put(PostgisNGDataStoreFactory.PORT.key, Integer.parseInt( pgDataConfig.getPort()));
        params.put(PostgisNGDataStoreFactory.DATABASE.key, pgDataConfig.getDbname());
        params.put(PostgisNGDataStoreFactory.SCHEMA.key, pgDataConfig.getSchema());
        params.put(PostgisNGDataStoreFactory.USER.key, pgDataConfig.getUsername());
        params.put(PostgisNGDataStoreFactory.PASSWD.key, pgDataConfig.getPassword());
        try {
            return DataStoreFinder.getDataStore(params);
        } catch (IOException e) {
            logger.error("Postgis数据库连接失败 - 连接配置 +" + pgDataConfig, e);
        }
        return null;
    }

}
