/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.common;


public class ErrorObj {
    private String msg = "";
    private int err = 0;
    
    public ErrorObj(int err_, String msg_){
        err = err_;
        msg = msg_;
    }
    
    public int getErrorNo(){
        return err;
    }
    
    public void setErrorNo(int err_){
        err = err_;
    }
    
    public String getErrorMsg(){
        return msg;
    }
    
    public void setErrorMsg(String msg_){
        msg = msg_;
    }
    
    public void setError(int err_, String msg_){
        err = err_;
        msg = msg_;
    }
}