/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

/**
 *
 * @author вадик
 */
public class Test2 {
    public static void main(String[] args){
        int n = Integer.decode("0x1111");
        System.out.println(n);

        String s1 = "a";
        String s2 = "b";
        System.out.println("ab".intern()==(s1+s2).intern());
        
        Object k =  "100";
        
        System.out.println(((Integer.valueOf((String)k))==100));
        
//        Integer a = new Integer(101);
//        Integer b  = new Integer(101);
//        String s = new String("a");
//        String s1= new String("a");
//        System.out.println(a==b);
//        System.out.println(s==s1);
    }
    
}
