/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author вадик
 */
class TTimeGrid extends TimeGrid{
    DataModule dm = DataModule.getInsatnce();

    @Override
    public void load() {
        cells.clear();
        Chip chip;
        Dataset ds = dm.getTable("schedule");
        ds.first();
        while (!ds.eof()){
            chip = new ru.viljinsky.Chip(ds.getInteger("subject_id"),ds.getInteger("teacher_id"),
                    ds.getInteger("room_id"),ds.getInteger("group_id"));
            chip.setCell(ds.getInteger("day_id"), ds.getInteger("lesson_id"));
            cells.add(chip);
            ds.next();
        }
    }

}

public class Main {
    
    
    public static void main(String[] args){
        DataModule dataModule = DataModule.getInsatnce();
        dataModule.open("test.xml");
        
        Dataset ds = dataModule.getTable("work_plan");
        ds.first();
        while (!ds.eof()){
            for (String columnName:ds.getColumns()){
                System.out.print(columnName +" = '"+ ds.getValue(columnName)+"'");
            }
            ds.next();
            System.out.println();
        }
        
        
        
        
        TTimeGrid timeGrid = new TTimeGrid();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(timeGrid.getGridMenu());
        menuBar.add(timeGrid.getCellMenu());
        
        
        JMenu menuData = new JMenu("Data");
        menuData.add(new AbstractAction("Data") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                TestData2.showTestData();
            }
        });
        
        menuBar.add(menuData);
        
        JFrame frame = new JFrame("TimeGrid2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(timeGrid);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
        timeGrid.load();
    }    
}
