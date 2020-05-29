package com.homework.wmj;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

// @SpringBootTest
class GaoDeMapApplicationTests {


	Connection c = null;  //构建连接
	@Test
	void contextLoads() {
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://47.100.37.7:5432/WMJDatabase?characterEncoding=utf-8\\",//testdb，数据库名
					"postgres", "zhang002508"); //用户名，密码
			System.out.println("Opened database successfully");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
	}

	@Test
	public void testA(){
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://120.26.79.47:5432/ChinaMap",//testdb，数据库名
					"postgres", "gyxdlwwcx"); //用户名，密码
			System.out.println("Opened database successfully");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
	}

}
