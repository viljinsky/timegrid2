/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import ru.viljinsky.xmldb.DataModule;
import ru.viljinsky.xmldb.Dataset;

/**
 *
 * @author вадик
 */
public class TestSQL {
    public static void main(String[] args) throws Exception{
        DataModule dm= DataModule.getInsatnce();
        dm.open();
        
        SQL sql = new SQL();
        
        Dataset dataset = sql.selectFilter("shift","parent not null");
        dataset.print();
    }
    
}
