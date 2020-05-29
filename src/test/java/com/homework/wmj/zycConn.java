package com.homework.wmj;

import java.sql.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;


public class zycConn {

    Connection c = null;  //构建连接
    public  zycConn()
    {

    }

    public void  getConn()
    {
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

    public ResultSet getRS( String SQLStr)
    {
        ResultSet rs = null;
        try {

            //,,,

            while(rs.next())
            {

                System.out.println("Opened database successfully");
            }

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return rs;
    }

    public String setUpdate( String SQLStr)
    {

        try {

            //,,,

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return "记录已经更新！";
    }

    public String setDelRd( String SQLStr)
    {
        ResultSet rs = null;
        Statement stmt = null;
        try {
            //,,,

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return "记录已经删除！";
    }

    public void closeConn( )//关闭连接
    {
        if(c != null)
        {
            try
            {
                c.close();
            }
            catch ( Exception e )
            {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
            }
        }
    }


   public static void main( String args[] )
     {
	   zycConn c = new zycConn();
	   c.getRS("select * from res_4m where cityname like '%南%' ") ;
     }
}
