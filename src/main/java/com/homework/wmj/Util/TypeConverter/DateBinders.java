package com.homework.wmj.Util.TypeConverter;


import com.homework.wmj.Util.Utils.JsonUtils;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateBinders{

    public static class GeoBinder extends PropertyEditorSupport{
        private static Logger logger = LoggerFactory.getLogger(GeoBinder.class);

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                Geometry geom = JsonUtils.geoJsonToGeom(text);
                geom.setSRID(4326);
                setValue(geom);
            } catch (IOException e) {
                e.printStackTrace();
                setValue(null);
                logger.error(text,"解析失败",e.getMessage(),e);
            }
        }
    }

    public static class LocalDateTimeBinder extends PropertyEditorSupport{
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                TemporalAccessor parse = dateTimeFormatter.parse(text);
                LocalDateTime dateTime = LocalDateTime.from(parse);
                setValue(dateTime);
            }catch (Exception e){
                setValue(null);
            }
        }
    }
}
