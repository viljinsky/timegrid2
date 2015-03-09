/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

/**
 *
 * @author вадик
 */



public class TestTeacher2  extends JFrame{
    DataModule dataModule=DataModule.getInsatnce();
    GridPanel panelMaster;
    GridPanel slave1;
    GridPanel slave2;
    
            
    public TestTeacher2(){
        super("TestTeacher2");
        
        getContentPane().setPreferredSize(new Dimension(800,600));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        panelMaster = new GridPanel("teacher");
//        panelMaster = new GridPanel();
        slave1 = new GridPanel("schedule");
        slave1.setMaster(panelMaster);
        
        slave2 = new GridPanel("work_plan");
        slave2.setMaster(panelMaster);
        
        JSplitPane p1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        p1.setLeftComponent(slave1);
        p1.setRightComponent(slave2);
        p1.setResizeWeight(0.5);
        
        splitPane.setTopComponent(panelMaster);
        splitPane.setBottomComponent(p1);//(new JScrollPane(slave1));
        getContentPane().add(splitPane);
        splitPane.setResizeWeight(.5);
        
    }
    
    public void open() {
        try{
            dataModule.open();
            panelMaster.open();
        
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }
    
    public static void main(String[] args){
        TestTeacher2 frame  = new TestTeacher2();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.open();
    }
    
}
