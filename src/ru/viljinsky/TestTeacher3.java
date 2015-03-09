/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author вадик
 */

class Panel extends JPanel{
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();
    GridPanel gridPanel = new GridPanel();
    
    public Panel(){
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(gridPanel);
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.5);
        tabbedPane.setPreferredSize(new Dimension(600,300));
        add(splitPane);
        
    }
    
    public void open(String tableName){
        GridPanel slave;
        try{
            if (!dataModule.isActive())
                throw new Exception("ДатаМодуль нот актив");
            // Переделать на список таблиц
            Dataset[] d =dataModule.getRefTables(tableName);
            for (Dataset ds:d){
                slave = new GridPanel(ds.tableName);
                tabbedPane.add(ds.tableName,slave);
                slave.setMaster(gridPanel);
            }
            
            gridPanel.open(tableName);
            updateUI();
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
}

public class TestTeacher3 extends JFrame{
    
    public static void main(String[] args){
        DataModule dm= DataModule.getInsatnce();
        dm.open();
        Panel panel = new Panel();
        TestTeacher3 frame = new TestTeacher3();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        panel.open("profile");
    }
}
