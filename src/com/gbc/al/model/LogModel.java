/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.model;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.data.Finger;
import com.gbc.al.database.MySqlFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvv
 */
public class LogModel {

    private static LogModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static LogModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new LogModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int insertLog(Finger finger) {
        int result = -1;
        String sqlStr = "";
        Connection conn = null;
        PreparedStatement repStatement = null;
        ResultSet rs = null;
        sqlStr = String.format("insert into log(`finger_id`, `finger_code`,`finger_name`, `source`, `lastupdate`, `createdate`) values(%d, '%s','%s', '%s', now(), now());", finger.getId(),finger.getCode(), finger.getName(), finger.getSource());
        try {
            conn =  MySqlFactory.getConnection();
            repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                
                 logger.error(getClass().getSimpleName() + ".insertLog: " + "connect to db error", null);
                return -1;
            }

            int count = repStatement.executeUpdate();
            if (count < 0) {
              logger.error(getClass().getSimpleName() + ".insertLog: " + "insert row to db error", null);
                return -1;
            }
            result = 0;

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".insertLog: " + ex.getMessage(), ex);
            return -1;
        }finally{
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(repStatement);
            MySqlFactory.safeClose(conn);

        }

        return result;

    }

    public int getTopLog(List<Finger> fingers) {
        int result = -1;
        String sqlStr = "";        
        Connection conn = null;
        PreparedStatement repStatement = null;
        ResultSet rs = null;
        sqlStr = String.format("select `id`, `finger_id`, `finger_code`,`finger_name`, `source`, `lastupdate`, `createdate`"
                + " from fingerdb.log "
                + " order by createdate desc limit 100;");
        logger.info(sqlStr);
        try {            
            conn =  MySqlFactory.getConnection();
            repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                
                 logger.error(getClass().getSimpleName() + ".getopLog: " + "connect to db error", null);
                return -1;
            }
            rs = repStatement.executeQuery();
            
            Finger u = null;            
            while (rs.next()) {
                u = new Finger();
                u.setId(rs.getLong("finger_id"));
                u.setCode(rs.getString("finger_code"));
                u.setName(rs.getString("finger_name"));
                u.setSource(rs.getString("source"));
                u.setCreateDate(rs.getString("createdate"));
                u.setLastUpdate(rs.getString("lastupdate"));               
                fingers.add(u);              
            }
            result = 0;
            rs.close();
            repStatement.close();

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".getopLog: " + ex.getMessage(), ex);
            return -1;
        }finally{
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(repStatement);
            MySqlFactory.safeClose(conn);

        }

        return result;

    }

}
