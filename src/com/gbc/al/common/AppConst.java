/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.common;

/**
 *
 * @author tamvv
 */
public class AppConst {

    public static final String Config_File = "conf/app.conf";
    //server
    public static final String ServerSection = "server";
    public static final String ServerHttpPort_Key = "http_port";
    public static final String ServerWebSocketPort_Key = "ws_port";

    //mysql
    public static final String MysqlSection = "mysql";
    public static final String MysqlHost_Key = "host";
    public static final String MysqlPort_Key = "port";
    public static final String MysqlUser_Key = "username";
    public static final String MysqlPass_Key = "password";
    public static final String MysqlDBName_Key = "dbname";

    //resource
    public static final String ResourceSection = "resource";
    public static final String ResourceDirKey = "dir";

    public static final String AuthSection = "auth";
    public static final String AuthThresholdKey = "threshold";
    public static final String AuthExecKey = "exec";
    public static final String AuthTempKey = "temp";

    public static final String ReportSection = "report";
    public static final String ReportHttpKey = "http";
    public static final String ReportPingKey = "ping";

    public static final String LebSection = "leb";
    public static final String LebSuccessKey = "succ";

}
