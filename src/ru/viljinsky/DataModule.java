/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    
    /**
     * Получение корневой таблици по имени_таблицы
     * @param tableName
     * @return
     * @throws Exception 
     */
    public Dataset getTable(String tableName) throws Exception{
        if (!active)
            throw new Exception ("ERROR! DataModule not active") ;
        
        for (Dataset dataset:tables)
            if (dataset.getTableName().equals(tableName)){
                return dataset;
            }
        
        throw new Exception(String.format("Таблица \"%s\" не найдена",tableName));
    }
    /**
     * Получение отцильтрованной таблицы по имени_таблицы
     * @param tableName
     * @param filter Map<Integer,Object>
     * @return
     * @throws Exception 
     */
    public Dataset getTable(String tableName,Map<Integer,Object> filter) throws Exception{   
        Dataset result = new Dataset(tableName);
        Dataset dataset = getTable(tableName);
        result.columns=dataset.columns;
        result.primary= dataset.primary;
        result.foreignMap = dataset.foreignMap;
        result.lookupMap = dataset.lookupMap;
        Object[] rowset ;
        l1:for (int i=0;i<dataset.size();i++){
            rowset = dataset.get(i);
            boolean b ;
            for (int col:filter.keySet()){
                b = (rowset[col].equals(filter.get(col)));
                if (!b) continue l1;
            }
            result.add(rowset);
        }
        return result;
    }
    /**
     * Получение списка подчинённых таблиц для таблицы с именем_тfблицы
     * @param tableName
     * @return сисок табли Dataset[]
     */
    public Dataset[] getRefTables(String tableName){
//        Set<Dataset> list = new HashSet<>();
        java.util.List<Dataset> list = new ArrayList<>();
        Dataset refDataset;
        for (Dataset ds : tables){
            if (ds.isReferences(tableName)){
                refDataset = new Dataset(ds.tableName);
                refDataset.columns=ds.columns;
                refDataset.primary=ds.primary;
                refDataset.lookupMap=ds.lookupMap;
                refDataset.foreignMap=ds.foreignMap;
                list.add(refDataset);
            }
        }
        return list.toArray(new Dataset[list.size()]);
    }
    
    public Dataset getTable(Integer index){
        if (index>=0)
            return tables.get(index);
        return null;
    }
    
    /**
     * Количество открытых датасетов
     * @return 
     */
    public Integer getTableCount(){
        return tables.size();
    }
    
    /**
     * Список коренвых датасетов
     * @return 
     */
    public Dataset[] getTables(){
        return tables.toArray(new Dataset[tables.size()]);
    }
    
    /**
     * Список имён корневых датасетов
     * @return 
     */
    public String[] getTableNames(){
        Set<String> result = new HashSet<>();
        for (Dataset dataset:tables){
            result.add(dataset.tableName);
        }
        return result.toArray(new String[tables.size()]);
    }

    public void close() {
        tables.clear();
        active=false;
    }
    
    class DataParser extends Parser{

        @Override
        public void addDataset(Dataset dataset) {
            tables.add(dataset);
        }
        
    }
    
    public void open(){
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
    
    public void save(String path) throws Exception{
        File file = new File(path);
        if (file.exists()){
            throw new Exception("Файл \""+path+"\"существует");
        }
        
        XMLExport xmlExport = new XMLExport();
        xmlExport.execute();
        xmlExport.save(path);
    }

    @Deprecated
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
