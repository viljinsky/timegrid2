/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;

/**
 *
 * @author вадик
 */
public class TestLookup {
    public static void main(String[] args) throws Exception{
        DataModule dm= DataModule.getInsatnce();
        dm.open();
        
        Dataset dataset = dm.getTable("schedule");
        dataset.first();
        while (!dataset.eof()){
            for (String columnName : dataset.getColumns()){
                System.out.print(columnName+"="+dataset.getValue(columnName));
                if (dataset.isLookup(columnName))
                    System.out.println(" ("+dataset.getLookupValue(columnName)+")");
                else
                    System.out.println();
            }
            System.out.println();
            
            
            dataset.next();
        }
        
    }
    
}
