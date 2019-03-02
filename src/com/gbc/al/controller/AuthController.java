/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.controller;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.MyUtils;
import com.gbc.al.data.Data;
import com.gbc.al.data.Finger;
import com.gbc.al.model.CommonModel;

import com.machinezoo.sourceafis.FingerprintTemplate;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.log4j.Logger;
import org.apache.sanselan.util.IOUtils;

/**
 *
 * @author tamvv
 */
public class AuthController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AuthController.class);

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
                content = AuthFinger(req);
                break;
            default:
                content = CommonModel.FormatResponse(-1, "Invalid command");
        }
        CommonModel.out(content, resp);
    }

    private String AuthFinger(HttpServletRequest req) {
        try {
            //read data request

            byte[] data = MyUtils.GetReqData(req.getInputStream());
            //data = Files.readAllBytes(Paths.get("/media/tamvv/Data1/Downloads/database/5/1.bmp"));
            FingerprintTemplate probe = new FingerprintTemplate().create(data);

            Finger user = Data.getInstance().AuthFinger(probe);
            // do something

            return CommonModel.FormatResponse(0, "success", user);
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".AuthFinger: " + ex.getMessage(), ex);
        }

        return CommonModel.FormatResponse(-1, "falure");

    }

}
