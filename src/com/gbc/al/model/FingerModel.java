/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.model;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.common.MyUtils;
import com.gbc.al.data.Finger;
import com.gbc.al.database.MySqlFactory;
import com.machinezoo.sourceafis.FingerprintTemplate;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvv
 */
public class FingerModel {

    private static FingerModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private String ResourceDir = Config.iniFile.getString(AppConst.ResourceSection, AppConst.ResourceDirKey, "./");

    public static FingerModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new FingerModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    //load to cache
    public int LoadUser(List<Finger> listUser) {

        int result = -1;
        String sqlStr = "";
        Connection conn = null;
        PreparedStatement repStatement = null;
        ResultSet rs = null;
        sqlStr = String.format("select  id, code, name, source, createdate, lastupdate from finger");

        try {
            conn =  MySqlFactory.getConnection();
            repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                 logger.error(getClass().getSimpleName() + ".LoadUser: " + "connect to db error", null);
                return -1;
            }

            rs = repStatement.executeQuery();
            Finger u = null;
            while (rs.next()) {
                u = new Finger();
                u.setId(rs.getLong("id"));
                u.setCode(rs.getString("code"));
                u.setName(rs.getString("name"));
                u.setSource(ResourceDir + "/" + rs.getString("source"));
                u.setCreateDate(rs.getString("createdate"));
                u.setLastUpdate(rs.getString("lastupdate"));
                u.loadTemplate();
                listUser.add(u);
            }
            result = 0;

        } catch (SQLException ex) {
           logger.error(getClass().getSimpleName() + ".LoadUser: " + ex.getMessage(), ex);
            return -1;
        }finally{
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(repStatement);
            MySqlFactory.safeClose(conn);

        }

        return result;
    }

    //
    public int GetUser(List<Finger> listUser) {

        int result = -1;
        String sqlStr = "";
        Connection conn = null;
        PreparedStatement repStatement = null;
        ResultSet rs = null;
        sqlStr = String.format("select  id ,code, name, source, createdate, lastupdate from finger order by createdate desc;");

        try {
            conn =  MySqlFactory.getConnection();
            repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                 logger.error(getClass().getSimpleName() + ".GetUser: " + "connect to db error", null);
                return -1;
            }

            rs = repStatement.executeQuery();
            Finger u = null;
            while (rs.next()) {
                u = new Finger();
                u.setId(rs.getLong("id"));
                u.setCode(rs.getString("code"));
                u.setName(rs.getString("name"));
                u.setSource(ResourceDir + "/" + rs.getString("source"));
                u.setCreateDate(rs.getString("createdate"));
                u.setLastUpdate(rs.getString("lastupdate"));                
                listUser.add(u);
            }
            result = 0;

        } catch (SQLException ex) {
           logger.error(getClass().getSimpleName() + ".GetUser: " + ex.getMessage(), ex);
            return -1;
        }finally{
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(repStatement);
            MySqlFactory.safeClose(conn);

        }

        return result;
    }

    public int Insert(Finger user) {

        int result = -1;
        String sqlStr = "";
        Connection conn = null;
        PreparedStatement repStatement = null;
        ResultSet rs = null;
        sqlStr = String.format("insert into finger(name, code, source, createdate, lastupdate) values('%s','%s', '%s', now(), now() );", user.getName(), user.getCode(), user.getSource());
        try {
           conn =  MySqlFactory.getConnection();
            repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                System.out.println("connect to db error");
                logger.error(getClass().getSimpleName() + ".Insert: " + "connect to db error", null);
                return -1;
            }

            int count = repStatement.executeUpdate();
            if (count < 0) {
                logger.error(getClass().getSimpleName() + ".Insert: " + "insert row error", null);
                return -1;
            }
            rs = repStatement.getGeneratedKeys();

            if (rs.next()) {
                long id = rs.getLong(1);
                user.setId(id);
            }

            result = 0;

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".Insert: " + ex.getMessage(), ex);
            return -1;
        }finally{
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(repStatement);
            MySqlFactory.safeClose(conn);

        }

        return result;
    }

}
