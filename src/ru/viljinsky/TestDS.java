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
public class TestDS {
    static DataModule dm =DataModule.getInsatnce();
    
    public static void fillShift() throws Exception{
        Dataset shift = dm.getTable("shift");
        Dataset shift_item = dm.getTable("shift_item");
        Dataset lessons = dm.getTable("lesson");
        Dataset day_list = dm.getTable("day_list");
        
        shift.first();
        
        while (!lessons.eof()){
            
            day_list.first();
            while (!day_list.eof()){
                lessons.first();
                while (!lessons.eof()){
//                    System.out.println(shift.getValue("id")+" "+day_list.getValue("id")+" "+lessons.getValue("id"));
                    shift_item.append(new Object[]{shift.getValue("id"),day_list.getValue("id"),lessons.getValue("id")});
                    lessons.next();
                }
                day_list.next();
            }
            lessons.next();
        }
    }
        
    
    
    public static void main(String[] args){
        dm.open();
        try{
            TestDS.fillShift();
            dm.save("t21.xml");
            System.out.println("OK");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
