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

    public int LoadUser(List<Finger> listUser) {
        
        if ( false){
            
            for (int i =1; i <= 10;i++){
                for (int j =1; j<= 20 ;j++){
                    try {
                        Finger u = new  Finger();
                        u.setId(i+j);
                        u.setName("["+String.valueOf(i) +"," + String.valueOf(j)+"]");
                        u.setSource("/media/tamvv/Data1/Downloads/database/"+ String.valueOf(i)+"/"+String.valueOf(j)+".bmp");
                        u.setCreateDate("2018-12-30");
                        byte[] image = Files.readAllBytes(Paths.get(u.getSource()));
                        FingerprintTemplate fgt = new FingerprintTemplate().create(image);
                        u.setFgt(fgt);
                        System.out.println(u.getSource());
                        
                        listUser.add(u);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(FingerModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            return 0;
        }
            
            
    
        

        int result = -1;
        String sqlStr = "";
        Connection conn = MySqlFactory.getConnection();
        ResultSet rs;
        sqlStr = String.format("select  id, name, source, createdate, lastupdate from finger");

        try {
            PreparedStatement repStatement = conn.prepareStatement(sqlStr);

            if (repStatement == null) {
                System.out.println("connect to db error");
                return -1;
            }

            rs = repStatement.executeQuery();
            Finger u = null;
            while (rs.next()) {
                u = new Finger();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setSource(ResourceDir + "/"+ rs.getString("source"));
                u.setCreateDate(rs.getString("createdate"));
                u.setLastUpdate(rs.getString("lastupdate"));
                u.loadTemplate();
                listUser.add(u);
            }
            result = 0;

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FingerModel.class.getName()).log(Level.SEVERE, null, ex);
            return  -1;
        }

        return result;
    }
    
    public int Insert( Finger user) {       
       
        int result = -1;
        String sqlStr = "";
        Connection conn = MySqlFactory.getConnection();
        ResultSet rs;
        sqlStr = String.format("insert into finger(name, source) values('%s', '%s' );", user.getName(), user.getSource());
        try {
            PreparedStatement repStatement = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

            if (repStatement == null) {
                System.out.println("connect to db error");
                return -1;
            }
            
            int count =  repStatement.executeUpdate();
            if (count < 0){
                 System.out.println("insert error" );
                return -1;
            }
            rs =  repStatement.getGeneratedKeys();
            
            if(rs.next())
                {
                    long id = rs.getLong(1);
           user.setId(id);
                }
           
            result = 0;

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FingerModel.class.getName()).log(Level.SEVERE, null, ex);
            return  -1;
        }

        return result;
    }
    
   
    
    

}
