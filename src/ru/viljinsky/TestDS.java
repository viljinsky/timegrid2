/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author вадик
 */
public class TestDS {
    static DataModule dm =DataModule.getInsatnce();
    
    
    public static void fillCurriculum() throws Exception{
        Map<String,Object> values = new HashMap<>();
        
        Dataset curriculum = dm.getTable("curriculum");
        Dataset subject = dm.getTable("subject");
        Dataset curriculum_item = dm.getTable("curriculum_item");
        curriculum.first();
        while (!curriculum.eof()){
            subject.first();
            while (!subject.eof()){
                values.put("curriculum_id", curriculum.getValue("id"));
                values.put("subject_id", subject.getValue("id"));
                curriculum_item.append(values);
                subject.next();
            }
            curriculum.next();
        }
        
    }
    
    public static void fillShift() throws Exception{
        Dataset shift = dm.getTable("shift");
        Dataset shift_item = dm.getTable("shift_item");
        Dataset lessons = dm.getTable("lesson");
        Dataset day_list = dm.getTable("day_list");
        
        shift.first();
        shift.next();
        
        Map<String,Object> values = new HashMap<>();
        lessons.first();
        while (!lessons.eof()){
            
            day_list.first();
            while (!day_list.eof()){
//                lessons.first();
//                while (!lessons.eof()){
                    values.put("shift_id",shift.getValue("id"));
                    values.put("day_id",day_list.getValue("id"));
                    values.put("lesson_id",lessons.getValue("id"));
                    shift_item.append(values);
//                    lessons.next();
//                }
                day_list.next();
            }
            lessons.next();
        }
    }
        
    
    
    public static void main(String[] args){
        dm.open();
        try{
            TestDS.fillShift();
            File f = new File("t211.xml");
            f.deleteOnExit();
            dm.save("t211.xml");
            System.out.println("OK");
            
            
            Dataset dataset = dm.getTable("shift_item");
            dataset.first();
            while (!dataset.eof()){
                for (String column:dataset.getColumns()){
                    System.out.println(column+" = "+dataset.getValue(column));
                }
                dataset.next();
                System.out.println();
            }
        
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
