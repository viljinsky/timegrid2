/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author вадик
 */
public class TestSet {
    public static void main(String[] args){
        Set<String> set = new HashSet<>();
        set.add("one");
        set.add("two");
        set.add("three");
        set.add("four");
        set.add("one");
        set.add("five");
        
        for (String s:set){
            System.out.println(s);
        }
        
        
        Map<Integer,String> t = new HashMap<>();
        t.put(1, "One");
        t.put(2, "Two");
        t.put(3, "Three");
        t.put(4, "Four");
        
        System.out.println(t.toString());
        
        for (Integer n:t.keySet()){
            if (t.get(n)=="One"){
                System.out.println("OK");
            }
        }
        
    }
    
}
