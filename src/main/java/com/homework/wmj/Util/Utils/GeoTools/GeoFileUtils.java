package com.homework.wmj.Util.Utils.GeoTools;

import com.homework.wmj.Util.Enum.EnumImp.ShpCharSet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class GeoFileUtils {

    public static Charset getCharsetFromCpg(String cpgPath) {
        Resource resource = new FileSystemResource(cpgPath);
        if(resource.exists()){
            try {
                InputStream inputStream = resource.getInputStream();
                String text = readStringFromInputStream(inputStream);
                if(text.equals("936")){
                    return ShpCharSet.GBK;
                }else if(text.equals("UTF-8")){
                    return ShpCharSet.UTF_8;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ShpCharSet.UTF_8;
    }

    public static String readStringFromInputStream(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String text = new String(bytes);
        return text;
    }
}
