/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author вадик
 */
public class DataModule {
    
    boolean active = false;
    List<Dataset> tables = null;
    
    private static DataModule instance = null;
    protected DataModule(){
    }
    
    public boolean isActive(){
        return active;
    }
    public static DataModule getInsatnce(){
        if (instance==null){
            instance = new DataModule();
        }
        return instance;
    }
    
    public Dataset getTable(String tableName){
        for (Dataset dataset:tables){
            if (dataset.getTableName().equals(tableName)){
                return dataset;
            }
        }
        return null;
    }
    
    public Dataset getTable(Integer index){
        return tables.get(index);
    }
    
    public String[] getTableNames(){
        Set<String> result = new HashSet<>();
        for (Dataset dataset:tables){
            result.add(dataset.tableName);
        }
        return result.toArray(new String[tables.size()]);
    }

//    public Iterable<Dataset> getTableNames() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
    class DataParser extends Parser{

        @Override
        public void addDataset(Dataset dataset) {
            System.out.println("table '"+dataset.getTableName()+"' OK");
            tables.add(dataset);
        }
        
    }
    
    public void open(){
//        tables = new ArrayList<>();
        URL url = DataModule.class.getResource("schedule.xml");
        if (url!=null){
            open(url.getPath());
        }
    }
    
    public void open(String path){
        tables = new ArrayList<>();
        try{
            Parser parser = new DataParser();
            parser.open(path);
            active = true;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void save(String path){
    }

    
    public Object lookUp(String tableName,String keyName,Object keyValue,String searchColumn) throws Exception{
        Dataset dataset = getTable(tableName);
        if (dataset!=null){
            return dataset.lookUp(keyName, keyValue, searchColumn);
        } else {
            new Exception("Dataset '"+tableName+"'not found");
        }
        return null;
    }
    
}
