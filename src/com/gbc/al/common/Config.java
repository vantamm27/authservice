/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.common;
import com.gbc.al.common.AppConst;
import com.gbc.al.common.IniFile;
import org.apache.commons.configuration.ConfigurationException;
/**
 *
 * @author thanhht2
 */
public class Config {
    public static IniFile iniFile = null;
    
    public static void init(String file) throws ConfigurationException {
        String confFile = System.getProperty("configuration");
        if (confFile == null) {
            confFile = file;
        }
        iniFile = new IniFile(confFile);
    }
}
