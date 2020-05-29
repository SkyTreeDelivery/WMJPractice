package com.homework.wmj.configuration;

import com.homework.wmj.configuration.Properties.JNIProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JNIConfigurer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private JNIProperties jniProperties;

    private static Logger logger = LoggerFactory.getLogger(JNIConfigurer.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            List<String> successArray = new ArrayList<>();
            List<String> failArray = new ArrayList<>();
            List<String> jninames = jniProperties.getNames();
            for (String jniname : jninames) {
                try{
                    System.loadLibrary(jniname);
                    successArray.add(jniname);
                }
                catch(UnsatisfiedLinkError e){
                    failArray.add(jniname);
                }
            }
            logger.info("加载成功~：" + successArray);
            logger.info("加载失败！：" + failArray);
        }
    }
}
