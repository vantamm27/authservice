/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.robot;

import com.gbc.al.common.AppConst;
import com.gbc.al.common.Config;
import com.gbc.al.common.MyUtils;
import com.gbc.al.data.Cache;
import com.gbc.al.data.Data;
import com.gbc.al.data.Session;
import com.gbc.al.data.Finger;
import com.gbc.al.model.CacheModel;
import com.gbc.al.model.FingerModel;
import com.gbc.al.model.LogModel;
import com.machinezoo.sourceafis.FingerprintTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Authentication;

/**
 *
 * @author tamvv
 */
public class R305 implements Runnable {

    private static String ResourceDir = Config.iniFile.getString(AppConst.ResourceSection, AppConst.ResourceDirKey, "./");
    private static String TempDir = Config.iniFile.getString(AppConst.AuthSection, AppConst.AuthTempKey, "/tmp/");
    private static String Exec = Config.iniFile.getString(AppConst.AuthSection, AppConst.AuthExecKey, "");
    private static String ReportHttp = Config.iniFile.getString(AppConst.ReportSection, AppConst.ReportHttpKey, "");
    private static String lebSucc = Config.iniFile.getString(AppConst.LebSection, AppConst.LebSuccessKey, "");

    private static boolean Register = false;
    private static Process Process = null;

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                try {
                    if (!Register) {
                        Auth();
                    }
                    count = 0;
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
                    count += 1;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
                    count += 1;
                }

                if (count > 5) {
                    System.exit(1);
                }
                Thread.sleep(1 * 1000);

            } catch (InterruptedException ex) {
                Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public static Finger Register(String name) throws Exception {
        Register = true;
        Finger user = new Finger();
        user.setName(name);
        if (Process != null) {
            Process.destroy();
        }
        Session session = ReadFinger();
        if (session.getError() == 0) {

            user.getName();
            user.setSource(session.getId() + ".bmp");
            if (!MyUtils.CopyFile(session.getPath(), ResourceDir + "/" + user.getSource())) {
                Register = false;
                Process = null;
                throw new Exception("copy resource error");

            }
            if (FingerModel.getInstance().Insert(user) != 0) {
                throw new Exception("insert to db error");
            }
            user.setSource(ResourceDir + "/" + user.getSource());

        } else {
            throw new Exception("read finger error");
        }

        Register = false;
        Process = null;

        return user;

    }

    public void Auth() throws Exception {

        // doc van tay
        Session session = ReadFinger();
        // doc thanh cong
        if (session.getError() == 0) {
            // doc file van tay
            byte[] data = Files.readAllBytes(Paths.get(session.getPath()));
            FingerprintTemplate probe = new FingerprintTemplate().create(data);
            // nhan dang van tay 
            Finger user = Data.getInstance().AuthFinger(probe);
            System.out.println(user.getName() + " " + user.getSource());
            if (user.getName() != null && user.getName().length() > 0) {
                // nhan dang thanh cong
                user.setSource(session.getPath());
                // sang leb
                leb(true); 
                //ghi log vao database
                LogModel.getInstance().insertLog(user);
                // post len servetr 
                process(user);
            } else {
                // nhan dang sai  do some thing 
                leb(false);
            }
        }

    }

    private static Session ReadFinger() throws IOException, Exception {
        System.out.println("ReadFinger");
        String dataDir = TempDir;
        String exec = Exec;
        System.out.println("ReadFinger" + " " + dataDir);
        System.out.println("ReadFinger" + " " + exec);
        Session session = new Session();
        session.setId(String.valueOf(System.currentTimeMillis()));
        session.setPath(String.format("%s/%s.bmp", dataDir, session.getId()));
        String cmd = "";
        cmd = exec + " " + session.getPath();
        System.out.println("ReadFinger" + " " + cmd);
        //cmd = "ping google.com";
        String result = Exec(cmd);
        System.out.println("ReadFinger" + " " + result);
        System.out.println("#####################");
        System.out.println(result);
        if (!result.startsWith("OK")) {
            System.out.println("error");
            System.out.println(result);
            System.out.println("failure");
            throw new Exception("read finger error: '" + result + "'");

        }
        System.out.println("Success");
        session.setError(0);
        return session;

    }

    private static String Exec(String cmd) throws IOException {
        String result = "";
        String s = null;

        // run the Unix "ps -ef" command
        // using the Runtime exec method:
        //Process p = Runtime.getRuntime().exec("ping google.com");
        Process = Runtime.getRuntime().exec(cmd);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(Process.getInputStream()));

        // read the output from the command        
        int count = 0;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(count + ": " + s);
            result += s + "\r\n";
            if (count > 10) {
                break;
            }
            count++;
        }
        return result;

    }

    void process(Finger user) {

        try {

            String url = ReportHttp + "?cm=report&user=" + user.getName() + "timestamp=" + String.valueOf(System.currentTimeMillis() / 1000);
            try {
                String result = MyUtils.HttpGetRequest(url);

                return;
            } catch (IOException ex) {
                Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
            }
            CacheModel.getInstance().Insert(user);

        } catch (IOException ex) {
            Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void leb(boolean status) {

        if (status) {
            try {

                Exec(lebSucc);
            } catch (IOException ex) {
                Logger.getLogger(R305.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

}
