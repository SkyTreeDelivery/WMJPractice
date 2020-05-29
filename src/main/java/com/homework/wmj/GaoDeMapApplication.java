package com.homework.wmj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.homework.wmj.mapper")
public class GaoDeMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaoDeMapApplication.class, args);
	}

}
