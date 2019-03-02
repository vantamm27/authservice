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
        Connection conn = MySqlFactory.getConnection();
        ResultSet rs;
        sqlStr = String.format("insert into log(`finger_id`,`finger_name`, `source`) values(%d, '%s', '%s');", finger.getId(), finger.getName(), finger.getSource());
        try {
            PreparedStatement repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

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
        }

        return result;

    }

}
