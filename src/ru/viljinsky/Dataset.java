/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;

/**
 *
 * @author вадик
 */
public class Dataset extends ArrayList<Object[]>{
    String tableName ;
    Map<Integer,String> columns;
    Map<String,String> lookup;
    Set<String> primary;

    protected Integer index=-1;
    
    public Dataset(String tableName){
        this.tableName=tableName;
        columns = new HashMap<>();
        lookup = new HashMap<>();
        primary = new HashSet<>();
    }

    public String[] getPrimary(){
        return primary.toArray(new String[primary.size()]);
    }
    
    
    public String getLookup(String columnName){
        return lookup.get(columnName);
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
    
    public Object getValue(String columnName){
        Object[] rowset = get(index);
        int n= getColumnIndex(columnName);
        return rowset[n];
    }
    
    public Integer getInteger(String columnName){
        Object value = getValue(columnName);
        if (value!=null){
            return Integer.parseInt((String)value);
        }
        return null;
    }
    
    public Object lookUp(String columnName,Object columnValue,String searchName) throws Exception{
        int n = getColumnIndex(columnName),k=getColumnIndex(searchName);
        if (n<0)
            new Exception("column '"+columnName+"' not found");
        if (k<0)
            new Exception("column '"+searchName+"' not found");
        
        Integer value;
        for (Object[] recordset:this){
            value = Integer.parseInt((String)recordset[n]);
            if (value.equals(columnValue)){
                return recordset[k];
            }
        }
        return null;
    }


    public int getColumnIndex(String columnName){
        for (int n:columns.keySet()){
            if (columns.get(n)==columnName){
                return n;
            }
        }
        return -1;
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
    
    public void addRecord(Attributes attr){
        String columnName ;
        Integer columnIndex;
        for (int i=0;i<attr.getLength();i++){
            columnName = attr.getQName(i);
            columnIndex = getColumnIndex(columnName);
            if (columnIndex<0)
                columnIndex = addColumn(columnName);
        }

        Object[] record = new Object[columns.size()];
        for (int i=0;i<record.length;i++){
            record[i]=attr.getValue(i);
        }
        super.add(record);

    }
    
    public boolean delete(){
        if (index>=0){
            Object[] recordset = get(index);
            this.remove(recordset);
            if (index>=this.size()){
                index=this.size()-1;
            }
            return true;
        }
        return false;
    }

    int getColumnCount() {
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
}

