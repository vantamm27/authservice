/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.common;

import com.gbc.al.controller.AuthController;
import com.gbc.al.data.NetInfo;
import static com.google.common.net.HttpHeaders.USER_AGENT;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletInputStream;

/**
 *
 * @author tamvv
 */
public class MyUtils {

    public static byte[] GetReqData(ServletInputStream inputStream) {

        if (inputStream == null) {
            System.out.println("input stream is null");
            return null;
        }

        byte[] buff = new byte[512];
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        try {
            System.out.println(inputStream.available());
            System.out.println(inputStream.isReady());
            int reviceLen = inputStream.read(buff);
            while (reviceLen > 0) {
                data.write(buff);
                reviceLen = inputStream.read(buff);
            }

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data.toByteArray();

    }

    public static boolean WriteToFile(String path, byte[] data) {

        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            return true;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public static String Exec(String cmd) throws IOException {
        String result = "";
        String s = null;

        // run the Unix "ps -ef" command
        // using the Runtime exec method:
        //Process p = Runtime.getRuntime().exec("ping google.com");
        Process p = Runtime.getRuntime().exec(cmd);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
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

    public static boolean CopyFile(String source, String dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        boolean result = false;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            result = true;
        } finally {
            input.close();
            output.close();
        }
        return result;
    }

    public static String HttpGetRequest(String url) throws MalformedURLException, IOException {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + con.getResponseCode());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();

    }

    public static String DateTime2Timestamp(String dateTime) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(dateTime);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return String.valueOf(timestamp.getTime() / 1000);
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of excption 
        }

        return "";

    }

    public static List<NetInfo> GetAllNetworkInfo() {
        List<NetInfo> netInfos = new ArrayList<NetInfo>();
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            NetInfo ni= null;
            while (e.hasMoreElements()) {

                NetworkInterface n = (NetworkInterface) e.nextElement();

                byte[] mac = n.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                ni.setMac(sb.toString());

                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    
                    ni  = new NetInfo();
                    ni.setIp(i.getHostAddress());
                    ni.setMac(sb.toString());
                    netInfos.add(ni);
                    continue;
                }

            }

        } catch (SocketException ex) {
            Logger.getLogger(MyUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return netInfos;
    }

}


