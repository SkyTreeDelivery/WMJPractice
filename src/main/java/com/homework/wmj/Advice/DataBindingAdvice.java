package com.homework.wmj.Advice;

import com.homework.wmj.Util.TypeConverter.DateBinders;
import org.locationtech.jts.geom.Point;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDateTime;

@ControllerAdvice
public class DataBindingAdvice {
    @InitBinder
    public void dataBind(WebDataBinder binder){
        binder.registerCustomEditor(Point.class, new DateBinders.GeoBinder());
        binder.registerCustomEditor(LocalDateTime.class,new DateBinders.LocalDateTimeBinder());
    }

}
