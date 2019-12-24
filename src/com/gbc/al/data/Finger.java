/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.data;

import com.machinezoo.sourceafis.FingerprintTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tamvv
 */
public class Finger {
    private long id = -1l;
    private String code;
    private String name;
    private String source;
    private transient FingerprintTemplate fgt = null;
    private String createDate;
    private String lastUpdate;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the fgt
     */
    public FingerprintTemplate getFgt() {
        return fgt;
    }

    /**
     * @param fgt the fgt to set
     */
    public void setFgt(FingerprintTemplate fgt) {
        this.fgt = fgt;
        
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public  boolean loadTemplate(){
        try {
            
            byte[] data = Files.readAllBytes(Paths.get(this.source));
           this.fgt = new FingerprintTemplate().create(data);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Finger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
                
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
}
