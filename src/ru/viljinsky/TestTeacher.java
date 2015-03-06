/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author вадик
 */
public class TestTeacher {
    static DataModule dm = DataModule.getInsatnce();
    
    public static void f1(String tableName) throws Exception{
        
        Map<String,Integer> m1 = new HashMap<>() ; // имя_поля-номер_поля текущего датасета
        Map<Integer,Object> m2 = new HashMap<>() ; // номер_поля-значение подчинённого датасета
        String references;                         // стока вида: teacher_id=id 

        
        Dataset dataset = dm.getTable(tableName);

        // получает список подчиненных таблиц
        Dataset[] dd = dm.getRefTables(tableName);
        for (Dataset d:dd){
            System.out.println("----->"+d.tableName);
            references = d.getReferences(tableName);
            System.out.println(references);
            m1.clear();
            m1.put(references.split("=")[1],d.getColumnIndex(references.split("=")[0]));
            System.out.println(m1);

        //----------------------------------------
            dataset.first();
            
            for (String column:dataset.getColumns()){
                System.out.print(column+"   ");
            }
            System.out.print("\n");
            dataset.first();
            while (!dataset.eof()){
                for (String column:dataset.getColumns()){
                    System.out.print("'"+dataset.getValue(column)+"' ");
                }
                System.out.println();
                
                m2.clear();
                for (String k:m1.keySet()){
                    m2.put(m1.get(k), dataset.getValue(k));
                }
                d.open(m2);
                d.first();
                while (!d.eof()){
                    for (String column:d.getColumns()){
                        System.out.print(column+" ='"+d.getValue(column)+"'  ");
                    }
                    System.out.print("\n");
                    d.next();
                }
                
                
                
                dataset.next();
            }
        }
        
    }
    
     static void f2() throws Exception{
        Dataset refDataset=null;
        Dataset dataset;
        
        Map<String,Integer> m1 = new HashMap<>() ; // имя_поля-номер_поля текущего датасета
        Map<Integer,Object> m2 = new HashMap<>() ; // номер_поля-значение подчинённого датасета
        String references;
        
        // получить имя_поля-номер поля
        for (Dataset ds:dm.tables){
            if (ds.isReferences("teacher")){
                System.out.println("---->"+ds.tableName);
                references = ds.getReferences("teacher");
                System.out.println("ref->"+references);
                refDataset = new Dataset(ds.tableName);
//                map.put(references.split("=")[0], null);
                m1.put(references.split("=")[1],ds.getColumnIndex(references.split("=")[0]));
            }
        }
        
        
        dataset = dm.getTable("teacher");
        
        dataset.first();
        while (!dataset.eof()){
            
            System.out.println("master "+dataset.getValue("id"));

            for (String k:m1.keySet()){
                m2.put(m1.get(k), dataset.getValue(k));
            }
            
            refDataset.open(m2);
            refDataset.first();
            while (!refDataset.eof()){
                System.out.println("\t ref"+refDataset.getValue("teacher_id")+" ");
                refDataset.next();
            }
            
            dataset.next();
        }
        
        for (String s:dataset.foreignMap.keySet()){
            System.out.println(" foreign key ="+s+" ref ="+dataset.foreignMap.get(s));
        }
        
    }
    
    public static void main(String[] args) throws Exception{
        dm.open();
        f1("subject_group");
    }
    
}
