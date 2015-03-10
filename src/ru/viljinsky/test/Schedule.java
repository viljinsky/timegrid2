/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ru.viljinsky.Chip;
import ru.viljinsky.Dataset;
import ru.viljinsky.TimeGrid;

/**
 *
 * @author вадик
 */
public class Schedule extends JFrame{
    Grid timeGrid = new Grid();
    Dataset dataset = null;
    
    class Grid extends TimeGrid{

        @Override
        public void load() {
            clear();
            Chip chip;
           
            if (dataset!=null){
                try{
                dataset.first();
                while (!dataset.eof()){
                    chip = new Chip();
//                    chip = new Chip(dataset.getInteger("subject_id"),
//                                    dataset.getInteger("teacher_id"),
//                                    dataset.getInteger("room_id"),
//                                    dataset.getInteger("group_id"));
                    
                    chip.setCell(dataset.getInteger("day_id"), dataset.getInteger("lesson_id"));
                    chip.setTeacher_id(dataset.getInteger("teacher_id"));
                    chip.setRoom_id(dataset.getInteger("room_id"));
                    chip.setGroup_id(dataset.getInteger("group_id"));
                    
                    chip.setTeacher_name(dataset.getLookupValue("teacher_id").toString());
                    chip.setSubject_name(dataset.getLookupValue("subject_id").toString());
                    chip.setRoom_no(dataset.getLookupValue("room_id").toString());
                    chip.setGroup_title(dataset.getLookupValue("group_id").toString());
                    
                    
                    addElement(chip);
                    dataset.next();
                }
                } catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, e.getMessage());
                }
            }
            
        }
        
    }

    static void showSchedule(Component owner,Dataset dataset) {
        Schedule f = new Schedule();
        f.dataset=dataset;
        f.pack();
        f.setVisible(true);
        f.timeGrid.load();
    }
    
    public Schedule(){
        super("Расписание");
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(timeGrid);
    }
    
    public static void main(String[] args){
        Schedule frame = new Schedule();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
