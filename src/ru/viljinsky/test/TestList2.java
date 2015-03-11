/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import ru.viljinsky.DataModule;

/**
 *
 * @author вадик
 */

public class TestList2 {
    public static void main(String[] args) throws Exception {
        DataModule dm =DataModule.getInsatnce();
        dm.open();
        String[] tables = dm.getTableNames();
        for (String s:tables){
            System.out.println(s);
        }
        
    }
    
}
