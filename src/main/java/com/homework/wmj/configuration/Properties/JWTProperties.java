package com.homework.wmj.configuration.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource({"classpath:config/jwt.properties"})
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    private String secret;
    private long expire;
}
