/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.al.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.DefaultConfigurationNode;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.Iterator;


public class IniFile {
    private HierarchicalINIConfiguration HINIconf = null;
    public IniFile(String file) throws ConfigurationException {
        init(file);
    }
    public void init(String file) throws ConfigurationException {
            HINIconf = new HierarchicalINIConfiguration(file);
    }
    
    public String getString(String section, String key, String strDefault){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        return sc.getString(key, strDefault);
    }
    public int getInt(String section, String key, int nDefault){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        return sc.getInt(key, nDefault);
    }

    public long getLong(String section, String key, long lDefault){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        return sc.getLong(key, lDefault);
    }
    public boolean getBool(String section, String key, boolean bDefault){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        return sc.getBoolean(key, bDefault);
    }
    public double getDouble(String section, String key, double dDefault){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        return sc.getDouble(key, dDefault);
    }
    public float getFloat(String section, String key, float fDefault){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        return sc.getFloat(key, fDefault);
    }
    public boolean setParam(String section, String key, Object value){
        SubnodeConfiguration sc = HINIconf.getSection(section);
        if (!sc.isEmpty()){
            sc.setProperty(key, value);
        }else{
            Set<ConfigurationNode> nodes = new HashSet<ConfigurationNode>();
            ConfigurationNode cn = new DefaultConfigurationNode();
            cn.setName(key);
            cn.setValue(value);
            nodes.add(cn);
            HINIconf.addNodes(section, nodes);
        }
        return true;
    }
    public Set<String> getSections(){
        try{
            return HINIconf.getSections();
        }catch(Throwable e){
            return Collections.emptySet(); 
        }
    }
    public Iterator<String> getKeys(String section){
        try{
            SubnodeConfiguration sc = HINIconf.getSection(section);
            return sc.getKeys();
            /*
            Iterator<String> it = sc.getKeys();
            while (it.hasNext()){ 
                String key = (it.next());  
                Object value =subConf.getString(key);
            }
            */
        }catch(Throwable e){
            return Collections.emptyIterator(); 
        }
    }
    public boolean save(){
        try{
            HINIconf.save();
            return true;
        }catch(Throwable e){
            return false;
        }
    }
    public boolean save(String filename){
        try{
            HINIconf.save(filename);
            return true;
        }catch(Throwable e){
            return false;
        }
    }

}
