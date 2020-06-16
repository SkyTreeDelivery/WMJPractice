package com.homework.wmj.Util.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.homework.wmj.configuration.Properties.JWTProperties;
import com.homework.wmj.pojo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    private static String secret;

    private static long expire;

    @Autowired
    JWTProperties jwtProperties;

    @PostConstruct
    private void init(){
        secret = jwtProperties.getSecret();
        expire = jwtProperties.getExpire();
    }

    public static String getToken(User user){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire * 1000); // 3600s之后过期
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        Algorithm algorithm = Algorithm.HMAC256(secret);  // 算法加盐
        String token = JWT.create()
                .withHeader(headers) // header
                .withIssuedAt(now)   //签发时间
                .withExpiresAt(expireDate)  //过期时间
                .withAudience(user.getId().toString()) // token持有者
                .sign(algorithm); // 签发
        return token;
    }

    public static DecodedJWT decodedToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // 加盐算法
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        return verifier.verify(token);
    }

    public static boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expireDate = decodedJWT.getExpiresAt();
        return expireDate.before(new Date());
    }



}
