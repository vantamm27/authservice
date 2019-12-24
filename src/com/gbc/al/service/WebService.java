/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.service;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.controller.AuthController;
import com.gbc.al.controller.RegisterController;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

//import org.eclipse.jetty.util.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;



//import org.eclipse.jetty.websocket.server.WebSocketHandler;
//import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
/**
 *
 * @author tamvv
 */
public class WebService implements Runnable{
    private static final Logger logger = Logger.getLogger(WebService.class);
    private Server server = new Server();
    private static WebService _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    
    public static WebService getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new WebService();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    @Override
    public void run(){
        try{
    
            int http_port = Config.iniFile.getInt(AppConst.ServerSection, AppConst.ServerHttpPort_Key, 9000);
            int ws_port = Config.iniFile.getInt(AppConst.ServerSection, AppConst.ServerWebSocketPort_Key, 9696);
            
            //Integer.valueOf(Config.getParam("server", "ws_port"));

            logger.info("Thread MonitorService started at port " + Integer.toString(http_port));
            logger.info("Thread MonitorWebService started at port " + Integer.toString(ws_port));
            //System.out.println("Thread WebService started at port " + Integer.toString(http_port));
            //System.out.println("Thread ws started at port " + Integer.toString(ws_port));

            ServerConnector connector = new ServerConnector(server);
            connector.setPort(http_port);
            connector.setIdleTimeout(30000);

            ServerConnector connectorWS = new ServerConnector(server);
            connectorWS.setPort(ws_port);
            connectorWS.setIdleTimeout(30000);

            //server.setConnectors(new Connector[]{connector});
            server.setConnectors(new Connector[]{connector, connectorWS});

            ServletHandler servletHandler = new ServletHandler();            
            // nhan dang van tay 
            //servletHandler.addServletWithMapping(AuthController.class, "/api/v1/auth/*");            
            // dang ky 1 van tay 
            servletHandler.addServletWithMapping(RegisterController.class, "/api/v1/register/*"); 
            servletHandler.addServletWithMapping(AuthController.class, "/api/v1/auth/*"); 
            ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            
            servletContext.setContextPath("/");
            servletContext.setHandler(servletHandler);

          

            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setResourceBase("./static/");            
            ContextHandler resourceContext = new ContextHandler();
            resourceContext.setContextPath("/");
            resourceContext.setHandler(resource_handler);
            
            // Specify the Session ID Manager
            HashSessionIdManager idmanager = new HashSessionIdManager();
            server.setSessionIdManager(idmanager);          
                     
            
            
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceContext, resourceContext,  servletContext, new DefaultHandler()});
            //handlers.setHandlers(new Handler[]{sessions, resourceContext, servletContext, new DefaultHandler()});
            server.setHandler(handlers);
            server.start();
            server.join();

            System.out.println("Thread WebService stopped . . .");
        }catch(Exception e){
            logger.error("Cannot start WebService: " + e.getMessage());
            System.out.println("Thread WebService get exception: " + e.getMessage());
            System.exit(1);
        }
    }
    public void stop() throws Exception {
        server.stop();
    }
}
