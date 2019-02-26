/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.controller;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.common.MyUtils;
import com.gbc.al.data.Data;
import com.gbc.al.data.Finger;
import com.gbc.al.model.CommonModel;
import com.gbc.al.model.FingerModel;
import com.gbc.al.robot.R305;
import com.google.api.client.util.DateTime;
import com.machinezoo.sourceafis.FingerprintTemplate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvv
 */
public class RegisterController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterController.class);
    private String ResourceDir = Config.iniFile.getString(AppConst.ResourceSection, AppConst.ResourceDirKey, "./");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            processs(req, resp);
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".handle: " + ex.getMessage(), ex);
        }
    }

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String content = "";
        pathInfo = pathInfo.toLowerCase();
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);

        switch (cmd) {
            case "finger":
                content = RegisterFinger(req);
                break;
            default:
                content = CommonModel.FormatResponse(-1, "Invalid command");
        }
        CommonModel.out(content, resp);
    }

    private String RegisterFinger(HttpServletRequest req) {
        try {
            //read data request
            String name = req.getParameter("name");
            if (name == null || name.length() <= 0) {
                return CommonModel.FormatResponse(0, "invalid parameter name", "");
            }
            String src = String.valueOf(name + "_" + System.nanoTime() + ".bmp");
            Finger newUser = R305.Register(name);
            
             byte[] data = Files.readAllBytes(Paths.get(newUser.getSource()));
                FingerprintTemplate probe = new FingerprintTemplate().create(data);               
                System.out.println(newUser.getName() + " " + newUser.getSource());
            FingerprintTemplate fgt = new FingerprintTemplate().create(data);
            newUser.setFgt(fgt);
            Data.getInstance().AddUserToCache(newUser);
            return CommonModel.FormatResponse(0, "success", newUser);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return CommonModel.FormatResponse(-1, "falure");

    }

    /*
    private String RegisterFinger(HttpServletRequest req){        
         try {
             //read data request
             String name = req.getParameter("name");
             if (name ==null ||name.length() <=0 ){
                 return CommonModel.FormatResponse(0, "invalid parameter name", "");
             }
             String src = String.valueOf(name + "_"+System.nanoTime()+".bmp");
             Finger newUser = new Finger();
             newUser.setName(name);
             newUser.setSource(src);
            
             

             byte[] data =  MyUtils.GetReqData(req.getInputStream());
             
            if (!MyUtils.WriteToFile(ResourceDir + "/"+src, data)){
                return CommonModel.FormatResponse(-1, "save data error");
            }
             
            if (FingerModel.getInstance().Insert(newUser) != 0){                
                             return CommonModel.FormatResponse(-1,"failure","");
            }
            FingerprintTemplate fgt =new FingerprintTemplate().create(data);
            newUser.setFgt(fgt);
            
             Data.getInstance().AddUserToCache(newUser);
            

             return CommonModel.FormatResponse(0, "success", newUser);
         } catch (IOException ex) {
             java.util.logging.Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        return CommonModel.FormatResponse(-1, "falure");
        
    }
     */
}
