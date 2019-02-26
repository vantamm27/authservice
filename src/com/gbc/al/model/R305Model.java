/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.model;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvv
 */
public class R305Model {
    private static R305Model _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private String ResourceDir = Config.iniFile.getString(AppConst.ResourceSection, AppConst.ResourceDirKey, "./");
    private String Exec = Config.iniFile.getString(AppConst.AuthSection, AppConst.AuthExecKey, "");


    public static R305Model getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new R305Model();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    

}
