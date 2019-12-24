/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.model;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.data.Cache;
import com.gbc.al.data.Finger;
import com.gbc.al.database.MySqlFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvv
 */
public class CacheModel {

    private static CacheModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private String ResourceDir = Config.iniFile.getString(AppConst.ResourceSection, AppConst.ResourceDirKey, "./");

    public static CacheModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new CacheModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int GetListCache(List<Cache> listCache) {

        int result = -1;
        String sqlStr = "";
        Connection conn = MySqlFactory.getConnection();
        ResultSet rs;
        sqlStr = String.format("select id, finger_code, finger_id, finger_name, status, createdate, lastupdate, TIMESTAMPDIFF(SECOND, createdate, now()) as latetime from cache Where status=1;");

        try {
            PreparedStatement repStatement = conn.prepareStatement(sqlStr);

            if (repStatement == null) {
                System.out.println("connect to db error");
                return -1;
            }

            rs = repStatement.executeQuery();
            Cache c = null;
            while (rs.next()) {
                c = new Cache();
                c.setId(rs.getLong("id"));
                c.setFinger_code(rs.getString("finger_code"));
                c.setFinger_id(rs.getLong("finger_id"));
                c.setFinger_name(rs.getString("finger_name"));
                c.setStatus(rs.getByte("status"));
                c.setCreateDate(rs.getString("createdate"));
                c.setLastUpdate(rs.getString("lastupdate"));
                c.setLatetime(rs.getLong("latetime"));
                listCache.add(c);
            }
            result = 0;

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".GetListCache: " + ex.getMessage(), ex);
            return -1;
        }

        return result;

    }

    public int Insert(Finger finger) {

        int result = -1;
        String sqlStr = "";
        Connection conn = MySqlFactory.getConnection();
        ResultSet rs;
        sqlStr = String.format("insert into cache(`finger_id`,`finger_code`, `finger_name`, `status`, `createdate`, `lastupdate`) values(%d, '%s', '%s',%d, now(), now());", finger.getId(), finger.getCode(), finger.getName(), 1);
        logger.info(sqlStr);
        try {
            PreparedStatement repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                System.out.println("connect to db error");
                return -1;
            }

            int count = repStatement.executeUpdate();
            if (count < 0) {
                System.out.println("insert error");
                return -1;
            }
            result = 0;

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".Insert: " + ex.getMessage(), ex);
            return -1;
        }

        return result;
    }

    public int UpdateStatus(Cache cache) {

        int result = -1;
        String sqlStr = "";
        Connection conn = MySqlFactory.getConnection();
        ResultSet rs;
        sqlStr = String.format("UPDATE `cache` SET `status`=%d, `lastupdate`=now() WHERE `id`= %d;", cache.getStatus(), cache.getId());
        try {
            PreparedStatement repStatement = conn.prepareStatement(sqlStr);

            if (repStatement == null) {
                System.out.println("connect to db error");
                return -1;
            }

            int count = repStatement.executeUpdate();
            if (count < 0) {
                System.out.println("insert error");
                return -1;
            }
            result = 0;

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".UpdateStatus: " + ex.getMessage(), ex);
            return -1;
        }

        return result;
    }
}
