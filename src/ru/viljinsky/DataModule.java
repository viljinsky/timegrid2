/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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
    
    class DataParser extends Parser{

        @Override
        public void addDataset(Dataset dataset) {
            System.out.println("table '"+dataset.getTableName()+"' OK");
            tables.add(dataset);
        }
        
    }
    
    public void open(){
        tables = new ArrayList<>();
        URL url = DataModule.class.getResource("schedule.xml");
        if (url!=null){
            try{
                Parser parser = new DataParser();
                parser.open(url.getPath());
                active = true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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
