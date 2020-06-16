package com.homework.wmj.Util.Utils.GeoTools;

import com.homework.wmj.Util.Enum.EnumImp.ShpCharSet;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.geotools.data.Transaction.AUTO_COMMIT;

/**
 * @author wangkang
 * @email iwuang@qq.com
 * @date 2019/1/24 16:27
 */
public class Geotools {

    private static Logger logger = LoggerFactory.getLogger(Geotools.class);

    /**
     * 通用，要素集写入postgis
     *
     * @param featureCollection
     * @param pgtableName       postgis创建的数据表
     * @return
     */
    public static void write2pg(FeatureCollection featureCollection, String pgtableName, DataStore postgisDatasore) throws IOException {
        if (Utility.isEmpty(featureCollection) || Utility.isEmpty(pgtableName)) {
            logger.error("参数无效");
            return;
        }
        SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(simpleFeatureType);

        typeBuilder.setName(pgtableName);

        SimpleFeatureType newtype = typeBuilder.buildFeatureType();
        postgisDatasore.createSchema(newtype);

        FeatureIterator iterator = featureCollection.features();
        FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = postgisDatasore.getFeatureWriterAppend(pgtableName, AUTO_COMMIT);

        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            SimpleFeature simpleFeature = featureWriter.next();
            Collection<Property> properties = feature.getProperties();
            Iterator<Property> propertyIterator = properties.iterator();
            while (propertyIterator.hasNext()) {
                Property property = propertyIterator.next();
                simpleFeature.setAttribute(property.getName().toString(), property.getValue());
            }
            featureWriter.write();
        }
        iterator.close();
        featureWriter.close();
    }

    /**
     * featureCollection写入到shp的datastore
     *
     * @param featureCollection
     * @param shpDataStore
     * @param geomFieldName     featureCollectio中的矢量字段，postgis可以修改使用不同的表名，默认为geom
     * @return
     */
    public static void write2shp(FeatureCollection featureCollection, ShapefileDataStore shpDataStore, String geomFieldName) throws IOException {
        if (Utility.isEmpty(geomFieldName)) {
            geomFieldName = featureCollection.getSchema().getGeometryDescriptor().getType().getName().toString();
        }
        FeatureIterator<SimpleFeature> iterator = featureCollection.features();
        //shp文件存储写入
        FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shpDataStore.getFeatureWriter(shpDataStore.getTypeNames()[0], AUTO_COMMIT);
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            SimpleFeature simpleFeature = featureWriter.next();
            Collection<Property> properties = feature.getProperties();
            Iterator<Property> propertyIterator = properties.iterator();

            while (propertyIterator.hasNext()) {
                Property property = propertyIterator.next();
                if (property.getName().toString().equalsIgnoreCase(geomFieldName)) {
                    simpleFeature.setAttribute("the_geom", property.getValue());
                } else {
                    simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                }
            }
            featureWriter.write();
        }
        iterator.close();
        featureWriter.close();
        shpDataStore.dispose();
    }

    public static void   geojsonFile2shp(String geojsonPath, String shpfilepath) throws IOException {
        String cpgFilePath = shpfilepath.split("\\.")[0] + ".cpg";
        Charset charset = GeoFileUtils.getCharsetFromCpg(cpgFilePath);
        geojsonFile2shp(geojsonPath, shpfilepath, ShpCharSet.UTF_8);
    }

    /**
     * 方法重载，默认使用UTF-8的Shp文件
     * @param geojsonPath
     * @param shpfilepath
     * @return
     */
    public static void   geojsonStr2shp(String geojsonPath, String shpfilepath) throws IOException {
        String cpgFilePath = shpfilepath.split("\\.")[0] + ".cpg";
        Charset charset = GeoFileUtils.getCharsetFromCpg(cpgFilePath);
        geojsonStr2shp(geojsonPath, shpfilepath, ShpCharSet.UTF_8);
    }

    /**
     * Geojson转成shpfile文件
     *
     * @param geojsonPath
     * @param shpfilepath
     * @return
     */
    public static void geojsonFile2shp(String geojsonPath, String shpfilepath, Charset shpChart) throws IOException {
        Utility.valiFileForRead(geojsonPath);
        FeatureJSON featureJSON = new FeatureJSON();
        featureJSON.setEncodeNullValues(true);
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(new InputStreamReader(new FileInputStream(geojsonPath),"utf-8"));
        File file = new File(shpfilepath);
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
        ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

        //postgis获取的Featuretype获取坐标系代码
        SimpleFeatureType pgfeaturetype = (SimpleFeatureType) featureCollection.getSchema();

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(pgfeaturetype);
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
        pgfeaturetype = typeBuilder.buildFeatureType();
        //设置成utf-8编码
        shpDataStore.setCharset(shpChart);
        shpDataStore.createSchema(pgfeaturetype);
        write2shp(featureCollection, shpDataStore, "");
    }

    public static void geojsonStr2shp(String geojson, String shpfilepath, Charset shpChart) throws IOException {
        FeatureJSON featureJSON = new FeatureJSON();
        featureJSON.setEncodeNullValues(true);
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(new InputStreamReader(new ByteArrayInputStream(geojson.getBytes()),"utf-8"));
        File file = new File(shpfilepath);
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
        ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

        //postgis获取的Featuretype获取坐标系代码
        SimpleFeatureType pgfeaturetype = (SimpleFeatureType) featureCollection.getSchema();

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(pgfeaturetype);
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
        pgfeaturetype = typeBuilder.buildFeatureType();
        //设置成utf-8编码
        shpDataStore.setCharset(shpChart);
        shpDataStore.createSchema(pgfeaturetype);
        write2shp(featureCollection, shpDataStore, "");
    }

    /**
     * geojson文件写入到postgis里
     *
     * @param geojsonPath
     * @param pgtableName
     * @return
     */
    public static void geojsonFile2pgtable(String geojsonPath, String pgtableName, DataStore postgisDatasore) throws IOException {
            if (Utility.isEmpty(geojsonPath) || Utility.isEmpty(pgtableName)) {
                return;
            }
            FeatureJSON featureJSON = new FeatureJSON();
            FeatureCollection featureCollection = featureJSON.readFeatureCollection(new FileInputStream(geojsonPath));
            write2pg(featureCollection, pgtableName,postgisDatasore);
    }

    public static void geojsonStr2pgtable(String geojson, String pgtableName, DataStore postgisDatasore) throws IOException {
            if (Utility.isEmpty(pgtableName)) {
                return;
            }
            FeatureJSON featureJSON = new FeatureJSON();
            FeatureCollection featureCollection = featureJSON.readFeatureCollection( new ByteArrayInputStream(geojson.getBytes()));
            write2pg(featureCollection, pgtableName,postgisDatasore);
    }

    public static void shp2geojson_path(String shpPath, String geojsonPath) throws IOException {
        String cpgFilePath = shpPath.split("\\.")[0] + ".cpg";
        Charset charset = GeoFileUtils.getCharsetFromCpg(cpgFilePath);
        shp2geojson_path(shpPath, geojsonPath, charset);
    }

    /**
     * 重载方法，默认UTF-8编码SHP文件
     *
     * @param shpPath
     * @param geojsonPath
     * @return
     */
    public static String shp2geojson(String shpPath, String geojsonPath) {
        String cpgFilePath = shpPath.split("\\.")[0] + ".cpg";
        Charset charset = GeoFileUtils.getCharsetFromCpg(cpgFilePath);
        return shp2geojson(shpPath, geojsonPath);
    }

    /**
     * shp转成geojson，保留15位小数
     *
     * @param shpPath     shp的路径
     * @param geojsonPath geojson的路径
     * @return
     */
    public static void shp2geojson_path(String shpPath, String geojsonPath, Charset shpCharset) throws IOException {
        if (!Utility.valiFileForRead(shpPath) || Utility.isEmpty(geojsonPath)) {
            return;
        }
        String s = shp2geojson(shpPath, shpCharset);
        FileUtils.writeStringToFile(new File(geojsonPath), s, Charsets.toCharset("utf-8"), false);
    }

    public static String shp2geojson(String shpPath, Charset shpCharset) {
        try {
            if (!Utility.valiFileForRead(shpPath)) {
                return null;
            }
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            ContentFeatureSource featureSource = shapefileDataStore.getFeatureSource();
            ContentFeatureCollection contentFeatureCollection = featureSource.getFeatures();
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
            String s = featureJSON.toString(contentFeatureCollection);
            shapefileDataStore.dispose();
            return s;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static void shp2pgtable(String shpPath, String pgtableName, DataStore postgisDatasore) throws IOException {
        String cpgFilePath = shpPath.split("\\.")[0] + ".cpg";
        Charset charset = GeoFileUtils.getCharsetFromCpg(cpgFilePath);
        shp2pgtable(shpPath, pgtableName, charset,postgisDatasore);
    }

    /**
     * shpfile文件导入到postgis中
     *
     * @param shpPath
     * @param pgtableName
     * @return
     */
    public static void shp2pgtable(String shpPath, String pgtableName, Charset shpCharset, DataStore postgisDatasore) throws IOException {
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
        shapefileDataStore.setCharset(shpCharset);
        FeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();
        write2pg(featureCollection, pgtableName,postgisDatasore);
    }

    /**
     * postgis数据表导出到成shpfile
     *
     * @param pgtableName
     * @param shpPath
     * @param geomField   postgis里的字段
     * @return
     */
    public static void pgtable2shp(String pgtableName, String shpPath, String geomField, DataStore postgisDatasore) throws IOException, FactoryException {

        FeatureSource featureSource = postgisDatasore.getFeatureSource(pgtableName);

        // 初始化 ShapefileDataStore
        File file = new File(shpPath);
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
        ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

        //postgis获取的Featuretype获取坐标系代码
        SimpleFeatureType pgfeaturetype = ((SimpleFeatureSource) featureSource).getSchema();
        String srid = pgfeaturetype.getGeometryDescriptor().getUserData().get("nativeSRID").toString();
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(pgfeaturetype);
        if (!srid.equals("0")) {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:" + srid, true);
            typeBuilder.setCRS(crs);
        }
        pgfeaturetype = typeBuilder.buildFeatureType();
        //设置成utf-8编码
        shpDataStore.setCharset(Charset.forName("utf-8"));
        shpDataStore.createSchema(pgfeaturetype);
        write2shp(featureSource.getFeatures(), shpDataStore, geomField);
    }

    /**
     * postgis指定的数据表转成geojson文件保留15位小数
     *
     * @param pgtableName 表名
     * @param geojsonpath geojson存放位置
     * @return
     */
    public static void pgtable2geojson_path(String pgtableName, String geojsonpath, DataStore postgisDatasore) throws IOException {
        String s = pgtable2geojson(pgtableName, postgisDatasore);
        FileUtils.writeStringToFile(new File(geojsonpath), s, Charsets.toCharset("utf-8"), false);
    }

    public static String pgtable2geojson(String pgtableName, DataStore postgisDatasore) throws IOException {
        LocalDateTime start = LocalDateTime.now();
        FeatureSource featureSource = postgisDatasore.getFeatureSource(pgtableName);
        FeatureCollection featureCollection = featureSource.getFeatures();

        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
        featureJSON.setEncodeNullValues(true);

        String result = featureJSON.toString(featureCollection);

        LocalDateTime end = LocalDateTime.now();
        long time = Duration.between(start, end).toMillis();
        logger.info("执行时间：" + time + "ms");
        return result;
    }

    public static void deletePgtable(String pgtableName, DataStore postgisDatasore) throws IOException {
        postgisDatasore.removeSchema(pgtableName);
    }
}
