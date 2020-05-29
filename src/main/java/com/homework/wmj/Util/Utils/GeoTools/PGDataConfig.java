package com.homework.wmj.Util.Utils.GeoTools;

import com.sun.istack.internal.NotNull;

public class PGDataConfig {

    @NotNull
    private String host;
    @NotNull
    private String port;
    @NotNull
    private String dbname;
    @NotNull
    private String schema;
    @NotNull
    private String username;
    @NotNull
    private String password;


    public PGDataConfig(){
        this.host = "47.100.37.7";
        this.port = "5432";
        this.dbname = "WMJDatabase";
        this.schema = "public";
        this.username = "postgres";
        this.password = "zhang002508";
    }

    public PGDataConfig(String host, String port, String dbname, String schema, String username, String password) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.schema = schema;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
