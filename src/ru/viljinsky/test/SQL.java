/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.util.HashMap;
import java.util.Map;
import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;

/**
 *
 * @author вадик
 */
public class SQL {
    
    DataModule dm = DataModule.getInsatnce();

    public Dataset insert(Dataset ds1,Dataset ds2) throws Exception{
        Map<Integer,Integer> map = new HashMap<>();
        for (int i=0;i<ds1.getColumnCount();i++){
            map.put(i, ds2.getColumnIndex(ds1.getColumnName(i)));
        }
        
        Dataset result = new Dataset("RESULT");
        ds2.first();
        Object[] rowset;
        Object[] r;
        int columnCount = ds1.getColumnCount();
        
        for (int i=0;i<ds2.size();i++){
            r=ds2.get(i);
            rowset= new Object[columnCount];
            for (int k:map.keySet()){
                rowset[k]=r[map.get(k)];
            }
            result.add(rowset);
        }
        return result;
    }
    
    public Dataset select(Dataset ds1,Dataset ds2){
        Dataset result = new Dataset("result");
        String ss;
        String tableName;
        
        tableName = ds1.getTableName();
        for (String columnName:ds1.getColumns()){
            if (columnName.contains("."))
                ss=columnName;
            else
                ss=tableName+"."+columnName;        
            result.addColumn(ss);
        }
        
        tableName=ds2.getTableName();
        for (String columnName:ds2.getColumns()){
            if (columnName.contains("."))
                ss=columnName;
            else
                ss=tableName+"."+columnName;        
            result.addColumn(ss);
        }
        
        Object[] rowset;
        Object[] r;
        int columnCount = result.getColumnCount();

        for (int i=0;i<ds1.size();i++){
            
            for (int j=0;j<ds2.size();j++){
                rowset = new Object[columnCount];
                
                int k=0;
                r=ds1.get(i);
                for (int m=0;m<r.length;m++)
                    rowset[k++]=r[m];
                
                
                r=ds2.get(j);
                for (int m=0;m<r.length;m++)
                    rowset[k++]=r[m];
                
                result.add(rowset);
            }
                    
        }
        
        return result;
    }
    
    
    /**
     * Возвращает датасет-декртово_обьеденение 
     * @param tableStr перечесление таблиц в формате:<br>
     *    table_name_1;table_name_2 .. table_name_n;
     * @return
     * @throws Exception 
     */
    public Dataset select(String tableStr) throws Exception{
        Dataset ds1;
        Dataset ds2;
        String[] tableNames = tableStr.split(";");
        if (tableNames.length==1){
            ds1 = dm.getTable(tableNames[0]);
        } else {
            ds1=dm.getTable(tableNames[0]);
            for (int i=1;i<tableNames.length;i++){
                ds2 = dm.getTable(tableNames[i]);
                ds1 = select(ds1,ds2);
            }
        }
        return ds1;
        
        
    }
    
    public Dataset select(String columns,String tables) throws Exception{
        Map<Integer,Integer> map= new HashMap<>();
        
        Dataset dataset = select(tables); // новый датасет select
        
        String[] c = columns.split(";");
        Dataset ds = new Dataset("view2");
        for (String columnName:c){
            ds.addColumn(columnName);
        }
        for (int i=0;i<c.length;i++){
            map.put(i,dataset.getColumnIndex(c[i]));
        }
        
        Object[] rowset;
        Object[] r;
        int ColumnCount = c.length;

        for (int i=0;i<dataset.size();i++){
            r=dataset.get(i);
            rowset = new Object[ColumnCount];
            for (int k:map.keySet())
                rowset[k]=r[map.get(k)];
            ds.add(rowset);
        }
        return ds;
    }
    
    /**
     * strFilter columName operation value
     * operation !=, =,>,<,
     * @param strTables
     * @param strFilter
     * @return
     * @throws Exception 
     */
    enum SQLOperator {
        EQUAL,
        NOT_EQUAL,
        GRETTER,
        GR_EQUAL,
        LESS,
        LESS_EQAL,
        EXISTS,
        NOT_EXISTS,
        IS_NULL
    }
    public Dataset selectFilter(String strTables,String strFilter) throws Exception{
        SQLOperator op = SQLOperator.IS_NULL;
        Dataset result = select(strTables);
        Dataset ds = new Dataset("v1");
        for (String column:result.getColumns()){
            ds.addColumn(column);
        }
        
        Object[] rowset;
        boolean isOK;
        
        for (int i=0;i<result.size();i++){
            rowset = result.get(i);
            isOK = false;
            switch (op){
                case EQUAL:
                    break;
                case NOT_EQUAL:
                    break;
                case GRETTER:;
                    break;
                case GR_EQUAL:
                    break;
                case LESS:
                    break;
                case LESS_EQAL:
                    break;
                case EXISTS:
                    break;
                case IS_NULL:
                    isOK = !((String)rowset[1]).isEmpty();
                    break;
                default:
                    isOK=false;
                        
            }
            if (isOK)
            ds.add(rowset);
        }
        
        return ds;
    }
    
    
    /**
     *  insert into subject select * from subject
     */
    public void test2(){
        try{
            Dataset d1 = dm.getTable("subject");
            Dataset d2 = dm.getTable("subject");
            Dataset d3 = insert(d1, d2);
            d3.print();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     *  select lesson.id,day_list.id from lesson,day_list
     */
    public void test1(){
        Dataset d;
        try{
            d = select("lesson.id;day_list.id","lesson;day_list");
            d.print();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void test3(){
        
        try{
            Dataset d = select("lesson.id;day_list.id","lesson;day_list");
            
            d.print();
            
            d=select("shift_item");
            d.print();
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception{

        
        SQL t = new SQL();
        t.dm.open();
        t.test1();
        
    }
    
}
