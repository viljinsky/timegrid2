/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.xmldb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;

/**
 *
 * @author вадик
 */

class DatasetException extends Exception{
};
        
abstract class AbstractDataset extends ArrayList<Object[]>{
    public static final String ERR_NO_RECORD_SELECTED="Ошибка: Нет активной записи (index<0)";
    DataModule dm = DataModule.getInsatnce();
    
    public abstract Integer append(Map<String,Object> map) throws Exception;
    public abstract Integer insert(Map<String,Object> map) throws Exception;
    public abstract void edit(Map<String,Object> map) throws Exception;
    public abstract boolean delete() throws Exception;
    
    public abstract Map<String,Object> getValues();
}

public class Dataset extends AbstractDataset{
    String tableName ;
    
    Set<String> primary;
    Set<String> unique;
    Map<Integer,String> columns;
    Map<String,String> lookupMap;
    Map<String,String> foreignMap;

    public Integer index=-1;
    
    public Map getForeignMap(){
        return foreignMap;
    }
    
    
    public Dataset(String tableName){
        this.tableName=tableName;
        columns = new HashMap<>();
        lookupMap = new HashMap<>();
        primary = new HashSet<>();
        foreignMap = new HashMap<>();
        unique = new HashSet<>();
    }

    public void setIndex(Integer index) {
        this.index= index;
    }

    public Integer getIndex() {
        return index;
    }
    
    public String[] getPrimary(){
        return primary.toArray(new String[primary.size()]);
    }

    public String getTableName(){
        return tableName;
    }
    
    public void first(){
        index = 0;
    }
    
    public void next(){
        if (index<size()){
            index+=1;
        }
    }
    
    public void prior(){
        if (index>0){
            index-=1;
        }
    }
    
    public void last(){
        index = size()-1;
    }
    
    public boolean eof(){
        return index>=size();
    }
    
    public boolean bof(){
        return index<=0;
    }
    
    public Object getValue(String columnName) throws Exception{
        Object[] rowset = get(index);
        int n= getColumnIndex(columnName);
        return rowset[n];
    }
    
    public Integer getInteger(String columnName) throws Exception{
        Object value = getValue(columnName);
        if (value!=null){
            return Integer.parseInt((String)value);
        }
        return null;
    }
    
    public String getString(String columnName) throws Exception {
        Object value = getValue(columnName);
        return value.toString();
    }

    public int getColumnIndex(String columnName) throws Exception{
        for (int n:columns.keySet())
            if (columns.get(n).equals(columnName))
                return n;
        throw new Exception(String.format("Поле \"%s\" в датасете \"%s\" не найдено",columnName,tableName));
    }

    public Integer addColumn(String columnName){
        int i = 0,n;
        for (Iterator it=columns.keySet().iterator();it.hasNext();){
            n=(Integer)it.next();
            if (n>=i) i= n+1;
        }
        columns.put(i, columnName);
        return i;
    }

    public String[] getColumns(){
        String[] result = new String[columns.size()];
        for (Iterator<Integer> it=columns.keySet().iterator();it.hasNext();){
            int n= it.next();
            result[n]=columns.get(n);
        }
        return result;
    }

    public void addEmptyRecord(){
        Object[] recordset = new Object[columns.size()];
        super.add(recordset);
        index = this.indexOf(recordset);
    }

    public boolean columnExists(String columnName){
        for (int i:columns.keySet()){
            if (columns.get(i).equals(columnName)){
                return true;
            }
        }
        return false;
    }
    public void addRecord(Attributes attr){
        String columnName ;
        for (int i=0;i<attr.getLength();i++){
            columnName = attr.getQName(i);
            if (!columnExists(columnName))
                addColumn(columnName);
        }

        Object[] record = new Object[columns.size()];
        for (int i=0;i<record.length;i++){
            record[i]=attr.getValue(i);
        }
        super.add(record);

    }
    
    public int getColumnCount() {
        return columns.size();
    }
    
    public Integer getRowCount(){
        return this.size();
    }
    
    public String getColumnName(Integer columnIndex){
        return columns.get(columnIndex);
    }
    
    public Object[] getRowset(Integer rowIndex){
        return this.get(rowIndex);
    }

    /**
     * Получить карту значение текущей записи <имя_поля> <значение>
     * @return 
     */
    @Override
    public Map<String,Object> getValues(){
        Map<String,Object> result = new HashMap<>();
        Object[] rowset = this.get(index);
        for (int col:columns.keySet()){
            result.put(columns.get(col),rowset[col]);
        }
        return result;
    }    
    
    
    @Deprecated
    public void open() throws Exception{
        Dataset ds = dm.getTable(tableName);
        this.clear();
        this.addAll(ds);
        this.columns=ds.columns;
        this.primary=ds.primary;
        this.lookupMap=ds.lookupMap;
    }
    
    /**
     * Открыть датасет с фильтом
     * 
     * @param map номер_поля-значение
     * @throws Exception 
     */
    public void open(Map<Integer,Object> map) throws Exception{
                
        Dataset ds = dm.getTable(tableName);
        this.columns=ds.columns;
        this.primary=ds.primary;
        this.lookupMap=ds.lookupMap;
        this.unique=ds.unique;
        
        
        this.clear();
        boolean b;
        for (Object[] rowset:ds){
            
            b=true;
            for (int n:map.keySet()){
                if (!rowset[n].equals(map.get(n))){
                    b=false;
                    break;
                }
            }
            if (b)    
                this.add(rowset);
        }
        
    }
    
    //--------------------- E D I T I N G -------------------------------------- 
    public void testUnique(Object[] rowset) throws Exception{
        Map<Integer,Object> keys = new HashMap<>();
        if (unique.size()==0) return;
        for (String s:unique){
            keys.put(getColumnIndex(s), rowset[getColumnIndex(s)]);
        }
        for (Object[] r:this){
            boolean b = false;
            for (int k:keys.keySet()){
                b = (!r[k].equals(keys.get(k)));
                if (b==true)  break;
                
            }
            if (b==false)
                throw new Exception("Нарушена уникальность записей");
        }

    }
    
    /**
     * Проверка уникальности  primary key записи
     * также элементы primary key должны иметь значение не null
     * 
     * @param rowset
     * @throws Exception 
     */
    public void testPrimary(Object[] rowset) throws Exception{
        
        Map<Integer,Object> keys = new HashMap<>();
        Object value;
        Integer columnIndex;
        
        for (String columnName :getPrimary()){
            columnIndex= getColumnIndex(columnName);
            value = rowset[columnIndex];
            if (value==null){
                throw new Exception("Поле \""+columnName+"\" - не может быть иметь значение \"null\"");
            }
            keys.put(columnIndex, value);
        }
        
        for (Object[] r:this){
            boolean b = true;
            if (index>=0 && r==get(index)){
                continue;
            }
            for (int col:keys.keySet()){
                b = r[col].equals(keys.get(col));
                if (!b) break;
            }
            if (b)
                throw new Exception("Нарушение примари_кей");
        }
    }
    
     /**
     *  Изменение текущей записи
     */
     
    @Override
    public void edit(Map<String, Object> values) throws Exception{
        Object[] rowset = new Object[getColumnCount()];
        for (int col:columns.keySet()){
            rowset[col]=values.get(columns.get(col));
        }
        testPrimary(rowset);
        testUnique(rowset);
        Object[] r = this.get(index);
        for (int i=0;i<r.length;i++){
            r[i]= rowset[i];
        }
    }
    
    @Override
    public Integer insert(Map<String,Object> values) throws Exception{
        if (index<0){
            throw new Exception(ERR_NO_RECORD_SELECTED);
        }
        int row = index;
        index=-1;
        Object[] rowset = new Object[getColumnCount()];
        for (String key:values.keySet()){
            rowset[getColumnIndex(key)]=values.get(key);
        }
        testPrimary(rowset);
        testUnique(rowset);
        add(row, rowset);
        // post
        if (this!=dm.getTable(tableName))
            dm.getTable(tableName).add(rowset);
        index = row;
        return index;
    }
    
    /**
     * Добавление новой записи
     * @param values  Map<String,Object> values
     * @throws Exception 
     */
    @Override
    public Integer append(Map<String,Object> values) throws Exception {
        Object[] rowset = new Object[getColumnCount()];
        for (String columnName : values.keySet()){
            rowset[getColumnIndex(columnName)]=values.get(columnName);
        }
        testPrimary(rowset);
        testUnique(rowset);
        this.add(rowset);
        if (this!=dm.getTable(tableName))
            dm.getTable(tableName).add(rowset);
        index = indexOf(rowset);
        return index;
    }


    /**
     * Удаление текущей записи
     * @return 
     */
    @Override
    public boolean delete() throws Exception{
        if (index<0)
            throw new Exception(ERR_NO_RECORD_SELECTED);
        Object[] recordset = get(index);
        this.remove(recordset);
        if (index>=this.size())
            index=this.size()-1;
        return true;
    }

    //--------------------- L O O K U P ----------------------------------------
    
    public boolean isLookup(String columnName){
        return lookupMap.get(columnName)!=null;
    }
    
    public Object getLookupValue(String columnName) throws Exception{
        Map lookup = getLookup(columnName);
        if (lookup!=null)
            return lookup.get(getValue(columnName));
        throw new Exception(String.format("Поле \"%s\" не является лукап полем датасета \"%s\"",columnName,tableName));
    }
    /**
     * Получене списка ключ-значение  для лукап-поля
     * @param columnName
     * @return  null если поле не лукап
     */
    public Map<Object,Object> getLookup(String columnName){
        String lookup;
        String t1,t2,t3;
        String[] f;
//        DataModule dm = DataModule.getInsatnce();
        Dataset ds;
        Map<Object,Object> map = new HashMap<>();
        String[] primary;
        
        lookup=lookupMap.get(columnName);
        if (lookup!=null){
            f=lookup.split(";");
            t1=f[0].split("\\.")[0];
            t2=f[0].split("\\.")[1];
            try{
                ds=dm.getTable(t1);
                primary = ds.getPrimary();
                if (primary.length==0){
                    System.err.println("Примари для \""+t1+"\" - не найден");
                    return null;
                }
                t3 = ds.getPrimary()[0];
                ds.first();
                while (!ds.eof()){
                    map.put(ds.getValue(t3), ds.getValue(t2));
                    ds.next();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return map;
        }
        return null;
    }

    
    //--------------------- R E F E R E N C E S --------------------------------

    public boolean hasReferences(){
        for (Dataset dataset:dm.getTables())
            if (dataset.isReferences(tableName))
                return true;
        return false;
    }
    /**
     * Порверка явлиется ли талица ссылкой для tableName
     * @param tableName
     * @return 
     */
    public boolean isReferences(String tableName){
        String references;
        for (String key:foreignMap.keySet()){
            references = foreignMap.get(key);
            if (references.split("\\.")[0].equals(tableName))
                return true;
        }
        return false;
    }
    
    /**
     *  Получение строки вида ид = имя_таблицы.ид
     * @param tableName
     * @return
     * @throws Exception 
     */
    public String getReferences(String tableName)throws Exception{
        String references;
        for (String key:foreignMap.keySet()){
            references = foreignMap.get(key);
            if (!references.contains(".")){
                throw new Exception("Неверный формат references ("+references+")");
            }
            if (references.split("\\.")[0].equals(tableName))
             return key+"=" + references.split("\\.")[1];
        }
        return null;
    }
    
    public Dataset[] getForeignDataset() throws Exception{
        List<Dataset> list = new ArrayList<>();
//        System.out.println("##"+tableName);
        for (Dataset dataset:dm.getTables()){
//            System.out.println("####"+dataset.tableName+" "+dataset.foreignMap);
            
            if (dataset.isReferences(tableName))
                list.add(dataset);
        }
//        System.out.println();
        return list.toArray(new Dataset[list.size()]);
    }
    
    
    /**
     * Получение списка подчинённых таблиц
     * @return 
     */
    public Dataset[] getRefTables(){
        List<Dataset> list = new ArrayList<>();
        Dataset refDataset;
        for (Dataset ds : dm.getTables()){
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
    
    @Override
    public String toString(){
        return tableName+" ("+size()+")";
    }

    public void print() {
        
        for (int i=0;i<getColumnCount();i++){
            System.out.print(getColumnName(i)+" ");
        }
        System.out.println();
        
        Object[] rowset;
        for (int i=0;i<size();i++){
            rowset = get(i);
            for (int j=0;j<rowset.length;j++){
                System.out.print((rowset[j]==null?"null":rowset[j].toString())+" ");
            }
            System.out.println();
        }
    }


    
}

