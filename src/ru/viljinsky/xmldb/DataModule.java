/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.xmldb;

import ru.viljinsky.xmldb.Dataset;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;


/**
 *
 * @author вадик
 */
public class DataModule {
    
    public static String ERR_DATAMODULE_NOT_ACTIVE= "ДатаМодуль не открыт";
    public static String ERR_TABLE_NOT_FOUND = "Таблица \"%s\" не найдена";
    public static String ERR_FILR_EXISTS ="Файл \"%s\" уже существует";
    
    String dataName = "schedule.xml";
    
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
            throw new Exception (ERR_DATAMODULE_NOT_ACTIVE) ;
        
        for (Dataset dataset:tables)
            if (dataset.getTableName().equals(tableName)){
                return dataset;
            }
        
        throw new Exception(String.format(ERR_TABLE_NOT_FOUND,tableName));
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
    
//    /**
//     * Получение списка подчинённых таблиц для таблицы с именем_тfблицы
//     * @param tableName
//     * @return сисок табли Dataset[]
//     */
//    public Dataset[] getRefTables(String tableName){
//        List<Dataset> list = new ArrayList<>();
//        Dataset refDataset;
//        for (Dataset ds : tables){
//            if (ds.isReferences(tableName)){
//                refDataset = new Dataset(ds.tableName);
//                refDataset.columns=ds.columns;
//                refDataset.primary=ds.primary;
//                refDataset.lookupMap=ds.lookupMap;
//                refDataset.foreignMap=ds.foreignMap;
//                list.add(refDataset);
//            }
//        }
//        return list.toArray(new Dataset[list.size()]);
//    }
    
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
    public String[] getTableNames() throws Exception{
        if (!active){
            throw new Exception(ERR_DATAMODULE_NOT_ACTIVE);
        }
        Set<String> result = new HashSet() {};
        for (Dataset dataset:tables){
            result.add(dataset.tableName);
        }
        return result.toArray(new String[tables.size()]);
    }

    public void close() {
        tables.clear();
        active=false;
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
            Parser parser = new Parser();
            parser.listener = new IParserListener() {

                @Override
                public void addTable(Dataset dataset) {
                    tables.add(dataset);
                }
            };
            parser.open(path);
            dataName=path;
            active = true;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void save(String path) throws Exception{
        File file = new File(path);
        if (file.exists()){
            throw new Exception(String.format(ERR_FILR_EXISTS, path));
        }
        
        XMLExport xmlExport = new XMLExport();
        xmlExport.execute();
        xmlExport.save(path);
    }

    public String getDataName() {
        return dataName;
    }

}
