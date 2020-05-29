package com.homework.wmj.Util.Utils;

public class GdalUtils {

    //TODO 测试使用
    static {
        try{
            System.loadLibrary("gdalalljni");
            System.out.println("本地库加载成功");
        }
        catch(UnsatisfiedLinkError e){
            System.out.println("本地库加载失败");
        }
    }



}
