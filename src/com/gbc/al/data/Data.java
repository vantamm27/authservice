/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.data;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.common.MyUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.gbc.al.model.FingerModel;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import java.util.logging.Level;

/**
 *
 * @author tamvv
 */
public class Data {

    private static Data _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static List<Finger> g_user = new ArrayList();
     private double threshold = Config.iniFile.getDouble(AppConst.AuthSection, AppConst.AuthThresholdKey, 50);
    private String ResourceDir = Config.iniFile.getString(AppConst.ResourceSection, AppConst.ResourceDirKey, "./");


    public static Data getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new Data();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int Init() {
        int result = -1;

        result = LoadUser();
        if (result < 0) {
            return result;
        }
        return result;

    }    
    
    public int LoadUser() {
        try {
            FingerModel.getInstance().LoadUser(g_user);           

            return 0;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public int AddUserToCache(Finger user ) {       
        
       
        g_user.add(user);
        return 0;
       
    }

    // co the chinh lai tra ve 1 list user
    // Nhận dạng vân tay 
    // cấu hình nguong threshold
    // trả về kết quả trùng khop nhất trên ngưỡng
    public Finger AuthFinger(FingerprintTemplate probe) {
        Finger user = new Finger();
        FingerprintMatcher matcher = new FingerprintMatcher().index(probe);
        double score = 0;
        double temp =0;
        for (Finger u : g_user) {
           temp = matcher.match(u.getFgt());
            System.out.println(u.getSource() + " " + temp);
            if (temp > score && temp > threshold) {
                user.setId(u.getId());
                user.setCode(u.getCode());
                user.setName(u.getName());
                user.setLastUpdate(u.getLastUpdate());
                user.setCreateDate(u.getCreateDate());
                score =  temp;
            }
        }
        return user;

    }

}
