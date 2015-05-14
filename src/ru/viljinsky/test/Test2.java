/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.util.HashMap;
import java.util.Map;
import ru.viljinsky.xmldb.DataModule;
import ru.viljinsky.xmldb.Dataset;

/**
 *
 * @author вадик
 */
public class Test2 {
    public static void main(String[] args) throws Exception{
        // select * from schedule inner join subject_group on schedule.group_id=subject_group.id where 
        DataModule dm= DataModule.getInsatnce();
        dm.open();
        Dataset dataset1 = dm.getTable("schedule");
        Object[] rowset1;
        
        Dataset dataset2 = dm.getTable("subject_group");
        Object[] rowset2;
        
        Map<Integer,Integer> map = new HashMap<>();
        map.put(dataset1.getColumnIndex("group_id"),dataset2.getColumnIndex("id"));
        
        
        
        for (int rowIndex1=0;rowIndex1<dataset1.size();rowIndex1++){
            rowset1=dataset1.getRowset(rowIndex1);
            label1:for (int rowIndex2=0;rowIndex2<dataset2.size();rowIndex2++){
                rowset2=dataset1.get(rowIndex2);
                
                boolean b = true;
                for (int i:map.keySet()){
                    b = (rowset2[i].equals(rowset1[map.get(i)]));
                    if (!b) continue label1;
                }
                
                for (int columnIndex1=0;columnIndex1<dataset1.getColumnCount();columnIndex1++){
                    System.out.print(rowset1[columnIndex1]+" ");
                }
                System.out.print("---");
                for (int columnIndex2=0;columnIndex2<dataset2.getColumnCount();columnIndex2++){
                    System.out.print(rowset2[columnIndex2]+" ");
                }
                
                System.out.println();
            }
        }
        
        
    }
}
