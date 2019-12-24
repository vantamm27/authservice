/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.database;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import snaq.db.ConnectionPool;





public class MySqlFactory {
    private static final Logger logger = Logger.getLogger(MySqlFactory.class);
    private static final String MYSQL_HOST = Config.iniFile.getString(AppConst.MysqlSection, AppConst.MysqlHost_Key, "");
    private static final String MYSQL_PORT = Config.iniFile.getString(AppConst.MysqlSection, AppConst.MysqlPort_Key, "");
    private static final String MYSQL_NAME = Config.iniFile.getString(AppConst.MysqlSection, AppConst.MysqlDBName_Key, "");
    private static final String MYSQL_USER = Config.iniFile.getString(AppConst.MysqlSection, AppConst.MysqlUser_Key, "");
    private static final String MYSQL_PASS = Config.iniFile.getString(AppConst.MysqlSection, AppConst.MysqlPass_Key, "");
    
    //private static final ConnectionPool pool;
    private static ConnectionPool pool;
    private static final String connStr;
    
    static {
        connStr = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/" + MYSQL_NAME
                + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useUnicode=true&characterEncoding=UTF-8&noAccessToProcedureBodies=true";
        pool = new ConnectionPool("local", 5, 10, 30, 180, connStr, MYSQL_USER, MYSQL_PASS);
    }
    
    public static Connection getConnection() {

        if (pool != null) {
            try {
                return pool.getConnection(20);
            } catch (SQLException ex) {
                logger.error("getConnection : " + ex.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }
    
    public static void safeClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("safeClose.Connection:" + e.getMessage(), e);
            }
        }
    }
    
    public static void safeClose(ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                logger.error("safeClose.ResultSet:" + e.getMessage(), e);
            }
        }
    }
    
    public static void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("safeClose.Statement:" + e.getMessage(), e);
            }
        }
    }    
}
