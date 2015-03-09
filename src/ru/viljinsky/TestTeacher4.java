/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author вадик
 */

class MasterDetail extends JSplitPane{
    Grid grid = new Grid();
    JTabbedPane tabbedPane= new JTabbedPane();
    public MasterDetail(){
        super(JSplitPane.VERTICAL_SPLIT);
        setTopComponent(new JScrollPane(grid));
        setBottomComponent(tabbedPane);
        setResizeWeight(0.5);
    }
    
    public void setTableName(String tableName){
    }
}

public class TestTeacher4 extends JFrame {
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();
    
    public TestTeacher4(){
        super("TestTeacher4");
        Container content = getContentPane();
        content.setPreferredSize(new Dimension(800,600));
        content.add(tabbedPane);
    }
    
    public void open() throws Exception{
        MasterDetail md;
        dataModule.open();
        for (String tableName: dataModule.getTableNames()){
            System.out.println(tableName);
            md = new MasterDetail();
            tabbedPane.addTab(tableName, md);
            
        }
    }
    
    public static void main(String[] args) throws Exception{
        TestTeacher4 frame = new TestTeacher4();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.open();
                
        
    }
}
