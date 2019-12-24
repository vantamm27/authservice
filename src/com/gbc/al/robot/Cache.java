/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.robot;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.common.MyUtils;
import com.gbc.al.model.CacheModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.gbc.al.data.NetInfo;

/**
 *
 * @author tamvv
 */
public class Cache implements Runnable {

    private static String ReportHttp = Config.iniFile.getString(AppConst.ReportSection, AppConst.ReportHttpKey, "");
    private static String PingHttp = Config.iniFile.getString(AppConst.ReportSection, AppConst.ReportPingKey, "");

    @Override
    public void run() {
        int count = 0;
        
        while (true) {
            try {
                process();  
                ping();                
                Thread.sleep( 5*60* 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void process() {

        try {
            List<com.gbc.al.data.Cache> caches = new ArrayList<com.gbc.al.data.Cache>();

            CacheModel.getInstance().GetListCache(caches);

            for (com.gbc.al.data.Cache cache : caches) {
                if (cache.getStatus() == 1) {
                    //String url = ReportHttp + "?cm=report&user=" + cache.getFinger_name() + "&timestamp=" + MyUtils.DateTime2Timestamp(cache.getCreateDate());
                    String url = ReportHttp + "?id=" + cache.getFinger_code() + "&latetime="+Long.toString(cache.getLatetime()) ;                    
                    try {
                        String result = MyUtils.HttpGetRequest(url);
                        // do something
                        cache.setStatus((byte) 0);
                        CacheModel.getInstance().UpdateStatus(cache);
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void ping() {
        List<com.gbc.al.data.NetInfo> netinfos = MyUtils.GetAllNetworkInfo();

        netinfos.forEach((n) -> {
            try {
                String url = PingHttp + "ip=" + n.getIp() + "&mac=" + n.getMac();
                String result = MyUtils.HttpGetRequest(url);
            } catch (IOException ex) {
                Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
