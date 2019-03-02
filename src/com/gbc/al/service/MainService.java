/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.service;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.data.Data;

import com.gbc.al.model.CommonModel;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.gbc.al.model.FingerModel;
import com.gbc.al.robot.Cache;
import com.gbc.al.robot.R305;

/**
 *
 * @author tamvv
 */
public class MainService {

    private static final Logger logger = Logger.getLogger(MainService.class);
    private static WebService webService = null;
    private static R305 r305Service = null;
    private static Cache cacheService = null;

    public static void main(String[] args) {
        System.out.println("################");
        try {

            Config.init(AppConst.Config_File);
            System.out.println("1");
            Data.getInstance().Init();
            System.out.println("2");

            webService = WebService.getInstance();
            new Thread(webService).start();
            System.out.println("3");
            r305Service = new R305();
            Thread r305handler = new Thread(r305Service);
            r305handler.start();

            cacheService = new Cache();
            Thread cachehandler = new Thread(cacheService);
            cachehandler.start();

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        logger.info("Shutdown thread before webservice getinstance");
        // http server auth, register
                        if (webService != null) {
                            webService.stop();
                        }
        // doc van tay tu may cham cong
                        if (r305handler != null) {
                            r305handler.stop();
                        }
        // dong bo cache khi mang chap chon
        
                        if (cachehandler != null) {
                            cachehandler.stop();
                        }

                    } catch (Exception e) {
                    }
                }
            }, "Stop Jetty Hook"));

        } catch (Throwable e) {
            String msg = "Exception encountered during startup.";
            logger.error(msg, e);
            System.out.println(msg);
            logger.error("Uncaught exception: " + e.getMessage(), e);
            System.out.println(e.getMessage());
            System.exit(3);
        }

    }
}