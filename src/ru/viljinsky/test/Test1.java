/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ru.viljinsky.dbcontrols.DBLookup;
import ru.viljinsky.dbcontrols.Grid;
import ru.viljinsky.xmldb.DataModule;
import ru.viljinsky.xmldb.Dataset;

/**
 *
 * @author вадик
 */
public class Test1 extends JPanel{
    DataModule dm =DataModule.getInsatnce();
    Grid grid = new Grid();
    FilterPanel filterPanel = new FilterPanel();
    
    class FilterPanel extends JPanel{
        DBLookup luTeacher = new DBLookup();
        
        DBLookup luRoom = new DBLookup();
        public FilterPanel(){
            setLayout(new FlowLayout(FlowLayout.LEFT));
            luTeacher.setPreferredSize(new Dimension(200, 22));
            luRoom.setPreferredSize(new Dimension(200,22));
            add(luTeacher);
            add(luRoom);
        }
    }
    public Test1(){
        setPreferredSize(new Dimension(800,600));
        setLayout(new BorderLayout());
        add(new JScrollPane(grid));
        add(filterPanel,BorderLayout.PAGE_START);
       
        open();
    }
    
    public void open(){
        try{
            Dataset dataset = dm.getTable("schedule");
            grid.setDataset(dataset);
            dataset = dm.getTable("teacher");
            filterPanel.luTeacher.setDataset(dataset,"id", new String[]{"first_name"});
            dataset.first();
            filterPanel.luTeacher.setValue(dataset.getValue("id"));
            
            dataset = dm.getTable("room");
            filterPanel.luRoom.setDataset(dataset,"id",new String[]{"number"});
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        DataModule.getInsatnce().open();
        JFrame frame = new JFrame("Test1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Test1());
        frame.pack();
        frame.setVisible(true);
    }
    
}
