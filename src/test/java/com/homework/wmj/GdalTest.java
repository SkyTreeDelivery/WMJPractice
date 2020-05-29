package com.homework.wmj;

import com.homework.wmj.Util.Utils.GeoTools.GeoFileUtils;
import com.homework.wmj.Util.Utils.GeoTools.Geotools;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;

public class GdalTest {

    private static String shpfilepath = "D:\\我的用户\\新建文件夹\\新建文件夹 (5)\\XianCh_point.shp";
    private static String geojsonpath = "D:\\我的用户\\新建文件夹\\新建文件夹 (5)\\新建文本文档.txt";
    private  static Geotools geotools;


    @Test
    public void testD(){

        LocalDateTime start = LocalDateTime.now();

        // geotools.geojson2shp(geojsonpath,shpfilepath);
        // geotools.geojson2shp(geojsonpath,shpfilepath, Geotools.ShpCharset.GBK);
        // geotools.geojson2pgtable(geojsonpath,"xiancheng_point");
        // geotools.shp2geojson(shpfilepath,geojsonpath);
        // geotools.shp2geojson(shpfilepath,geojsonpath,Geotools.ShpCharset.GBK);
        // geotools.shp2pgtable(shpfilepath,"xiancheng_point");
        // geotools.shp2pgtable(shpfilepath,"xiancheng_point", Geotools.ShpCharset.GBK);
        // geotools.pgtable2geojson("xiancheng_point",geojsonpath);
        // geotools.pgtable2shp("xiancheng_point",shpfilepath,"geometry");


        LocalDateTime end = LocalDateTime.now();
        Duration time = Duration.between(start, end);
        System.out.println("耗时：" + time.toMillis());
    }

    @Test
    public void testE(){
        String text = shpfilepath.split("\\.")[0] + ".cpg";
        Charset charsetFromCpg = GeoFileUtils.getCharsetFromCpg(text);
        System.out.println(charsetFromCpg);
    }


    @Test
    public void testA(){
        // String filePath="PG:dbname=Beijing host=localhost port=5432 user=postgres password=gyxdlwwcx";
        // // gdal.AllRegister();
        // // gdal.open
        //
        //
        // String shpFileName = "D:\\我的用户\\新建文件夹\\全国行政区数据_2019\\gadm36_CHN_0.shp";
        // String geojsonFileName = "D:\\我的用户\\新建文件夹\\新建文本文档.txt";
        // // 注册所有的驱动
        // ogr.RegisterAll();
        // // 为了支持中文路径，请添加下面这句代码
        // gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        // // 为了使属性表字段支持中文，请添加下面这句
        // gdal.SetConfigOption("SHAPE_ENCODING","");
        // //shp文件所在的位置
        // //String strVectorFile = "D:\\sichuan\\sichuanPointALL.shp";
        // //打开数据
        // DataSource ds = ogr.Open(shpFileName,0);
        // if (ds == null)
        // {
        //     System.out.println("打开文件失败！" );
        //     return;
        // }
        // System.out.println("打开文件成功！" );
        // org.gdal.ogr.Driver dv = ogr.GetDriverByName("GeoJSON");
        // if (dv == null)
        // {
        //     System.out.println("打开驱动失败！" );
        //     return;
        // }
        // System.out.println("打开驱动成功！" );
        // // //输出geojson的位置及文件名
        // // //String geojsonFileName = "D:\\data\\sichuan.geojson";
        // // dv.CopyDataSource(ds, geojsonFileName);
        // // System.out.println(geojsonFileName);
    }

    @Test
    public void testB(){
        try{
            System.loadLibrary("gdalalljni");
            System.out.println("本地库加载成功");
        }
        catch(UnsatisfiedLinkError e){
            System.out.println("本地库加载失败");
        }
    }

    @Test
    public void testC(){
        try{
            System.loadLibrary("gdalalljni");
            System.out.println("本地库加载成功");
        }
        catch(UnsatisfiedLinkError e){
            System.out.println("本地库加载失败");
        }

        // gdal.AllRegister();
        //
        // String filePath="PG:dbname=postgres host=47.100.37.7 port=5432 user=postgres password=zhang002508";
        // Dataset dataset = gdal.OpenEx(filePath);
        //
        // if(dataset == null){
        //     System.out.println("数据库打开失败");
        //     return;
        // }
        //
        // Layer layer = dataset.GetLayer("city_point");
        // int count = dataset.GetLayerCount();
        // for(int i = 0; i < count; i++){
        //     String s = dataset.GetLayer(i).GetName();
        //     System.out.println(s);
        // }
        // if(layer == null){
        //     System.out.println("图层加载失败");
        //     return;
        // }
        // layer.ResetReading();
        // List<Feature> features = new ArrayList<>();
        // Feature feature = layer.GetNextFeature();
        // while(feature != null){
        //     features.add(feature);
        //     feature = layer.GetNextFeature();
        // }
        // System.out.println(features);

    }




}
