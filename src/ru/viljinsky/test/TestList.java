/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author вадик
 */

class MyList extends ArrayList<Object[]>{
    public void print(){
        for (Object[] r:this){
            for (Object o:r)
                System.out.print(o+" ");
            System.out.println();
        }
    }
}

public class TestList {
    public static void main(String[] args){
        MyList list1 = new MyList();
        list1.add(new Object[]{1,2,3,4,5});
        list1.add(new Object[]{2,3,4,5,6});
        list1.add(new Object[]{3,4,5,6,7});
        list1.add(new Object[]{4,5,6,7,8});
        
        MyList list2 = new MyList();
        for (Object[] r:list1){
            if ((list1.indexOf(r) % 2)==0)
            list2.add(r);
        }

        list1.get(3)[0]=99;
        list2.get(0)[3]=88;
        
        list1.print();
        System.out.println();
        list2.print();
        
    }
    
}
