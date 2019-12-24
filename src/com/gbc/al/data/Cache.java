/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.data;

/**
 *
 * @author tamvv
 */
public class Cache {
    private long id;
    private long finger_id;
    private String finger_code;
    private String finger_name;
    private String createDate;
    private byte status;
    private String lastUpdate;
    private long latetime;

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
     * @return the finger_id
     */
    public long getFinger_id() {
        return finger_id;
    }

    /**
     * @param user_id the finger_id to set
     */
    public void setFinger_id(long user_id) {
        this.finger_id = user_id;
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
     * @return the status
     */
    public byte getStatus() {
        return status;
    }

    /**
     * @param Status the status to set
     */
    public void setStatus(byte Status) {
        this.status = Status;
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

    /**
     * @return the finger_name
     */
    public String getFinger_name() {
        return finger_name;
    }

    /**
     * @param finger_name the finger_name to set
     */
    public void setFinger_name(String finger_name) {
        this.finger_name = finger_name;
    }

    /**
     * @return the finger_code
     */
    public String getFinger_code() {
        return finger_code;
    }

    /**
     * @param finger_code the finger_code to set
     */
    public void setFinger_code(String finger_code) {
        this.finger_code = finger_code;
    }

    /**
     * @return the latetime
     */
    public long getLatetime() {
        return latetime;
    }

    /**
     * @param latetime the latetime to set
     */
    public void setLatetime(long latetime) {
        this.latetime = latetime;
    }
    
}
